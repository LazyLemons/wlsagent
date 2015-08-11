/**
 * This file is part of Wlsagent.
 * <p>
 * Wlsagent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Wlsagent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Wlsagent. If not, see <http://www.gnu.org/licenses/>.
 */

package net.wait4it.nagios.wlsagent.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

import net.wait4it.nagios.wlsagent.core.Result;
import net.wait4it.nagios.wlsagent.core.Status;
import net.wait4it.nagios.wlsagent.core.WLSProxy;

/**
 * Gets statistics for JDBC datasources.
 *
 * The following metrics are available:
 *
 *   - The datasource current pool size
 *   - The active connection count
 *   - The number of threads waiting for
 *     a connection from the pool
 *
 * @author Yann Lambret
 * @author Kiril Dunn
 *
 */
public class JDBCTest extends TestUtils implements Test {

    /**
     * WebLogic JDBC datasources stats.
     *
     * @param proxy   an applicative proxy for the target WLS instance
     * @param params  a pipe separated list of datasource names, or
     *                a wildcard character (*) for all datasources
     * @return result collected data and test status
     */
    public Result run(WLSProxy proxy, String params) {
        Result result = new Result();
        List<String> output = new ArrayList<String>();
        List<String> message = new ArrayList<String>();
        int code = 0;

        Map<String, String> datasources = new HashMap<String, String>();

        // Test code for a specific datasource
        int testCode = Status.OK.getCode();

        // Message prefix
        String prefix = "";

        // Performance data
        int capacity;
        int activeCount;
        int waitingCount;
        int available; // The number of database connections that are currently idle and available to be used by applications in this instance of the data source
        int unavailable; // The number of connections currently in use by applications or being tested in this instance of the data source.
        int highestAvailable; // Highest number of database connections that were idle and available to be used by an application at any time in this instance of the data source since the data source was deployed
        int highWait; // The longest connection reserve wait time in seconds.

        // Parses HTTP query params
        for (String s : Arrays.asList(params.split("\\|"))) {
            datasources.put(s.split(",", 2)[0], s.split(",", 2)[1]);
        }

        try {
            ObjectName jdbcServiceRuntimeMbean = proxy.getMBean("JDBCServiceRuntime");
            ObjectName[] jdbcDataSourceRuntimeMbeans = proxy.getMBeans(jdbcServiceRuntimeMbean, "JDBCDataSourceRuntimeMBeans");
            for (ObjectName datasourceRuntime : jdbcDataSourceRuntimeMbeans) {
                String datasourceName = (String) proxy.getAttribute(datasourceRuntime, "Name");
                if (datasources.containsKey("*") || datasources.containsKey(datasourceName)) {

                    capacity = (Integer) proxy.getAttribute(datasourceRuntime, "CurrCapacity");
                    activeCount = (Integer) proxy.getAttribute(datasourceRuntime, "ActiveConnectionsCurrentCount");
                    waitingCount = (Integer) proxy.getAttribute(datasourceRuntime, "WaitingForConnectionCurrentCount");
                    available = (Integer) proxy.getAttribute(datasourceRuntime, "NumAvailable");
                    unavailable = (Integer) proxy.getAttribute(datasourceRuntime, "NumUnavailable");
                    highestAvailable = (Integer) proxy.getAttribute(datasourceRuntime, "HighestNumAvailable");
                    highWait = (Integer) proxy.getAttribute(datasourceRuntime, "WaitSecondsHighCount");

                    String out = "";
                    out += "capacity=" + capacity + ";;;0;150; ";
                    out += "active=" + activeCount + ";;;0;150; ";
                    out += "waiting=" + waitingCount + ";;;0;150; ";
                    out += "available=" + available + ";;;0;150; ";
                    out += "unavailable=" + unavailable + ";;;0;150; ";
                    out += "highestAvailable=" + highestAvailable + ";;;0;150; ";
                    out += "maxWait=" + highWait + "s;;;0;10;";

                    output.add(out);

                    double percentUnavailable = ((double) unavailable / (double) capacity) * 100D;

                    message.add((int) percentUnavailable + "% unavailable (" + unavailable + "/" + capacity + ") - " + available + " available");

                    if (percentUnavailable >= 10) {
                        testCode = Status.CRITICAL.getCode();
                    }


                    if (testCode == Status.WARNING.getCode() || testCode == Status.CRITICAL.getCode()) {
                        code = (testCode > code) ? testCode : code;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(Status.UNKNOWN);
            result.setMessage(e.toString());
            return result;
        }

        for (Status status : Status.values()) {
            if (code == status.getCode()) {
                result.setStatus(status);
                break;
            }
        }

        result.setOutput(formatOut(output));
        result.setMessage(formatMsg(prefix, message));

        return result;
    }

}

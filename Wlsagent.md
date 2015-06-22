### Introduction ###

Wlsagent is a small JMX client, which exposes performance metrics for WebLogic 9.x and 10.x servers.

You can get the performance data by submitting simple HTTP requests to the plugin, as it embeds a Jetty container. The plugin runs in the background and handles every monitoring request, which allows a small memory footprint (a 32MB JVM heap is sufficient), no CPU overhead and short response time.

### Prerequisites ###

In order to use the plugin, you need a suitable 1.6 SDK for your operating system. You also need to create a weblogic user on each domain you want to monitor (the user has to belong to the monitor group).

### Wlsagent in 5 minutes ###

First things first, go to the 'Downloads' section and retrieve the most recent bundle (.tar.gz archive). In this archive, you will find a wlsagent directory with these contents:

```
lib
run.sh
wlsagent-r185.jar
wlsagent.sh
```

The lib directory contains the required Jetty dependencies. I'm using an 'org.eclipse.jetty.aggregate' artifact so there is only one jar for the jetty container, plus the servlet api jar.

You have to add some extra dependencies to the plugin classpath, by creating symbolic links to your WebLogic distribution files, or by copying them into the 'lib' directory. Required dependencies are wlfullclient.jar, wlclient.jar and wljmxclient.jar.

To generate the wlfullclient.jar, go to the ${WL\_HOME}/server/lib directory and issue this command:

```
java -jar wljarbuilder.jar
```

The 'lib' directory contents should then look like this:

```
jetty-servlet-7.6.2.v20120308.jar
servlet-api-2.5.jar
@wlclient.jar
@wlfullclient.jar
@wljmxclient.jar
```

Let's move on to the run.sh script. In this script, you have to set the correct value for JAVA\_HOME variable, and you can also specify the host and the port on which the plugin will be listening. Default is '0.0.0.0:9090', which means the plugin will bind on all network interfaces.

End with the wlsagent.sh script, which you should only amend if you have changed the default '9090' port value in the run.sh script.

Then type :

```
./run.sh
```

And send a test request to your server (the port specified here is the connector of the target server):

```
./wlsagent.sh 'hostname=hydre2&port=7001&username=nagios&password=Weblogic10&jvm=UsedMemory,90,95'
```

You should get something like:

server1 is in RUNNING state, status OK|HeapSize=256M;;;0;512 UsedMemory=194M;;;0;512

### Building the plugin ###

**Ant build**

The files provided in the 'Downloads' section have been compiled with a 1.6 SDK and Jetty 7.6.2 dependencies. If you want to build the plugin by yourself, just follow these steps:

  * Download the last jetty 7 distribution [here](http://download.eclipse.org/jetty/).
  * Create a lib directory in your working directory.
  * Copy the servlet-api-2.5.jar in the lib directory.
  * Download the jetty-servlet jar from [here](http://mvnrepository.com/artifact/org.eclipse.jetty.aggregate/jetty-servlet), and put it in the lib directory. Make sure the version number matches the version number of the full distribution.
  * Copy the suitable wlfullclient.jar for your WebLogic distribution in the lib directory (see 'wlsagent in 5 minutes' section).
  * Check out the project source files:

```
svn checkout http://wlsagent.googlecode.com/svn/trunk/src
```

  * Copy the following Ant build.xml file in your working directory:

```
<?xml version="1.0" encoding="UTF-8" ?>

<project name="wlsagent" basedir="." default="build">

    <property name="src.dir"     value="src"/>
    <property name="lib.dir"     value="lib"/>
    <property name="build.dir"   value="build"/>
    <property name="classes.dir" value="${build.dir}/classes"/>
    <property name="jar.dir"     value="${build.dir}/jar"/>
    <property name="javadoc.dir" value="${build.dir}/javadoc" />
    <property name="main-class"  value="net.wait4it.nagios.wlsagent.core.WLSAgent"/>

    <path id="classpath">
        <fileset dir="${lib.dir}" includes="**/*.jar"/>
    </path>

    <target name="clean">
        <delete dir="${build.dir}"/>
    </target>

    <target name="compile">
        <mkdir dir="${classes.dir}"/>
        <javac srcdir="${src.dir}" destdir="${classes.dir}" classpathref="classpath" includeantruntime="false"/>
    </target>

    <target name="jar" depends="compile">
        <mkdir dir="${jar.dir}"/>
        <jar destfile="${jar.dir}/${ant.project.name}.jar" basedir="${classes.dir}">
            <manifest>
                <attribute name="Main-Class" value="${main-class}"/>
            </manifest>
        </jar>
    </target>

    <target name="javadoc">
        <javadoc packagenames="src" sourcepath="${src.dir}" destdir="${javadoc.dir}" classpathref="classpath">
            <fileset dir="${src.dir}">
                <include name="**" />
            </fileset>
        </javadoc>
    </target>

    <target name="build" depends="clean,jar"/>

</project>
```

  * Run ant.

### Running the plugin ###

You can use the sample script below to run the plugin:

```
#!/bin/bash

cd $(dirname "$0")

JAVA_HOME=""
HOST="0.0.0.0"
PORT="9090"

CLASSPATH=".:wlsagent-r185.jar"

# Add Jetty dependencies to the plugin classpath.
CLASSPATH="${CLASSPATH}:lib/servlet-api-2.5.jar:lib/jetty-servlet-7.6.2.v20120308.jar" 

# Add WebLogic dependencies to the plugin classpath.
# The 'wljmxclient.jar' must be loaded before the 'wlfullclient.jar'.
CLASSPATH="${CLASSPATH}:lib/wlclient.jar:lib/wljmxclient.jar:lib/wlfullclient.jar"

${JAVA_HOME}/bin/java -Xmx32m -cp ${CLASSPATH} net.wait4it.nagios.wlsagent.core.WLSAgent ${HOST} ${PORT} > /dev/null 2>&1 &
```

The content of the working directory should look like this:
  * the lib directory
  * the wlsagent-[r185](https://code.google.com/p/wlsagent/source/detail?r=185).jar file
  * the previous shell script (run.sh)

The lib directory contains the jar dependencies for Jetty and the WebLogic implementation of the 't3' protocol:

```
jetty-servlet-7.6.2.v20120308.jar
servlet-api-2.5.jar
@wlclient.jar
@wlfullclient.jar
@wljmxclient.jar
```

### Using the plugin ###

Once the plugin is running, you can invoke it for instance with wget. In the example below, the plugin is listening on the port 9090, and the target server on the port 7500.

```
wget -q -O - 'http://localhost:9090/wlsagent/WlsAgent' --post-data='hostname=localhost&port=7500'
```


The above command produces the following output:

3|java.io.IOException: Unhandled exception in lookup

As you can see, every non authenticated request is denied. Note that the first character of the command output string is the regular Nagios exit code (UNKNOWN in this case).

In order to monitor your application servers, you can create a specific user on the WebLogic side, and make sure it belongs to the monitor group. This is particularly important if your WebLogic domain is secured with SSL, as an administrative user can't use an non-administrative port.

Let's retry by adding the credentials :

```
wget -q -O - 'http://localhost:9090/wlsagent/WlsAgent' --post-data='hostname=localhost&port=7500&username=nagios&password=nagios' 
```

This time, we get this output:

0|server1 is in RUNNING state, status OK|

The Nagios exit code is 0 (OK), because we didn't perform any test on the one hand, and because the 'server1' instance is in the expected state (RUNNING) on the other hand. If your server is in ADMIN state for some reason, you will get this output:

2|server1 is in ADMIN state, status CRITICAL|

Next we're going to get information about the JVM heap usage by adding [jvm](Jvm.md)=UsedMemory,80,90 to the request parameters :

```
wget -q -O - 'http://localhost:9090/wlsagent/WlsAgent' --post-data='hostname=localhost&port=7500&username=nagios&password=nagios&jvm=UsedMemory,80,90'
```

The two numeric values at the end are the warning and critical thresholds for the memory usage, we will go back on this later.

The command output is:

0|server1 is in RUNNING state, status OK|HeapSize=256M;;;0;512 UsedMemory=194M;;;0;512

As you can see, we get the current heap size, the maximum heap size and the amount of memory currently used by the server. If you're using a JRockit JVM, you will also get the amount of CPU used by the server process (see [jvm](Jvm.md) option).

Let's try to change the warning threshold value to '30':

```
wget -q -O - 'http://localhost:9090/wlsagent/WlsAgent' --post-data='hostname=localhost&port=7500&username=nagios&password=nagios&jvm=UsedMemory,30,90'
```

We get this:

1|server1 is in RUNNING state, status WARNING: memory used (200/512)|HeapSize=256M;;;0;512 UsedMemory=200M;;;0;512

A warning alert is raised by the test, as the ratio used memory / maximum memory is superior to 30% (our warning threshold).

See [Invocation](Invocation.md) page of this wiki for script samples.

Available options are:

  * [jvm](Jvm.md)
  * [thread-pool](ThreadPool.md)
  * [jta](Jta.md)
  * [jdbc](Jdbc.md)
  * [jms-runtime](JmsRuntime.md)
  * [jms-queue](JmsQueue.md)
  * [component](Component.md)

### Screenshots ###

![http://wlsagent.googlecode.com/svn/wiki/images/image_001.png](http://wlsagent.googlecode.com/svn/wiki/images/image_001.png)

![http://wlsagent.googlecode.com/svn/wiki/images/image_002.png](http://wlsagent.googlecode.com/svn/wiki/images/image_002.png)

![http://wlsagent.googlecode.com/svn/wiki/images/image_003.png](http://wlsagent.googlecode.com/svn/wiki/images/image_003.png)

![http://wlsagent.googlecode.com/svn/wiki/images/image_004.png](http://wlsagent.googlecode.com/svn/wiki/images/image_004.png)

![http://wlsagent.googlecode.com/svn/wiki/images/image_005.png](http://wlsagent.googlecode.com/svn/wiki/images/image_005.png)
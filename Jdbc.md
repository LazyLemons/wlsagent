### jdbc option ###

  * Invocation

```
jdbc=<datasource 1>,w,c|<datasource 2>,w,c|...|<datasource N>,w,c
```

In this generic example, 'datasource 1' ... 'datasource N' would be jdbc datasource names (JNDI names actually), and 'w' and 'c' would be warning and critical thresholds.

So you could have for instance:

```
jdbc=jdbc/ds1,2,5|jdbc/ds2,2,5
```

When using this option, you can specify 'warning' and 'critical' threshold values. The comparison is made beetween the thresholds and the 'waiting' value. So for the ds2 datasource, a warning alert will be raised if 10 or 11 active connections are required by the application, and a critical alert will be raised for 12 active connections required or more.

You can also call the jdbc test with a wildcard:

```
jdbc=*,2,5
```

Notice the 'warning' and 'critical' thresholds will be the same for all datasources though.

  * Output

The output would be the same for the two above invocations (assuming these only two datasources are configured on you target server):

```
jdbc-jdbc/ds1-capacity=1 jdbc-jdbc/ds1-active=0 jdbc-jdbc/ds1-waiting=0 jdbc-jdbc/ds2-capacity=8 jdbc-jdbc/ds2-active=2 jdbc-jdbc/ds2-waiting=0
```

The capacity value is the current jdbc pool size at the time the test is performed. The active value is the number of active JDBC connections for the monitored datasource, and the waiting value corresponds to the number of threads waiting for a connection from the pool. This last value is used to raise an alert.

**Specific messages**

If the test raises an alert, this kind of message appears at the beginning of the performance data:

```
server1 is in RUNNING state, status CRITICAL - JDBC connection waiting count: jdbc/ds1 (6)
```
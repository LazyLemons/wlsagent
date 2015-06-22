This project is a Nagios plugin which aims to provide JMX monitoring capabilities for WebLogic servers (9.x, 10.x & 11g releases), with the smallest possible memory footprint.

Monitoring is achieved with simple HTTP requests, through the use of an embedded Jetty container.

Current features are:

  * JVM heap monitoring
  * Server thread pool monitoring, including hung thread detection
  * JTA transactions monitoring
  * JDBC datasources monitoring
  * JMS connections monitoring
  * JMS queues monitoring
  * HTTP sessions monitoring
  * Clustering support

See [this page](Wlsagent.md) for getting started.

If you're also interested in WebSphere monitoring, you might want to take a look at the [wasagent](http://code.google.com/p/wasagent/) project.
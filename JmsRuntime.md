### jms-runtime option ###

  * Invocation

```
jms-runtime=CurrentConnections,500,750
```

500 is the warning threshold for the current connection count. 750 is the critical threshold for the current connection count.

These values are arbitrary values that are compared to the current JMS connection count of your application server.

  * Output

```
JmsRuntime-current=581
```

The output gives the current number of JMS connections handled by your application server. If there are several JMS servers deployed on the target JVM, you will get the total number of connections.

  * Specific messages

If the test raises an alert, this kind of message appears at the beginning of the performance data:

```
server1 is in RUNNING state, status WARNING - JMS runtime connection count (581)
```
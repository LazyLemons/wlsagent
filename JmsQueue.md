### jms-queue option ###

The jms-queue option allows you to monitor the WebLogic server JMS queues usage.

  * Invocation

```
jms-queue=DistributedQueue0,150,200|DistributedQueue1,150,200
```

DistributedQueue0 and DistributedQueue1 would be your JMS resources WebLogic names, and '150' and '200' would be 'warning' and 'critical' thresholds for both of them.

You can also call the jms-queue test with a wildcard character:

```
jms-queue=*,150,200
```

Notice the 'warning' and 'critical' thresholds will be the same for all queues though.

  * Output

```
jms-DistributedQueue0-current=2 jms-DistributedQueue0-pending=0 jms-DistributedQueue1-current=0 jms-DistributedQueue1-pending=0
```

The 'current' value is the current number of messages in the destination, and the 'pending' value is the pending number of messages for the destination (non committed or acknowledged messages). The 'current' value is used to raise an alert. So for the DistributedQueue0 queue, a warning alert will be raised if the current number of messages in the destination is beetween 150 and 200.

  * Specific messages

If the test raises an alert, this kind of message appears at the beginning of the performance data:

```
server1 is in RUNNING state, status WARNING - JMS message count: DistributedQueue0 (168)
```
### thread-pool option ###

  * Invocation

```
thread-pool=ThreadStuckCount,5,10
```

5 and 10 are respectively the warning and the critical threshold for the thread stuck count value. In this case, a warning alert would be raised if the number of threads marked as stuck is between 5 and 9.

  * Output

```
ThreadPoolSize=16 ThreadActiveCount=14;;;0;16 ThreadHoggingCount=2;;;0;16 ThreadStuckCount=0;;;0;16 Throughput=14.31
```

The ThreadPoolSize value is the total number of threads in the pool. The ThreadActiveCount value is the active thread count at the time the test is performed. The minimal value is always 0, and the max value is the ThreadPoolSize value (i.e. 'ExecuteThreadTotalCount' attribute of the ThreadPoolRuntimeMBean). The ThreadHoggingCount is the number of threads being hogged by a request for much more than the execution time. If they remain in this state for long enough, they will be marked as stuck (while still being considered as hogged). The ThreadStuckCount is the number of threads currently marked as stuck. The Throughput value is the mean number of requests completed per second.

To sum up: ThreadActiveCount >= ThreadHoggingCount >= ThreadStuckCount

  * Specific messages

If the test raises an alert, this kind of message appears at the beginning of the performance data:

```
server1 is in RUNNING state, status WARNING - thread pool stuck count (6/16)
```
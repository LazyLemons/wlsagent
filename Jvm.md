### jvm option ###

The jvm option allows you to monitor the WebLogic server JVM heap usage (works with both JRockit and Sun JVMs).

  * Invocation

```
jvm=UsedMemory,90,95
```

90 is the warning threshold for the JVM heap usage. 95 is the critical threshold for the JVM heap usage.

These values are compared to the UsedMemory / HeapSizeMax ratio, i.e. the currently amount of memory used by the application server divided by the JVM heap max size (see below).

  * Output

```
HeapSize=256M;;;0;512 UsedMemory=227M;;;0;512
```

If you're using a JRockit JVM, the output would be:

```
HeapSize=256M;;;0;512 UsedMemory=227M;;;0;512 JvmProcessorLoad=7%;;;0;100
```

The HeapSize is the current heap size at the time the test is performed. The UsedMemory is the amount of memory used at the time the test is performed. Both values are given in MB, the minimal value is always 0, and the max value is the same for both properties (i.e. 'HeapSizeMax' attribute of the JVMRuntimeMBean). The JvmProcessorLoad is the amount of CPU used by the JVM (JRockit only).

  * Specific messages

If this test raises an alert, this kind of message appears at the beginning of the performance data:

```
server1 is in RUNNING state, status CRITICAL - memory used (501/512)
```
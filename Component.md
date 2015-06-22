### component option ###

  * Invocation

```
component=<app 1>,w,c|<app 2>,w,c|...|<app N>,w,c
```

Notice that 'app1' and 'app2' must be the exact context root of your application. The numbers that follow the context root are 'warning' and 'critical' threshold values. For each web application, the current active HTTP session count is compared to the specified thresholds.

You can also call the component test with a wildcard character:

```
component=*,100,150
```

Notice the 'warning' and 'critical' thresholds will be the same for all applications though.

  * Output

With the above example, you will get this kind of output:

```
app1=101 app2=158
```

The output gives the current active HTTP session count for each application specified by the user. If you're using a wildcard, WebLogic internal components won't appear in the application list.

  * Specific messages

If the test raises an alert, this kind of message appears at the beginning of the performance data:

```
server1 is in RUNNING state, status CRITICAL - HTTP session count: app1(101), app2(158)
```

In this example, both applications appears in the list as they both raised an alert ('warning' alert for app1 and 'critical' alert for app2). The overall status is the most critical.
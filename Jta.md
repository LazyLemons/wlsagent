### jta option ###

  * Invocation

```
jta=ActiveTransactions,15,30
```

15 is the warning threshold for the transaction active count. 30 is the critical threshold for the transaction active count.

These values are arbitrary values that are compared to the active transaction count of your application server.

  * Output

```
ActiveTransactions=3
```

The output gives the current active transaction count of your application server.

  * Specific messages

If the test raises an alert, this kind of message appears at the beginning of the performance data:

```
server1 is in RUNNING state, status WARNING - transaction active count (18)
```
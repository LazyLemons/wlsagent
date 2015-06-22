### Bash sample script ###

```
#!/bin/bash

PORT="9090"

DATA=$(wget -q -O - http://localhost:${PORT}/wlsagent/WLSAgent --post-data=$@ 2> /dev/null)

[ $? != 0 ] && exit 2
echo ${DATA} | awk -F\| '{ print $2"|"$3  ; exit $1 }'
exit $?
```
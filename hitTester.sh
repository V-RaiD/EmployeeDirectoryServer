#!/usr/bin/bash
curl -X POST -i http://$1:$2/user -H 'content-type:application/json' -d '{"eid": '$3', "sid": '$4', "lim": '$5'}'

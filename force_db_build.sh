#! /bin/bash

#curl --data "user_cred=jarusk&password=password" http://localhost:18001/login

for i in $( seq -f "%02g" 1 25); do
		curl -i -s -k  -X $'POST' --data-binary $'user_cred=jarusk&password=password' "http://localhost:180$i/login" &
done

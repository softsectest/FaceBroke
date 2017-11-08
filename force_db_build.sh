#! /bin/bash

#curl --data "user_cred=jarusk&password=password" http://localhost:18001/login

for i in $( seq -f "%02g" 1 25); do
		curl -i -s -k  -X $'POST' \
		    -H $'User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:58.0) Gecko/20100101 Firefox/58.0' -H $'Referer: http://localhost:18001/register' -H $'Content-Type: application/x-www-form-urlencoded' -H $'Upgrade-Insecure-Requests: 1' \
			    -b $'csrftoken=yvEZvrtkNA4V8yuBUkYqtD4Qqy9xM2A00tK7fxZXgbcxtlLVA2Gbfn8EqbNbaqEa; JSESSIONID=B58237172678C36EB4B70B8B89BC8C9D' \
				    --data-binary $'null=null&user_cred=jarusk&password=password' \
					    "http://localhost:180$i/login" &
done

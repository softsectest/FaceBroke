#! /bin/bash


docker build -t facebroke .

for i in $(seq -f "%02g" 1 25); do
		docker run -e POSTGRES_USER=pguser -e POSTGRES_PASSWORD=pguser -e POSTGRES_DB=pgdb -v "dbconfigfb_$i:/etc/postgresql" -v "dblogfb_$i:/var/log/postgresql" -v "dbdatafb_$i:/var/lib/postgresql/data" --expose 5432 --rm --name "pg_$i" -d postgres:10.0-alpine
		docker run -e HIBERNATE_FB_URL=jdbc:postgresql://db/pgdb -p "180$i:8080" --rm --name "fb_$i" --link "pg_$i:db" -d facebroke
done

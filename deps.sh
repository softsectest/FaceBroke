#! /bin/bash

sudo yum install -y htop docker git

sudo curl -L https://github.com/docker/compose/releases/download/1.17.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose

sudo usermod -aG docker ec2-user

sudo service docker start

#!/bin/bash

set -e

sudo apt-get update
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"

sudo apt-get update

sudo apt-cache madison docker-ce
sudo apt-get install -y docker-ce=18.06.3~ce~3-0~ubuntu

sudo usermod -aG docker $USER

cat > daemon.json <<EOF
{
  "registry-mirrors": ["http://f1361db2.m.daocloud.io", "https://registry.docker-cn.com", "https://docker.mirrors.ustc.edu.cn"]
}
EOF

sudo mv daemon.json /etc/docker/daemon.json


sudo systemctl restart docker

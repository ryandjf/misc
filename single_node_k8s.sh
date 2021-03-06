#!/bin/bash

set -e

sudo apt-get update && sudo apt-get install -y apt-transport-https

curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | sudo apt-key add - 

sudo add-apt-repository "deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main"

sudo apt-get update
apt-cache madison kubeadm
sudo apt-get install -y kubelet=1.13.4-00 kubeadm=1.13.4-00 kubectl=1.13.4-00
sudo apt-mark hold kubelet kubeadm kubectl

swapoff -a

KUBE_VERSION=v1.13.4
KUBE_PAUSE_VERSION=3.1
ETCD_VERSION=3.2.24
DNS_VERSION=1.2.6

K8S_GCR_URL=k8s.gcr.io
PRIVATE_REGISTRY_URL=docker.io/ryandjf

images=(
    kube-apiserver:${KUBE_VERSION}
    kube-controller-manager:${KUBE_VERSION}
    kube-scheduler:${KUBE_VERSION}
    kube-proxy:${KUBE_VERSION}
    pause:${KUBE_PAUSE_VERSION}
    etcd:${ETCD_VERSION}
    coredns:${DNS_VERSION}
)


for image in ${images[@]} ; do
  docker pull "$PRIVATE_REGISTRY_URL/google-containers.${image}"
  docker tag  "$PRIVATE_REGISTRY_URL/google-containers.${image}" $K8S_GCR_URL/$image
  docker rmi "$PRIVATE_REGISTRY_URL/google-containers.${image}"
done

docker images

sudo kubeadm init --kubernetes-version v1.13.4 --apiserver-cert-extra-sans="52.80.91.32,ec2-52-80-91-32.cn-north-1.compute.amazonaws.com.cn"

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

sudo sysctl net.bridge.bridge-nf-call-iptables=1
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"

kubectl taint nodes --all node-role.kubernetes.io/master-

kubectl get nodes

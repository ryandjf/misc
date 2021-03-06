#!/bin/bash

set -e

# Check version in https://kubernetes.io/docs/reference/setup-tools/kubeadm/kubeadm-init/
# Search "Running kubeadm without an internet connection"
# For running kubeadm without an internet connection you have to pre-pull the required master images for the version of choice:
# Check the versions and images needed for certian version of k8s, please run 'kubeadm config images list --kubernetes-version v1.12.2'

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

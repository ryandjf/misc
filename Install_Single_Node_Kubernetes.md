## Install Single Node Kubernetes on AWS

### 更换apt源为163 (Optional)

```
cat /etc/apt/sources.list

deb http://mirrors.163.com/ubuntu/ xenial main
deb-src http://mirrors.163.com/ubuntu/ xenial main

deb http://mirrors.163.com/ubuntu/ xenial-updates main
deb-src http://mirrors.163.com/ubuntu/ xenial-updates main

deb http://mirrors.163.com/ubuntu/ xenial universe
deb-src http://mirrors.163.com/ubuntu/ xenial universe
deb http://mirrors.163.com/ubuntu/ xenial-updates universe
deb-src http://mirrors.163.com/ubuntu/ xenial-updates universe

deb http://mirrors.163.com/ubuntu/ xenial-security main
deb-src http://mirrors.163.com/ubuntu/ xenial-security main
deb http://mirrors.163.com/ubuntu/ xenial-security universe
deb-src http://mirrors.163.com/ubuntu/ xenial-security universe

```

### 安装Docker

https://docs.docker.com/install/linux/docker-ce/ubuntu/

注意选择Kubernetes支持的版本 https://github.com/kubernetes/kubernetes/blob/master/cmd/kubeadm/app/util/system/docker_validator_test.go

```

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

```

### 配置docker mirror (Optional)

创建（或修改）/etc/docker/daemon.json。官方中国镜像速度还行。
```
{
    "registry-mirrors": ["http://f1361db2.m.daocloud.io", "https://registry.docker-cn.com", "https://docker.mirrors.ustc.edu.cn"],
    "insecure-registries" : ["ec2-52-81-55-170.cn-north-1.compute.amazonaws.com.cn:30500"]
}
```

重启docker服务
```
sudo systemctl restart docker
```

### 增加kubernetes aliyun镜像源

```

sudo apt-get update && sudo apt-get install -y apt-transport-https

curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | sudo apt-key add - 

sudo add-apt-repository "deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main"

```
or 
```

sudo apt-get update && sudo apt-get install -y apt-transport-https
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -

echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update
sudo apt-get install -y kubectl

```

### 安装kubeadm/kubelet/kubectl
```

sudo apt-get update
apt-cache madison kubeadm
sudo apt-get install -y kubelet=1.13.4-00 kubeadm=1.13.4-00 kubectl=1.13.4-00
sudo apt-mark hold kubelet kubeadm kubectl

```

如果你有多台机器，非master节点不需要安装kubeadm/kubectl。当然装了也没啥坏处。

### 关闭swap

kubernetes要求必须关闭swap。
```

swapoff -a

```
同时把/etc/fstab包含swap那行记录删掉。


### 拉取k8s的包并tag

首先查询下当前版本需要哪些docker image。
```

kubeadm config images list --kubernetes-version v1.13.4

```
You may see following:
```

k8s.gcr.io/kube-apiserver:v1.13.4
k8s.gcr.io/kube-controller-manager:v1.13.4
k8s.gcr.io/kube-scheduler:v1.13.4
k8s.gcr.io/kube-proxy:v1.13.4
k8s.gcr.io/pause:3.1
k8s.gcr.io/etcd:3.2.24
k8s.gcr.io/coredns:1.2.6

```

必须要指定版本，这样kubeadm才不会去连k8s.io。kubeadm init同理。


推荐使用USTC的镜像

```

docker pull gcr.mirrors.ustc.edu.cn/google-containers/kube-controller-manager:v1.13.4
docker pull gcr.mirrors.ustc.edu.cn/google-containers/kube-apiserver:v1.13.4
docker pull gcr.mirrors.ustc.edu.cn/google-containers/kube-scheduler:v1.13.4
docker pull gcr.mirrors.ustc.edu.cn/google-containers/kube-proxy:v1.13.4
docker pull gcr.mirrors.ustc.edu.cn/google-containers/pause:3.1
docker pull gcr.mirrors.ustc.edu.cn/google-containers/etcd:3.2.24
docker pull gcr.mirrors.ustc.edu.cn/google-containers/coredns:1.2.6

docker tag gcr.mirrors.ustc.edu.cn/google-containers/kube-controller-manager:v1.13.4 k8s.gcr.io/kube-controller-manager:v1.13.4
docker tag gcr.mirrors.ustc.edu.cn/google-containers/kube-apiserver:v1.13.4 k8s.gcr.io/kube-apiserver:v1.13.4
docker tag gcr.mirrors.ustc.edu.cn/google-containers/kube-scheduler:v1.13.4 k8s.gcr.io/kube-scheduler:v1.13.4
docker tag gcr.mirrors.ustc.edu.cn/google-containers/kube-proxy:v1.13.4 k8s.gcr.io/kube-proxy:v1.13.4
docker tag gcr.mirrors.ustc.edu.cn/google-containers/pause:3.1 k8s.gcr.io/pause:3.1
docker tag gcr.mirrors.ustc.edu.cn/google-containers/etcd:3.2.24 k8s.gcr.io/etcd:3.2.24
docker tag gcr.mirrors.ustc.edu.cn/google-containers/coredns:1.2.6 k8s.gcr.io/coredns:1.2.6

docker rmi gcr.mirrors.ustc.edu.cn/google-containers/kube-controller-manager:v1.13.4
docker rmi gcr.mirrors.ustc.edu.cn/google-containers/kube-apiserver:v1.13.4
docker rmi gcr.mirrors.ustc.edu.cn/google-containers/kube-scheduler:v1.13.4
docker rmi gcr.mirrors.ustc.edu.cn/google-containers/kube-proxy:v1.13.4
docker rmi gcr.mirrors.ustc.edu.cn/google-containers/pause:3.1
docker rmi gcr.mirrors.ustc.edu.cn/google-containers/etcd:3.2.24
docker rmi gcr.mirrors.ustc.edu.cn/google-containers/coredns:1.2.6

```

如果使用anjia0532的镜像，机器人自动跟官方同步，非常及时。
```
docker pull anjia0532/google-containers.kube-controller-manager:v1.12.2
docker pull anjia0532/google-containers.kube-apiserver:v1.12.2
docker pull anjia0532/google-containers.kube-scheduler:v1.12.2
docker pull anjia0532/google-containers.kube-proxy:v1.12.2
docker pull anjia0532/google-containers.pause:3.1
docker pull anjia0532/google-containers.etcd:3.2.24
docker pull anjia0532/google-containers.coredns:1.2.6

docker tag anjia0532/google-containers.kube-controller-manager:v1.12.2 k8s.gcr.io/kube-controller-manager:v1.12.2
docker tag anjia0532/google-containers.kube-apiserver:v1.12.2 k8s.gcr.io/kube-apiserver:v1.12.2
docker tag anjia0532/google-containers.kube-scheduler:v1.12.2 k8s.gcr.io/kube-scheduler:v1.12.2
docker tag anjia0532/google-containers.kube-proxy:v1.12.2 k8s.gcr.io/kube-proxy:v1.12.2
docker tag anjia0532/google-containers.pause:3.1 k8s.gcr.io/pause:3.1
docker tag anjia0532/google-containers.etcd:3.2.24 k8s.gcr.io/etcd:3.2.24
docker tag anjia0532/google-containers.coredns:1.2.6 k8s.gcr.io/coredns:1.2.6

docker rmi anjia0532/google-containers.kube-controller-manager:v1.12.2
docker rmi anjia0532/google-containers.kube-apiserver:v1.12.2
docker rmi anjia0532/google-containers.kube-scheduler:v1.12.2
docker rmi anjia0532/google-containers.kube-proxy:v1.12.2
docker rmi anjia0532/google-containers.pause:3.1
docker rmi anjia0532/google-containers.etcd:3.2.24
docker rmi anjia0532/google-containers.coredns:1.2.6
```

### 开始安装master

好了，万事俱备，开始安装master。
```

sudo kubeadm init --kubernetes-version v1.13.4 --apiserver-cert-extra-sans="52.80.13.76,ec2-52-80-13-76.cn-north-1.compute.amazonaws.com.cn"

```

To start using your cluster, you need to run the following as a regular user:
```

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

```

### 准备计算资源

因为我只有1个节点，要“计算资源”的话，就只能将master节点的taint去掉，否则普通的Pod默认不会调度上来。
```

kubectl taint nodes --all node-role.kubernetes.io/master-


```
如果你有多个节点的话，不需要去掉master的taint。其他节点参照上面的准备阶段在各个节点上做好准备工作以后，只要再Join一下就行了。Join命令在kubeadm init的输出信息里有。

```

kubeadm join --token={token} {master ip}

```

```

sudo systemctl restart docker

```

## 安装weave scope
我这里准备使用weave Network（主要是想用下weave scope，它只支持weave类型的网络）。
注意如果是flannel网络方案，必须要设置--pod-network-cidr 10.244.0.0/16，其他类型的网络，请参考官方的说明。
部署weave网络

```

sudo sysctl net.bridge.bridge-nf-call-iptables=1
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"


```


### Unable to connect to the server: x509: certificate is valid for (Optional)

One option is to tell kubectl that you don't want the certificate to be validated. Obviously this brings up security issues but I guess you are only testing so here you go:

```

kubectl --insecure-skip-tls-verify --context=employee-context get pods

```

The better option is to fix the certificate. Easiest if you reinitialize the cluster by running kubeadm reset on all nodes including the master and then do

```

kubeadm init --apiserver-cert-extra-sans=114.215.201.87

```

It's also possible to fix that certificate without wiping everything, but that's a bit more tricky. Execute something like this on the master as root:

```
rm /etc/kubernetes/pki/apiserver.*
kubeadm alpha phase certs all --apiserver-advertise-address=0.0.0.0 --apiserver-cert-extra-sans=52.80.8.7
docker rm -f `docker ps -q -f 'name=k8s_kube-apiserver*'`
systemctl restart kubelet
```


### Kubernete Web UI (Optional)
```
kubectl create -f https://raw.githubusercontent.com/kubernetes/dashboard/master/aio/deploy/recommended/kubernetes-dashboard.yaml

kubectl proxy

http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/.

```


```
Unable to update cni config: No networks found in /etc/cni/net.d
journalctl -xe
```


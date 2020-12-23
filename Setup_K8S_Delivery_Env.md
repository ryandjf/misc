# Setup the cluster

## Setup EKS
* Setup AWS named profile by folloiwng [Named Profile](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html)
  ```
  export AWS_PROFILE=sea 
  export AWS_REGION=ap-southeast-1
  ```
* Create EKS cluster
  ```
  export CLUSTER_NAME=twcs-delivery

  eksctl create cluster --name=${CLUSTER_NAME} --node-type m5.xlarge --version=1.18 --nodes=3 
  ```
* Check cluster nodes
  ```
  kubectl get node
  ```
* Setup mirror proxy (optional)

  Check section 2.4 of [步骤2-创建EKS集群](https://github.com/aws-samples/eks-workshop-greater-china/blob/master/china/2020_EKS_Launch_Workshop/%E6%AD%A5%E9%AA%A42-%E5%88%9B%E5%BB%BAEKS%E9%9B%86%E7%BE%A4.md) 
  
  [amazon-api-gateway-mutating-webhook-for-k8](https://github.com/aws-samples/amazon-api-gateway-mutating-webhook-for-k8)

* [Setup helm](https://eksworkshop.com/beginner/060_helm/)

## Dashboard
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml
```

[Creating sample user](https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md)

```
kubectl proxy

http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/.
```

## Prometheus (Optional)
```

helm install prometheus-release stable/prometheus-operator

kubectl port-forward $(kubectl get pods --selector=app=grafana --output=jsonpath="{.items..metadata.name}") 3000

```

## Setup Istio (Optional)

  * [Customizable Install with Helm](https://istio.io/docs/setup/install/helm/)

  * [Knative Istio Setup](https://knative.dev/docs/install/installing-istio)

# Setup DevOps Env

* Create namespace for devops
  ```
  kubectl create namespace devops
  ```
* Setup Clustr Role Binding
```
cat operator-clusterrolebinding.yaml
```
```
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: operator-clusterrolebinding
subjects:
- kind: ServiceAccount
  name: default
  namespace: devops
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: ""
```
```
kubectl create -f operator-clusterrolebinding.yaml
```
* [Deploy jenkins](https://eksworkshop.com/intermediate/210_jenkins/)
  ```
  helm install jenkins-release stable/jenkins --namespace devops --set rbac.create=true,agent.resources.limits.memory=2Gi,master.servicePort=80,master.serviceType=LoadBalancer,master.installPlugins={kubernetes:1.28.3\,workflow-aggregator:2.6\,workflow-job:2.40\,credentials-binding:1.24\,git:4.4.5\,htmlpublisher:1.25\,blueocean:1.24.3\,influxdb:2.5\,command-launcher:1.5\,jdk-tool:1.4\,workflow-basic-steps:2.23}
  ```
  Get the login url
  ```
  export JENKINS_SERVICE_IP=$(kubectl get svc --namespace devops jenkins-release --template "{{ range (index .status.loadBalancer.ingress 0) }}{{ . }}{{ end }}")
  echo http://$JENKINS_SERVICE_IP/login  
  ```
  Get the password
  
  ```
  printf $(kubectl get secret --namespace devops jenkins-release -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode);echo
  ```
* Setup InfluxDB
Add Four Key Metrics InfluxDB target in Manage Jenkins -> Configure System -> InfluxDB Targets (replace password)

```
Description: 4KeyMetrics_InfluxDB
URL: https://influxdb.4keymetrics.com
Username: admin
Password: XXXXXXXX
Database: FourKeyMetrics

```

* Setup SonarQube
  ```
  helm repo add oteemocharts https://oteemo.github.io/charts
  
  helm install sonarqube --namespace devops \
    --set service.externalPort=9000 \
    --set service.type=ClusterIP \
    --set plugins.install.0=https://binaries.sonarsource.com/Distribution/sonar-java-plugin/sonar-java-plugin-6.3.0.21585.jar \
    --set plugins.install.1=https://binaries.sonarsource.com/Distribution/sonar-javascript-plugin/sonar-javascript-plugin-6.2.0.12043.jar \
    --set plugins.install.2=https://binaries.sonarsource.com/Distribution/sonar-typescript-plugin/sonar-typescript-plugin-2.1.0.4359.jar \
    oteemocharts/sonarqube
  ```
* Setup CI Database
```
helm install devops-db-release --namespace devops \
    --set mysqlDatabase=product_database \
    --set service.port=3306 \
    stable/mysql;

printf $(kubectl get secret --namespace devops devops-db-release-mysql -o jsonpath="{.data.mysql-root-password}" | base64 --decode);echo
```

# Setup Dev Env
## Create namespace for dev env
  ```
  kubectl create namespace dev
  ```
## Setup MySQL cluster
Check [Stateful containers using StatefulSets](https://eksworkshop.com/beginner/170_statefulset/)

or 

```
helm install dev-db-release --namespace dev \
    --set mysqlDatabase=product_database \
    --set service.port=3306 \
    stable/mysql;

printf $(kubectl get secret --namespace dev dev-db-release-mysql -o jsonpath="{.data.mysql-root-password}" | base64 --decode);echo
```

# Setup the pipeline

## Craeate Maven Cache PVC
```
  kubectl create -f maven-with-cache-pvc.yml -n devops
  kubectl create -f gradle-with-cache-pvc.yml -n devops
```

## Docker Registry and Secret

### Docker Hub
* Option 1: Docker Hub Secret

```
  kubectl create secret docker-registry jenkins-docker-secret \ 
    --docker-server=https://index.docker.io/v1/
    --docker-username=<username>  \
    --docker-password=<password> \
    --docker-email=<email-address> \
    --namespace=devops
```

* Option 2: Docker Secret for Kaniko

Get your docker registry user and password encoded in base64
```
echo -n USER:PASSWORD | base64
```

Create a config.json file with your Docker registry url and the previous generated base64 string
```
{
    "auths": {
        "https://index.docker.io/v1/": {
            "auth": "xxxxxxxxxxxxxxx"
        }
    }
}
```
Run kaniko with the config.json inside /kaniko/.docker/config.json

```
kubectl create secret generic docker-config-secret -n devops --from-file=config.json

or 

kubectl create configmap docker-config -n devops --from-file=config.json

```

### AWS ECR

[Pushing to Amazon ECR](https://github.com/GoogleContainerTools/kaniko#pushing-to-amazon-ecr)

* Create ECR config
```
cat config.json
```
```
{
    "credHelpers": {
        "521593154378.dkr.ecr.ap-southeast-1.amazonaws.com": "ecr-login"
    }
}
```

```
kubectl create configmap docker-config  -n devops --from-file=<path to config.json>
```

* Create AWS secret, Option 1
```
kubectl create secret generic aws-secret -n devops --from-file=<path to .aws/credentials>
```

* Create AWS secret, Option 2
```
helm install --name aws-ecr-credential architectminds/aws-ecr-credential \
  --set-string aws.account=<aws account nubmer> \
  --set aws.region=<aws region> \
  --set aws.accessKeyId=<base64> \
  --set aws.secretAccessKey=<base64> \
  --set targetNamespace=default
```
That chart will create a secret object names aws-registry.

In you kubernetes deployment use imagePullSecrets: aws-registry

## Create Pipeline
[Example Repo](https://github.com/ryandjf/example-product-service)

* Update MySQL connection url and password.

* Create Jenkins pipeline

* Notes:
  
  You may get OOMKilled pod while running the build pipeline. Go into Manage Jenkins -> Configure System on your jenkins box under Cloud -> Images -> Kubernetes Pod Template -> Containers -> Container Template -> EnvVars, click "Advanced" (It's just above "delete container")
  
  Change the value of "Limit Memory" to 2Gi.

# EKS Persistent Storage
[How do I use persistent storage in Amazon EKS?](https://aws.amazon.com/premiumsupport/knowledge-center/eks-persistent-storage/)

## Amazon EBS CSI Driver
[EBS CSI Driver](https://eksworkshop.com/beginner/170_statefulset/ebs_csi_driver/)

### Configure IAM Policy

```
curl -sSL -o ebs-cni-policy.json https://raw.githubusercontent.com/kubernetes-sigs/aws-ebs-csi-driver/v0.4.0/docs/example-iam-policy.json

export EBS_CNI_POLICY_NAME="Amazon_EBS_CSI_Driver"

aws iam create-policy \
  --region ${AWS_REGION} \
  --policy-name ${EBS_CNI_POLICY_NAME} \
  --policy-document file://ebs-cni-policy.json

export EBS_CNI_POLICY_ARN=$(aws --region ${AWS_REGION} iam list-policies --query 'Policies[?PolicyName==`'$EBS_CNI_POLICY_NAME'`].Arn' --output text)

```

### Configure IAM Role for Service Account
```
eksctl utils associate-iam-oidc-provider --region=$AWS_REGION --cluster=${CLUSTER_NAME} --approve

eksctl create iamserviceaccount --cluster ${CLUSTER_NAME} \
  --name ebs-csi-controller-irsa \
  --namespace kube-system \
  --attach-policy-arn $EBS_CNI_POLICY_ARN \
  --override-existing-serviceaccounts \
  --approve

```

### Deploy EBS CSI Driver
```
for file in kustomization.yml deployment.yml attacher-binding.yml provisioner-binding.yml; do
  curl -sSLO https://raw.githubusercontent.com/aws-samples/eks-workshop/master/content/beginner/170_statefulset/ebs_csi_driver.files/$file
done

kubectl apply -k ./ebs_csi_driver


```

### Define Storage Class
```
curl -sSL https://eksworkshop.com/beginner/170_statefulset/storageclass.files/mysql-storageclass.yml

kubectl create -f ./templates/mysql-storageclass.yml

kubectl describe storageclass mysql-gp2

```

# Cleanup
```

helm uninstall 

```

# References
[AWS EKS Workshop](https://eksworkshop.com/)
[AWS China EKS Workshop](https://github.com/aws-samples/eks-workshop-greater-china)
[Debug Init Containers](https://kubernetes.io/docs/tasks/debug-application-cluster/debug-init-containers/)

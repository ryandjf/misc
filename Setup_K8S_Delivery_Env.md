# Setup the cluster
* Setup AWS named profile by folloiwng [Named Profile](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html)
    ```
    export AWS_PROFILE=cnn 
    ```
* Create EKS cluster
  ```
  CLUSTER_NAME=twcs-delivery

  eksctl create cluster --name=${CLUSTER_NAME} --node-type m5.xlarge --nodes=2
  ```
* Check cluster nodes
  ```
  kubectl get node
  ```
* Setup mirror proxy (optional)

  Check section 2.4 of [步骤2-创建EKS集群](https://github.com/aws-samples/eks-workshop-greater-china/blob/master/china/2020_EKS_Launch_Workshop/%E6%AD%A5%E9%AA%A42-%E5%88%9B%E5%BB%BAEKS%E9%9B%86%E7%BE%A4.md) 
* Create namespace for devops
  ```
  kubectl create namespace devops
  ```
* [Setup helm](https://eksworkshop.com/beginner/060_helm/)
* [Deploy jenkins](https://eksworkshop.com/intermediate/210_jenkins/)
  ```
  helm install cicd stable/jenkins --namespace devops --set rbac.create=true,Agent.resources.limits.memory=2Gi,master.servicePort=80,master.serviceType=LoadBalancer
  ```
  Get the login url
  ```
  export SERVICE_IP=$(kubectl get svc --namespace devops cicd-jenkins --template "{{ range (index .status.loadBalancer.ingress 0) }}{{ . }}{{ end }}")
  echo http://$SERVICE_IP/login
  ```
  Get the password
  
  ```
  printf $(kubectl get secret --namespace devops cicd-jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode);echo
  ```

# Setup MySQL cluster
Check [Stateful containers using StatefulSets](https://eksworkshop.com/beginner/170_statefulset/)
# Setup the pipeline

[Example Repo](https://github.com/ryandjf/example-product-service)

You may get OOMKilled pod while running the build pipeline. Go into Manage Jenkins -> Configure System on your jenkins box under Cloud -> Images -> Kubernetes Pod Template -> Containers -> Container Template -> EnvVars, click "Advanced" (It's just above "delete container")

Change the value of "Limit Memory" to 2Gi.

Save the config and replay your job. 

This can be worked around by adding an override to the Jenkins install, something like:
```
helm upgrade -i --reuse-values --recreate-pods jenkins-test --namespace jenkins-test \
--set Master.AdminPassword=admin \
--set Agent.AlwaysPullImage=true \
--set Agent.resources.limits.memory=2Gi \
--set rbac.install=true \
stable/jenkins
```

# Setup SonarQube
  ```
  helm repo add oteemocharts https://oteemo.github.io/charts
  
  helm install sonarqube oteemocharts/sonarqube --namespace devops --set service.externalPort=9000,service.type=LoadBalancer
  ```
# References
[AWS EKS Workshop](https://eksworkshop.com/)
[AWS China EKS Workshop](https://github.com/aws-samples/eks-workshop-greater-china)
[Debug Init Containers](https://kubernetes.io/docs/tasks/debug-application-cluster/debug-init-containers/)

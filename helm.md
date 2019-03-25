# Helm

## Helm Init
```

helm init --upgrade -i registry.cn-hangzhou.aliyuncs.com/google_containers/tiller:v2.13.0 --stable-repo-url https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts

```

## Linter to ensure best practices
```

helm lint ./deploy/example-product-service

```

## Dry-run of a helm install
```

helm install --dry-run --debug ./deploy/example-product-service

```

## Deploy the chart
```

helm install --name example-product-service ./deploy/example-product-service --set service.type=NodePort

```

## Package the chart
```

helm package ./deploy/example-product-service

```

## Install from package
```

helm install --name example3 mychart-0.1.0.tgz --set service.type=NodePort

```

## Run a local repository to serve our chart.
```

helm serve

```

## See your chart in the local repository and install it from there:
```

helm search local
helm install --name example4 local/mychart --set service.type=NodePort

```

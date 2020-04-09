# JenkinsX

```

jx get env

jx get pipelines

jx get applications -e staging

jx create docker auth --host "https://index.docker.io/v1/" --user "ryandjf" --secret "XXXX" --email "ryandjf@gmail.com"


Watch pipeline activity via:    jx get activity -f acme-springapp1 -w
Browse the pipeline log via:    jx get build logs ryandjf/acme-springapp1/master
You can list the pipelines via: jx get pipelines
When the pipeline is complete:  jx get applications

git checkout -b wip
git commit -am 'wip'
git push --set-upstream origin wip

jx create pullrequest --title 'First PR' --body 'what I can say' --batch-mode

jx promote acme-springapp1 --version 0.0.1 --env production --batch-mode

vi ~/.jx/gitAuth.yaml

jx add app jx-app-ui --version 0.1.49

jx ui -p 8080

jx create devpod

jx sync


GIT_USER=ryandjf

git clone https://github.com/$GIT_USER/jenkins-x-kubernetes

cd jenkins-x-kubernetes

git remote add upstream https://github.com/jenkins-x-buildpacks/jenkins-x-kubernetes

git clone https://github.com/$GIT_USER/jenkins-x-classic

cd jenkins-x-classic

git remote add upstream https://github.com/jenkins-x-buildpacks/jenkins-x-classic



jx edit buildpack \
-u https://github.com/$GIT_USER/jenkins-x-classic \
-r master \
-b

jx edit buildpack \
-u https://github.com/$GIT_USER/jenkins-x-kubernetes \
-r master \
-b


jx import --pack tutorial-build-pack --batch-mode


```


## XCode
```

xcode-select --install

```

## Pip
To install pip, securely download get-pip.py. :
```

curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py

python get-pip.py

```

## Homebrew
```

/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

```

Set up mirror
···
#该镜像是 Homebrew 的 formula 索引的镜像 
cd "$(brew --repo)"
git remote set-url origin https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/brew.git

cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"
git remote set-url origin https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/homebrew-core.git

brew update

#该镜像是 Homebrew 二进制预编译包的镜像
echo 'export HOMEBREW_BOTTLE_DOMAIN=https://mirrors.tuna.tsinghua.edu.cn/homebrew-bottles' >> ~/.bash_profile
source ~/.bash_profile
···

Restore
```
cd "$(brew --repo)"

git remote set-url origin https://github.com/Homebrew/brew.git

 

cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"

git remote set-url origin https://github.com/Homebrew/homebrew-core

 

brew update
```

## Git
```

brew install git
git --version
git config --global user.name "Junfeng Dai"
git config --global user.email "ryandjf@hotmail.com"


```

## Zsh

```

brew install zsh

```

## Install oh-my-zsh
```

sh -c "$(curl -fsSL https://raw.github.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"

```

## Sublime Text
```

sudo ln -s "/Applications/Sublime Text.app/Contents/SharedSupport/bin/subl" /usr/local/bin/sublime
export PATH=/usr/local/bin:$PATH

```

To use Sublime Text as the editor for many commands that prompt for input, set your EDITOR environment variable:

`export EDITOR='subl -w'`

Specifying -w will cause the subl command to not exit until the file is closed.

## Bash Profile
```

export PATH=$PATH:/usr/local/bin

export PATH=$PATH:/Users/jfdai/Tools/gradle-5.0/bin

source ~/.bash_profile

```


## AWS CLI
```

sudo -H pip install awscli
aws --version

```

## aws-iam-authenticator
```
curl -o aws-iam-authenticator https://amazon-eks.s3-us-west-2.amazonaws.com/1.11.5/2018-12-06/bin/darwin/amd64/aws-iam-authenticator

curl -o aws-iam-authenticator https://amazon-eks.s3-us-west-2.amazonaws.com/1.10.3/2018-07-26/bin/linux/amd64/aws-iam-authenticator

sudo mv ./aws-iam-authenticator /usr/local/bin/aws-iam-authenticator
sudo chmod +x /usr/local/bin/aws-iam-authenticator
/usr/local/bin/aws-iam-authenticator --help

go get -u -v github.com/kubernetes-sigs/aws-iam-authenticator/cmd/aws-iam-authenticator

```

## kubectl
```

brew install kubernetes-cli

curl -o kubectl https://amazon-eks.s3-us-west-2.amazonaws.com/1.11.5/2018-12-06/bin/darwin/amd64/kubectl

curl -LO "https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/darwin/amd64/kubectl"
sudo mv -f ./kubectl /usr/local/bin/kubectl
sudo chmod +x /usr/local/bin/kubectl

kubectl version

```

## helm
```

brew install kubernetes-helm

helm init --history-max 200
helm init --service-account tiller --upgrade -i registry.cn-hangzhou.aliyuncs.com/google_containers/tiller:v2.13.0 --stable-repo-url https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
helm init --upgrade -i registry.cn-hangzhou.aliyuncs.com/google_containers/tiller:v2.13.0 --stable-repo-url https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts

```
Kubernetes helm配置国内镜像源

删除默认的源
```
helm repo remove stable
```
增加新的国内镜像源

```
helm repo add stable https://burdenbear.github.io/kube-charts-mirror/
```
或者
```
helm repo add stable https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
```

查看helm源添加情况
```
helm repo list
```

## kops

```

brew install kops

```

## eksctl
```

curl --silent --location "https://github.com/weaveworks/eksctl/releases/download/latest_release/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
sudo chmod +x /usr/local/bin/eksctl

```

## virtualenv
```

sudo pip install virtualenv

```

Using virtualenv:

```

virtualenv <DIR>
source <DIR>/bin/activate

```


## Miniconda

```

conda info --envs
conda create --name python35 python=3.5.6

source activate python35
source deactivate

conda search beautifulsoup4
conda install beautifulsoup4
conda list

```

## Terraform
```

curl -o terraform.zip https://releases.hashicorp.com/terraform/0.11.11/terraform_0.11.11_darwin_amd64.zip
unzip terraform.zip -d /usr/local/bin
rm -rf terraform.zip
terraform --version

```

## Ansible
```

sudo launchctl limit maxfiles unlimited
sudo pip install ansible
ansible --version
ansible-playbook --version

```

## PEM to Pub
```

openssl rsa -in key.pem -pubout -out key.pub
ssh-keygen -y -f key.pem > key.pub

```

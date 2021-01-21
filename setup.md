# Package Manager
## Homebrew
```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Set up mirror
```
#该镜像是 Homebrew 的 formula 索引的镜像 

cd "$(brew --repo)"
git remote set-url origin https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/brew.git

cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"
git remote set-url origin https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/homebrew-core.git

brew update

#该镜像是 Homebrew 二进制预编译包的镜像

echo 'export HOMEBREW_BOTTLE_DOMAIN=https://mirrors.tuna.tsinghua.edu.cn/homebrew-bottles' >> ~/.bash_profile
source ~/.bash_profile
```

Restore
```
cd "$(brew --repo)"

git remote set-url origin https://github.com/Homebrew/brew.git

cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"

git remote set-url origin https://github.com/Homebrew/homebrew-core

brew update
```

## Pip
To install pip, securely download get-pip.py. :
```
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py

python get-pip.py
```

## SDKMAN
```
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk version
```

# Brew Tools
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

## kubectl
```
brew install kubectl

kubectl version --client
```

## helm
```
brew install helm
```
配置国内镜像源

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

# Others

## XCode
```
xcode-select --install
```

## Install oh-my-zsh
```
sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"

git clone https://github.com/zsh-users/zsh-syntax-highlighting.git ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-syntax-highlighting

git clone https://github.com/zsh-users/zsh-autosuggestions ${ZSH_CUSTOM:-~/.oh-my-zsh/custom}/plugins/zsh-autosuggestions

```
Themes:
```
ZSH_THEME="agnoster"
```

Plugins might begin to look like this:
```
plugins=(
  aws
  brew
  docker
  git
  node
  npm
  pip
  z
  zsh-syntax-highlighting
  zsh-autosuggestions
)
```

Install Powerline fonts
```
# clone
git clone https://github.com/powerline/fonts.git --depth=1
# install
cd fonts
./install.sh
# clean-up a bit
cd ..
rm -rf fonts
```

Change font in iTerm2 -> Preferences -> Profiles -> Text to 'Meslo LG S for Powerline'
Change iTerm2 -> Preferences -> Profiles -> Window, Columns to 204, Rows to 47.

# AWS
## AWS CLI
```
curl "https://awscli.amazonaws.com/AWSCLIV2.pkg" -o "AWSCLIV2.pkg"
sudo installer -pkg AWSCLIV2.pkg -target /
```
Or
```
sudo -H pip install awscli
aws --version

```

## aws-iam-authenticator
```
brew install aws-iam-authenticator
```
Or

```
curl -o aws-iam-authenticator https://amazon-eks.s3-us-west-2.amazonaws.com/1.11.5/2018-12-06/bin/darwin/amd64/aws-iam-authenticator

curl -o aws-iam-authenticator https://amazon-eks.s3-us-west-2.amazonaws.com/1.10.3/2018-07-26/bin/linux/amd64/aws-iam-authenticator

sudo mv ./aws-iam-authenticator /usr/local/bin/aws-iam-authenticator
sudo chmod +x /usr/local/bin/aws-iam-authenticator
/usr/local/bin/aws-iam-authenticator --help

go get -u -v github.com/kubernetes-sigs/aws-iam-authenticator/cmd/aws-iam-authenticator

```

## eksctl
```
brew tap weaveworks/tap
brew install weaveworks/tap/eksctl
```
Or

```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
sudo chmod +x /usr/local/bin/eksctl

```

# Python
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

## virtualenv
```

sudo pip install virtualenv

```

Using virtualenv:

```

virtualenv <DIR>
source <DIR>/bin/activate

```

# Usage Tips
## PEM to Pub
```
openssl rsa -in key.pem -pubout -out key.pub
ssh-keygen -y -f key.pem > key.pub

```

## Bash Profile
```

export PATH=$PATH:/usr/local/bin

export PATH=$PATH:/Users/jfdai/Tools/gradle-5.0/bin

source ~/.bash_profile

```

## Sublime Text
```

sudo ln -s "/Applications/Sublime Text.app/Contents/SharedSupport/bin/subl" /usr/local/bin/sublime
export PATH=/usr/local/bin:$PATH

```

To use Sublime Text as the editor for many commands that prompt for input, set your EDITOR environment variable:

`export EDITOR='subl -w'`

Specifying -w will cause the subl command to not exit until the file is closed.


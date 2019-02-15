#!/bin/bash

set -e

GOCD_VERSION=v18.8.0

NEXUS_VERSION=3.14.0

images=(
# GoCD
    gocd/gocd-agent-centos-7:${GOCD_VERSION}
    gocd/gocd-server:${GOCD_VERSION}

# Jenkins
    jenkins/jenkins:lts
    ubuntu:16.04

# Nexus
    sonatype/nexus3:${NEXUS_VERSION}
)


for image in ${images[@]} ; do
  docker pull "${image}"
done

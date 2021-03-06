#!/bin/bash

set -e

# https://www.docker.elastic.co/
ELASTIC_VERSION=6.2.3
ELASTIC_REGISTRY=docker.elastic.co
PRIVATE_REGISTRY=docker.io/ryandjf

images=(
    elasticsearch/elasticsearch:${ELASTIC_VERSION}
    logstash/logstash:${ELASTIC_VERSION}
    kibana/kibana:${ELASTIC_VERSION}
    beats/filebeat:${ELASTIC_VERSION}
    beats/metricbeat:${ELASTIC_VERSION}
)


for image in ${images[@]} ; do
  private_image_name="$PRIVATE_REGISTRY/${image/\//.}" #replace first slash occurence with dot
  docker pull "${private_image_name}"
  docker tag  "${private_image_name}" "$ELASTIC_REGISTRY/$image"
  docker rmi  "${private_image_name}"
done

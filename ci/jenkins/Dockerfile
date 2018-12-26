FROM jenkins/jenkins:lts

USER root
RUN apt-get update && apt-get install -y \
  curl \
  && rm -rf /var/lib/apt/lists/*

ENV DOCKER_CLIENT_VERSION=1.12.3
ENV DOCKER_API_VERSION=1.24
RUN curl -fsSL https://get.docker.com/builds/Linux/x86_64/docker-${DOCKER_CLIENT_VERSION}.tgz \
  | tar -xzC /usr/local/bin --strip=1 docker/docker

#RUN /usr/local/bin/install-plugins.sh
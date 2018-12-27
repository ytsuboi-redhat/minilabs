FROM jenkins/jenkins:lts

USER root
RUN apt-get update && apt-get install -y locales fonts-noto curl vim firefox-esr fluxbox x11vnc xvfb && rm -rf /var/lib/apt/lists/*

RUN ln -sf  /usr/share/zoneinfo/Asia/Tokyo /etc/localtime
RUN locale-gen ja_JP.UTF-8

ENV LANG ja_JP.UTF-8

ENV DOCKER_CLIENT_VERSION=1.12.3
ENV DOCKER_API_VERSION=1.24
RUN curl -fsSL https://get.docker.com/builds/Linux/x86_64/docker-${DOCKER_CLIENT_VERSION}.tgz \
  | tar -xzC /usr/local/bin --strip=1 docker/docker

EXPOSE 5900

COPY plugins.txt /var/jenkins_home/
RUN /usr/local/bin/install-plugins.sh < /var/jenkins_home/plugins.txt
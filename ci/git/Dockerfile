FROM centos:7

RUN yum install -y git git-daemon
RUN mkdir -p /opt/git/todo-app.git

WORKDIR /opt/git/todo-app.git

RUN git --bare init

EXPOSE 9418

CMD ["git", "daemon", "--enable=receive-pack", "--reuseaddr", "--base-path=/opt/git/", "--export-all", "/opt/git/", "--verbose"]
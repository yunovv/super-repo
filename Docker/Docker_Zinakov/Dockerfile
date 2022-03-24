FROM ubuntu:latest
RUN apt-get update
RUN apt-get install -y python3
RUN apt-get install -y python3-pip
RUN python3 -m pip install pyyaml
RUN python3 -m pip install flask
COPY app.py parameters.yml /tmp/
ADD templates /tmp/templates
WORKDIR /tmp
#ENTRYPOINT sleep infinity
ENTRYPOINT python3 app.py

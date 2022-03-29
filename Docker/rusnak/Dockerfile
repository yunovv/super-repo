FROM ubuntu:latest

RUN apt update && apt upgrade && apt install python3.8 -y && apt install python3-pip -y


WORKDIR /app
COPY ./requirements.txt /app/
COPY ../example_app_yunov/parameters.yml /app/
COPY ../example_app_yunov/app.py /app/
COPY ../example_app_yunov/templates/index.html /app/tempate/

RUN pip3 install -r requirements.txt --user

ENTRYPOINT python3.8 app.py
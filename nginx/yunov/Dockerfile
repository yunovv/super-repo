FROM nginx:latest

COPY ./config_static/nginx.conf /etc/nginx/
COPY ./config_static/my_service.conf /etc/nginx/conf.d/
COPY ./config_static/proxy_service.conf /etc/nginx/conf.d/

COPY ./config_static/static/ /etc/nginx/static_out/
FROM nginx:1.23.2-alpine

# 删除nginx 默认配置
RUN rm /etc/nginx/conf.d/default.conf
# 添加我们自己的配置 default.conf 在下面
COPY ./default.conf /etc/nginx/conf.d/

# 暴露端口,需要和服务的端口一致
EXPOSE 80

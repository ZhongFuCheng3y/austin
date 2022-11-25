#!/bin/bash


####################################
# @description 添加RabbitMQ节点到集群
# 可参考 https://www.rabbitmq.com/clustering.html#creating-ram
# @params $? => 代表上一个命令执行后的退出状态: 0->成功,1->失败
# @example => sh init-rabbitmq.sh
# @author topsuder
# @date 2022/11/16 14:24
####################################


# reset first node
echo "Reset first rabbitmq node."
docker exec rabbitmq-1 /bin/bash -c 'rabbitmqctl stop_app'
docker exec rabbitmq-1 /bin/bash -c 'rabbitmqctl reset'
docker exec rabbitmq-1 /bin/bash -c 'rabbitmqctl start_app'


# build cluster
echo "Starting to build rabbitmq cluster with two ram nodes."
docker exec rabbitmq-2 /bin/bash -c 'rabbitmqctl stop_app'
docker exec rabbitmq-2 /bin/bash -c 'rabbitmqctl reset'
# 参数“--ram”表示设置为内存节点，忽略此参数默认为磁盘节点
docker exec rabbitmq-2 /bin/bash -c 'rabbitmqctl join_cluster rabbit@my-rabbit-1'
# docker exec rabbitmq-2 /bin/bash -c 'rabbitmqctl join_cluster --ram rabbit@my-rabbit-1'
docker exec rabbitmq-2 /bin/bash -c 'rabbitmqctl start_app'


# check cluster status
#echo "Check cluster status:"
#docker exec rabbitmq-1 /bin/bash -c 'rabbitmqctl cluster_status'
#docker exec rabbitmq-2 /bin/bash -c 'rabbitmqctl cluster_status'


#echo "Starting to create user."
#docker exec rabbitmq-1 /bin/bash -c 'rabbitmqctl add_user admin admin@123'

#echo "Set tags for new user."
#docker exec rabbitmq-1 /bin/bash -c 'rabbitmqctl set_user_tags admin administrator'

#echo "Grant permissions to new user."
#docker exec rabbitmq-1 /bin/bash -c "rabbitmqctl set_permissions -p '/' admin '.*' '.*' '.*'"

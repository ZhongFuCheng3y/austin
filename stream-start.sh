#!/bin/bash

docker cp ./austin-stream/target/austin-stream-0.0.1-SNAPSHOT.jar austin_jobmanager_1:/opt/
docker exec -ti austin_jobmanager_1 flink run /opt/austin-stream-0.0.1-SNAPSHOT.jar

# local test
# docker cp ./austin-stream-0.0.1-SNAPSHOT.jar flink_docker_jobmanager_1:/opt/
# docker exec -ti flink_docker_jobmanager_1 flink run /opt/austin-stream-0.0.1-SNAPSHOT.jar


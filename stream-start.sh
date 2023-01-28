#!/bin/bash

docker cp ./austin-stream/target/austin-stream-0.0.1-SNAPSHOT.jar austin_jobmanager_1:/opt/
docker exec -ti austin_jobmanager_1 flink run /opt/austin-stream-0.0.1-SNAPSHOT.jar

# stream local test
# docker cp ./austin-stream-0.0.1-SNAPSHOT.jar austin_jobmanager_1:/opt/austin-stream-test-0.0.1-SNAPSHOT.jar
# docker exec -ti austin_jobmanager_1 flink run /opt/austin-stream-test-0.0.1-SNAPSHOT.jar


# data-house
# ./flink run austin-data-house-0.0.1-SNAPSHOT.jar


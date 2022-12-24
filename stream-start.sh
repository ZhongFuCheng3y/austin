#!/bin/bash

docker cp ./austin-stream/target/austin-stream-0.0.1-SNAPSHOT.jar austin_jobmanager_1:/opt/

docker exec -ti austin_jobmanager_1 flink run /opt/austin-stream-0.0.1-SNAPSHOT.jar

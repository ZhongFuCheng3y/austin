package com.java3y.austin.datahouse;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.ExecutionCheckpointingOptions;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

import java.time.Duration;

/**
 * flink启动类
 *
 * @author 3y
 */
public class AustinBootStrap {

    public static void main(String[] args) throws Exception {

        TableEnvironment tableEnv = TableEnvironment.create(EnvironmentSettings.inStreamingMode());
        tableEnv.getConfig().getConfiguration().set(ExecutionCheckpointingOptions.CHECKPOINTING_MODE, CheckpointingMode.EXACTLY_ONCE);
        tableEnv.getConfig().getConfiguration().set(ExecutionCheckpointingOptions.CHECKPOINTING_INTERVAL, Duration.ofSeconds(20));
        tableEnv.getConfig().setSqlDialect(SqlDialect.HIVE);

        String catalogName = "my_hive";

        HiveCatalog catalog = new HiveCatalog(catalogName, "austin_hive", null);

        tableEnv.registerCatalog(catalogName, catalog);
        tableEnv.useCatalog(catalogName);

        tableEnv.executeSql("CREATE DATABASE IF NOT EXISTS austin");
        tableEnv.executeSql("DROP TABLE IF EXISTS austin.message_anchor_info");

        tableEnv.executeSql("create table austin.message_anchor_info(" +
                "ids String,\n" +
                "state String,\n" +
                "businessId String,\n" +
                "log_ts Timestamp(3),\n" +
                "WATERMARK FOR log_ts AS log_ts -INTERVAL '5' SECOND" +
                ")WITH(" +
                " 'connector' = 'kafka',\n" +
                "'topic' = 'austinTraceLog',\n" +
                " 'properties.bootstrap.servers' = 'kafka_ip:9092',\n" +
                "'properties.group.id' = 'flink1',\n" +
                "'scan.startup.mode' = 'earliest-offset',\n" +
                "'format' = 'json',\n" +
                "'json.fail-on-missing-field' = 'false',\n" +
                "'json.ignore-parse-errors' = 'true'" +
                ")");


//        tableEnv.executeSql("CREATE DATABASE IF NOT EXISTS hive_tmp");
//        tableEnv.executeSql("DROP TABLE IF EXISTS hive_tmp.log_hive");
//
//        tableEnv.executeSql(" CREATE TABLE hive_tmp.log_hive (\n" +
//                "                     user_id STRING,\n" +
//                "                     order_amount DOUBLE\n" +
//                "           ) PARTITIONED BY (\n" +
//                "                     dt STRING,\n" +
//                "                     hr STRING\n" +
//                "           ) STORED AS PARQUET\n" +
//                "             TBLPROPERTIES (\n" +
//                "                    'sink.partition-commit.trigger' = 'partition-time',\n" +
//                "                    'sink.partition-commit.delay' = '1 min',\n" +
//                "                    'format' = 'json',\n" +
//                "                    'sink.partition-commit.policy.kind' = 'metastore,success-file',\n" +
//                "                    'partition.time-extractor.timestamp-pattern'='$dt $hr:00:00'" +
//                "           )");
//        tableEnv.getConfig().setSqlDialect(SqlDialect.DEFAULT);
//
//        tableEnv.executeSql("" +
//                "        INSERT INTO hive_tmp.log_hive\n" +
//                "        SELECT\n" +
//                "               user_id,\n" +
//                "               order_amount,\n" +
//                "               DATE_FORMAT(log_ts, 'yyyy-MM-dd'), DATE_FORMAT(log_ts, 'HH')\n" +
//                "               FROM austin.message_anchor_info");

    }
}

package com.java3y.austin.datahouse;


import com.java3y.austin.datahouse.constants.DataHouseConstant;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

/**
 * hive启动类
 * <p>
 * 接受Kafka的消息 写入hive表中
 *
 * @author 3y
 */
public class AustinHiveBootStrap {

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1).enableCheckpointing(3000);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 1.初始化catalog
        String catalogName = DataHouseConstant.CATALOG_NAME;
        HiveCatalog catalog = new HiveCatalog(catalogName, DataHouseConstant.CATALOG_DEFAULT_DATABASE, null);
        tableEnv.registerCatalog(catalogName, catalog);
        tableEnv.useCatalog(catalogName);

        // 2.创建Kafka源表
        String kafkaSourceTableCreate = "DROP TABLE IF EXISTS " + DataHouseConstant.CATALOG_DEFAULT_DATABASE + "." + DataHouseConstant.KAFKA_SOURCE_TABLE_NAME;
        tableEnv.executeSql(kafkaSourceTableCreate);
        String kafkaSourceTableDdl = "CREATE TABLE <CATALOG_DEFAULT_DATABASE>.<KAFKA_SOURCE_TABLE_NAME> (\n" +
                "`ids` String,\n" +
                "`state` String,\n" +
                "`businessId` String,\n" +
                "`logTimestamp` String\n" +
                ") WITH (\n" +
                " 'connector' = 'kafka'," +
                " 'topic' = '<KAFKA_TOPIC>',\n" +
                " 'properties.bootstrap.servers' = '<KAFKA_IP_PORT>',\n" +
                " 'properties.group.id' = 'group_test_01',\n" +
                " 'format' = 'json',\n" +
                " 'json.fail-on-missing-field' = 'true',\n" +
                " 'json.ignore-parse-errors' = 'false',\n" +
                " 'scan.topic-partition-discovery.interval'='1s',\n" +
                " 'scan.startup.mode' = 'latest-offset')";
        kafkaSourceTableDdl = kafkaSourceTableDdl.replace("<CATALOG_DEFAULT_DATABASE>", DataHouseConstant.CATALOG_DEFAULT_DATABASE)
                .replace("<KAFKA_SOURCE_TABLE_NAME>", DataHouseConstant.KAFKA_SOURCE_TABLE_NAME)
                .replace("<KAFKA_IP_PORT>", DataHouseConstant.KAFKA_IP_PORT)
                .replace("<KAFKA_TOPIC>", DataHouseConstant.KAFKA_TOPIC);

        tableEnv.executeSql(kafkaSourceTableDdl);

        // 创建写入hive的表
        tableEnv.getConfig().setSqlDialect(SqlDialect.HIVE);
        tableEnv.executeSql("DROP TABLE IF EXISTS " + DataHouseConstant.CATALOG_DEFAULT_DATABASE + "." + DataHouseConstant.KAFKA_SINK_TABLE_NAME);
        String kafkaSinkTableDdl = "CREATE TABLE IF NOT EXISTS  <CATALOG_DEFAULT_DATABASE>.<KAFKA_SINK_TABLE_NAME> (\n" +
                "`ids` String,\n" +
                "`state` String,\n" +
                "`businessId` String,\n" +
                "`logTimestamp` String\n" +
                ") STORED AS PARQUET\n" +
                "TBLPROPERTIES (\n" +
                "  'sink.partition-commit.policy.kind' = 'metastore,success-file',\n" +
                "  'sink.partition-commit.trigger' = 'partition-time',\n" +
                "  'sink.partition-commit.delay' = '1 min',\n" +
                "  'sink.buffer-flush.max-rows'='10',\n" +
                "  'sink.buffer-flush.interval' = '5s'\n" +
                ")";
        kafkaSinkTableDdl = kafkaSinkTableDdl.replace("<CATALOG_DEFAULT_DATABASE>", DataHouseConstant.CATALOG_DEFAULT_DATABASE)
                .replace("<KAFKA_SINK_TABLE_NAME>", DataHouseConstant.KAFKA_SINK_TABLE_NAME);
        tableEnv.executeSql(kafkaSinkTableDdl);

        // 3. 将kafka_source 数据写入到kafka_sink 完成
        tableEnv.getConfig().setSqlDialect(SqlDialect.DEFAULT);
        tableEnv.executeSql("INSERT INTO " + DataHouseConstant.CATALOG_DEFAULT_DATABASE + "." + DataHouseConstant.KAFKA_SINK_TABLE_NAME + " SELECT ids,state,businessId,logTimestamp FROM " + DataHouseConstant.CATALOG_DEFAULT_DATABASE + "." + DataHouseConstant.KAFKA_SOURCE_TABLE_NAME);

    }
}

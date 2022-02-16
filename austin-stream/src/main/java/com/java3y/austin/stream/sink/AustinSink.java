package com.java3y.austin.stream.sink;

import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.domain.AnchorInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * mock
 */
@Slf4j
public class AustinSink extends RichSinkFunction<AnchorInfo> {

    @Override
    public void invoke(AnchorInfo value, Context context) throws Exception {

        log.error("sink consume value:{}", JSON.toJSONString(value));

    }
}

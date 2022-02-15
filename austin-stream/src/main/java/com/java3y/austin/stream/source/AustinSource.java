package com.java3y.austin.stream.source;

import com.java3y.austin.common.domain.AnchorInfo;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源  mock
 *
 * @author 3y
 */
public class AustinSource extends RichSourceFunction<AnchorInfo> {
    @Override
    public void run(SourceContext<AnchorInfo> sourceContext) throws Exception {
        List<AnchorInfo> anchorInfoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            anchorInfoList.add(AnchorInfo.builder()
                    .state(10).businessId(333L)
                    .timestamp(System.currentTimeMillis()).build());

        }
        for (AnchorInfo anchorInfo : anchorInfoList) {
            sourceContext.collect(anchorInfo);
        }
    }

    @Override
    public void cancel() {

    }
}

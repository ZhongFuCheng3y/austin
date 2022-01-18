package com.java3y.austin.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.java3y.austin.enums.AnchorState;
import lombok.Builder;
import lombok.Data;

/**
 * @author 3y
 * @date 2021/12/11
 * 去重服务所需要的参数
 */
@Builder
@Data
public class DeduplicationParam {
    /**
     * TaskIno信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重时间
     * 单位：秒
     */
    @JSONField(name = "time")
    private Long deduplicationTime;

    /**
     * 需达到的次数去重
     */
    @JSONField(name = "num")
    private Integer countNum;

    /**
     * 标识属于哪种去重
     */
    private AnchorState anchorState;
}

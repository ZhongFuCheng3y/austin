package com.java3y.austin.web.vo.amis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * https://aisuda.bce.baidu.com/amis/zh-CN/components/timeline#timeline-item
 *
 * @author 3y
 * 时间线 Vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLineItemVo {

    /**
     * items
     */
    private List<TimeLineItemVo.ItemsVO> items;

    /**
     * ItemsVO
     */
    @Data
    @Builder
    public static class ItemsVO {
        /**
         * time
         */
        private String time;
        /**
         * title
         */
        private String title;
        /**
         * detail
         */
        private String detail;
        /**
         * color
         */
        private String color;
        /**
         * icon
         */
        private String icon;
    }
}

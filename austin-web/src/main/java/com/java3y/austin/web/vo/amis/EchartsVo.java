package com.java3y.austin.web.vo.amis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 3y
 * 图表的Vo
 * https://aisuda.bce.baidu.com/amis/zh-CN/components/chart
 * https://www.runoob.com/echarts/echarts-setup.html
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EchartsVo {
    /**
     * title 标题
     */
    @JsonProperty
    private TitleVO title;
    /**
     * tooltip 提示
     */
    @JsonProperty
    private TooltipVO tooltip;
    /**
     * legend 图例
     */
    @JsonProperty
    private LegendVO legend;
    /**
     * xAxis x轴
     */
    @JsonProperty
    private XaxisVO xAxis;
    /**
     * yAxis y轴
     */
    @JsonProperty
    private YaxisVO yAxis;
    /**
     * series 系列列表
     * <p>
     * 每个系列通过 type 决定自己的图表类型
     */
    @JsonProperty
    private List<SeriesVO> series;

    /**
     * TitleVO
     */
    @Data
    @Builder
    public static class TitleVO {
        /**
         * text
         */
        private String text;
    }

    /**
     * TooltipVO
     */
    @Data
    @Builder
    public static class TooltipVO {
        private String color;
    }

    /**
     * LegendVO
     */
    @Data
    @Builder
    public static class LegendVO {
        /**
         * data
         */
        private List<String> data;
    }

    /**
     * XAxisVO
     */
    @Data
    @Builder

    public static class XaxisVO {
        /**
         * data
         */
        private List<String> data;
    }

    /**
     * YAxisVO
     */
    @Data
    @Builder
    public static class YaxisVO {
        private String type;
    }

    /**
     * SeriesVO
     */
    @Data
    @Builder
    public static class SeriesVO {
        /**
         * name
         */
        private String name;
        /**
         * type
         */
        private String type;
        /**
         * data
         */
        private List<Integer> data;
    }
}

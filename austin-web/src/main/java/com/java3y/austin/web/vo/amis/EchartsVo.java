package com.java3y.austin.web.vo.amis;

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
    private TitleVO title;
    /**
     * tooltip 提示
     */
    private TooltipVO tooltip;
    /**
     * legend 图例
     */
    private LegendVO legend;
    /**
     * xAxis x轴
     */
    private XAxisVO xAxis;
    /**
     * yAxis y轴
     */
    private YAxisVO yAxis;
    /**
     * series 系列列表
     * <p>
     * 每个系列通过 type 决定自己的图表类型
     */
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

    public static class XAxisVO {
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

    public static class YAxisVO {
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

package com.java3y.austin.web.vo.amis;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * amis的通用转化类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonAmisVo {

    private String type;

    private String label;

    private String value;

    private String name;

    private boolean required;

    private String size;

    private boolean addable;

    private boolean editable;

    private boolean needConfirm;

    /**
     * columns
     */
    @JSONField(name = "columns")
    private List<ColumnsDTO> columns;

    /**
     * ColumnsDTO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ColumnsDTO {
        /**
         * nameX
         */
        @JSONField(name = "name")
        private String name;
        /**
         * labelX
         */
        @JSONField(name = "label")
        private String label;

        /**
         * type
         */
        @JSONField(name = "type")
        private String type;
        /**
         * placeholder
         */
        @JSONField(name = "placeholder")
        private String placeholder;

        /**
         * type
         */
        @JSONField(name = "required")
        private Boolean required;

        @JSONField(name = "quickEdit")
        private Boolean quickEdit;

    }
}

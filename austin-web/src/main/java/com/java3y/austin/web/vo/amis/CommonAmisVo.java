package com.java3y.austin.web.vo.amis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

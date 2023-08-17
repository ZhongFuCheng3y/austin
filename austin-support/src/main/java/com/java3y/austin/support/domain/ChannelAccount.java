package com.java3y.austin.support.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author 3y
 * 渠道账号信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class ChannelAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账号名称
     */
    private String name;

    /**
     * 发送渠道
     *
     * @see com.java3y.austin.common.enums.ChannelType
     */
    private Integer sendChannel;

    /**
     * 账号配置
     */
    private String accountConfig;

    /**
     * 是否删除
     * 0：未删除
     * 1：已删除
     */
    private Integer isDeleted;

    /**
     * 账号拥有者
     */
    private String creator;

    /**
     * 创建时间 单位 s
     */
    private Integer created;

    /**
     * 更新时间 单位s
     */
    private Integer updated;

}

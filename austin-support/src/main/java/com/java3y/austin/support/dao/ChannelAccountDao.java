package com.java3y.austin.support.dao;


import com.java3y.austin.support.domain.ChannelAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 渠道账号信息 Dao
 *
 * @author 3y
 */
public interface ChannelAccountDao extends JpaRepository<ChannelAccount, Long> {


    /**
     * 查询 列表
     *
     * @param deleted     0：未删除 1：删除
     * @param channelType 渠道值
     * @param creator     创建者
     * @return
     */
    List<ChannelAccount> findAllByIsDeletedEqualsAndCreatorEqualsAndSendChannelEquals(Integer deleted, String creator, Integer channelType);

    /**
     * 查询 列表
     *
     * @param deleted     0：未删除 1：删除
     * @param channelType 渠道值
     * @return
     */
    List<ChannelAccount> findAllByIsDeletedEqualsAndSendChannelEquals(Integer deleted, Integer channelType);

    /**
     * 根据创建者检索相关的记录
     *
     * @param creator
     * @return
     */
    List<ChannelAccount> findAllByCreatorEquals(String creator);

    /**
     * 统计未删除的条数
     *
     * @param deleted
     * @return
     */
    Long countByIsDeletedEquals(Integer deleted);

}

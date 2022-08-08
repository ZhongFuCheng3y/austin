package com.java3y.austin.support.dao;


import com.java3y.austin.support.domain.ChannelAccount;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.domain.SmsRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 渠道账号信息 Dao
 *
 * @author 3y
 */
public interface ChannelAccountDao extends JpaRepository<ChannelAccount, Long> {


    /**
     * 查询 列表（分页)
     *
     * @param deleted     0：未删除 1：删除
     * @param channelType 渠道值
     * @return
     */
    List<ChannelAccount> findAllByIsDeletedEqualsAndSendChannelEquals(Integer deleted, Integer channelType);


    /**
     * 统计未删除的条数
     *
     * @param deleted
     * @return
     */
    Long countByIsDeletedEquals(Integer deleted);
}

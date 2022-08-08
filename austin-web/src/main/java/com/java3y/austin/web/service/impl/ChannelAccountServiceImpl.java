package com.java3y.austin.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.support.dao.ChannelAccountDao;
import com.java3y.austin.support.domain.ChannelAccount;
import com.java3y.austin.web.service.ChannelAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 3y
 */
@Service
public class ChannelAccountServiceImpl implements ChannelAccountService {

    @Autowired
    private ChannelAccountDao channelAccountDao;
    @Override
    public ChannelAccount save(ChannelAccount channelAccount) {
        if (channelAccount.getId() == null) {
            channelAccount.setCreated(Math.toIntExact(DateUtil.currentSeconds()));
            channelAccount.setIsDeleted(AustinConstant.FALSE);
        }
        channelAccount.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        return channelAccountDao.save(channelAccount);
    }

    @Override
    public List<ChannelAccount> queryByChannelType(Integer channelType) {
        return channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(AustinConstant.FALSE, channelType);
    }

    @Override
    public List<ChannelAccount> list() {
        return channelAccountDao.findAll();
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        channelAccountDao.deleteAllById(ids);
    }
}

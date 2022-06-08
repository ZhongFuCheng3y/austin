package com.java3y.austin.web.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.enums.FileType;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.web.service.MaterialService;
import com.java3y.austin.web.vo.UploadResponseVo;
import com.taobao.api.FileItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author 3y
 */
@Slf4j
@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String DING_DING_URL = "https://oapi.dingtalk.com/media/upload";

    @Override
    public BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType) {
        OapiMediaUploadResponse rsp;
        try {
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + sendAccount);
            DingTalkClient client = new DefaultDingTalkClient(DING_DING_URL);
            OapiMediaUploadRequest req = new OapiMediaUploadRequest();
            FileItem item = new FileItem(new StringBuilder().append(IdUtil.fastSimpleUUID()).append(file.getOriginalFilename()).toString(),
                    file.getInputStream());
            req.setMedia(item);
            req.setType(FileType.dingDingNameByCode(fileType));
            rsp = client.execute(req, accessToken);
            if (rsp.getErrcode() == 0L) {
                return new BasicResultVO(RespStatusEnum.SUCCESS, UploadResponseVo.builder().id(rsp.getMediaId()).build());
            }
            log.error("MaterialService#dingDingMaterialUpload fail:{}", rsp.getErrmsg());
        } catch (Exception e) {
            log.error("MaterialService#dingDingMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail("未知错误，联系管理员");
    }

}

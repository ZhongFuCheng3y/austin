package com.java3y.austin.web.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.constant.SendChanelUrlConstant;
import com.java3y.austin.common.dto.account.DingDingWorkNoticeAccount;
import com.java3y.austin.common.dto.account.EnterpriseWeChatRobotAccount;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.enums.EnumUtil;
import com.java3y.austin.common.enums.FileType;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.handler.domain.wechat.robot.EnterpriseWeChatRootResult;
import com.java3y.austin.support.utils.AccessTokenUtils;
import com.java3y.austin.support.utils.AccountUtils;
import com.java3y.austin.web.service.MaterialService;
import com.java3y.austin.web.utils.SpringFileUtils;
import com.java3y.austin.web.vo.UploadResponseVo;
import com.taobao.api.FileItem;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxCpErrorMsgEnum;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author 3y
 */
@Slf4j
@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private AccessTokenUtils accessTokenUtils;


    @Override
    public BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType) {
        OapiMediaUploadResponse rsp;
        try {
            DingDingWorkNoticeAccount account = accountUtils.getAccountById(Integer.valueOf(sendAccount), DingDingWorkNoticeAccount.class);
            String accessToken = accessTokenUtils.getAccessToken(ChannelType.DING_DING_WORK_NOTICE.getCode(), Integer.valueOf(sendAccount), account, false);
            DingTalkClient client = new DefaultDingTalkClient(SendChanelUrlConstant.DING_DING_UPLOAD_URL);
            OapiMediaUploadRequest req = new OapiMediaUploadRequest();
            FileItem item = new FileItem(new StringBuilder().append(IdUtil.fastSimpleUUID()).append(file.getOriginalFilename()).toString(),
                    file.getInputStream());
            req.setMedia(item);
            req.setType(EnumUtil.getDescriptionByCode(Integer.valueOf(fileType), FileType.class));
            rsp = client.execute(req, accessToken);
            if (rsp.isSuccess()) {
                return new BasicResultVO(RespStatusEnum.SUCCESS, UploadResponseVo.builder().id(rsp.getMediaId()).build());
            }
            log.error("MaterialService#dingDingMaterialUpload fail:{}", rsp.getErrmsg());
        } catch (Exception e) {
            log.error("MaterialService#dingDingMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR.getMsg());
    }

    @Override
    public BasicResultVO enterpriseWeChatRootMaterialUpload(MultipartFile multipartFile, String sendAccount, String fileType) {
        try {
            EnterpriseWeChatRobotAccount weChatRobotAccount = accountUtils.getAccountById(Integer.valueOf(sendAccount), EnterpriseWeChatRobotAccount.class);
            String key = weChatRobotAccount.getWebhook().substring(weChatRobotAccount.getWebhook().indexOf(CommonConstant.EQUAL_STRING) + 1);

            // 企业微信机器人 默认只上传"file"文件类型
            String url = SendChanelUrlConstant.ENTERPRISE_WE_CHAT_ROBOT_URL.replace("<KEY>", key).replace("<TYPE>", "file");
            String response = HttpRequest.post(url)
                    .form(IdUtil.fastSimpleUUID(), SpringFileUtils.getFile(multipartFile))
                    .execute().body();
            EnterpriseWeChatRootResult result = JSON.parseObject(response, EnterpriseWeChatRootResult.class);
            if (Integer.valueOf(WxCpErrorMsgEnum.CODE_0.getCode()).equals(result.getErrcode())) {
                return new BasicResultVO(RespStatusEnum.SUCCESS, UploadResponseVo.builder().id(result.getMediaId()).build());
            }
            log.error("MaterialService#enterpriseWeChatRootMaterialUpload fail:{}", result.getErrmsg());
        } catch (Exception e) {
            log.error("MaterialService#enterpriseWeChatRootMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR.getMsg());
    }

    @Override
    public BasicResultVO enterpriseWeChatMaterialUpload(MultipartFile multipartFile, String sendAccount, String fileType) {
        try {
            WxCpDefaultConfigImpl accountConfig = accountUtils.getAccountById(Integer.valueOf(sendAccount), WxCpDefaultConfigImpl.class);
            WxCpServiceImpl wxCpService = new WxCpServiceImpl();
            wxCpService.setWxCpConfigStorage(accountConfig);
            WxMediaUploadResult result = wxCpService.getMediaService()
                    .upload(EnumUtil.getDescriptionByCode(Integer.valueOf(fileType), FileType.class), SpringFileUtils.getFile(multipartFile));
            if (CharSequenceUtil.isNotBlank(result.getMediaId())) {
                return new BasicResultVO(RespStatusEnum.SUCCESS, UploadResponseVo.builder().id(result.getMediaId()).build());
            }
            log.error("MaterialService#enterpriseWeChatMaterialUpload fail:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("MaterialService#enterpriseWeChatMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR.getMsg());
    }
}

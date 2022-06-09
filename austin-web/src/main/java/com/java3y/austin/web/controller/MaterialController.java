package com.java3y.austin.web.controller;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.enums.FileType;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.web.service.MaterialService;
import com.java3y.austin.web.vo.DataParam;
import com.java3y.austin.web.vo.amis.UserTimeLineVo;
import com.taobao.api.FileItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 素材管理接口
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/material")
@Api("素材管理接口")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class MaterialController {


    @Autowired
    private MaterialService materialService;


    /**
     * 素材上传接口
     *
     * @param file        文件内容
     * @param sendAccount 发送账号
     * @param sendChannel 发送渠道
     * @param fileType    文件类型
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("/素材上传接口")
    public BasicResultVO uploadMaterial(@RequestParam("file") MultipartFile file, String sendAccount, Integer sendChannel, String fileType) {
        if (ChannelType.DING_DING_WORK_NOTICE.getCode().equals(sendChannel)) {
            return materialService.dingDingMaterialUpload(file, sendAccount, fileType);
        }
        return BasicResultVO.success();
    }

}

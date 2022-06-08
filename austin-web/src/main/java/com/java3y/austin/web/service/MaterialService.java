package com.java3y.austin.web.service;


import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.web.vo.MessageTemplateParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 素材接口
 *
 * @author 3y
 */
public interface MaterialService {


    /**
     * 钉钉素材上传
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType);



}

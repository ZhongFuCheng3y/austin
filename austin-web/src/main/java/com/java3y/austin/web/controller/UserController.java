package com.java3y.austin.web.controller;


import com.java3y.austin.web.annotation.AustinAspect;
import com.java3y.austin.web.annotation.AustinResult;
import com.java3y.austin.web.config.JwtConfig;
import com.java3y.austin.web.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录接口
 *
 * @author 3y
 */
@Slf4j
@AustinAspect
@RequestMapping("/user")
@RestController
@Api("用户controller")
public class UserController {

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * @param creator 创建人
     * @return
     */
    @GetMapping("/login")
    @ApiOperation("登录")
    @AustinResult
    public ResponseEntity<Map> login(String creator) {
        Map resultMap = new HashMap();
        resultMap.put("creator", creator);

        String token = JwtUtil.createToken(creator, jwtConfig.getKey(), 24 * 60 * 60);
        resultMap.put("token", token);
        return ResponseEntity.ok(resultMap);
    }
}

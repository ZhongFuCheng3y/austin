package com.java3y.austin;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

/**
 * @description: 测试基类, 主要用于前置处理和后置处理
 * @date: 2023/3/9 9:40
 * @author: pendj
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class BaseTestController {

    @Autowired
    protected MockMvc mvc;

    protected HttpHeaders headers;
    protected ResultActions resultActions;

    @BeforeEach
    public void beforeEach() {
        headers = new HttpHeaders();
        headers.add(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue());
    }

    @AfterEach
    public void afterEach() {
        try {
            MvcResult mvcResult = resultActions
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            log.info("response content: \n {}", content);
        } catch (Exception e) {
            log.error("error message: \n {}", e.getMessage());
        }
    }
}
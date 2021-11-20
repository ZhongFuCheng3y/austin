package com.java3y.austin.service;

import com.java3y.austin.domain.SendRequest;
import com.java3y.austin.domain.SendResponse;

/**
 * 发送接口
 *
 * @author 3y
 */
public interface SendService {


    SendResponse send(SendRequest sendRequest);


    SendResponse batchSend(SendRequest sendRequest);
}

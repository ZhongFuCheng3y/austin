package com.java3y.austin.controller;


import com.java3y.austin.service.CronTaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"定时任务接口"})
@RestController
public class XxlJobController {

    @Autowired
    private CronTaskService cronTaskService;


    @RequestMapping("/xxl/add/task")
    public Integer addTask() {

       // return taskService.saveTask();
        return null;
    }
}

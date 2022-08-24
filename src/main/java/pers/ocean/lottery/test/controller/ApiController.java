package pers.ocean.lottery.test.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.ocean.lottery.rpc.IActivityBooth;
import pers.ocean.lottery.rpc.req.ActivityReq;
import pers.ocean.lottery.rpc.res.ActivityRes;

/**
 * @Description
 * @Author ocean_wll
 * @Date 2021/9/27 10:26 上午
 */
@RestController
public class ApiController {

    private final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Reference(interfaceClass = IActivityBooth.class)
    private IActivityBooth activityBooth;

    @GetMapping("/rpc")
    public void test_rpc() {
        ActivityReq req = new ActivityReq();
        req.setActivityId(100002L);
        RpcContext.getContext().setAttachment("x-token", "1111");
        ActivityRes result = activityBooth.queryActivityById(req);
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }

    @PostMapping
    public void init() {
        for (int i = 0; i < 10; i++) {
            try {
                ActivityReq req = new ActivityReq();
                req.setActivityId(100002L);
                RpcContext.getContext().setAttachment("x-token", "1111");
                ActivityRes result = activityBooth.queryActivityById(req);
                logger.info("测试结果：{}", JSON.toJSONString(result));
            } catch (Throwable t) {
                System.out.println(t);
            }
        }

    }
}

package pers.ocean.lottery.test;

import com.alibaba.fastjson.JSON;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pers.ocean.lottery.rpc.IActivityBooth;
import pers.ocean.lottery.rpc.req.ActivityReq;
import pers.ocean.lottery.rpc.res.ActivityRes;

/**
 * @Description
 * @Author ocean_wll
 * @Date 2021/8/26 7:33 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Reference(interfaceClass = IActivityBooth.class)
    private IActivityBooth activityBooth;

    @Test
    public void test_rpc() {
        ActivityReq req = new ActivityReq();
        req.setActivityId(100002L);
        ActivityRes result = activityBooth.queryActivityById(req);
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }
}

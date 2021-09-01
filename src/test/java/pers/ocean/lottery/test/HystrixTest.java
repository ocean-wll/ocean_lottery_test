package pers.ocean.lottery.test;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.junit.Test;

/**
 * @Description
 * @Author ocean_wll
 * @Date 2021/9/1 2:05 下午
 */
public class HystrixTest {

    @Test
    public void method1() throws Exception {
        // 设置超时时间
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.coreSize", 8);
        ConfigurationManager.getConfigInstance().setProperty(
            "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 100000);
        // set the rolling percentile more granular so we see data change every second rather than every 10
        // seconds as is the default
        ConfigurationManager.getConfigInstance().setProperty(
            "hystrix.command.default.metrics.rollingPercentile.numBuckets", 60);

        //每个Command对象只能调用一次,不可以重复调用,
        //重复调用对应异常信息:This instance can only be executed once. Please instantiate a new instance.
        HelloWorldCommand helloWorldCommand = new HelloWorldCommand("Synchronous-hystrix");
        //使用execute()同步调用代码,效果等同于:helloWorldCommand.queue().get();
        String result = helloWorldCommand.execute();
        System.err.println("result=" + result);

        //
        //helloWorldCommand = new HelloWorldCommand("Asynchronous-hystrix");
        ////异步调用,可自由控制获取结果时机,
        //Future<String> future = helloWorldCommand.queue();
        //
        ////get操作不能超过command定义的超时时间,默认:1秒
        //result = future.get(10000, TimeUnit.MILLISECONDS);
        //System.err.println("result=" + result);
        System.err.println("mainThread=" + Thread.currentThread().getName());
    }

    class HelloWorldCommand extends HystrixCommand<String> {

        private final String name;

        public HelloWorldCommand(String name) {
            //最少配置:指定命令组名(CommandGroup)
            super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
            this.name = name;
        }

        @Override
        protected String run() throws InterruptedException {
            // 这里进行dubbo的泛化调用
            DubboGenericServiceTest dubboGenericServiceTest = new DubboGenericServiceTest();
            dubboGenericServiceTest.dubboServer();

            TimeUnit.SECONDS.sleep(5);

            // 依赖逻辑封装在run()方法中
            return "Hello " + name + " thread:" + Thread.currentThread().getName();
        }
    }
}

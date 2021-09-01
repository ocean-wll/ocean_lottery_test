package pers.ocean.lottery.test;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;

/**
 * @Description
 * @Author ocean_wll
 * @Date 2021/9/1 10:56 上午
 */
public class DubboGenericServiceTest {

    @Test
    public void method1() {
        ApplicationConfig app = new ApplicationConfig("simpleSpring");
        //RegistryConfig reg = new RegistryConfig("multicast://224.5.6.7:1234");
        RegistryConfig reg = new RegistryConfig("zookeeper://127.0.0.1:2181");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(app);
        bootstrap.registry(reg);
        bootstrap.start();

        try {
            // 引用远程服务
            ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
            // 弱类型接口名
            reference.setInterface("pers.ocean.samplespring.service.DubboHelloService");
            //reference.setGroup("dev");
            //reference.setVersion("1.0");
            //reference.setRetries(0);
            //// RpcContext中设置generic=gson
            //RpcContext.getContext().setAttachment("generic","gson");
            // 声明为泛化接口
            reference.setGeneric(true);
            reference.setCheck(false);
            GenericService genericService = ReferenceConfigCache.getCache().get(reference);
            // 传递参数对象的json字符串进行一次调用
            Object res = genericService.$invoke("sayHi", new String[] {}, new Object[] {});
            System.out.println(
                "result[setUser]：" + res); // 响应结果:result[setUser]：{name=Tom, class=com.xxx.api.service.User, age=24}
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            bootstrap.stop();
        }
    }
}

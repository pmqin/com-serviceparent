package com.pmqin.client;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pmqin.service.SayHelloToClient;

public class MyClient {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "applicationConsumer.xml" });
        context.start();
        // 获取服务器那边的bean
        SayHelloToClient demoService = (SayHelloToClient) context.getBean("demoService");

        System.out.println(demoService.sayHello("list"));
         List<String> getlist=demoService.getPermissions(78l);
         for (String string : getlist) {
            System.out.println(string);
        }
    }

}

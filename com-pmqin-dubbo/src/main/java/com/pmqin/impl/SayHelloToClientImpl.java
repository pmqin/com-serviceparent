package com.pmqin.impl;

import java.util.ArrayList;
import java.util.List;

import com.pmqin.service.SayHelloToClient;

public class SayHelloToClientImpl  implements SayHelloToClient{

    @Override
    public String sayHello(String hello) {
        System.out.println("我接收到了：" + hello);  
        return hello + "dddd你也好啊！！！" ;  
    }

    @Override
    public List<String> getPermissions(Long id) {
        List<String> demo = new ArrayList<String>();
        demo.add(String.format("Permission_%d", id - 1));
        demo.add(String.format("Permission_%d", id));
        demo.add(String.format("Permission_%d", id + 1));
        return demo;
    }

}

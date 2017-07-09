package com.pmqin.service;

import java.util.List;

public interface SayHelloToClient {
    public  String sayHello(String hello);

    public  List<String> getPermissions(Long id);
}

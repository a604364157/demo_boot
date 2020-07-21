package com.jjx.boot.common.handler;

import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author jiangjx
 */
public class Test {

    @SneakyThrows
    public static void main(String[] args) {
        //这种方式会按顺序执行所有注册的handler
        TaskRegister.INSTANCE.init(Arrays.asList(new DefaultTaskHandler(), new OtherTaskHandler()));
        TaskRegister.INSTANCE.getHandler().before(new HashMap<>(2));
    }

}

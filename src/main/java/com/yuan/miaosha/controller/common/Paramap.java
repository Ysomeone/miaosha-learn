package com.yuan.miaosha.controller.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yuan
 * @Date 2020/3/17 23:08
 * @Version 1.0
 */
public class Paramap extends HashMap<String, Object> implements Map<String, Object> {
    private static final long serialVersionUID = 1L;

    private Paramap() {
    }

    public static Paramap create() {
        return new Paramap();
    }

    @Override
    public Paramap put(String name, Object value) {
        super.put(name, value);
        return this;
    }

}
package com.yuan.miaosha.controller.common;

/**
 * @Author yuan
 * @Date 2020/5/13 22:54
 * @Version 1.0
 */
public class LuaScript {

    /**
     * -1 已经抢到红包   -2 红包被抢光  re 红包金额 ，keys[1]、keys[2]、keys[3]分别为存储小红包的key、红包领取记录key、用户id
     */
    public static String redLua = "if redis.call('hexists',KEYS[2],KEYS[3])  ~=0 then \n" +
            "  return '-1';\n" +
            " else \n" +
            "local re=redis.call('rpop',KEYS[1]);\n" +
            "if re then\n" +
            "redis.call('hset',KEYS[2],KEYS[3],1);\n" +
            "return re;\n" +
            "else\n" +
            "return '-2';\n" +
            "end\n" +
            "end";
}

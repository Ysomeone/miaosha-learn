package com.yuan.miaosha.controller.common;

/**
 * 状态码信息
 *
 * @author yuan
 */
public class ApiConstants {
    public static final String ok = "200000";
    /**
     * 不存在该商品
     */
    public static final String ERROR100 = "300100";
    /**
     * 你已经抢购成功，请勿重复购买
     */
    public static final String ERROR200 = "300200";

    /**
     * 该商品库存不足
     */
    public static final String ERROR300 = "300300";

    /**
     * 抢购失败，请重新操作
     */
    public static final String ERROR400 = "300400";
}

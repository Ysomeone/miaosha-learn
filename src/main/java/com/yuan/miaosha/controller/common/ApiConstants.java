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

    /**
     * 红包已经完了
     */
    public static final String ERROR500 = "300500";

    /**
     * 已经抢到红包
     */
    public static final String ERROR600 = "300600";

    /**
     * 人数过多，请再次尝试！
     */
    public static final String ERROR700 = "300700";

    /**
     * 请勿重新提交
     */
    public static final String ERROR800 = "300800";
}

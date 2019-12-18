
package com.haier.hailian.contract.util;

/**
 * 常量
 */
public class Constant {

    public static String JWT_AUTH_HEADER = "Authorization";
    public static String CODE_LOGINFAIL = "401001";

    /**
     * 链群状态 10待承接； 20抢入中 ；30已结束；40 已失效；
     */
    public enum ChainStatus {
        /**
         * 待支付
         */
        Wait(10),
        /**
         * 抢入中
         */
        Progress(20),
        /**
         * 已结束
         */
        Finished(30),
        /**
         * 已失效
         */
        Expired(40);

        private int value;

        ChainStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 链群状态 01高； 02中 ；03低
     */
    public enum ProductStru {
        /**
         * 高
         */
        High("01"),
        /**
         * 中
         */
        Midd("02"),
        /**
         * 低
         */
        Low("03");

        private String value;

        ProductStru(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}

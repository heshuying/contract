
package com.haier.hailian.contract.util;

/**
 * 常量
 */
public class Constant {

    public static String JWT_AUTH_HEADER = "Authorization";
    public static String CODE_LOGINFAIL = "401001";

    public static String CODE_VALIDFAIL = "401002";
    public static String MSG_VALIDFAIL = "数据校验失败";

    public static String CODE_DATA_NOTFOUND= "404001";
    public static String MSG_DATA_NOTFOUND= "数据不存在";

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

    /**
     * 合约因素编码枚举对象
     */
    public enum FactorCode {
        /**
         * 收入
         */
        Incom("T01001","收入"),
        /**
         * 销量
         */
        Sales("T01002","销量"),
        /**
         * 成本
         */
        Cost("T01003","成本"),
        /**
         * 利润率
         */
        Lrl("T02002","利润率"),
        /**
         * 毛利率
         */
        Mll("T02004","毛利率"),
        /**
         *利润额
         */
        Lre("T01017","利润额");
        /**
         * 高端占比
         */
        HighPercent("T03001","高端占比"),
        /**
         * 低端占比
         */
        LowPercent("T03002","低端占比");

        private String value;
        private String name;
        FactorCode(String value, String name) {
            this.value = value;
            this.name=name;
        }

        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
    }

    /**
     * 合约因素类型枚举
     */
    public enum FactorType {
        /**
         * 底限
         */
        Bottom("01"),
        /**
         * 抢单
         */
        Grab("02"),
        /**
         * E2E
         */
        E2E("03");

        private String value;

        FactorType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


}

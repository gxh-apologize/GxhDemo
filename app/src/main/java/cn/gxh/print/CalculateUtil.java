package cn.gxh.print;

import java.math.BigDecimal;

/**
 * 金额计算工具类
 * Created  by gxh on 2018/11/29 16:36
 */
public class CalculateUtil {

    public static BigDecimal toBigDecimalWithTwo(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2);
    }


    public static double getBigDecimalForStrReturnDouble(String str) {
        return getBigDecimalForStrReturnDouble(str, 2);
    }

    /**
     * String--->double
     *
     * @param str
     * @param scale 几位小数
     * @return
     */
    public static double getBigDecimalForStrReturnDouble(String str, Integer scale) {
        BigDecimal one = toBigDecimal(str);
        if (null != scale) {
            return one.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return one.doubleValue();
    }

    /**
     * double---->BigDecimal
     *
     * @param d
     * @return
     */
    public static BigDecimal toBigDecimal(double d) {
        if (d == 0) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(d);
        }
    }

    /**
     * String---->BigDecimal
     *
     * @param val
     * @return
     */
    public static BigDecimal toBigDecimal(String val) {
        if (val == null || "".equals(val.trim())) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(val);
        }
    }

    /**
     * BigDecimal的加法算法封装
     *
     * @param b1
     * @param bn
     * @return
     */
    public static BigDecimal safeAdd(BigDecimal b1, BigDecimal... bn) {
        if (null == b1) {
            b1 = BigDecimal.ZERO;
        }
        if (null != bn) {
            for (BigDecimal b : bn) {
                b1 = b1.add(null == b ? BigDecimal.ZERO : b);
            }
        }
        return b1;
    }


    /**
     * BigDecimal的减法算法封装
     *
     * @param isZero 当减法结果负数时返回什么   true:返回0   false:返回负数结果
     * @param b1
     * @param bn
     * @return
     */
    public static BigDecimal safeSubtract(Boolean isZero, BigDecimal b1, BigDecimal... bn) {
        if (null == b1) {
            b1 = BigDecimal.ZERO;
        }
        BigDecimal r = b1;
        if (null != bn) {
            for (BigDecimal b : bn) {
                r = r.subtract((null == b ? BigDecimal.ZERO : b));
            }
        }
        return isZero ? (r.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : r) : r;
    }

    /**
     * BigDecimal的除法算法封装
     * 结果小数后两位
     * 除数或者被除数为0时返回0
     *
     * @param b1
     * @param b2
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T extends Number> BigDecimal safeDivide(T b1, T b2, BigDecimal defaultValue) {
        if (null == b1 || null == b2) {
            return defaultValue;
        }
        try {
            return BigDecimal.valueOf(b1.doubleValue()).divide(BigDecimal.valueOf(b2.doubleValue()), 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T extends Number> BigDecimal safeDivide(T b1, T b2) {
        return safeDivide(b1, b2, BigDecimal.ZERO);
    }


    /**
     * BigDecimal的乘法算法封装
     *
     * @param b1
     * @param b2
     * @param <T>
     * @return
     */
    public static <T extends Number> BigDecimal safeMultiply(T b1, T b2) {
        if (null == b1 || null == b2) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    public static <T extends Number> BigDecimal safeMultiply(T b1, T b2,int scale) {
        if (null == b1 || null == b2) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }


    public static <T extends Number> BigDecimal safeMultiply(BigDecimal bigDecimal, T b2) {
        if (null == b2) {
            return BigDecimal.ZERO;
        }
        return bigDecimal.multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 抹分
     * <p>
     * 测试结果:
     * 11.23---->11.20
     * 0----->0.00
     * 11.26--->11.20
     *
     * @param bigDecimal
     * @return
     */
    public static BigDecimal setFen(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        //String value=String.valueOf(bigDecimal.doubleValue());
        return toBigDecimal(bigDecimal, 1, BigDecimal.ROUND_DOWN);
    }

    /**
     * 抹角
     * <p>
     * 测试结果：
     * 11.26---->11.00
     * 0.5--->0.00
     *
     * @param bigDecimal
     * @return
     */
    public static BigDecimal setJiao(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        //String value=String.valueOf(bigDecimal.doubleValue());
        return toBigDecimal(bigDecimal, 0, BigDecimal.ROUND_DOWN);
    }


    /**
     * 取整
     * <p>
     * 测试结果：
     * 11.3---->13
     *
     * @param bigDecimal
     * @return
     */
    public static BigDecimal setUp(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        return toBigDecimal(bigDecimal, 0, BigDecimal.ROUND_UP);
    }



    /**
     * 四舍五入到角
     * <p>
     * 45.67---->45.70
     * 45.64--->45.60
     *
     * @param bigDecimal
     * @return
     */
    public static BigDecimal setHalfUp(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        //String value=String.valueOf(bigDecimal.doubleValue());
        return toBigDecimal(bigDecimal, 1, BigDecimal.ROUND_HALF_UP);
    }


    public static BigDecimal toBigDecimal(BigDecimal bigDecimal, int scale, int mode) {
        return bigDecimal.setScale(scale, mode).setScale(2);
    }

    /**
     * 将字符串格式为0:00 ,四舍五入
     *
     * 测试:
     * 12-->12.00
     * 12.789-->12.79
     * 12.784-->12.78
     *
     * @param oldStr
     * @return
     */
    public static String strToStrWithTwo(String oldStr) {
        return String.format("%.2f", Double.valueOf(oldStr));
    }
}

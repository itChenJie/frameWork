package org.basis.framework.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @Description 金额校验工具类
 * @Author ChenWenJie
 * @Data 2021/6/11 2:55 下午
 **/
public class MoneyUtil {
    public static final DecimalFormat FORMATTER = new DecimalFormat("0.##");

    public static final DecimalFormat FORMATTER2 = new DecimalFormat("0.00");

    /** 金额转换比例， 分转元 */
    public static final BigDecimal AMT_RATE = new BigDecimal(0.01);

    /** 金额转换比例， 元转分 */
    public static final BigDecimal AMT_RATE_2 = new BigDecimal(100);

    /** 税率 */
    public static final BigDecimal TAX_RATE = new BigDecimal(1.06);

    /** 保留两位精度 */
    public static final Integer SCALE = 2;

    /** 不保留精度 */
    public static final Integer SCALE_2 = 0;

    public static final Long ONE_HUNDRED_L = 100L;

    public static final String YUAN = "元";

    /**
     *
     * @Title: format
     * @Description: 转换为字符串，保留两位精度
     * @param money
     * @return
     */
    public static final String format(BigDecimal money) {
        return FORMATTER.format(money);
    }

    /**
     *
     * @Title: format2
     * @Description: 转换为字符串，保留两位精度，位数不足，以0补充
     * @param money
     * @return
     */
    public static final String format2(BigDecimal money) {
        return FORMATTER2.format(money);
    }

    /**
     *
     * @param money
     * @param accuracy  小数点后位数
     * @param stripTrailingZeros 是否删除尾部0
     * @return
     */
    public static final String standardFormat(BigDecimal money, int accuracy, boolean stripTrailingZeros) {
        String format = getStandardFormatString(accuracy, stripTrailingZeros);
        NumberFormat nf = new DecimalFormat(format);
        return nf.format(money) ;
    }

    /**
     * 将分转换成元
     */
    public static BigDecimal transferAmount(BigDecimal amount){
        if(amount != null){
            // 保留两位小数
            amount = amount.multiply(AMT_RATE).setScale(SCALE,
                    BigDecimal.ROUND_HALF_UP);
        }
        return amount;
    }

    /**
     * 将元转换成分
     */
    public static BigDecimal transferAmount2(BigDecimal amount){
        if(amount != null){
            // 不保留小数
            amount = amount.multiply(AMT_RATE_2).setScale(SCALE_2,
                    BigDecimal.ROUND_HALF_UP);
        }
        return amount;
    }

    /**
     * 设置BigDecimal的精度
     */
    public static BigDecimal setAmountScale(BigDecimal amount){
        if(amount != null){
            amount = amount.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
        }
        return amount;
    }

    /**
     * 设置BigDecimal的精度
     */
    public static BigDecimal setAmountScale(Integer scale, BigDecimal amount){
        if(amount != null){
            amount = amount.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        return amount;
    }

    /**
     * 将分转换成元
     */
    public static String transferAmountToStr(BigDecimal amount){
        String result = null;
        if(amount != null){
            // 保留两位小数
            amount = amount.multiply(AMT_RATE).setScale(SCALE,
                    BigDecimal.ROUND_HALF_UP);
            result = FORMATTER.format(amount);
        }
        return result;
    }
    /**
     * 具体将分转换成元
     */
    public static String transferAmountLongToStr(Long number){
        String result = null;
        if(number != null){
            BigDecimal amount = new BigDecimal(number);
            // 保留两位小数
            amount = amount.multiply(AMT_RATE).setScale(SCALE,
                    BigDecimal.ROUND_HALF_UP);
            result = FORMATTER.format(amount);
        }
        return result;
    }

    /**
     * 保留两位精度
     * @param amount1
     * @param amount2
     * @return
     */
    public static String divideAmountRetString(Long amount1, Long amount2) {
        if(amount1 == null || amount2 == null) {
            return null;
        }
        BigDecimal resultAmt = new BigDecimal(amount1).divide(new BigDecimal(amount2), SCALE,
                BigDecimal.ROUND_HALF_UP);
        return resultAmt.toString();
    }

    /**
     *
     * @param accuracy 精度位数
     * @param stripTrailingZeros 是否删除尾部的0
     * @return
     */
    public static String getStandardFormatString(int accuracy, boolean stripTrailingZeros) {
        String formatStr = "#,##0";
        String accuracyStr = ".";
        if (stripTrailingZeros) {
            for (int i = 0; i < accuracy; i++) {
                accuracyStr += "#";
            }
        } else {
            for (int i = 0; i < accuracy; i++) {
                accuracyStr += "0";
            }
        }

        // 如果精度str不为空
        if(!".".equals(accuracyStr)) {
            formatStr += accuracyStr;
        }

        return formatStr;
    }
}

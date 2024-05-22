package org.basis.framework.encryption.desensitization.util;

import java.util.Objects;

/**
 * @author ChenJie
 * 脱敏工具栏
 */
public class SensitiveUtil {
    private static final String DEFAULT_MASK = "*";

    public SensitiveUtil() {
    }

    public static String desValue(String origin, int prefixLen, int suffixLen) {
        if (!Objects.isNull(origin) && !origin.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int i = 0;

            for(int n = origin.length(); i < n; ++i) {
                if (i < prefixLen) {
                    sb.append(origin.charAt(i));
                } else if (i > n - suffixLen - 1) {
                    sb.append(origin.charAt(i));
                } else {
                    sb.append("*");
                }
            }

            return sb.toString();
        } else {
            return origin;
        }
    }

    public static String chineseName(String fullName) {
        return fullName == null ? null : desValue(fullName, 1, 0);
    }

    public static String idCardNum(String id) {
        return desValue(id, 4, 2);
    }

    public static String mobilePhone(String num) {
        return desValue(num, 3, 4);
    }

    public static String email(String email) {
        if (!Objects.isNull(email) && !email.isEmpty()) {
            int index = email.indexOf("@");
            return index <= 1 ? email : desValue(email.substring(0, index), 1, 0).concat(email.substring(index));
        } else {
            return email;
        }
    }

    public static String bankCard(String bankCard) {
        return desValue(bankCard, 6, 4);
    }
}

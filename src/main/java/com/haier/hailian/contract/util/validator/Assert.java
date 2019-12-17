
package com.haier.hailian.contract.util.validator;


import com.haier.hailian.contract.dto.RException;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据校验
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RException(message);
        }
    }
}

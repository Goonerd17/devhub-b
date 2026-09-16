package teamdevhub.devhub.platform.shared.util;

import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

public final class StringUtil {

    private StringUtil() {}

    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }

        return string.isBlank();
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String defaultIfEmpty(String string, String defaultString) {
        if (isEmpty(string)) {
            return defaultString;
        }

        return string;
    }

    public static String truncate(String string, int maxLength) {
        if (isEmpty(string)) {
            return string;
        }

        if (maxLength < 0) {
            throw BusinessRuleException.of(ErrorCode.STRING_LENGTH_INVALID);
        }

        if (string.length() <= maxLength) {
            return string;
        }

        return string.substring(0, maxLength);
    }
}
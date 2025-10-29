package utils;
import java.math.BigDecimal;

public class BigDecimalUtils {

    /** 
     * Convert string to BigDecimal. 
     * If invalid, return defaultValue.
     */
    public static BigDecimal toBigDecimal(String str, BigDecimal defaultValue) {
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Overload: default defaultValue = 0
    public static BigDecimal toBigDecimal(String str) {
        return toBigDecimal(str, BigDecimal.ZERO);
    }
    
    public static boolean isValidBigDecimal(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

package utils;
import BUS.BUSManager;
import DTO.KhuyenMaiDTO;
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
    
    public static java.math.BigDecimal parseAmount(String text) {
        if (text == null) return java.math.BigDecimal.ZERO;
        String cleaned = text.replaceAll("[^0-9\\-\\.]", ""); // giữ số, dấu âm, dấu chấm thập phân (nếu có)
        if (cleaned.trim().isEmpty()) return java.math.BigDecimal.ZERO;
        try {
            // nếu có dấu thập phân '.' thì dùng BigDecimal trực tiếp,
            // nếu không có thì parse là nguyên
            return new java.math.BigDecimal(cleaned);
        } catch (NumberFormatException ex) {
            return java.math.BigDecimal.ZERO;
        }
    }

    // formatter cho hiển thị tiền (nhóm hàng nghìn, không có chữ thập phân)
    public static final java.text.NumberFormat currencyFormatter 
            = java.text.NumberFormat.getNumberInstance(new java.util.Locale("vi", "VN"));
    {
        currencyFormatter.setGroupingUsed(true);
        currencyFormatter.setMaximumFractionDigits(0);
        currencyFormatter.setMinimumFractionDigits(0);
    }
    
    public static String formatAmount(java.math.BigDecimal amount) {
        if (amount == null) return "";
        // đảm bảo không âm nếu không muốn âm
        java.math.BigDecimal display = amount.setScale(0, java.math.RoundingMode.HALF_UP);
        return currencyFormatter.format(display);
    }

    
}

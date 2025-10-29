package utils;

public class ValidationUtils {

    // Kiểm tra tên khách hàng (chỉ chứa chữ cái, khoảng trắng, tối thiểu 2 ký tự)
    public static boolean isValidCustomerName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        // Cho phép chữ cái, dấu cách, dấu tiếng Việt
        return name.matches("^[\\p{L} .'-]{2,}$");
    }

    // Kiểm tra số điện thoại (Việt Nam)
    // Hợp lệ: bắt đầu bằng 0 hoặc +84, sau đó là 9 hoặc 10 chữ số
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return phone.matches("^(0|\\+84)(\\d{9})$");
    }

    // Kiểm tra số tiền khách đưa (phải là số thực > 0)
    public static boolean isValidFloat(String amount) {
        if (amount == null || amount.trim().isEmpty()) return false;
        try {
            float value = Float.parseFloat(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Kiểm tra số tiền khách đưa (phải là số nguyên > 0)
    public static boolean isValidInt(String amount) {
        if (amount == null || amount.trim().isEmpty()) return false;
        try {
            int value = Integer.parseInt(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

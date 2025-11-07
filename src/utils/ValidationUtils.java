package utils;

import BUS.BUSManager;

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
    public static boolean isValidFloatBiggerThanZero(String amount) {
        if (amount == null || amount.trim().isEmpty()) return false;
        try {
            float value = Float.parseFloat(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Kiểm tra số tiền khách đưa (phải là số nguyên > 0)
    public static boolean isValidIntBiggerThanZero(String amount) {
        if (amount == null || amount.trim().isEmpty()) return false;
        try {
            int value = Integer.parseInt(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // Kiểm tra chuỗi có phải là số nguyên hợp lệ không
    public static boolean isValidInt(String text) {
        if (text == null || text.trim().isEmpty()) return false;
        try {
            Integer.parseInt(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Kiểm tra chuỗi có phải là số thực hợp lệ không
    public static boolean isValidDecimal(String text) {
        if (text == null || text.trim().isEmpty()) return false;
        try {
            new java.math.BigDecimal(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Hàm mới: kiểm tra chuỗi là bất kỳ dạng số nào (int hoặc decimal)
    public static boolean isAnyNumber(String text) {
        if (text == null || text.trim().isEmpty()) return false;
        return isValidInt(text) || isValidDecimal(text);
    }

    // check if ma KM existed
    public static Integer validateMaKM(String maKMText) {
        try {
            // Empty or invalid string
            if (maKMText == null || maKMText.trim().isEmpty()) {
                return null;
            }

            // Try parse integer
            int maKM = Integer.parseInt(maKMText.trim());

            // Check if the promotion code exists in database
            if (BUSManager.khuyenMaiBUS.checkIfMaKmExist(maKM)) {
                return maKM;
            } else {
                System.out.println("⚠️ Mã khuyến mãi không tồn tại: " + maKM);
                return null; // invalid -> return null
            }

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Mã khuyến mãi không hợp lệ (không phải số): " + maKMText);
            return null;
        }
    }
    
    // Kiểm tra chuỗi không được để trống
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

}

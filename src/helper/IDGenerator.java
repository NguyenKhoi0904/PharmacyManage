package helper;

import java.util.*;
import BUS.BUSManager;

public class IDGenerator {
    private static final Random random = new Random();

    /**
     * Sinh ID kiểu int (8 chữ số), đảm bảo không trùng trong toàn bộ hệ thống
     */
    public static int generateUniqueID() {
        Set<Integer> allIDs = getAllExistingIDs();
        int newID;

        do {
            newID = random.nextInt(90000000) + 10000000; // 10000000 -> 99999999
            // newID = random.nextInt(9000) + 1000;
        } while (allIDs.contains(newID));

        return newID;
    }

    /**
     * Gom tất cả ID từ mọi bảng để đảm bảo không trùng
     */
    private static Set<Integer> getAllExistingIDs() {
        Set<Integer> ids = new HashSet<>();

        try {
            BUSManager.taiKhoanBUS.getListTaiKhoan().forEach(dto -> ids.add(dto.getMaTk()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.nhanVienBUS.getListNhanVien().forEach(dto -> ids.add(dto.getMaNv()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.khachHangBUS.getListKhachHang().forEach(dto -> ids.add(dto.getMaKh()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.thuocBUS.getListThuoc().forEach(dto -> ids.add(dto.getMaThuoc()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.nhaCungCapBUS.getListNhaCungCap().forEach(dto -> ids.add(dto.getMaNcc()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.loHangBUS.getListLoHang().forEach(dto -> ids.add(dto.getMaLh()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.phieuNhapBUS.getListPhieuNhap().forEach(dto -> ids.add(dto.getMaPn()));
        } catch (Exception ignored) {
        }
        try {
            // BUSManager.chiTietPnBUS.getListChiTietPn().forEach(dto ->
            // ids.add(dto.getMa()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.hoaDonBUS.getListHoaDon().forEach(dto -> ids.add(dto.getMaHd()));
        } catch (Exception ignored) {
        }
        try {
            // BUSManager.chiTietHdBUS.getAll().forEach(dto -> ids.add(dto.getMaCTHD()));
        } catch (Exception ignored) {
        }
        try {
            BUSManager.khuyenMaiBUS.getListKhuyenMai().forEach(dto -> ids.add(dto.getMaKm()));
        } catch (Exception ignored) {
        }

        return ids;
    }
}

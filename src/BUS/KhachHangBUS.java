package BUS;

import java.util.ArrayList;

import DAO.KhachHangDAO;
import DAO.TaiKhoanDAO;
import DTO.KhachHangDTO;

public class KhachHangBUS {
    private ArrayList<KhachHangDTO> listKhachHang;
    private final KhachHangDAO khachHangDAO;
    private final TaiKhoanDAO taiKhoanDAO;

    public KhachHangBUS() {
        this.khachHangDAO = KhachHangDAO.getInstance();
        this.taiKhoanDAO = TaiKhoanDAO.getInstance();
        this.listKhachHang = this.khachHangDAO.selectAll();
    }

    // ========== DATABASE HANDLE ==========
    public boolean addKhachHang(KhachHangDTO khachHangDTO) {
        // thêm vào db + list
        if (this.khachHangDAO.insert(khachHangDTO) > 0) {
            this.listKhachHang.add(khachHangDTO);
            return true;
        }
        System.out.println("lỗi hàm addKhachHang mục <this.khachHangDAO.insert>");
        return false;
    }

    public boolean updateKhachHang(int ma_kh, KhachHangDTO khachHangDTO) {
        if (this.khachHangDAO.update(khachHangDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listKhachHang.size(); i++) {
                if (this.listKhachHang.get(i).getMaKh() == ma_kh) {
                    this.listKhachHang.set(i, khachHangDTO);
                    break;
                }
            }
            return true;
        }
        System.out.println("Lỗi CSDL: Không thể cập nhật khách hàng.");
        return false;
    }

    public boolean deleteKhachHang(int ma_kh) {
        int ma_tk_cua_kh = this.khachHangDAO.selectById(String.valueOf(ma_kh)).getMaTk();

        if (this.khachHangDAO.deleteById(String.valueOf(ma_kh)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listKhachHang.size(); i++) {
                if (this.listKhachHang.get(i).getMaKh() == ma_kh) {
                    this.listKhachHang.remove(i);
                    break;
                }
            }

            // đặt trạng thái của khách hàng trong bảng taikhoan = 0
            this.taiKhoanDAO.deleteById(String.valueOf(ma_tk_cua_kh));

            return true;
        }
        System.out.println("lỗi hàm deleteKhachHang: Không tìm thấy khách hàng hoặc lỗi CSDL.");
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    // ========== GET DỮ LIỆU ==========
    public ArrayList<KhachHangDTO> getListKhachHang() {
        return this.listKhachHang;
    }

    public KhachHangDTO getKhachHangByMaKh(int ma_kh) {
        for (KhachHangDTO kh : this.listKhachHang) {
            if (kh.getMaKh() == ma_kh) {
                return kh;
            }
        }
        return null;

    }
}

package BUS;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;

import DAO.NhanVienDAO;
import DAO.TaiKhoanDAO;
import DTO.NhanVienDTO;

public class NhanVienBUS {
    private ArrayList<NhanVienDTO> listNhanVien;
    private NhanVienDAO nhanVienDAO;
    private TaiKhoanDAO taiKhoanDAO;

    public NhanVienBUS() {
        this.nhanVienDAO = NhanVienDAO.getInstance();
        this.taiKhoanDAO = TaiKhoanDAO.getInstance();
        this.listNhanVien = nhanVienDAO.selectAll();
    }

    // ========== DATABASE HANDLE =========
    private static boolean isEighteenOrOlder(LocalDate ngay_sinh, LocalDate ngay_vao_lam) {
        return Period.between(ngay_sinh, ngay_vao_lam).getYears() >= 18;
    }

    public boolean addNhanVien(NhanVienDTO nhanVienDTO) {
        // kiểm tra nếu ngày vào làm sớm hơn ngày sinh và ít nhất 18 tuổi
        if (isEighteenOrOlder(nhanVienDTO.getNgaySinh().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                nhanVienDTO.getNgayVaoLam().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            System.out.println("lỗi hàm addNhanVien chưa đủ 18 tuổi");
            return false;
        } else if (nhanVienDTO.getNgaySinh().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .isAfter(nhanVienDTO.getNgayVaoLam().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            System.out.println("lỗi hàm addNhanVien ngày sinh sau ngày vào làm");
            return false;
        }

        // kiểm tra nếu email nhà cung cấp sai định dạng đuôi
        if (!nhanVienDTO.getEmail().contains("@email.com") &&
                !nhanVienDTO.getEmail().contains("@gmail.com")) {
            System.out.println("lỗi hàm addNhanVien sai định dạng email");
            return false;
        }

        // thêm vào db + list
        if (this.nhanVienDAO.insert(nhanVienDTO) > 0) {
            this.listNhanVien.add(nhanVienDTO);
            return true;
        }
        System.out.println("lỗi hàm addNhanVien mục <this.nhanVienDAO.insert>");
        return false;
    }

    public boolean updateNhanVien(int ma_nv, NhanVienDTO nhanVienDTO) {
        if (this.nhanVienDAO.update(nhanVienDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listNhanVien.size(); i++) {
                if (this.listNhanVien.get(i).getMaNv() == ma_nv) {
                    this.listNhanVien.set(i, nhanVienDTO);
                    break;
                }
            }
            return true;
        }
        System.out.println("Lỗi CSDL: Không thể cập nhật nhân viên.");
        return false;
    }

    public boolean deleteNhanVien(int ma_nv) {
        int ma_tk_cua_nv = this.nhanVienDAO.selectById(String.valueOf(ma_nv)).getMaTk();

        if (this.nhanVienDAO.deleteById(String.valueOf(ma_nv)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listNhanVien.size(); i++) {
                if (this.listNhanVien.get(i).getMaNv() == ma_nv) {
                    this.listNhanVien.get(i).setTrangThai(0);;
                    break;
                }
            }

            // đặt trạng thái của nhân viên trong bảng taikhoan = 0
            this.taiKhoanDAO.deleteById(String.valueOf(ma_tk_cua_nv));

            return true;
        }
        System.out.println("lỗi hàm deleteNhanVien: Không tìm thấy nhân viên hoặc lỗi CSDL.");
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    // ========== GET DỮ LIỆU ==========

    public ArrayList<NhanVienDTO> getListTaiKhoan() {
        return this.listNhanVien;
    }

    public NhanVienDTO getNhanVienByMaNv(int ma_nv) {
        for (NhanVienDTO nv : this.listNhanVien) {
            if (nv.getMaNv() == ma_nv) {
                return nv;
            }
        }
        return null;
    }
}

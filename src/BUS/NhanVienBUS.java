package BUS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;

public class NhanVienBUS {
    // singleton instance
    private static NhanVienBUS instance;

    // field
    private ArrayList<NhanVienDTO> listNhanVien;
    private final NhanVienDAO nhanVienDAO;
    private final TaiKhoanBUS taiKhoanBUS;

    private NhanVienBUS() {
        this.nhanVienDAO = NhanVienDAO.getInstance();
        this.listNhanVien = nhanVienDAO.selectAll();
        this.taiKhoanBUS = TaiKhoanBUS.getInstance();
    }

    // singleton init
    public static NhanVienBUS getInstance() {
        if (instance == null) {
            instance = new NhanVienBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE =========
    public boolean addNhanVien(NhanVienDTO nhanVienDTO) {
        // kiểm tra nếu mã tài khoản của NhanVienDTO không tồn tại trong bảng taikhoan
        if (!this.taiKhoanBUS.getMapByMaTk().containsKey(nhanVienDTO.getMaTk())) {
            return false;
        }

        // kiểm tra nếu mã nhân viên của NhanVienDTO đã tồn tại trong bảng nhanvien
        if (this.checkIfMaNvExist(nhanVienDTO)) {
            return false;
        }

        // kiểm tra nếu ngày vào làm sớm hơn ngày sinh và ít nhất 18 tuổi
        if (this.checkEffectiveDate(nhanVienDTO)) {
            return false;
        }

        // kiểm tra nếu email nhân viên sai định dạng đuôi
        if (!nhanVienDTO.getEmail().contains("@email.com") &&
                !nhanVienDTO.getEmail().contains("@gmail.com")) {
            JOptionPane.showMessageDialog(null, "lỗi hàm addNhanVien sai định dạng email", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu lương âm
        if (nhanVienDTO.getLuong().compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "Lương của nhân viên không được âm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.nhanVienDAO.insert(nhanVienDTO) > 0) {
            this.listNhanVien.add(nhanVienDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm addNhanVien mục <this.nhanVienDAO.insert>", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateNhanVien(NhanVienDTO nhanVienDTO) {
        // FK check: kiểm tra nếu mã tài khoản của NhanVienDTO không tồn tại trong bảng
        // taikhoan
        if (!this.taiKhoanBUS.checkIfMaTkExist(nhanVienDTO.getMaTk())) {
            return false;
        }

        // kiểm tra nếu mã nhân viên của NhanVienDTO không tồn tại trong bảng nhanvien
        if (!this.checkIfMaNvExist(nhanVienDTO)) {
            return false;
        }

        // kiểm tra nếu ngày vào làm sớm hơn ngày sinh và ít nhất 18 tuổi
        if (!this.checkEffectiveDate(nhanVienDTO)) {
            return false;
        }

        // kiểm tra nếu email nhà cung cấp sai định dạng đuôi
        if (!nhanVienDTO.getEmail().contains("@email.com") &&
                !nhanVienDTO.getEmail().contains("@gmail.com")) {
            JOptionPane.showMessageDialog(null, "lỗi hàm addNhanVien sai định dạng email", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu lương âm
        if (nhanVienDTO.getLuong().compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "Lương của nhân viên không được âm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.nhanVienDAO.update(nhanVienDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listNhanVien.size(); i++) {
                if (this.listNhanVien.get(i).getMaNv() == nhanVienDTO.getMaNv()) {
                    this.listNhanVien.set(i, nhanVienDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật nhân viên.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteNhanVien(int ma_nv) {
        int ma_tk_cua_nv = this.nhanVienDAO.selectById(String.valueOf(ma_nv)).getMaTk();

        if (this.nhanVienDAO.deleteById(String.valueOf(ma_nv)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listNhanVien.size(); i++) {
                if (this.listNhanVien.get(i).getMaNv() == ma_nv) {
                    this.listNhanVien.get(i).setTrangThai(0);
                    break;
                }
            }

            // đặt trạng thái của nhân viên trong bảng taikhoan = 0
            this.taiKhoanBUS.deleteTaiKhoan(ma_tk_cua_nv);

            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm deleteNhanVien: Không tìm thấy nhân viên hoặc lỗi CSDL.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========
    private static boolean isEighteenOrOlder(LocalDate ngay_sinh, LocalDate ngay_vao_lam) {
        return Period.between(ngay_sinh, ngay_vao_lam).getYears() >= 18;
    }

    private boolean checkIfMaNvExist(NhanVienDTO nhanVienDTO) {
        if (this.getMapByMaNv().containsKey(nhanVienDTO.getMaNv())) {
            // nếu mã nhân viên của nhân viên đã tồn tại trong bảng nhanvien
            JOptionPane.showMessageDialog(null, "Mã nhân viên của nhân viên đã tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Mã nhân viên của nhân viên không tồn tại", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    private boolean checkEffectiveDate(NhanVienDTO nhanVienDTO) {
        if (!isEighteenOrOlder(nhanVienDTO.getNgaySinh().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                nhanVienDTO.getNgayVaoLam().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            JOptionPane.showMessageDialog(null, "Nhân viên chưa đủ 18 tuổi", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (nhanVienDTO.getNgaySinh().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .isAfter(nhanVienDTO.getNgayVaoLam().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            JOptionPane.showMessageDialog(null, "Ngày sinh phải trước ngày vào làm", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // ========== GET DỮ LIỆU ==========

    public ArrayList<NhanVienDTO> getListNhanVien() {
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

    // sử dụng trong TaiKhoanBUS
    public NhanVienDTO getNhanVienByMaTk(int ma_tk) {
        for (NhanVienDTO nv : this.listNhanVien) {
            if (nv.getMaTk() == ma_tk) {
                return nv;
            }
        }
        return null;
    }

    /**
     * 
     * @return HashMap&lt;NhanVienDTO.getMaNv,NhanVienDTO&gt;
     */
    public HashMap<Integer, NhanVienDTO> getMapByMaNv() {
        HashMap<Integer, NhanVienDTO> mapMaNv = new HashMap<Integer, NhanVienDTO>();
        for (NhanVienDTO nv : this.listNhanVien) {
            mapMaNv.put(nv.getMaNv(), nv);
        }
        return mapMaNv;
    }
}

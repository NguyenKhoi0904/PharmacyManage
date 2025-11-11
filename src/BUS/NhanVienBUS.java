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
    private TaiKhoanBUS taiKhoanBUS;

    private NhanVienBUS() {
        this.nhanVienDAO = NhanVienDAO.getInstance();
        this.listNhanVien = nhanVienDAO.selectAll();
    }

    // singleton init
    public static NhanVienBUS getInstance() {
        if (instance == null) {
            instance = new NhanVienBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addNhanVien(NhanVienDTO nhanVienDTO) {
        // FK check: mã tài khoản phải tồn tại
        if (taiKhoanBUS == null || taiKhoanBUS.getTaiKhoanByMaTk(nhanVienDTO.getMaTk()) == null) {
            JOptionPane.showMessageDialog(null, "Mã tài khoản không tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu mã nhân viên đã tồn tại
        if (this.checkIfMaNvExist(nhanVienDTO)) {
            return false;
        }

        // kiểm tra ngày sinh - ngày vào làm
        if (!this.checkEffectiveDate(nhanVienDTO)) {
            return false;
        }

        // kiểm tra email
        if (!nhanVienDTO.getEmail().contains("@email.com") && !nhanVienDTO.getEmail().contains("@gmail.com")) {
            JOptionPane.showMessageDialog(null, "Sai định dạng email", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra lương âm
        if (nhanVienDTO.getLuong().compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "Lương của nhân viên không được âm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.nhanVienDAO.insert(nhanVienDTO) > 0) {
            this.listNhanVien.add(nhanVienDTO);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Lỗi CSDL khi thêm nhân viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateNhanVien(NhanVienDTO nhanVienDTO) {
        // FK check: mã tài khoản phải tồn tại
        if (taiKhoanBUS == null || taiKhoanBUS.getTaiKhoanByMaTk(nhanVienDTO.getMaTk()) == null) {
            JOptionPane.showMessageDialog(null, "Mã tài khoản không tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu mã nhân viên không tồn tại
        if (!this.checkIfMaNvExist(nhanVienDTO)) {
            return false;
        }

        // kiểm tra ngày sinh - ngày vào làm
        if (!this.checkEffectiveDate(nhanVienDTO)) {
            return false;
        }

        // kiểm tra email
        if (!nhanVienDTO.getEmail().contains("@email.com") && !nhanVienDTO.getEmail().contains("@gmail.com")) {
            JOptionPane.showMessageDialog(null, "Sai định dạng email", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra lương âm
        if (nhanVienDTO.getLuong().compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "Lương của nhân viên không được âm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.nhanVienDAO.update(nhanVienDTO) > 0) {
            for (int i = 0; i < this.listNhanVien.size(); i++) {
                if (this.listNhanVien.get(i).getMaNv() == nhanVienDTO.getMaNv()) {
                    this.listNhanVien.set(i, nhanVienDTO);
                    break;
                }
            }
            return true;
        }

        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật nhân viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteNhanVien(int ma_nv) {
        int ma_tk_cua_nv = this.nhanVienDAO.selectById(String.valueOf(ma_nv)).getMaTk();

        if (this.nhanVienDAO.deleteById(String.valueOf(ma_nv)) > 0) {
            for (NhanVienDTO nv : listNhanVien) {
                if (nv.getMaNv() == ma_nv) {
                    nv.setTrangThai(0);
                    break;
                }
            }
            if (taiKhoanBUS != null) {
                taiKhoanBUS.deleteTaiKhoan(ma_tk_cua_nv);
            }
            return true;
        }

        JOptionPane.showMessageDialog(null, "Không thể xóa nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========
    private static boolean isEighteenOrOlder(LocalDate ngay_sinh, LocalDate ngay_vao_lam) {
        return Period.between(ngay_sinh, ngay_vao_lam).getYears() >= 18;
    }

    private boolean checkIfMaNvExist(NhanVienDTO nhanVienDTO) {
        if (this.getMapByMaNv().containsKey(nhanVienDTO.getMaNv())) {
            return true;
        }
        return false;
    }

    private boolean checkEffectiveDate(NhanVienDTO nhanVienDTO) {
        LocalDate ns = nhanVienDTO.getNgaySinh().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate nvl = nhanVienDTO.getNgayVaoLam().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (!isEighteenOrOlder(ns, nvl)) {
            JOptionPane.showMessageDialog(null, "Nhân viên chưa đủ 18 tuổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (ns.isAfter(nvl)) {
            JOptionPane.showMessageDialog(null, "Ngày sinh phải trước ngày vào làm", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

    public NhanVienDTO getNhanVienByMaTk(int ma_tk) {
        for (NhanVienDTO nv : this.listNhanVien) {
            if (nv.getMaTk() == ma_tk) {
                return nv;
            }
        }
        return null;
    }

    public HashMap<Integer, NhanVienDTO> getMapByMaNv() {
        HashMap<Integer, NhanVienDTO> mapMaNv = new HashMap<>();
        for (NhanVienDTO nv : this.listNhanVien) {
            mapMaNv.put(nv.getMaNv(), nv);
        }
        return mapMaNv;
    }

    // ========== SET BUS ==========
    public void setTaiKhoanBUS(TaiKhoanBUS taiKhoanBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
    }
}

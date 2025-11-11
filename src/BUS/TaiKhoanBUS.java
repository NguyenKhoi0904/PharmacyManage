package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.TaiKhoanDAO;
import DTO.KhachHangDTO;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import helper.BCrypt;

public class TaiKhoanBUS {

    // singleton instance
    private static TaiKhoanBUS instance;

    //  Lưu tài khoản hiện đang đăng nhập
    private static TaiKhoanDTO currentUser;

    // field
    private ArrayList<TaiKhoanDTO> listTaiKhoan;
    private final TaiKhoanDAO taiKhoanDAO;
    private NhanVienBUS nhanVienBUS;
    private KhachHangBUS khachHangBUS;

    private TaiKhoanBUS() {
        this.taiKhoanDAO = TaiKhoanDAO.getInstance();
        this.listTaiKhoan = this.taiKhoanDAO.selectAll();
    }

    // singleton init
    public static TaiKhoanBUS getInstance() {
        if (instance == null) {
            instance = new TaiKhoanBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========

    public boolean addTaiKhoan(TaiKhoanDTO taiKhoanDTO) {
        // kiểm tra nếu tài khoản đã tồn tại (ma_tk,tai_khoan)
        if (this.getMapByMaTk().containsKey(taiKhoanDTO.getMaTk())) {
            JOptionPane.showMessageDialog(null, "Mã tài khoản đã tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (this.getMapByTaiKhoan().containsKey(taiKhoanDTO.getTaiKhoan())) {
            JOptionPane.showMessageDialog(null, "Tài khoản đăng nhập đã tồn tại", "Lỗi Đăng Nhập",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra mật khẩu hợp lệ
        if (!this.checkIfPasswordValidate(taiKhoanDTO, "Thêm Tài Khoản")) {
            return false;
        }

        // chuyển mật khẩu thành dạng băm
        String hashedPassword = hashPassword(taiKhoanDTO.getMatKhau());
        if (hashedPassword == null) {
            JOptionPane.showConfirmDialog(null, "Mật khẩu băm rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        taiKhoanDTO.setMatKhau(hashedPassword);

        // thêm vào db + list
        if (this.taiKhoanDAO.insert(taiKhoanDTO) > 0) {
            this.listTaiKhoan.add(taiKhoanDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể thêm tài khoản", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteTaiKhoan(int ma_tk) {
        // kiểm tra nếu có nhân viên hay khách hàng đang liên kết với tài khoản này
        // => không cho xoá
        if (NhanVienBUS.getInstance().getNhanVienByMaTk(ma_tk) != null) {
            JOptionPane.showMessageDialog(null, "Tài khoản nhân viên vẫn còn liên kết,xoá nhân viên trước", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (KhachHangBUS.getInstance().getKhachHangByMaTk(ma_tk) != null) {
            JOptionPane.showMessageDialog(null, "Tài khoản khách hàng vẫn còn liên kết,xoá khách hàng trước", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu tài khoản không tồn tại
        if (!this.getMapByMaTk().containsKey(ma_tk)) {
            JOptionPane.showMessageDialog(null, "Lỗi không tìm được mã tài khoản hoặc tài khoản không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }

        String id = String.valueOf(ma_tk);

        if (this.taiKhoanDAO.deleteById(id) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listTaiKhoan.size(); i++) {
                if (this.listTaiKhoan.get(i).getMaTk() == ma_tk) {
                    this.listTaiKhoan.remove(i);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể xoá tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateTaiKhoan(TaiKhoanDTO taiKhoanDTO) {
        // kiểm tra nếu tài khoản không tồn tại
        if (!this.getMapByMaTk().containsKey(taiKhoanDTO.getMaTk())) {
            JOptionPane.showMessageDialog(null, "lỗi không tìm được mã tài khoản hoặc tài khoản không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu tên tài khoản đã tồn tại
        TaiKhoanDTO existing = this.getMapByTaiKhoan().get(taiKhoanDTO.getTaiKhoan());
        if (existing != null && existing.getMaTk() != taiKhoanDTO.getMaTk()) {
            JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại, hãy dùng tên khác", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra mật khẩu hợp lệ
        if (!this.checkIfPasswordValidate(taiKhoanDTO, "Sửa Tài Khoản")) {
            return false;
        }

        // chuyển mật khẩu dạng băm nếu chưa được chuyển
        if (!taiKhoanDTO.getMatKhau().startsWith("$2a$")) {
            taiKhoanDTO.setMatKhau(hashPassword(taiKhoanDTO.getMatKhau()));
        }

        if (taiKhoanDAO.update(taiKhoanDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listTaiKhoan.size(); i++) {
                if (this.listTaiKhoan.get(i).getMaTk() == taiKhoanDTO.getMaTk()) {
                    this.listTaiKhoan.set(i, taiKhoanDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể cập nhật tài khoản.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public boolean checkIfMaTkExist(int ma_tk) {
        if (this.getMapByMaTk().containsKey(ma_tk)) {
            // nếu mã tài khoản của khách hàng/nhân viên đã tồn tại trong bảng taikhoan
            return true;
        }
        return false;
    }

    private boolean checkIfPasswordValidate(TaiKhoanDTO taiKhoanDTO, String titleAlertPanel) {
        if (taiKhoanDTO.getMatKhau() == null || taiKhoanDTO.getMatKhau().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mật khẩu null hoặc rỗng", "Lỗi " + titleAlertPanel,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (taiKhoanDTO.getMatKhau().length() < 6) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải có ít nhất 6 kí tự", "Lỗi " + titleAlertPanel,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (taiKhoanDTO.getMatKhau().matches("^[^0-9]*$")) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải có ít nhất 1 số", "Lỗi " + titleAlertPanel,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (!taiKhoanDTO.getMatKhau().matches(".*[^a-zA-Z0-9].*")) {
            JOptionPane.showMessageDialog(null, "Mật khẩu phải có ít nhất 1 ký tự đặc biệt", "Lỗi " + titleAlertPanel,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // ✅ Hàm đăng nhập: nếu đúng, lưu luôn user đang đăng nhập
    public Object login(String taiKhoan, String matKhauTho) {
        TaiKhoanDTO taiKhoanDB = taiKhoanDAO.selectByTaiKhoan(taiKhoan);

        if (taiKhoanDB != null && checkLogin(matKhauTho, taiKhoanDB.getMatKhau())) {
            // ✅ Lưu tài khoản đăng nhập hiện tại
            TaiKhoanBUS.setCurrentUser(taiKhoanDB);

            // Nếu là nhân viên
            NhanVienDTO nv = this.nhanVienBUS.getNhanVienByMaTk(taiKhoanDB.getMaTk());
            if (nv != null) return nv;

            // Nếu là khách hàng
            KhachHangDTO kh = this.khachHangBUS.getKhachHangByMaTk(taiKhoanDB.getMaTk());
            if (kh != null) return kh;
        }
        return null;
    }

    // Hàm BCrypt
    private String hashPassword(String matKhauTho) {
        return BCrypt.hashpw(matKhauTho, BCrypt.gensalt());
    }

    private boolean checkLogin(String matKhauTho, String matKhauBam) {
        return BCrypt.checkpw(matKhauTho, matKhauBam);
    }

    // ========== GET DỮ LIỆU ==========

    public ArrayList<TaiKhoanDTO> getListTaiKhoan() {
        return this.listTaiKhoan;
    }

    public TaiKhoanDTO getTaiKhoanByMaTk(int ma_tk) {
        for (TaiKhoanDTO tk : this.listTaiKhoan) {
            if (tk.getMaTk() == ma_tk) {
                return tk;
            }
        }
        return null;
    }

    public HashMap<Integer, TaiKhoanDTO> getMapByMaTk() {
        HashMap<Integer, TaiKhoanDTO> mapTaiKhoan = new HashMap<>();
        for (TaiKhoanDTO tk : this.listTaiKhoan) {
            mapTaiKhoan.put(tk.getMaTk(), tk);
        }
        return mapTaiKhoan;
    }

    public HashMap<String, TaiKhoanDTO> getMapByTaiKhoan() {
        HashMap<String, TaiKhoanDTO> mapTaiKhoan = new HashMap<>();
        for (TaiKhoanDTO tk : this.listTaiKhoan) {
            mapTaiKhoan.put(tk.getTaiKhoan(), tk);
        }
        return mapTaiKhoan;
    }

    // ========== SET BUS ==========

    public void setNhanVienBUS(NhanVienBUS nhanVienBUS) {
        this.nhanVienBUS = nhanVienBUS;
    }

    public void setKhachHangBUS(KhachHangBUS khachHangBUS) {
        this.khachHangBUS = khachHangBUS;
    }

    public int generate_maTK() {
        int max = 0;
        for (TaiKhoanDTO tk : listTaiKhoan) {
            if (tk.getMaTk() > max) max = tk.getMaTk();
        }
        return max + 1;
    }

    //  Getter/Setter cho currentUser
    public static void setCurrentUser(TaiKhoanDTO user) {
        currentUser = user;
    }

    public static TaiKhoanDTO getCurrentUser() {
        return currentUser;
    }
}

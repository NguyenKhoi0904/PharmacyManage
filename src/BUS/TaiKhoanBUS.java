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

    // field
    private ArrayList<TaiKhoanDTO> listTaiKhoan;
    private final TaiKhoanDAO taiKhoanDAO;
    private NhanVienBUS nhanVienBUS;
    private KhachHangBUS khachHangBUS;

    private TaiKhoanBUS() {
        this.taiKhoanDAO = TaiKhoanDAO.getInstance();
        // this.nhanVienBUS = NhanVienBUS.getInstance();
        // this.khachHangBUS = KhachHangBUS.getInstance();
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
            JOptionPane.showConfirmDialog(null, "addTaiKhoanDTO : mật khẩu băm rỗng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        taiKhoanDTO.setMatKhau(hashedPassword);

        // thêm vào db + list
        if (this.taiKhoanDAO.insert(taiKhoanDTO) > 0) {
            this.listTaiKhoan.add(taiKhoanDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm addTaiKhoanDTO mục <this.listTaiKhoan.add(taiKhoanDTO)>", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteTaiKhoan(int ma_tk) {
        // kiểm tra nếu có nhân viên hay khách hàng đang liên kết với tài khoản này
        // => không cho xoá
        if (NhanVienBUS.getInstance().getNhanVienByMaTk(ma_tk) == null) {
            JOptionPane.showMessageDialog(null, "Tài khoản nhân viên vẫn còn liên kết,xoá nhân viên trước", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (KhachHangBUS.getInstance().getKhachHangByMaTk(ma_tk) == null) {
            JOptionPane.showMessageDialog(null, "Tài khoản khách hàng vẫn còn liên kết,xoá khách hàng trước", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu tài khoản không tồn tại
        if (!this.getMapByMaTk().containsKey(ma_tk)) {
            JOptionPane.showMessageDialog(null, "lỗi không tìm được mã tài khoản hoặc tài khoản không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }

        String id = String.valueOf(ma_tk);

        if (this.taiKhoanDAO.deleteById(id) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listTaiKhoan.size(); i++) {
                if (this.listTaiKhoan.get(i).getMaTk() == ma_tk) {
                    this.listTaiKhoan.remove(i);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm deleteTaiKhoan: lỗi CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        // if (this.getMapByTaiKhoan().containsKey(taiKhoanDTO.getTaiKhoan())) {
        // JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại,hãy sử dụng tên
        // khác", "Lỗi Đăng Nhập",
        // JOptionPane.ERROR_MESSAGE);
        // }
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
        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật tài khoản.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public boolean checkIfMaTkExist(int ma_tk) {
        if (this.getMapByMaTk().containsKey(ma_tk)) {
            // nếu mã tài khoản của khách hàng/nhân viên đã tồn tại trong bảng taikhoan
            JOptionPane.showMessageDialog(null, "Mã tài khoản đã tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean checkIfPasswordValidate(TaiKhoanDTO taiKhoanDTO, String titleAlertPanel) {
        if (taiKhoanDTO.getMatKhau() == null || taiKhoanDTO.getMatKhau().isEmpty()) {
            // kiểm tra nếu mật khẩu rỗng
            JOptionPane.showMessageDialog(null, "Mật khẩu null hoặc rỗng", "Lỗi " + titleAlertPanel,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (taiKhoanDTO.getMatKhau().length() < 6) {
            // mật khẩu ít nhất 6 kí tự
            JOptionPane.showMessageDialog(null, "Mật khẩu phải có ít nhất 6 kí tự", "Lỗi " + titleAlertPanel,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (taiKhoanDTO.getMatKhau().matches("^[^0-9]*$")) {
            // mật khẩu phải có ít nhất 1 số
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

    // Hàm kiểm tra đăng nhập (Sử dụng BCrypt Check)
    // trả về kiểu tài khoản nhân viên hay khách hàng đăng nhập
    public Object login(String taiKhoan, String matKhauTho) {
        TaiKhoanDTO taiKhoanDB = taiKhoanDAO.selectByTaiKhoan(taiKhoan);

        if (taiKhoanDB != null) {
            // dùng BCrypt để so sánh mật khẩu thô với mật khẩu đã băm
            if (checkLogin(matKhauTho, taiKhoanDB.getMatKhau())) {
                // Nếu là nhân viên
                NhanVienDTO nv = this.nhanVienBUS.getNhanVienByMaTk(taiKhoanDB.getMaTk());
                if (nv != null)
                    return nv;

                // Nếu là khách hàng
                KhachHangDTO kh = this.khachHangBUS.getKhachHangByMaTk(taiKhoanDB.getMaTk());
                if (kh != null)
                    return kh;
            }
        }
        return null;
    }

    // Hàm BCrypt: Mã hóa mật khẩu (hàm nội bộ)
    private String hashPassword(String matKhauTho) {
        // Sử dụng salt factor là 10 (mặc định)
        return BCrypt.hashpw(matKhauTho, BCrypt.gensalt());
    }

    // Hàm BCrypt: Kiểm tra mật khẩu (hàm nội bộ)
    private boolean checkLogin(String matKhauTho, String matKhauBam) {
        // So sánh mật khẩu thô với mật khẩu đã băm trong DB
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

    /**
     * 
     * @return HashMap&lt;TaiKhoanDTO.getMaTk,TaiKhoanDTO&gt;
     */
    public HashMap<Integer, TaiKhoanDTO> getMapByMaTk() {
        HashMap<Integer, TaiKhoanDTO> mapTaiKhoan = new HashMap<Integer, TaiKhoanDTO>();
        for (TaiKhoanDTO tk : this.listTaiKhoan) {
            mapTaiKhoan.put(tk.getMaTk(), tk);
        }
        return mapTaiKhoan;
    }

    /**
     * 
     * @return HashMap&lt;TaiKhoanDTO.getTaiKhoan,TaiKhoanDTO&gt;
     */
    public HashMap<String, TaiKhoanDTO> getMapByTaiKhoan() {
        HashMap<String, TaiKhoanDTO> mapTaiKhoan = new HashMap<String, TaiKhoanDTO>();
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
}
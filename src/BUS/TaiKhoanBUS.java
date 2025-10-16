package BUS;

import java.util.ArrayList;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;
import helper.BCrypt;

public class TaiKhoanBUS {
    private ArrayList<TaiKhoanDTO> listTaiKhoan;
    private final TaiKhoanDAO taiKhoanDAO;

    public TaiKhoanBUS() {
        this.taiKhoanDAO = TaiKhoanDAO.getInstance();
        this.listTaiKhoan = this.taiKhoanDAO.selectAll();
    }

    // ========== DATABASE HANDLE ==========

    public boolean addTaiKhoan(TaiKhoanDTO taiKhoanDTO) {
        // kiểm tra nếu mật khẩu rỗng
        if (taiKhoanDTO.getMatKhau() == null || taiKhoanDTO.getMatKhau().isEmpty()) {
            System.out.println("lỗi hàm addTaiKhoanDTO mật khẩu rỗng hoặc mật khẩu trống");
            return false;
        }

        // chuyển mật khẩu thành dạng băm
        String hashedPassword = hashPassword(taiKhoanDTO.getMatKhau());
        if (hashedPassword == null) {
            System.out.println("lỗi hàm addTaiKhoanDTO mật khẩu băm rỗng");
            return false;
        }
        taiKhoanDTO.setMatKhau(hashedPassword);

        // thêm vào db + list
        if (this.taiKhoanDAO.insert(taiKhoanDTO) > 0) {
            this.listTaiKhoan.add(taiKhoanDTO);
            return true;
        }
        System.out.println("lỗi hàm addTaiKhoanDTO mục <this.listTaiKhoan.add(taiKhoanDTO)>");
        return false;
    }

    public boolean deleteTaiKhoan(int maTk) {
        String id = String.valueOf(maTk);

        if (this.taiKhoanDAO.deleteById(id) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listTaiKhoan.size(); i++) {
                if (this.listTaiKhoan.get(i).getMaTk() == maTk) {
                    this.listTaiKhoan.remove(i);
                    break;
                }
            }
            return true;
        }
        System.out.println("lỗi hàm deleteTaiKhoan: Không tìm thấy tài khoản hoặc lỗi CSDL.");
        return false;
    }

    public boolean updateTaiKhoan(int ma_tk, TaiKhoanDTO taiKhoanDTO) {
        // chuyển mật khẩu dạng băm nếu chưa được chuyển
        if (!taiKhoanDTO.getMatKhau().startsWith("$2a$")) {
            taiKhoanDTO.setMatKhau(hashPassword(taiKhoanDTO.getMatKhau()));
        }

        if (taiKhoanDAO.update(taiKhoanDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listTaiKhoan.size(); i++) {
                if (this.listTaiKhoan.get(i).getMaTk() == ma_tk) {
                    this.listTaiKhoan.set(i, taiKhoanDTO);
                    break;
                }
            }
            return true;
        }
        System.out.println("Lỗi CSDL: Không thể cập nhật tài khoản.");
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    // Hàm kiểm tra đăng nhập (Sử dụng BCrypt Check)
    public TaiKhoanDTO login(String taiKhoan, String matKhauTho) {
        TaiKhoanDTO taiKhoanDB = taiKhoanDAO.selectByTaiKhoan(taiKhoan);

        if (taiKhoanDB != null) {
            // dùng BCrypt để so sánh mật khẩu thô với mật khẩu đã băm
            if (checkPassword(matKhauTho, taiKhoanDB.getMatKhau())) {
                return taiKhoanDB;
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
    private boolean checkPassword(String matKhauTho, String matKhauBam) {
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

}
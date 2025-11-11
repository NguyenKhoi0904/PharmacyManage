package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.KhachHangDAO;
import DTO.KhachHangDTO;

public class KhachHangBUS {
    // singleton instance
    private static KhachHangBUS instance;

    // field
    private ArrayList<KhachHangDTO> listKhachHang;
    private KhachHangDAO khachHangDAO;
    private TaiKhoanBUS taiKhoanBUS;
    private HoaDonBUS hoaDonBUS;

    private KhachHangBUS() {
        this.khachHangDAO = KhachHangDAO.getInstance();
        this.taiKhoanBUS = TaiKhoanBUS.getInstance();
        // this.hoaDonBUS = HoaDonBUS.getInstance();
        this.listKhachHang = this.khachHangDAO.selectAll();
    }

    // singleton init
    public static KhachHangBUS getInstance() {
        if (instance == null) {
            instance = new KhachHangBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addKhachHang(KhachHangDTO khachHangDTO) {
        // kiểm tra nếu mã tài khoản của khách hàng không tồn tại trong bảng taikhoan
        if (!this.taiKhoanBUS.getMapByMaTk().containsKey(khachHangDTO.getMaTk())) {
            JOptionPane.showMessageDialog(null, "Mã tài khoản khách hàng không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu mã khách hàng đã tồn tại trong bảng khachhang
        if (this.checkIfMaKhExist(khachHangDTO)) {
            return false;
        }

        // kiểm tra nếu điểm tích luỹ âm
        if (khachHangDTO.getDiemTichLuy() < 0) {
            JOptionPane.showMessageDialog(null, "Điểm tích luỹ của khách hàng không được âm", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.khachHangDAO.insert(khachHangDTO) > 0) {
            this.listKhachHang.add(khachHangDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm addKhachHang mục <this.khachHangDAO.insert>", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateKhachHang(KhachHangDTO khachHangDTO) {
        // kiểm tra nếu mã tài khoản của khách hàng không tồn tại trong bảng taikhoan
        if (!this.taiKhoanBUS.getMapByMaTk().containsKey(khachHangDTO.getMaTk())) {
            JOptionPane.showMessageDialog(null, "Mã tài khoản khách hàng không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu điểm tích luỹ âm
        if (khachHangDTO.getDiemTichLuy() < 0) {
            JOptionPane.showMessageDialog(null, "Điểm tích luỹ của khách hàng không được âm", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.khachHangDAO.update(khachHangDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listKhachHang.size(); i++) {
                if (this.listKhachHang.get(i).getMaKh() == khachHangDTO.getMaKh()) {
                    this.listKhachHang.set(i, khachHangDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật khách hàng.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteKhachHang(int ma_kh) {
        // nếu khách hàng đã từng phát sinh hoadon, thì không được xoá
//        if (this.hoaDonBUS.checkIfMaKhExist(ma_kh)) {
//            JOptionPane.showMessageDialog(null, "Không thể xoá khách hàng đã phát sinh hoá đơn", "Lỗi",
//                    JOptionPane.ERROR_MESSAGE);
//            return false;
//        }

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
            this.taiKhoanBUS.deleteTaiKhoan(ma_tk_cua_kh);

            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm deleteKhachHang: Không tìm thấy khách hàng hoặc lỗi CSDL.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    private boolean checkIfMaKhExist(KhachHangDTO khachHangDTO) {
        if (this.getMapByMaKh().containsKey(khachHangDTO.getMaKh())) {
            // nếu mã khách hàng của khách hàng đã tồn tại trong bảng khachhang
            JOptionPane.showMessageDialog(null, "Mã khách hàng của khách hàng đã tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

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

    // sử dụng trong TaiKhoanBUS
    public KhachHangDTO getKhachHangByMaTk(int ma_tk) {
        for (KhachHangDTO kh : khachHangDAO.selectAllIncludeInactive()) {
            if (kh.getMaTk() == ma_tk) {
                return kh;
            }
        }
        return null;

    }

    /**
     * 
     * @return HashMap&lt;KhachHangDTO.getMaKh,KhachHangDTO&gt;
     */
    public HashMap<Integer, KhachHangDTO> getMapByMaKh() {
        HashMap<Integer, KhachHangDTO> mapMaKh = new HashMap<Integer, KhachHangDTO>();
        for (KhachHangDTO kh : this.listKhachHang) {
            mapMaKh.put(kh.getMaKh(), kh);
        }
        return mapMaKh;
    }

    // ========== SET BUS ==========

    public void setTaiKhoanBUS(TaiKhoanBUS taiKhoanBUS) {
        this.taiKhoanBUS = taiKhoanBUS;
    }

    public void setHoaDonBUS(HoaDonBUS hoaDonBUS) {
        this.hoaDonBUS = hoaDonBUS;
    }
    
    
    
    public int generate_maKH(){
        int max = 0;
        for (KhachHangDTO kh: this.khachHangDAO.selectAllIncludeInactive()){
            if(kh.getMaKh() > max) max = kh.getMaKh();
        }
        return max+1;
    }
    
}

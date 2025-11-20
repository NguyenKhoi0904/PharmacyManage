package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.KhuyenMaiDAO;
import DTO.ChiTietHdDTO;
import DTO.KhachHangDTO;
import DTO.KhuyenMaiDTO;
import java.time.LocalDate;
import utils.ValidationUtils;

public class KhuyenMaiBUS {
    // singleton instance
    private static KhuyenMaiBUS instance;

    // field
    private ArrayList<KhuyenMaiDTO> listKhuyenMai;
    private KhuyenMaiDAO khuyenMaiDAO;

    private KhuyenMaiBUS() {
        this.khuyenMaiDAO = KhuyenMaiDAO.getInstance();
        this.listKhuyenMai = this.khuyenMaiDAO.selectAll();
    }

    // singleton init
    public static KhuyenMaiBUS getInstance() {
        if (instance == null) {
            instance = new KhuyenMaiBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addKhuyenMai(KhuyenMaiDTO khuyenMaiDTO) {
        // kiểm tra nếu mã khuyến mãi đã tồn tại
        if (this.checkIfMaKmExist(khuyenMaiDTO.getMaKm())) {
            JOptionPane.showMessageDialog(null, "Mã khuyến mãi đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu tên khuyến mãi đã tồn tại
        if (this.checkIfTenKmExist(khuyenMaiDTO.getTenKm())) {
            JOptionPane.showMessageDialog(null, "Tên khuyến mãi đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu ngày bắt đầu sau ngày kết thúc
        if (!this.checkEffectiveDate(khuyenMaiDTO)) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.khuyenMaiDAO.insert(khuyenMaiDTO) > 0) {
            this.listKhuyenMai.add(khuyenMaiDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể thêm khuyến mãi", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateKhuyenMai(KhuyenMaiDTO khuyenMaiDTO) {
        // kiểm tra nếu mã khuyến mãi không tồn tại
        if (!this.checkIfMaKmExist(khuyenMaiDTO.getMaKm())) {
            JOptionPane.showMessageDialog(null, "Mã khuyến mãi không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu ngày bắt đầu sau ngày kết thúc
        if (!this.checkEffectiveDate(khuyenMaiDTO)) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.khuyenMaiDAO.update(khuyenMaiDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listKhuyenMai.size(); i++) {
                if (this.listKhuyenMai.get(i).getMaKm() == khuyenMaiDTO.getMaKm()) {
                    this.listKhuyenMai.set(i, khuyenMaiDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể cập nhật khuyến mãi.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteKhuyenMai(int ma_km) {
        if (this.khuyenMaiDAO.deleteById(String.valueOf(ma_km)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listKhuyenMai.size(); i++) {
                if (this.listKhuyenMai.get(i).getMaKm() == ma_km) {
                    this.listKhuyenMai.remove(i);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không tìm thấy khuyến mãi.",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public boolean checkIfMaKmExist(int ma_km) {
        if (this.getMapByMaKm().containsKey(ma_km)) {
            // nếu mã khuyến mãi đã tồn tại trong bảng khuyenmai
            return true;
        }
        return false;
    }

    private boolean checkIfTenKmExist(String ten_km) {
        if (this.getMapByTenKm().containsKey(ten_km)) {
            // nếu tên khuyến mãi đã tồn tại trong bảng khuyenmai
            return true;
        }
        return false;
    }

    private boolean checkEffectiveDate(KhuyenMaiDTO khuyenMaiDTO) {
        LocalDate start = khuyenMaiDTO.getNgayBatDau().toLocalDate();
        LocalDate end = khuyenMaiDTO.getNgayKetThuc().toLocalDate();
        LocalDate today = LocalDate.now();

        // Nếu ngày bắt đầu sau ngày kết thúc thì sai hoặc hôm nay sau ngày hiệu lực
        return !start.isAfter(end) || !today.isAfter(end);
    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<KhuyenMaiDTO> getListKhuyenMai() {
        return this.listKhuyenMai;
    }

    public KhuyenMaiDTO getKhuyenMaiByMaKm(int ma_km) {
        for (KhuyenMaiDTO km : this.listKhuyenMai) {
            if (km.getMaKm() == ma_km) {
                return km;
            }
        }
        return null;

    }

    /**
     * 
     * @return HashMap&lt;KhuyenMaiDTO.getMaNcc,KhuyenMaiDTO&gt;
     */
    public HashMap<Integer, KhuyenMaiDTO> getMapByMaKm() {
        HashMap<Integer, KhuyenMaiDTO> mapMaKm = new HashMap<Integer, KhuyenMaiDTO>();
        for (KhuyenMaiDTO km : this.listKhuyenMai) {
            mapMaKm.put(km.getMaKm(), km);
        }
        return mapMaKm;
    }

    /**
     * 
     * @return HashMap&lt;KhuyenMaiDTO.getTenKm,KhuyenMaiDTO&gt;
     */
    private HashMap<String, KhuyenMaiDTO> getMapByTenKm() {
        HashMap<String, KhuyenMaiDTO> mapMaKm = new HashMap<String, KhuyenMaiDTO>();
        for (KhuyenMaiDTO km : this.listKhuyenMai) {
            mapMaKm.put(km.getTenKm(), km);
        }
        return mapMaKm;
    }

    public static java.math.BigDecimal getPhanTramGiamFromMaKM(String txtMaKM) {
        if (ValidationUtils.isValidIntBiggerThanZero(txtMaKM)) {
            int maKM = Integer.parseInt(txtMaKM);
            KhuyenMaiDTO km = BUSManager.khuyenMaiBUS.getKhuyenMaiByMaKm(maKM);
            if (BUSManager.khuyenMaiBUS.isKMValid(km)) {
                return km.getGiaTriKm().divide(java.math.BigDecimal.valueOf(100));
            }
        }
        return java.math.BigDecimal.ZERO;
    }

    public Boolean isKMValid(KhuyenMaiDTO km) {
        if (km == null) {
            return false;
        }

        if (km.getTrangThai() < 1) {
            return false;
        }

        return checkEffectiveDate(km);
    }
    
    public boolean checkDieuKienKM(KhuyenMaiDTO km, KhachHangDTO kh, ArrayList<ChiTietHdDTO> listCTHD) {
        switch (km.getEDieuKienKM()) {

            case MUA_2_TANG_1:
                return checkMua2Tang1(listCTHD);

            case DU_DIEM:
                return checkDuDiem(km, kh);

            default:
                return false;
        }
    }


    /** -------------------------------
     * 1. Điều kiện "Mua 2 tặng 1"
     * Phải có ít nhất một mặt hàng có số lượng >= 3
     --------------------------------*/
    private boolean checkMua2Tang1(ArrayList<ChiTietHdDTO> listCTHD) {
        for (ChiTietHdDTO ct : listCTHD) {
            if (ct.getSoLuong() >= 3) {
                return true;
            }
        }
        return false;
    }


    /** -------------------------------
     * 2. Điều kiện "Đủ điểm"
     * Kiểm tra KH đủ điểm để áp dụng KM
     * Giả sử km.getDiemCan() là số điểm cần
     --------------------------------*/
    private boolean checkDuDiem(KhuyenMaiDTO km, KhachHangDTO kh) {
        if (kh == null) return false;  

        int diemKH = kh.getDiemTichLuy();
        int diemCan = km.getDiemCanTichLuy(); // điểm yêu cầu của chương trình KM

        return diemKH >= diemCan;
    }
}


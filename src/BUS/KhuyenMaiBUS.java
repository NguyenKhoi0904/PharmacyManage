package BUS;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.KhuyenMaiDAO;
import DTO.KhuyenMaiDTO;

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
        if (this.checkEffectiveDate(khuyenMaiDTO)) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải trước ngày kết thúc", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.khuyenMaiDAO.insert(khuyenMaiDTO) > 0) {
            this.listKhuyenMai.add(khuyenMaiDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "lỗi hàm addKhuyenMai mục <this.khuyenMaiDAO.insert>", "Lỗi",
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
        if (this.checkEffectiveDate(khuyenMaiDTO)) {
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
        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật khuyến mãi.", "Lỗi",
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
        JOptionPane.showMessageDialog(null, "lỗi hàm deleteKhuyenMai: Không tìm thấy khuyến mãi hoặc lỗi CSDL.",
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
        if (khuyenMaiDTO.getNgayBatDau().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .isAfter(khuyenMaiDTO.getNgayKetThuc().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            return false;
        }
        return true;
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
}

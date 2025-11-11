package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.ChiTietPnDAO;
import DTO.ChiTietPnDTO;

public class ChiTietPnBUS {

    // Singleton
    private static ChiTietPnBUS instance;

    // Fields
    private ArrayList<ChiTietPnDTO> listChiTietPn;
    private final ChiTietPnDAO chiTietPnDAO;
    private LoHangBUS loHangBUS;

    private ChiTietPnBUS() {
        this.chiTietPnDAO = ChiTietPnDAO.getInstance();
        this.listChiTietPn = this.chiTietPnDAO.selectAll();
    }

    public static ChiTietPnBUS getInstance() {

        if (instance == null) {
            instance = new ChiTietPnBUS();
        }
        return instance;
    }

    // ==============================
    // THÊM CHI TIẾT PHIẾU NHẬP
    // ==============================
    public boolean addChiTietPn(ChiTietPnDTO chiTietPnDTO) {
        // Kiểm tra trùng key
        if (this.getMapByKey().containsKey(generateKey(chiTietPnDTO.getMaPn(), chiTietPnDTO.getMaLh()))) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập đã tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra LoHangBUS đã được set
        if (this.loHangBUS == null) {
            JOptionPane.showMessageDialog(null, "Lỗi hệ thống: LoHangBUS chưa được khởi tạo!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra mã lô hàng hợp lệ
        if (this.loHangBUS.getLoHangByMaLh(chiTietPnDTO.getMaLh()) == null) {
            JOptionPane.showMessageDialog(null, "Mã lô hàng không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Ghi vào DB
        if (this.chiTietPnDAO.insert(chiTietPnDTO) > 0) {
            // Cập nhật tồn kho
            this.loHangBUS.updateSlTonLoHang(chiTietPnDTO.getMaLh(), chiTietPnDTO.getSoLuong());
            // Thêm vào cache
            this.listChiTietPn.add(chiTietPnDTO);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Không thể thêm chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ==============================
    // CẬP NHẬT CHI TIẾT PHIẾU NHẬP
    // ==============================
    public boolean updateChiTietPn(ChiTietPnDTO chiTietPnDTO) {
        if (!this.getMapByKey().containsKey(generateKey(chiTietPnDTO.getMaPn(), chiTietPnDTO.getMaLh()))) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập không tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);







            return false;
        }

        if (this.chiTietPnDAO.update(chiTietPnDTO) > 0) {
            for (int i = 0; i < this.listChiTietPn.size(); i++) {
                ChiTietPnDTO ct = this.listChiTietPn.get(i);
                if (ct.getMaPn() == chiTietPnDTO.getMaPn() && ct.getMaLh() == chiTietPnDTO.getMaLh()) {
                    this.listChiTietPn.set(i, chiTietPnDTO);
                    break;
                }





            }
            return true;

        }

        JOptionPane.showMessageDialog(null, "Không thể cập nhật chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ==============================
    // XOÁ CHI TIẾT PHIẾU NHẬP THEO MÃ PN + MÃ LH
    // ==============================
    public boolean deleteChiTietPn(int maPn, int maLh) {
        if (!this.getMapByKey().containsKey(generateKey(maPn, maLh))) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập không tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.chiTietPnDAO.deleteById(maPn, maLh) > 0) {
            ChiTietPnDTO chiTiet = this.getChiTietPnByKey(maPn, maLh);
            if (chiTiet != null && this.loHangBUS != null) {
                this.loHangBUS.updateSlTonLoHang(chiTiet.getMaLh(), -chiTiet.getSoLuong());



            }


            // Nếu phiếu nhập không còn chi tiết -> xoá phiếu nhập
            if (this.chiTietPnDAO.selectAllByMaPn(maPn).isEmpty()) {
                PhieuNhapBUS.getInstance().deletePhieuNhap(maPn);
            }

            this.listChiTietPn.removeIf(ct -> ct.getMaPn() == maPn && ct.getMaLh() == maLh);
            return true;









        }

        JOptionPane.showMessageDialog(null, "Không thể xoá chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ==============================
    // XOÁ TOÀN BỘ CHI TIẾT THEO MÃ PN
    // ==============================
    public boolean deleteAllByMaPn(int maPn) {
        if (!this.getMapByMaPn().containsKey(maPn)) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập không tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        ArrayList<ChiTietPnDTO> listToDelete = new ArrayList<>(this.getListChiTietPnByMaPn(maPn));

        if (this.chiTietPnDAO.deleteById(maPn) > 0) {
            // Cập nhật lại tồn kho
            for (ChiTietPnDTO ct : listToDelete) {
                if (this.loHangBUS != null)
                    this.loHangBUS.updateSlTonLoHang(ct.getMaLh(), -ct.getSoLuong());
            }

            // Xoá phiếu nhập nếu không còn chi tiết
            if (this.chiTietPnDAO.selectAllByMaPn(maPn).isEmpty()) {
                PhieuNhapBUS.getInstance().deletePhieuNhap(maPn);

            }

            this.listChiTietPn.removeIf(ct -> ct.getMaPn() == maPn);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Không thể xoá chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ==============================
    // LẤY DỮ LIỆU
    // ==============================
    public ArrayList<ChiTietPnDTO> getListChiTietPn() {
        return this.listChiTietPn;
    }

    public ArrayList<ChiTietPnDTO> getListChiTietPnByMaPn(int ma_pn) {
        return this.chiTietPnDAO.selectAllByMaPn(ma_pn);

    }

    private ChiTietPnDTO getChiTietPnByKey(int maPn, int maLh) {
        for (ChiTietPnDTO ct : this.listChiTietPn) {
            if (ct.getMaPn() == maPn && ct.getMaLh() == maLh) {
                return ct;

            }
        }
        return null;
    }

    private HashMap<Integer, ChiTietPnDTO> getMapByMaPn() {
        HashMap<Integer, ChiTietPnDTO> map = new HashMap<>();
        for (ChiTietPnDTO ct : this.listChiTietPn) {
            map.put(ct.getMaPn(), ct);
        }
        return map;
    }

    public HashMap<String, ChiTietPnDTO> getMapByKey() {
        HashMap<String, ChiTietPnDTO> map = new HashMap<>();
        for (ChiTietPnDTO ct : this.listChiTietPn) {
            map.put(generateKey(ct.getMaPn(), ct.getMaLh()), ct);
        }
        return map;
    }

    private String generateKey(int maPn, int maLh) {
        return maPn + "_" + maLh;
    }

    // ==============================
    // SETTER
    // ==============================
    public void setLoHangBUS(LoHangBUS loHangBUS) {
        this.loHangBUS = loHangBUS;
    }
}

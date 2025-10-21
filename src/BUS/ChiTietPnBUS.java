package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.ChiTietPnDAO;
import DTO.ChiTietPnDTO;

public class ChiTietPnBUS {

    // singleton instance
    private static ChiTietPnBUS instance;

    // field
    private ArrayList<ChiTietPnDTO> listChiTietPn;
    private final ChiTietPnDAO chiTietPnDAO;
    private final LoHangBUS loHangBUS;

    private ChiTietPnBUS() {
        this.chiTietPnDAO = ChiTietPnDAO.getInstance();
        this.loHangBUS = LoHangBUS.getInstance();
        this.listChiTietPn = this.chiTietPnDAO.selectAll();
    }

    // singleton init
    public static ChiTietPnBUS getInstance() {
        if (instance == null) {
            instance = new ChiTietPnBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========

    public boolean addChiTietPn(ChiTietPnDTO chiTietPnDTO) {
        // kiểm tra nếu đã tồn tại mã phiếu nhập và mã lô hàng
        if (this.getMapByKey().containsKey(generateKey(chiTietPnDTO.getMaPn(), chiTietPnDTO.getMaLh()))) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập đã tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu lô hàng có tồn tại
        if (this.loHangBUS.getLoHangByMaLh(chiTietPnDTO.getMaLh()) == null) {
            JOptionPane.showMessageDialog(null, "Mã lô hàng không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.chiTietPnDAO.insert(chiTietPnDTO) > 0) {
            this.loHangBUS.updateSlTonLoHang(chiTietPnDTO.getMaLh(), chiTietPnDTO.getSoLuong());
            this.listChiTietPn.add(chiTietPnDTO);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể thêm chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateChiTietPn(ChiTietPnDTO chiTietPnDTO) {
        // kiểm tra tồn tại
        if (!this.getMapByKey().containsKey(generateKey(chiTietPnDTO.getMaPn(), chiTietPnDTO.getMaLh()))) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập không tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.chiTietPnDAO.update(chiTietPnDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listChiTietPn.size(); i++) {
                ChiTietPnDTO ct = this.listChiTietPn.get(i);
                if (ct.getMaPn() == chiTietPnDTO.getMaPn() && ct.getMaLh() == chiTietPnDTO.getMaLh()) {
                    this.listChiTietPn.set(i, chiTietPnDTO);
                    break;
                }
            }
            return true;
        }

        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteChiTietPn(int maPn, int maLh) {
        // kiểm tra tồn tại
        if (!this.getMapByKey().containsKey(generateKey(maPn, maLh))) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập không tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.chiTietPnDAO.deleteById(maPn, maLh) > 0) {
            // trừ tồn kho
            ChiTietPnDTO chiTiet = this.getChiTietPnByKey(maPn, maLh);
            if (chiTiet != null) {
                this.loHangBUS.updateSlTonLoHang(chiTiet.getMaLh(), -chiTiet.getSoLuong());
            }

            // kiểm tra nếu chi tiết phiếu nhập đó là cuối cùng -> xoá phiếu nhập
            if (this.chiTietPnDAO.selectAllByMaPn(maPn).isEmpty()) {
                PhieuNhapBUS.getInstance().deletePhieuNhap(maPn);
            }

            // cập nhật cache
            this.listChiTietPn.removeIf(ct -> ct.getMaPn() == maPn && ct.getMaLh() == maLh);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể xoá chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteAllByMaPn(int maPn) {
        // kiểm tra tồn tại
        if (!this.getMapByMaPn().containsKey(maPn)) {
            JOptionPane.showMessageDialog(null, "Chi tiết phiếu nhập không tồn tại!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.chiTietPnDAO.deleteById(maPn) > 0) {
            // trừ tồn kho
            ChiTietPnDTO chiTiet = this.getChiTietPnByKey(maPn);
            if (chiTiet != null) {
                this.loHangBUS.updateSlTonLoHang(chiTiet.getMaLh(), -chiTiet.getSoLuong());
            }

            // kiểm tra nếu chi tiết phiếu nhập đó là cuối cùng -> xoá phiếu nhập
            if (this.chiTietPnDAO.selectAllByMaPn(maPn).isEmpty()) {
                PhieuNhapBUS.getInstance().deletePhieuNhap(maPn);
            }

            // cập nhật cache
            this.listChiTietPn.removeIf(ct -> ct.getMaPn() == maPn);
            return true;
        }

        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể xoá chi tiết phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== GET DỮ LIỆU ==========

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

    private ChiTietPnDTO getChiTietPnByKey(int maPn) {
        for (ChiTietPnDTO ct : this.listChiTietPn) {
            if (ct.getMaPn() == maPn) {
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

    // helper - 2 PK
    private String generateKey(int maPn, int maLh) {
        return maPn + "_" + maLh;
    }
}

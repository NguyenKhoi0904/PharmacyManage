package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.PhieuNhapDAO;
import DTO.ChiTietPnDTO;
import DTO.PhieuNhapDTO;

public class PhieuNhapBUS {

    // singleton instance
    private static PhieuNhapBUS instance;

    // field
    private ArrayList<PhieuNhapDTO> listPhieuNhap;
    private final PhieuNhapDAO phieuNhapDAO;
    private ChiTietPnBUS chiTietPnBUS;
    private LoHangBUS loHangBUS;
    private NhanVienBUS nhanVienBUS;

    private PhieuNhapBUS() {
        this.phieuNhapDAO = PhieuNhapDAO.getInstance();
        // this.chiTietPnBUS = ChiTietPnBUS.getInstance();
        this.listPhieuNhap = this.phieuNhapDAO.selectAll();
        // this.loHangBUS = LoHangBUS.getInstance();
        // this.nhanVienBUS = NhanVienBUS.getInstance();
    }

    // singleton init
    public static PhieuNhapBUS getInstance() {
        if (instance == null) {
            instance = new PhieuNhapBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addPhieuNhap(PhieuNhapDTO phieuNhapDTO, ArrayList<ChiTietPnDTO> danhSachChiTietPn) {
        // kiểm tra nếu mã phiếu nhập đã tồn tại
        if (this.checkIfMaPnExist(phieuNhapDTO)) {
            JOptionPane.showMessageDialog(null, "Mã phiếu nhập đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu mã nhân viên không tồn tại trong bảng nhanvien
        if (!this.nhanVienBUS.getMapByMaNv().containsKey(phieuNhapDTO.getMaNv())) {
            JOptionPane.showMessageDialog(null, "Mã nhân viên không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra danh sách chi tiết phiếu nhập
        if (danhSachChiTietPn == null || danhSachChiTietPn.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Lỗi hàm addPhieuNhap : danhSachPn rỗng hoặc null", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra ma_lh trước khi insert phiếu nhập
        for (ChiTietPnDTO ct : danhSachChiTietPn) {
            if (this.loHangBUS.getLoHangByMaLh(ct.getMaLh()) == null) {
                JOptionPane.showMessageDialog(null, "Mã lô hàng không tồn tại: " + ct.getMaLh());
                return false;
            }
        }

        // thêm vào db + list
        if (this.phieuNhapDAO.insert(phieuNhapDTO) > 0) {
            for (ChiTietPnDTO ct : danhSachChiTietPn) {
                ct.setMaPn(phieuNhapDTO.getMaPn());

                this.chiTietPnBUS.addChiTietPn(ct);

                // tăng số lượng tồn trong lô hàng
                this.loHangBUS.updateSlTonLoHang(ct.getMaLh(), ct.getSoLuong());
            }

            this.listPhieuNhap.add(phieuNhapDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Lỗi hàm addPhieuNhap mục <this.phieuNhapDAO.insert>", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updatePhieuNhap(int ma_pn, PhieuNhapDTO phieuNhapDTO) {
        // kiểm tra nếu mã phiếu nhập không tồn tại
        if (!this.getMapByMaPn().containsKey(ma_pn)) {
            JOptionPane.showMessageDialog(null, "Mã phiếu nhập không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu mã nhân viên không tồn tại trong bảng nhanvien
        if (!this.nhanVienBUS.getMapByMaNv().containsKey(phieuNhapDTO.getMaNv())) {
            JOptionPane.showMessageDialog(null, "Mã nhân viên không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.phieuNhapDAO.update(phieuNhapDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listPhieuNhap.size(); i++) {
                if (this.listPhieuNhap.get(i).getMaPn() == ma_pn) {
                    this.listPhieuNhap.set(i, phieuNhapDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Lỗi CSDL: Không thể cập nhật phiếu nhập.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deletePhieuNhap(int ma_pn) {
        // lấy chi tiết phiếu nhập trước khi xóa để hoàn tác tồn kho
        ArrayList<ChiTietPnDTO> chiTietList = this.chiTietPnBUS.getListChiTietPnByMaPn(ma_pn);

        if (this.phieuNhapDAO.deleteById(String.valueOf(ma_pn)) > 0) {

            // cập nhật tồn kho trong bảng lohang sau khi xoá phiếu nhập
            if (chiTietList != null) {
                for (ChiTietPnDTO chiTiet : chiTietList) {
                    // trừ đi số lượng nhập trước đó
                    int sl_hoan_tac = -(chiTiet.getSoLuong());
                    this.loHangBUS.updateSlTonLoHang(chiTiet.getMaLh(), sl_hoan_tac);
                }
            }

            // xoá chi tiết phiếu nhập
            this.chiTietPnBUS.deleteAllByMaPn(ma_pn);

            // cập nhật cache
            this.listPhieuNhap.removeIf(pn -> pn.getMaPn() == ma_pn);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Lỗi hàm deletePhieuNhap: Không tìm thấy hoặc lỗi CSDL.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public boolean checkIfMaPnExist(PhieuNhapDTO phieuNhapDTO) {
        if (this.getMapByMaPn().containsKey(phieuNhapDTO.getMaPn())) {
            // nếu mã phiếu nhập đã tồn tại
            return true;
        }
        return false;
    }

    // ========== GET DỮ LIỆU ==========

    public ArrayList<PhieuNhapDTO> getListPhieuNhap() {
        return this.listPhieuNhap;
    }

    public PhieuNhapDTO getPhieuNhapByMaPn(int ma_pn) {
        for (PhieuNhapDTO pn : this.listPhieuNhap) {
            if (pn.getMaPn() == ma_pn) {
                return pn;
            }
        }
        return null;
    }

    public HashMap<Integer, PhieuNhapDTO> getMapByMaPn() {
        HashMap<Integer, PhieuNhapDTO> mapMaPn = new HashMap<Integer, PhieuNhapDTO>();
        for (PhieuNhapDTO pn : this.listPhieuNhap) {
            mapMaPn.put(pn.getMaPn(), pn);
        }
        return mapMaPn;
    }

    // ========== SET BUS ==========
    public void setChiTietPnBUS(ChiTietPnBUS chiTietPnBUS) {
        this.chiTietPnBUS = chiTietPnBUS;
    }

    public void setLoHangBUS(LoHangBUS loHangBUS) {
        this.loHangBUS = loHangBUS;
    }

    public void setNhanVienBUS(NhanVienBUS nhanVienBUS) {
        this.nhanVienBUS = nhanVienBUS;
    }
}

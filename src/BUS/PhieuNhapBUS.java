package BUS;

import java.util.ArrayList;

import DAO.ChiTietPnDAO;
import DAO.LoHangDAO;
import DAO.PhieuNhapDAO;
import DTO.ChiTietPnDTO;
import DTO.PhieuNhapDTO;

public class PhieuNhapBUS {
    private ArrayList<PhieuNhapDTO> listPhieuNhap;
    private final PhieuNhapDAO phieuNhapDAO;
    private final ChiTietPnDAO chiTietPnDAO;
    private final LoHangDAO loHangDAO;

    public PhieuNhapBUS() {
        this.phieuNhapDAO = PhieuNhapDAO.getInstance();
        this.chiTietPnDAO = ChiTietPnDAO.getInstance();
        this.loHangDAO = LoHangDAO.getInstance();
        this.listPhieuNhap = this.phieuNhapDAO.selectAll();
    }

    // ========== DATABASE HANDLE ==========
    public boolean addPhieuNhap(PhieuNhapDTO phieuNhapDTO, ArrayList<ChiTietPnDTO> danhSachChiTietPn) {
        // kiểm tra danh sách chi tiết phiếu nhập
        if (danhSachChiTietPn == null || danhSachChiTietPn.isEmpty()) {
            System.out.println("lỗi hamgf addPhieuNhap danhSachPn rỗng hoặc null");
            return false;
        }

        // thêm vào db + list
        if (this.phieuNhapDAO.insert(phieuNhapDTO) > 0) {
            for (ChiTietPnDTO ct : danhSachChiTietPn) {
                ct.setMaPn(phieuNhapDTO.getMaPn());

                chiTietPnDAO.insert(ct);

                // tăng số lượng tồn trong lô hàng
                this.loHangDAO.updateSlTon(ct.getMaLh(), ct.getSoLuong());
            }

            this.listPhieuNhap.add(phieuNhapDTO);
            return true;
        }
        System.out.println("lỗi hàm addPhieuNhap mục <this.phieuNhapDAO.insert>");
        return false;
    }

    public boolean updatePhieuNhap(int ma_pn, PhieuNhapDTO phieuNhapDTO) {
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
        System.out.println("Lỗi CSDL: Không thể cập nhật phiếu nhập.");
        return false;
    }

    public boolean deletePhieuNhap(int ma_pn, int ma_lh) {
        // lấy chi tiết phiếu nhập trước khi xóa để hoàn tác tồn kho
        ArrayList<ChiTietPnDTO> chiTietList = this.chiTietPnDAO.selectAllByMa(ma_pn, ma_lh);

        if (this.phieuNhapDAO.deleteById(String.valueOf(ma_pn)) > 0) {

            // cập nhật tồn kho trong bảng lohang sau khi xoá phiếu nhập
            if (chiTietList != null) {
                for (ChiTietPnDTO chiTiet : chiTietList) {
                    // trừ đi số lượng nhập trước đó
                    int sl_hoan_tac = -(chiTiet.getSoLuong());

                    loHangDAO.updateSlTon(chiTiet.getMaLh(), sl_hoan_tac);
                }
            }

            // xoá chi tiết khỏi bảng chitiet_pn
            chiTietPnDAO.deleteById(ma_pn, ma_lh);

            // cập nhật cache
            for (int i = 0; i < this.listPhieuNhap.size(); i++) {
                if (this.listPhieuNhap.get(i).getMaPn() == ma_pn) {
                    this.listPhieuNhap.get(i).setTrangThai(0);
                    break;
                }
            }
            return true;
        }
        System.out.println("lỗi hàm deletePhieuNhap: Không tìm thấy hoặc lỗi CSDL.");
        return false;
    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<PhieuNhapDTO> getListPhieuNhap() {
        return this.listPhieuNhap;
    }

    public PhieuNhapDTO getPhieuNhapByMaPn(int ma_ncc) {
        for (PhieuNhapDTO pn : this.listPhieuNhap) {
            if (pn.getMaPn() == ma_ncc) {
                return pn;
            }
        }
        return null;

    }
}

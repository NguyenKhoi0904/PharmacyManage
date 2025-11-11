package BUS;

import java.util.ArrayList;
import javax.swing.JOptionPane;

import DAO.ChiTietHdDAO;
import DTO.ChiTietHdDTO;
import DTO.HoaDonDTO;
import java.math.BigDecimal;

public class ChiTietHdBUS {

    // singleton instance
    private static ChiTietHdBUS instance;

    private final ChiTietHdDAO chiTietHdDAO;
    private ArrayList<ChiTietHdDTO> listChiTietHd;

    private ChiTietHdBUS() {
        this.chiTietHdDAO = ChiTietHdDAO.getInstance();
        this.listChiTietHd = this.chiTietHdDAO.selectAll();
    }

    // singleton init
    public static ChiTietHdBUS getInstance() {
        if (instance == null) {
            instance = new ChiTietHdBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========

    public boolean addChiTietHd(ChiTietHdDTO chiTietHd) {
        if (this.chiTietHdDAO.insert(chiTietHd) > 0) {
            this.listChiTietHd.add(chiTietHd);
            
            try {
                ArrayList<ChiTietHdDTO> listCTHD = getListChiTietHdByMaHd(chiTietHd.getMaHd());
                BigDecimal newSum = BigDecimal.ZERO;

                for (ChiTietHdDTO cthd : listCTHD) {
                    newSum = newSum.add(cthd.getDonGia().multiply(BigDecimal.valueOf(cthd.getSoLuong())));
                }

                HoaDonDTO hd = BUSManager.hoaDonBUS.getHoaDonByMaHd(chiTietHd.getMaHd());
                hd.setTongTien(newSum);
                BUSManager.hoaDonBUS.updateHoaDon(hd.getMaHd(), hd);

                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Không thể cập nhật HĐ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    public boolean updateChiTietHd(ChiTietHdDTO chiTietHd) {
        // Lấy chi tiết cũ trước khi update để tính toán tồn kho
        ChiTietHdDTO old = null;
        for (ChiTietHdDTO ct : listChiTietHd) {
            if (ct.getMaHd() == chiTietHd.getMaHd()
                    && ct.getMaLh() == chiTietHd.getMaLh()
                    && ct.getMaThuoc() == chiTietHd.getMaThuoc()) {
                old = ct;
                break;
            }
        }

        if (old == null) {
            JOptionPane.showMessageDialog(null, "Chi tiết hoá đơn không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // Hoàn tác tồn kho cũ: + số lượng cũ
            BUSManager.loHangBUS.updateSlTonLoHang(old.getMaLh(), old.getSoLuong());

            // Trừ tồn kho mới: - số lượng mới
            BUSManager.loHangBUS.updateSlTonLoHang(chiTietHd.getMaLh(), -chiTietHd.getSoLuong());

            // Cập nhật chi tiết hóa đơn
            if (this.chiTietHdDAO.update(chiTietHd) > 0) {
                // Cập nhật cache
                listChiTietHd.set(listChiTietHd.indexOf(old), chiTietHd);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể cập nhật tồn kho: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        
        try {
            ArrayList<ChiTietHdDTO> listCTHD = getListChiTietHdByMaHd(chiTietHd.getMaHd());
            BigDecimal newSum = BigDecimal.ZERO;

            for (ChiTietHdDTO cthd : listCTHD) {
                newSum = newSum.add(cthd.getDonGia().multiply(BigDecimal.valueOf(cthd.getSoLuong())));
            }
            
            HoaDonDTO hd = BUSManager.hoaDonBUS.getHoaDonByMaHd(chiTietHd.getMaHd());
            hd.setTongTien(newSum);
            BUSManager.hoaDonBUS.updateHoaDon(hd.getMaHd(), hd);
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể cập nhật HĐ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }


    public boolean deleteChiTietHd(int ma_hd, int ma_lh, int ma_thuoc) {
        // Lấy chi tiết trước khi xóa
        ChiTietHdDTO ctToDelete = null;
        for (ChiTietHdDTO ct : listChiTietHd) {
            if (ct.getMaHd() == ma_hd && ct.getMaLh() == ma_lh && ct.getMaThuoc() == ma_thuoc) {
                ctToDelete = ct;
                break;
            }
        }

        if (ctToDelete == null) return false;

        try {
            // Hoàn tác tồn kho: + lại số lượng đã dùng
            BUSManager.loHangBUS.updateSlTonLoHang(ctToDelete.getMaLh(), ctToDelete.getSoLuong());

            // Xóa chi tiết
            if (this.chiTietHdDAO.deleteById(ma_hd, ma_lh, ma_thuoc) > 0) {
                listChiTietHd.remove(ctToDelete);
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể cập nhật tồn kho khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }


    public boolean deleteAllByMaHd(int ma_hd) {
        ArrayList<ChiTietHdDTO> chiTietList = getListChiTietHdByMaHd(ma_hd);

        if (chiTietList.isEmpty()) {
            System.err.println("Không có chi tiết hoá đơn nào để xoá với mã HD=" + ma_hd);
            return false;
        }

        try {
            for (ChiTietHdDTO ct : chiTietList) {
                // Hoàn tác tồn kho cho từng chi tiết
                BUSManager.loHangBUS.updateSlTonLoHang(ct.getMaLh(), ct.getSoLuong());
            }

            // Xóa tất cả chi tiết hóa đơn
            if (this.chiTietHdDAO.deleteAllById(ma_hd) > 0) {
                listChiTietHd.removeIf(ct -> ct.getMaHd() == ma_hd);
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể cập nhật tồn kho khi xóa tất cả chi tiết: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }


    // ========== GET DỮ LIỆU ==========
    public ArrayList<ChiTietHdDTO> getListChiTietHd() {
        return this.listChiTietHd;
    }

    public ArrayList<ChiTietHdDTO> getListChiTietHdByMaHd(int ma_hd) {
        ArrayList<ChiTietHdDTO> result = new ArrayList<>();
        for (ChiTietHdDTO ct : this.listChiTietHd) {
            if (ct.getMaHd() == ma_hd) {
                result.add(ct);
            }
        }
        return result;
    }
    
    public boolean existsCTHD(int maHd, int maLh, int maThuoc) {
        return ChiTietHdDAO.getInstance().selectByCompositeKey(maHd, maLh, maThuoc) != null;
    }

    public ChiTietHdDTO getCTHDBy3Key(int maHd, int maLh, int maThuoc) {
        return ChiTietHdDAO.getInstance().selectByCompositeKey(maHd, maLh, maThuoc);
    }
}

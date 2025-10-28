package BUS;

import java.util.ArrayList;
import javax.swing.JOptionPane;

import DAO.ChiTietHdDAO;
import DTO.ChiTietHdDTO;

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
            return true;
        }
        return false;
    }

    public boolean updateChiTietHd(ChiTietHdDTO chiTietHd) {
        if (this.chiTietHdDAO.update(chiTietHd) > 0) {
            for (int i = 0; i < this.listChiTietHd.size(); i++) {
                ChiTietHdDTO current = this.listChiTietHd.get(i);
                if (current.getMaHd() == chiTietHd.getMaHd()
                        && current.getMaLh() == chiTietHd.getMaLh()
                        && current.getMaThuoc() == chiTietHd.getMaThuoc()) {
                    this.listChiTietHd.set(i, chiTietHd);
                    return true;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Không thể cập nhật chi tiết hoá đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteChiTietHd(int ma_hd, int ma_lh, int ma_thuoc) {
        if (this.chiTietHdDAO.deleteById(ma_hd, ma_lh, ma_thuoc) > 0) {
            this.listChiTietHd
                    .removeIf(ct -> ct.getMaHd() == ma_hd && ct.getMaLh() == ma_lh && ct.getMaThuoc() == ma_thuoc);
            return true;
        }
        return false;
    }

    public boolean deleteAllByMaHd(int ma_hd) {
        if (this.chiTietHdDAO.deleteAllById(ma_hd) > 0) {
            this.listChiTietHd.removeIf(ct -> ct.getMaHd() == ma_hd);
            return true;
        }
        System.err.println("Không có chi tiết hoá đơn nào để xoá với mã HD=" + ma_hd);
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
    
}

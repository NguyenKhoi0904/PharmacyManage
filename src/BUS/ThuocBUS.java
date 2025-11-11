package BUS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.ThuocDAO;
import DTO.ThuocDTO;

public class ThuocBUS {

    // singleton instance
    private static ThuocBUS instance;

    // field
    private ArrayList<ThuocDTO> listThuoc;
    private ThuocDAO thuocDAO;
    private DanhMucThuocBUS danhMucThuocBUS;
    private LoHangBUS loHangBUS;

    private ThuocBUS() {
        this.thuocDAO = ThuocDAO.getInstance();
        // this.danhMucThuocBUS = DanhMucThuocBUS.getInstance();
        // this.loHangBUS = LoHangBUS.getInstance();
        this.listThuoc = this.thuocDAO.selectAll();
    }

    // get singleton
    public static ThuocBUS getInstance() {
        if (instance == null) {
            instance = new ThuocBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addThuoc(ThuocDTO thuocDTO) {
        // kiểm tra nếu mã thuốc đã tồn tại
        if (this.getMapByMaThuoc().containsKey(thuocDTO.getMaThuoc())) {
            JOptionPane.showMessageDialog(null, "Mã thuốc đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu ma_dmt của thuocDTO không tồn tại trong list danh mục thuốc
        if (!this.danhMucThuocBUS.checkMaDmtExist(thuocDTO.getMaDmt())) {
            JOptionPane.showMessageDialog(null, "Mã danh mục thuốc không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.thuocDAO.insert(thuocDTO) > 0) {
            this.listThuoc.add(thuocDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể thêm thuốc", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateThuoc(ThuocDTO thuocDTO) {
        // kiểm tra nếu mã thuốc không tồn tại
        if (!this.getMapByMaThuoc().containsKey(thuocDTO.getMaThuoc())) {
            JOptionPane.showMessageDialog(null, "Mã thuốc không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu ma_dmt của thuocDTO không tồn tại trong list danh mục thuốc
        if (!this.danhMucThuocBUS.checkMaDmtExist(thuocDTO.getMaDmt())) {
            JOptionPane.showMessageDialog(null, "Mã danh mục thuốc không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.thuocDAO.update(thuocDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listThuoc.size(); i++) {
                if (this.listThuoc.get(i).getMaThuoc() == thuocDTO.getMaThuoc()) {
                    this.listThuoc.set(i, thuocDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể cập nhật thuốc", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteThuoc(int ma_thuoc) {
        // kiểm tra nếu ma_thuoc của listThuoc không tồn tại trong list
        if (!this.getMapByMaThuoc().containsKey(ma_thuoc)) {
            JOptionPane.showMessageDialog(null, "Mã thuốc không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu vẫn còn lô hàng tồn tại -> không cho xoá
        if (this.checkIfLoHangExist(ma_thuoc)) {
            JOptionPane.showMessageDialog(null, "Không thể xoá thuốc khi vẫn còn lô hàng được chỉ đến", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.thuocDAO.deleteById(String.valueOf(ma_thuoc)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listThuoc.size(); i++) {
                if (this.listThuoc.get(i).getMaThuoc() == ma_thuoc) {
                    this.listThuoc.remove(i);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "Không thể xoá thuốc.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public BigDecimal getTongTienThuoc() {
        BigDecimal bd = new BigDecimal(0);
        for (ThuocDTO thuoc : this.listThuoc) {
            bd = bd.add(
                    thuoc.getGia().multiply(BigDecimal.valueOf(this.loHangBUS.getSlTonByMaThuoc(thuoc.getMaThuoc()))));

        }
        return bd;
    }

    private boolean checkIfLoHangExist(int ma_thuoc) {
        // Trả về true nếu CÓ lô hàng chỉ đến thuốc này
        return this.loHangBUS.getListLoHang()
                .stream()
                .anyMatch(lh -> lh.getMaThuoc() == ma_thuoc);
    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<ThuocDTO> getListThuoc() {
        return this.listThuoc;
    }

    public ThuocDTO getThuocByMaThuoc(int ma_thuoc) {
        for (ThuocDTO thuoc : this.listThuoc) {
            if (thuoc.getMaThuoc() == ma_thuoc) {
                return thuoc;
            }
        }
        return null;
    }

    /**
     * 
     * @return HashMap&lt;ThuocDTO.getMaThuoc,ThuocDTO&gt;
     */
    public HashMap<Integer, ThuocDTO> getMapByMaThuoc() {
        HashMap<Integer, ThuocDTO> mapThuoc = new HashMap<Integer, ThuocDTO>();
        for (ThuocDTO thuoc : this.listThuoc) {
            mapThuoc.put(thuoc.getMaThuoc(), thuoc);
        }
        return mapThuoc;
    }

    // ========== SET BUS ==========
    public void setDanhMucThuocBUS(DanhMucThuocBUS danhMucThuocBUS) {
        this.danhMucThuocBUS = danhMucThuocBUS;
    }

    public void setLoHangBUS(LoHangBUS loHangBUS) {
        this.loHangBUS = loHangBUS;
    }
}

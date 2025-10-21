package BUS;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.DanhMucThuocDAO;
import DTO.DanhMucThuocDTO;

public class DanhMucThuocBUS {

    // singleton instance
    private static DanhMucThuocBUS instance;

    // field
    private ArrayList<DanhMucThuocDTO> listDanhMucThuoc;
    private DanhMucThuocDAO danhMucThuocDAO;

    private DanhMucThuocBUS() {
        this.danhMucThuocDAO = DanhMucThuocDAO.getInstance();
        this.listDanhMucThuoc = this.danhMucThuocDAO.selectAll();
    }

    // get singleton
    public static DanhMucThuocBUS getInstance() {
        if (instance == null) {
            instance = new DanhMucThuocBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addDanhMucThuoc(DanhMucThuocDTO danhMucThuocDTO) {
        // kiểm tra nếu ma_dmt đã tồn tại trong list danh mục thuốc
        if (this.checkMaDmtExist(danhMucThuocDTO.getMaDmt())) {
            JOptionPane.showMessageDialog(null, "Mã danh mục thuốc đã tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.danhMucThuocDAO.insert(danhMucThuocDTO) > 0) {
            this.listDanhMucThuoc.add(danhMucThuocDTO);
            return true;
        }
        JOptionPane.showMessageDialog(null, "addDanhMucThuoc: mục <this.danhMucThuocDAO.insert>", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean updateDanhMucThuoc(DanhMucThuocDTO danhMucThuocDTO) {
        // kiểm tra nếu ma_dmt của thuocDTO không tồn tại trong list danh mục thuốc
        if (!this.checkMaDmtExist(danhMucThuocDTO.getMaDmt())) {
            JOptionPane.showMessageDialog(null, "Mã danh mục thuốc không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.danhMucThuocDAO.update(danhMucThuocDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listDanhMucThuoc.size(); i++) {
                if (this.listDanhMucThuoc.get(i).getMaDmt() == danhMucThuocDTO.getMaDmt()) {
                    this.listDanhMucThuoc.set(i, danhMucThuocDTO);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "updateDanhMucThuoc: lỗi CSDL", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteDanhMucThuoc(int ma_dmt) {
        // kiểm tra nếu ma_dmt của thuocDTO không tồn tại trong list danh mục thuốc
        if (!this.checkMaDmtExist(ma_dmt)) {
            JOptionPane.showMessageDialog(null, "Mã danh mục thuốc không tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // kiểm tra nếu vẫn còn thuốc thuộc danh mục này
        if (!ThuocBUS.getInstance().getListThuoc().stream()
                .filter(t -> t.getMaDmt() == ma_dmt)
                .toList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không thể xoá danh mục còn thuốc liên kết", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.danhMucThuocDAO.deleteById(String.valueOf(ma_dmt)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listDanhMucThuoc.size(); i++) {
                if (this.listDanhMucThuoc.get(i).getMaDmt() == ma_dmt) {
                    this.listDanhMucThuoc.remove(i);
                    break;
                }
            }
            return true;
        }
        JOptionPane.showMessageDialog(null, "deleteDanhMucThuoc: lỗi CSDL.", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public boolean checkMaDmtExist(int ma_dmt) {
        return this.getMapByDmt().containsKey(ma_dmt);
    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<DanhMucThuocDTO> getListDanhMucThuoc() {
        return this.listDanhMucThuoc;
    }

    public DanhMucThuocDTO getDmtByMaDmt(int ma_dmt) {
        for (DanhMucThuocDTO dm_thuoc : this.listDanhMucThuoc) {
            if (dm_thuoc.getMaDmt() == ma_dmt) {
                return dm_thuoc;
            }
        }
        return null;
    }

    /**
     * 
     * @return HashMap&lt;DanhMucThuocDTO.getMaDmt,DanhMucThuocDTO&gt;
     */
    public HashMap<Integer, DanhMucThuocDTO> getMapByDmt() {
        HashMap<Integer, DanhMucThuocDTO> map = new HashMap<Integer, DanhMucThuocDTO>();
        for (DanhMucThuocDTO dmt : this.listDanhMucThuoc) {
            map.put(dmt.getMaDmt(), dmt);
        }
        return map;
    }
}

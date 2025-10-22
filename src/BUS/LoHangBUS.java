package BUS;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DAO.LoHangDAO;
import DTO.LoHangDTO;

public class LoHangBUS {

    // singleton instance
    private static LoHangBUS instance;

    // field
    private ArrayList<LoHangDTO> listLoHang;
    private final LoHangDAO loHangDAO;
    private ThuocBUS thuocBUS;
    // private final NhaCungCapBUS nhaCungCapBUS;

    private LoHangBUS() {
        this.loHangDAO = LoHangDAO.getInstance();
        // this.thuocBUS = ThuocBUS.getInstance();
        // this.nhaCungCapBUS = NhaCungCapBUS.getInstance();
        this.listLoHang = this.loHangDAO.selectAll();
    }

    // singleton init
    public static LoHangBUS getInstance() {
        if (instance == null) {
            instance = new LoHangBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========
    public boolean addLoHang(LoHangDTO loHangDTO) {
        // kiểm tra nếu ngay_sx sau han_sd
        if (this.checkEffectiveDate(loHangDTO)) {
            JOptionPane.showMessageDialog(null, "Ngày sản xuất phải trước hạn sử dụng", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // thêm vào db + list
        if (this.loHangDAO.insert(loHangDTO) > 0) {
            this.listLoHang.add(loHangDTO);
            return true;
        }
        System.out.println("lỗi hàm addLoHang mục <this.loHangDAO.insert>");
        return false;
    }

    public boolean updateLoHang(int ma_lh, LoHangDTO loHangDTO) {
        // kiểm tra nếu ngay_sx sau han_sd
        if (this.checkEffectiveDate(loHangDTO)) {
            JOptionPane.showMessageDialog(null, "Ngày sản xuất phải trước hạn sử dụng", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.loHangDAO.update(loHangDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listLoHang.size(); i++) {
                if (this.listLoHang.get(i).getMaLh() == ma_lh) {
                    this.listLoHang.set(i, loHangDTO);
                    break;
                }
            }
            return true;
        }
        System.out.println("Lỗi CSDL: Không thể cập nhật lô hàng.");
        return false;
    }

    public boolean deleteLoHang(int ma_lh) {
        // kiểm tra nếu vẫn còn mã nhà cung cấp hoạt động
        // if
        // (this.nhaCungCapBUS.getMapByMaNcc().containsKey(this.getLoHangByMaLh(ma_lh).getMaNcc()))
        // {
        // JOptionPane.showMessageDialog(null, "Không thể xoá lô hàng khi nhà cung cấp
        // vẫn tồn tại", "Lỗi",
        // JOptionPane.ERROR_MESSAGE);
        // return false;
        // }

        // if
        // (!NhaCungCapBUS.getInstance().getMapByMaNcc().containsKey(this.getLoHangByMaLh(ma_lh).getMaNcc()))
        // {
        // JOptionPane.showMessageDialog(null, "Không thể xoá lô hàng khi nhà cung cấp
        // đã bị xoá", "Lỗi",
        // JOptionPane.ERROR_MESSAGE);
        // return false;
        // }

        // kiểm tra nếu vẫn còn mã thuốc hoạt động
        if (this.thuocBUS.getMapByMaThuoc().containsKey(this.getLoHangByMaLh(ma_lh).getMaThuoc())) {
            JOptionPane.showMessageDialog(null, "Không thể xoá lô hàng khi thuốc vẫn tồn tại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.loHangDAO.deleteById(String.valueOf(ma_lh)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listLoHang.size(); i++) {
                if (this.listLoHang.get(i).getMaLh() == ma_lh) {
                    this.listLoHang.remove(i);
                    break;
                }
            }
            return true;
        }
        System.out.println("lỗi hàm deleteLoHang: Không tìm thấy lô hàng hoặc lỗi CSDL.");
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    private boolean checkEffectiveDate(LoHangDTO loHangDTO) {
        if (loHangDTO.getNgaySx().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .isAfter(loHangDTO.getHanSd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            return false;
        }
        return true;
    }

    // thêm,bớt số lượng tồn trong lô hàng
    // public boolean updateSlTonLoHang(int ma_lh, int so_luong) {
    // return this.loHangDAO.updateSlTon(ma_lh, so_luong) > 0;
    // }

    public boolean updateSlTonLoHang(int ma_lh, int so_luong) {
        if (this.loHangDAO.updateSlTon(ma_lh, so_luong) > 0) {
            LoHangDTO lh = getLoHangByMaLh(ma_lh);
            if (lh != null)
                lh.setSlTon(so_luong);
            return true;
        }
        return false;
    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<LoHangDTO> getListLoHang() {
        return this.listLoHang;
    }

    public LoHangDTO getLoHangByMaLh(int ma_lh) {
        for (LoHangDTO lh : this.listLoHang) {
            if (lh.getMaLh() == ma_lh) {
                return lh;
            }
        }
        return null;

    }

    // sử dụng trong ThuocBUS.getTongTienThuoc
    public int getSlTonByMaThuoc(int ma_thuoc) {
        for (LoHangDTO lh : this.listLoHang) {
            if (lh.getMaThuoc() == ma_thuoc) {
                return lh.getSlTon();
            }
        }
        return 0;
    }

    public HashMap<Integer, LoHangDTO> getMapByMaLh() {
        HashMap<Integer, LoHangDTO> mapMaLh = new HashMap<Integer, LoHangDTO>();
        for (LoHangDTO lh : this.listLoHang) {
            mapMaLh.put(lh.getMaLh(), lh);
        }
        return mapMaLh;
    }

    // ========== SET BUS ==========
    public void setThuocBUS(ThuocBUS thuocBUS) {
        this.thuocBUS = thuocBUS;
    }
}

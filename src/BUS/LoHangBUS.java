package BUS;

import java.util.ArrayList;

import DAO.LoHangDAO;
import DTO.LoHangDTO;

public class LoHangBUS {
    private ArrayList<LoHangDTO> listLoHang;
    private final LoHangDAO loHangDAO;

    public LoHangBUS() {
        this.loHangDAO = LoHangDAO.getInstance();
        this.listLoHang = this.loHangDAO.selectAll();
    }

    // ========== DATABASE HANDLE ==========
    public boolean addLoHang(LoHangDTO loHangDTO) {
        // thêm vào db + list
        if (this.loHangDAO.insert(loHangDTO) > 0) {
            this.listLoHang.add(loHangDTO);
            return true;
        }
        System.out.println("lỗi hàm addLoHang mục <this.loHangDAO.insert>");
        return false;
    }

    public boolean updateLoHang(int ma_lh, LoHangDTO loHangDTO) {
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
        if (this.loHangDAO.deleteById(String.valueOf(ma_lh)) > 0) {
            // cập nhật cache: Đặt trạng thái = 0
            for (int i = 0; i < this.listLoHang.size(); i++) {
                if (this.listLoHang.get(i).getMaNcc() == ma_lh) {
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

    // thêm,bớt số lượng tồn trong lô hàng
    public boolean updateSlTonLoHang(int ma_lh, int so_luong) {
        return this.loHangDAO.updateSlTon(ma_lh, so_luong) > 0;
    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<LoHangDTO> getListLoHang() {
        return this.listLoHang;
    }

    public LoHangDTO getLoHangByMaLh(int ma_lh) {
        for (LoHangDTO lh : this.listLoHang) {
            if (lh.getMaNcc() == ma_lh) {
                return lh;
            }
        }
        return null;

    }
}

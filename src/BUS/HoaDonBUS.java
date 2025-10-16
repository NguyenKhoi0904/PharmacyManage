package BUS;

import java.util.ArrayList;

import DAO.ChiTietHdDAO;
import DAO.HoaDonDAO;
import DAO.LoHangDAO;
import DTO.ChiTietHdDTO;
import DTO.HoaDonDTO;

public class HoaDonBUS {
    private ArrayList<HoaDonDTO> listHoaDon;
    private HoaDonDAO hoaDonDAO;
    private LoHangDAO loHangDAO;
    private ChiTietHdDAO chiTietHdDAO;

    public HoaDonBUS() {
        this.hoaDonDAO = HoaDonDAO.getInstance();
        this.loHangDAO = LoHangDAO.getInstance();
        this.chiTietHdDAO = ChiTietHdDAO.getInstance();
        this.listHoaDon = this.hoaDonDAO.selectAll();
    }

    // ========== DATABASE HANDLE ==========
    public boolean addHoaDon(HoaDonDTO hoaDon, ArrayList<ChiTietHdDTO> danhSachChiTiet) {
        if (danhSachChiTiet == null || danhSachChiTiet.isEmpty()) {
            System.out.println("Lỗi nghiệp vụ: Hóa đơn phải có ít nhất một chi tiết.");
            return false;
        }

        // 1. NGHIỆP VỤ KIỂM TRA TỒN KHO (Phải kiểm tra trước khi ghi vào CSDL)
        for (ChiTietHdDTO chiTiet : danhSachChiTiet) {
            if (!loHangDAO.checkSlTon(chiTiet.getMaLh(), chiTiet.getSoLuong())) {
                System.out.println("Lỗi tồn kho: Lô hàng " + chiTiet.getMaLh() + " không đủ số lượng.");
                return false;
            }
        }

        // thêm hóa đơn cha vào CSDL
        if (this.hoaDonDAO.insert(hoaDon) > 0) {

            // 3. Lặp và thêm từng Chi Tiết Hóa Đơn
            for (ChiTietHdDTO chiTiet : danhSachChiTiet) {

                // 3.1. Thêm Chi Tiết vào bảng chitiet_hd
                chiTietHdDAO.insert(chiTiet);

                // trừ bớt số lượng tồn trong lô hàng
                this.loHangDAO.updateSlTon(chiTiet.getMaLh(), chiTiet.getSoLuong());
            }

            // 4. Cập nhật cache
            this.listHoaDon.add(hoaDon);
            return true;
        }
        System.out.println("Lỗi CSDL: Không thể thêm Hóa Đơn cha.");
        return false;
    }

    public boolean updateHoaDon(int ma_hd, HoaDonDTO hoaDonDTO) {
        if (this.hoaDonDAO.update(hoaDonDTO) > 0) {
            // cập nhật cache
            for (int i = 0; i < this.listHoaDon.size(); i++) {
                if (this.listHoaDon.get(i).getMaHd() == ma_hd) {
                    this.listHoaDon.set(i, hoaDonDTO);
                    break;
                }
            }
            return true;
        }
        System.out.println("Lỗi CSDL: Không thể cập nhật hoá đơn.");
        return false;
    }

    // Hàm nghiệp vụ: Xóa hóa đơn (Soft Delete)
    public boolean deleteHoaDon(int ma_hd) {
        // 1. NGHIỆP VỤ: Lấy Chi Tiết HĐ trước khi xóa để hoàn tác tồn kho
        ArrayList<ChiTietHdDTO> chiTietList = chiTietHdDAO.selectAllByMa(ma_hd);

        if (this.hoaDonDAO.deleteById(String.valueOf(ma_hd)) > 0) {

            // 2. Hoàn tác TỒN KHO (Nghiệp vụ quan trọng)
            if (chiTietList != null) {
                for (ChiTietHdDTO chiTiet : chiTietList) {
                    // Cộng lại số lượng đã bán vào tồn kho
                    this.loHangDAO.updateSlTon(chiTiet.getMaLh(), chiTiet.getSoLuong());
                }
            }

            // 3. XÓA HARD DELETE Chi Tiết khỏi bảng chitiet_hd
            this.chiTietHdDAO.deleteById(ma_hd); // Dùng mã HĐ để xóa tất cả chi tiết

            // 4. Cập nhật cache
            for (int i = 0; i < this.listHoaDon.size(); i++) {
                if (this.listHoaDon.get(i).getMaHd() == ma_hd) {
                    this.listHoaDon.get(i).setTrangThai(0);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    // public boolean deleteHoaDon(int ma_hd) {
    // if (this.hoaDonDAO.deleteById(String.valueOf(ma_hd)) > 0) {
    // // cập nhật cache: Đặt trạng thái = 0
    // for (int i = 0; i < this.listHoaDon.size(); i++) {
    // if (this.listHoaDon.get(i).getMaHd() == ma_hd) {
    // this.listHoaDon.remove(i);
    // break;
    // }
    // }

    // this.hoaDonDAO.deleteById(String.valueOf(ma_hd));

    // return true;
    // }
    // System.out.println("lỗi hàm deleteHoaDon: Không tìm thấy hoá đơn hoặc lỗi
    // CSDL.");
    // return false;
    // }

    // ========== BUSINESS LOGIC ==========

    // ========== GET DỮ LIỆU ==========
    public ArrayList<HoaDonDTO> getListHoaDon() {
        return this.listHoaDon;
    }

    public HoaDonDTO getHoaDonByMaHd(int ma_hd) {
        for (HoaDonDTO hd : this.listHoaDon) {
            if (hd.getMaHd() == ma_hd) {
                return hd;
            }
        }
        return null;
    }
}

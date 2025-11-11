package BUS;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

import DAO.HoaDonDAO;
import DTO.HoaDonDTO;
import DTO.LoHangDTO;
import DTO.ChiTietHdDTO;

public class HoaDonBUS {

    // singleton instance
    private static HoaDonBUS instance;

    // field
    private ArrayList<HoaDonDTO> listHoaDon;
    private final HoaDonDAO hoaDonDAO;
    private ChiTietHdBUS chiTietHdBUS;
    private NhanVienBUS nhanVienBUS;
    private KhachHangBUS khachHangBUS;
    private KhuyenMaiBUS khuyenMaiBUS;
    private LoHangBUS loHangBUS;

    private HoaDonBUS() {
        this.hoaDonDAO = HoaDonDAO.getInstance();
        // this.chiTietHdBUS = ChiTietHdBUS.getInstance();
        this.listHoaDon = this.hoaDonDAO.selectAll();
        // this.nhanVienBUS = NhanVienBUS.getInstance();
        // this.khachHangBUS = KhachHangBUS.getInstance();
        // this.khuyenMaiBUS = KhuyenMaiBUS.getInstance();
        // this.loHangBUS = LoHangBUS.getInstance();
    }

    // singleton init
    public static HoaDonBUS getInstance() {
        if (instance == null) {
            instance = new HoaDonBUS();
        }
        return instance;
    }

    // ========== DATABASE HANDLE ==========

    public boolean addHoaDon(HoaDonDTO hoaDonDTO, ArrayList<ChiTietHdDTO> danhSachChiTietHd) {
        // ======== 1. KIỂM TRA DỮ LIỆU ========
        if (this.checkIfMaHdExist(hoaDonDTO.getMaHd()))
            return false;

        if (!this.nhanVienBUS.getMapByMaNv().containsKey(hoaDonDTO.getMaNv())) {
            JOptionPane.showMessageDialog(null, "Mã nhân viên không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!this.khachHangBUS.getMapByMaKh().containsKey(hoaDonDTO.getMaKh())) {
            JOptionPane.showMessageDialog(null, "Mã khách hàng không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (hoaDonDTO.getMaKm() != null && !this.khuyenMaiBUS.getMapByMaKm().containsKey(hoaDonDTO.getMaKm())) {
            JOptionPane.showMessageDialog(null, "Mã khuyến mãi không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (danhSachChiTietHd == null || danhSachChiTietHd.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Danh sách chi tiết hoá đơn rỗng hoặc null", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // ======== 2. KIỂM TRA TỒN KHO TRƯỚC ========
        for (ChiTietHdDTO ct : danhSachChiTietHd) {
            LoHangDTO lh = this.loHangBUS.getLoHangByMaLh(ct.getMaLh());
            if (lh == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy lô hàng " + ct.getMaLh(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (lh.getSlTon() < ct.getSoLuong()) {
                JOptionPane.showMessageDialog(null, "Không đủ tồn kho cho lô hàng " + ct.getMaLh(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // ======== 3. INSERT HOÁ ĐƠN + CHI TIẾT ========
        ArrayList<ChiTietHdDTO> daThemChiTiet = new ArrayList<>();
        boolean hoaDonDaThem = false;

        try {
            // thêm hóa đơn
            if (this.hoaDonDAO.insert(hoaDonDTO) <= 0) {
                JOptionPane.showMessageDialog(null, "Không thể thêm hóa đơn vào CSDL", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            hoaDonDaThem = true;

            // thêm từng chi tiết
            for (ChiTietHdDTO ct : danhSachChiTietHd) {
                ct.setMaHd(hoaDonDTO.getMaHd());

                // giảm số lượng tồn kho trước khi add
                this.loHangBUS.updateSlTonLoHang(ct.getMaLh(), -ct.getSoLuong());

                if (!this.chiTietHdBUS.addChiTietHd(ct)) {
                    JOptionPane.showMessageDialog(null, "Không thể thêm chi tiết hoá đơn", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    throw new Exception("Lỗi thêm chi tiết hóa đơn " + ct.getMaLh());
                }

                daThemChiTiet.add(ct);
            }

            // thêm thành công toàn bộ
            this.listHoaDon.add(hoaDonDTO);
            JOptionPane.showMessageDialog(null, "Thêm hoá đơn thành công!", "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        }

        // ======== 4. ROLLBACK KHI CÓ LỖI ========
        catch (Exception e) {
            System.err.println("Rollback vì lỗi: " + e.getMessage());

            // phục hồi tồn kho
            for (ChiTietHdDTO ct : daThemChiTiet) {
                this.loHangBUS.updateSlTonLoHang(ct.getMaLh(), ct.getSoLuong());
                if (!this.chiTietHdBUS.deleteChiTietHd(ct.getMaHd(), ct.getMaLh(),
                        ct.getMaThuoc())) {
                    JOptionPane.showMessageDialog(null, "Không thể xoá chi tiết hoá đơn", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (!daThemChiTiet.isEmpty()) {
                for (ChiTietHdDTO ct : daThemChiTiet) {
                    this.loHangBUS.updateSlTonLoHang(ct.getMaLh(), ct.getSoLuong());
                    this.chiTietHdBUS.deleteChiTietHd(ct.getMaHd(), ct.getMaLh(), ct.getMaThuoc());
                    // if (!this.chiTietHdBUS.deleteChiTietHd(ct.getMaHd(), ct.getMaLh(),
                    // ct.getMaThuoc())) {
                    // JOptionPane.showMessageDialog(null, "Không thể xoá chi tiết hoá đơn", "Lỗi",
                    // JOptionPane.ERROR_MESSAGE);
                    // return false;
                    // }
                }
            }

            // xoá hóa đơn nếu đã thêm
            if (hoaDonDaThem) {
                this.hoaDonDAO.deleteById(String.valueOf(hoaDonDTO.getMaHd()));
            }

            JOptionPane.showMessageDialog(null, "Thêm hoá đơn thất bại! Dữ liệu đã được phục hồi.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean addHD(HoaDonDTO hoaDonDTO) {
        // ======== 1. KIỂM TRA DỮ LIỆU ========
        if (this.checkIfMaHdExist(hoaDonDTO.getMaHd())) {
            System.out.println("Mã hóa đơn đã tồn tại");
            return false;
        }

        if (!this.nhanVienBUS.getMapByMaNv().containsKey(hoaDonDTO.getMaNv())) {
            JOptionPane.showMessageDialog(null, "Mã nhân viên không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!this.khachHangBUS.getMapByMaKh().containsKey(hoaDonDTO.getMaKh())) {
            JOptionPane.showMessageDialog(null, "Mã khách hàng không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (hoaDonDTO.getMaKm() != null && !this.khuyenMaiBUS.getMapByMaKm().containsKey(hoaDonDTO.getMaKm())) {
            JOptionPane.showMessageDialog(null, "Mã khuyến mãi không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // ======== 2. THÊM HÓA ĐƠN ========
        if (this.hoaDonDAO.insert(hoaDonDTO) > 0) {
            this.listHoaDon.add(hoaDonDTO);
            JOptionPane.showMessageDialog(null, "Thêm hóa đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Không thể thêm hóa đơn vào CSDL", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public boolean addCTHD(int maHd, ArrayList<ChiTietHdDTO> danhSachChiTietHd) {
        if (danhSachChiTietHd == null || danhSachChiTietHd.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Danh sách chi tiết hoá đơn rỗng hoặc null", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        ArrayList<ChiTietHdDTO> daThemChiTiet = new ArrayList<>();

        try {
            // ======== 1. KIỂM TRA TỒN KHO ========
            for (ChiTietHdDTO ct : danhSachChiTietHd) {
                LoHangDTO lh = this.loHangBUS.getLoHangByMaLh(ct.getMaLh());
                if (lh == null) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy lô hàng " + ct.getMaLh(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (lh.getSlTon() < ct.getSoLuong()) {
                    JOptionPane.showMessageDialog(null, "Không đủ tồn kho cho lô hàng " + ct.getMaLh(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            // ======== 2. THÊM CHI TIẾT HOÁ ĐƠN ========
            for (ChiTietHdDTO ct : danhSachChiTietHd) {
                ct.setMaHd(maHd);

                // giảm tồn kho trước khi add
                this.loHangBUS.updateSlTonLoHang(ct.getMaLh(), -ct.getSoLuong());

                if (!this.chiTietHdBUS.addChiTietHd(ct)) {
                    throw new Exception("Không thể thêm chi tiết hoá đơn " + ct.getMaLh());
                }

                daThemChiTiet.add(ct);
            }

            JOptionPane.showMessageDialog(null, "Thêm chi tiết hoá đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }

        // ======== 3. ROLLBACK KHI CÓ LỖI ========
        catch (Exception e) {
            System.err.println("Rollback vì lỗi: " + e.getMessage());

            for (ChiTietHdDTO ct : daThemChiTiet) {
                this.loHangBUS.updateSlTonLoHang(ct.getMaLh(), ct.getSoLuong());
                this.chiTietHdBUS.deleteChiTietHd(ct.getMaHd(), ct.getMaLh(), ct.getMaThuoc());
            }

            JOptionPane.showMessageDialog(null, "Thêm chi tiết hoá đơn thất bại! Dữ liệu đã được phục hồi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateHoaDon(int ma_hd, HoaDonDTO hoaDonDTO) {
        // kiểm tra nếu mã hoá đơn không tồn tại
        if (!this.getMapByMaHd().containsKey(ma_hd)) {
            JOptionPane.showMessageDialog(null, "Mã hoá đơn không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.hoaDonDAO.update(hoaDonDTO) > 0) {
            for (int i = 0; i < this.listHoaDon.size(); i++) {
                if (this.listHoaDon.get(i).getMaHd() == ma_hd) {
                    this.listHoaDon.set(i, hoaDonDTO);
                    break;
                }
            }
            return true;
        }

        JOptionPane.showMessageDialog(null, "Không thể cập nhật hoá đơn trong CSDL", "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    public boolean deleteHoaDon(int ma_hd) throws Exception {
        // kiểm tra nếu hoá đơn đã xoá
        if (this.checkIfAlreadyDeleted(ma_hd)) {
            JOptionPane.showMessageDialog(null, "Hoá đơn đã được xoá trước đó", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // lấy chi tiết hóa đơn trước khi xóa để hoàn tác tồn kho
//        ArrayList<ChiTietHdDTO> chiTietList = this.chiTietHdBUS.getListChiTietHdByMaHd(ma_hd);

        // hoàn tác tồn kho -> huỷ đơn nên + lại phần đã thanh toán
//        if (chiTietList != null) {
//            for (ChiTietHdDTO chiTiet : chiTietList) {
//                try {
//                    // nếu updateSlTonLoHang thất bại -> gây lệch tồn kho
//                    this.loHangBUS.updateSlTonLoHang(chiTiet.getMaLh(), chiTiet.getSoLuong());
//                } catch (Exception e) {
//                    throw new Exception("Không thể cập nhật tồn kho cho lô " + chiTiet.getMaLh());
//                }
//
//            }
//        }

        // xoá chi tiết hoá đơn
        if (!this.chiTietHdBUS.deleteAllByMaHd(ma_hd)) {
            JOptionPane.showMessageDialog(null, "Không thể xoá chi tiết hoá đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.hoaDonDAO.deleteById(String.valueOf(ma_hd)) > 0) {

            // cập nhật cache
            this.listHoaDon.removeIf(hd -> hd.getMaHd() == ma_hd);

            return true;
        }

        JOptionPane.showMessageDialog(null, "Lỗi CSDL khi xoá hoá đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    // ========== BUSINESS LOGIC ==========

    public boolean checkIfMaHdExist(int maHD) {
        if (this.getMapByMaHd().containsKey(maHD)) {
            JOptionPane.showMessageDialog(null, "Mã hoá đơn đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    // dùng trong KhachHangBUS kiểm tra khách hàng có hoá đơn chưa
    public boolean checkIfMaKhExist(int ma_kh) {
        for (HoaDonDTO hd : this.listHoaDon) {
            if (hd.getMaKh() == ma_kh)
                return true;
        }
        return false;
    }

    private boolean checkIfAlreadyDeleted(int ma_hd) {
        if (this.getHoaDonByMaHd(ma_hd) == null) {
            JOptionPane.showMessageDialog(null, "Hóa đơn không tồn tại hoặc đã bị xoá", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;

    }

    // ========== GET DỮ LIỆU ==========
    public ArrayList<HoaDonDTO> getListHoaDon() {
        return this.listHoaDon;
    }

    public HoaDonDTO getHoaDonByMaHd(int ma_hd) {
        for (HoaDonDTO hd : this.listHoaDon) {
            if (hd.getMaHd() == ma_hd)
                return hd;
        }
        return null;
    }

    public HashMap<Integer, HoaDonDTO> getMapByMaHd() {
        HashMap<Integer, HoaDonDTO> map = new HashMap<>();
        for (HoaDonDTO hd : this.listHoaDon) {
            map.put(hd.getMaHd(), hd);
        }
        return map;
    }

    // ========== SET BUS ==========
    public void setChiTietHdBUS(ChiTietHdBUS chiTietHdBUS) {
        this.chiTietHdBUS = chiTietHdBUS;
    }

    public void setLoHangBUS(LoHangBUS loHangBUS) {
        this.loHangBUS = loHangBUS;
    }

    public void setNhanVienBUS(NhanVienBUS nhanVienBUS) {
        this.nhanVienBUS = nhanVienBUS;
    }

    public void setKhachHangBUS(KhachHangBUS khachHangBUS) {
        this.khachHangBUS = khachHangBUS;
    }

    public void setKhuyenMaiBUS(KhuyenMaiBUS khuyenMaiBUS) {
        this.khuyenMaiBUS = khuyenMaiBUS;
    }
}

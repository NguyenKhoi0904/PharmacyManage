package BUS;

public class BUSManager {
    public static TaiKhoanBUS taiKhoanBUS;
    public static NhanVienBUS nhanVienBUS;
    public static KhachHangBUS khachHangBUS;
    public static ThuocBUS thuocBUS;
    public static DanhMucThuocBUS danhMucThuocBUS;
    public static NhaCungCapBUS nhaCungCapBUS;
    public static LoHangBUS loHangBUS;
    public static PhieuNhapBUS phieuNhapBUS;
    public static ChiTietPnBUS chiTietPnBUS;
    public static HoaDonBUS hoaDonBUS;
    public static ChiTietHdBUS chiTietHdBUS;
    public static KhuyenMaiBUS khuyenMaiBUS;

    public static void initAllBUS() {
        System.out.println("Khởi tạo các BUS...");

        // khởi tạo trước tất cả BUS
        taiKhoanBUS = TaiKhoanBUS.getInstance();
        nhanVienBUS = NhanVienBUS.getInstance();
        khachHangBUS = KhachHangBUS.getInstance();
        thuocBUS = ThuocBUS.getInstance();
        danhMucThuocBUS = DanhMucThuocBUS.getInstance();
        nhaCungCapBUS = NhaCungCapBUS.getInstance();
        loHangBUS = LoHangBUS.getInstance();
        phieuNhapBUS = PhieuNhapBUS.getInstance();
        chiTietPnBUS = ChiTietPnBUS.getInstance();
        hoaDonBUS = HoaDonBUS.getInstance();
        chiTietHdBUS = ChiTietHdBUS.getInstance();
        khuyenMaiBUS = KhuyenMaiBUS.getInstance();

        // sau đó mới thiết lập mối quan hệ giữa các BUS
        taiKhoanBUS.setNhanVienBUS(nhanVienBUS);
        taiKhoanBUS.setKhachHangBUS(khachHangBUS);
        nhanVienBUS.setTaiKhoanBUS(taiKhoanBUS);
        khachHangBUS.setTaiKhoanBUS(taiKhoanBUS);
        khachHangBUS.setHoaDonBUS(hoaDonBUS);
        thuocBUS.setDanhMucThuocBUS(danhMucThuocBUS);
        thuocBUS.setLoHangBUS(loHangBUS);
        loHangBUS.setThuocBUS(thuocBUS);
        phieuNhapBUS.setChiTietPnBUS(chiTietPnBUS);
        phieuNhapBUS.setLoHangBUS(loHangBUS);
        phieuNhapBUS.setNhanVienBUS(nhanVienBUS);
        chiTietPnBUS.setLoHangBUS(loHangBUS);
        hoaDonBUS.setChiTietHdBUS(chiTietHdBUS);
        hoaDonBUS.setKhachHangBUS(khachHangBUS);
        hoaDonBUS.setKhuyenMaiBUS(khuyenMaiBUS);
        hoaDonBUS.setLoHangBUS(loHangBUS);
        hoaDonBUS.setNhanVienBUS(nhanVienBUS);
        chiTietHdBUS.setHoaDonBUS(hoaDonBUS);

        System.out.println("BUSManager init hoàn tất.");
    }

    static {

    }
}

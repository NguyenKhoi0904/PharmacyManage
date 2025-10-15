/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author aries
 */
public class HoaDon {
    private String id;
    private Date ngayXuat;
    private String phuongThucThanhToan;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private double tongTien = 0;
    private List<KhuyenMai> danhSachKhuyenMai;
    private List<ChiTietHoaDon> danhSachChiTietHoaDon;
    private int state;

    public HoaDon() {
        this.danhSachKhuyenMai = new ArrayList<>();
        this.danhSachChiTietHoaDon = new ArrayList<>();    
    }

    public HoaDon(String id, Date ngayXuat, String phuongThucThanhToan, KhachHang khachHang, NhanVien nhanVien, int state) {
        this.id = id;
        this.ngayXuat = ngayXuat;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.danhSachKhuyenMai = new ArrayList<>();
        this.danhSachChiTietHoaDon = new ArrayList<>();
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(Date ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public List<KhuyenMai> getDanhSachKhuyenMai() {
        return danhSachKhuyenMai;
    }

    public void setDanhSachKhuyenMai(List<KhuyenMai> danhSachKhuyenMai) {
        this.danhSachKhuyenMai = danhSachKhuyenMai;
    }

    public List<ChiTietHoaDon> getDanhSachChiTietHoaDon() {
        return danhSachChiTietHoaDon;
    }

    public void setDanhSachChiTietHoaDon(List<ChiTietHoaDon> danhSachChiTietHoaDon) {
        this.danhSachChiTietHoaDon = danhSachChiTietHoaDon;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "HoaDon{" + "id=" + id + ", ngayXuat=" + ngayXuat + ", phuongThucThanhToan=" + phuongThucThanhToan + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien + ", tongTien=" + tongTien + ", danhSachKhuyenMai=" + danhSachKhuyenMai + ", danhSachChiTietHoaDon=" + danhSachChiTietHoaDon + ", state=" + state + '}';
    }
    

    
    
}

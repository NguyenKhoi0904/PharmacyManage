/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

/**
 *
 * @author aries
 */
public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private Thuoc thuoc;
    private LoHang loHang;
    private double donGia;
    private int soLuong;
    private int state;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, Thuoc thuoc, double donGia, int soLuong, LoHang loHang, int state) {
        this.hoaDon = hoaDon;
        this.thuoc = thuoc;
        this.loHang = loHang;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.state = state;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) {
        this.loHang = loHang;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" + "hoaDon=" + hoaDon + ", thuoc=" + thuoc + ", loHang=" + loHang + ", donGia=" + donGia + ", soLuong=" + soLuong + ", state=" + state + '}';
    }
    
    
    public double tongTien(){
        return this.getSoLuong() * this.getDonGia();
    }
}

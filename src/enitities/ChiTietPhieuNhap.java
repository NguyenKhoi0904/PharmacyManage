/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

/**
 *
 * @author aries
 */
public class ChiTietPhieuNhap {
    private PhieuNhap phieuNhap;
    private LoHang loHang;
    private Thuoc thuoc;
    private int SoLuong;
    private double donGia;
    private int state;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(PhieuNhap phieuNhap, LoHang loHang, Thuoc thuoc, int SoLuong, double donGia, int state) {
        this.phieuNhap = phieuNhap;
        this.loHang = loHang;
        this.thuoc = thuoc;
        this.SoLuong = SoLuong;
        this.donGia = donGia;
        this.state = state;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) {
        this.loHang = loHang;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" + "phieuNhap=" + phieuNhap + ", loHang=" + loHang + ", thuoc=" + thuoc + ", SoLuong=" + SoLuong + ", donGia=" + donGia + ", state=" + state + '}';
    }
    
}

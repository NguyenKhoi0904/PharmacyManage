package com.mycompany.pharmacystore.DTO;

import java.math.BigDecimal;

public class ChiTietHdDTO {
    private int maHd; 
    private int maThuoc; 
    private BigDecimal donGia;
    private int soLuong;

    // Constructor
    public ChiTietHdDTO(int maHd, int maThuoc, BigDecimal donGia, int soLuong) {
        this.maHd = maHd;
        this.maThuoc = maThuoc;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    // get
    public int getMaHd() { return maHd; }
    public int getMaThuoc() { return maThuoc; }
    public BigDecimal getDonGia() { return donGia; }
    public int getSoLuong() { return soLuong; }

    // set
    public void setMaHd(int maHd) { this.maHd = maHd; }
    public void setMaThuoc(int maThuoc) { this.maThuoc = maThuoc; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    @Override
    public String toString() {
        return "ChiTietHdDTO{" +
                "maHd=" + maHd +
                ", maThuoc=" + maThuoc +
                ", donGia=" + donGia +
                ", soLuong=" + soLuong +
                '}';
    }
}
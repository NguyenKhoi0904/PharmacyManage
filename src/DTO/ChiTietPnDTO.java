package com.mycompany.pharmacystore.DTO;

import java.math.BigDecimal;

public class ChiTietPnDTO {
    private int maPn; 
    private int maLh; 
    private BigDecimal donGia;
    private int soLuong;

    // Constructor
    public ChiTietPnDTO(int maPn, int maLh, BigDecimal donGia, int soLuong) {
        this.maPn = maPn;
        this.maLh = maLh;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    // get
    public int getMaPn() { return maPn; }
    public int getMaLh() { return maLh; }
    public BigDecimal getDonGia() { return donGia; }
    public int getSoLuong() { return soLuong; }

    // set
    public void setMaPn(int maPn) { this.maPn = maPn; }
    public void setMaLh(int maLh) { this.maLh = maLh; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    @Override
    public String toString() {
        return "ChiTietPnDTO{" +
                "maPn=" + maPn +
                ", maLh=" + maLh +
                ", donGia=" + donGia +
                ", soLuong=" + soLuong +
                '}';
    }
}
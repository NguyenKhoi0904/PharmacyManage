package DTO;

import java.math.BigDecimal;

public class ChiTietHdDTO {
    private int maHd;
    private int maLh;
    private int maThuoc;
    private BigDecimal donGia;
    private int soLuong;

    // Constructor
    public ChiTietHdDTO(int maHd, int maLh, int maThuoc, BigDecimal donGia, int soLuong) {
        this.maHd = maHd;
        this.maLh = maLh;
        this.maThuoc = maThuoc;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }
    
    public ChiTietHdDTO(){
        
    }

    // get
    public int getMaHd() {
        return this.maHd;
    }

    public int getMaLh() {
        return this.maLh;
    }

    public int getMaThuoc() {
        return this.maThuoc;
    }

    public BigDecimal getDonGia() {
        return this.donGia;
    }

    public int getSoLuong() {
        return this.soLuong;
    }

    // set
    public void setMaHd(int maHd) {
        this.maHd = maHd;
    }

    public void setMaLh(int ma_lh) {
        this.maLh = ma_lh;
    }

    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

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
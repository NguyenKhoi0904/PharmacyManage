package com.mycompany.pharmacystore.DTO;

import java.math.BigDecimal; // Sử dụng BigDecimal cho kiểu DECIMAL
import java.sql.Date;

public class LoHangDTO {
    private int maLh;
    private int maNcc;
    private int slNhap;
    private int slTon;
    private Date ngaySx;
    private Date hanSd;
    private BigDecimal giaNhap;

    // Constructor
    public LoHangDTO(int maLh, int maNcc, int slNhap, int slTon, Date ngaySx, Date hanSd, BigDecimal giaNhap) {
        this.maLh = maLh;
        this.maNcc = maNcc;
        this.slNhap = slNhap;
        this.slTon = slTon;
        this.ngaySx = ngaySx;
        this.hanSd = hanSd;
        this.giaNhap = giaNhap;
    }

    // get
    public int getMaLh() { return maLh; }
    public int getMaNcc() { return maNcc; }
    public int getSlNhap() { return slNhap; }
    public int getSlTon() { return slTon; }
    public Date getNgaySx() { return ngaySx; }
    public Date getHanSd() { return hanSd; }
    public BigDecimal getGiaNhap() { return giaNhap; }

    // set
    public void setMaLh(int maLh) { this.maLh = maLh; }
    public void setMaNcc(int maNcc) { this.maNcc = maNcc; }
    public void setSlNhap(int slNhap) { this.slNhap = slNhap; }
    public void setSlTon(int slTon) { this.slTon = slTon; }
    public void setNgaySx(Date ngaySx) { this.ngaySx = ngaySx; }
    public void setHanSd(Date hanSd) { this.hanSd = hanSd; }
    public void setGiaNhap(BigDecimal giaNhap) { this.giaNhap = giaNhap; }

    @Override
    public String toString() {
        return "LoHangDTO{" +
                "maLh=" + maLh +
                ", maNcc=" + maNcc +
                ", slTon=" + slTon +
                ", hanSd=" + hanSd +
                ", giaNhap=" + giaNhap +
                '}';
    }
}
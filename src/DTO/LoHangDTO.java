package DTO;

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
    private int trangThai;

    // Constructor
    public LoHangDTO(int maLh, int maNcc, int slNhap, int slTon, Date ngaySx, Date hanSd, BigDecimal giaNhap,
            int trangThai) {
        this.maLh = maLh;
        this.maNcc = maNcc;
        this.slNhap = slNhap;
        this.slTon = slTon;
        this.ngaySx = ngaySx;
        this.hanSd = hanSd;
        this.giaNhap = giaNhap;
        this.trangThai = trangThai;
    }

    // get
    public int getMaLh() {
        return this.maLh;
    }

    public int getMaNcc() {
        return this.maNcc;
    }

    public int getSlNhap() {
        return this.slNhap;
    }

    public int getSlTon() {
        return this.slTon;
    }

    public Date getNgaySx() {
        return this.ngaySx;
    }

    public Date getHanSd() {
        return this.hanSd;
    }

    public BigDecimal getGiaNhap() {
        return this.giaNhap;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaLh(int maLh) {
        this.maLh = maLh;
    }

    public void setMaNcc(int maNcc) {
        this.maNcc = maNcc;
    }

    public void setSlNhap(int slNhap) {
        this.slNhap = slNhap;
    }

    public void setSlTon(int slTon) {
        this.slTon = slTon;
    }

    public void setNgaySx(Date ngaySx) {
        this.ngaySx = ngaySx;
    }

    public void setHanSd(Date hanSd) {
        this.hanSd = hanSd;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "LoHangDTO{" +
                "maLh=" + this.maLh +
                ", maNcc=" + this.maNcc +
                ", slTon=" + this.slTon +
                ", hanSd=" + this.hanSd +
                ", giaNhap=" + this.giaNhap +
                ", trangthai=" + this.trangThai +
                '}';
    }
}
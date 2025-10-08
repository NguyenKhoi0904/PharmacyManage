package com.mycompany.pharmacystore.DTO;

import java.math.BigDecimal;
import java.sql.Date;

public class HoaDonDTO {
    private int maHd;
    private int maKh; 
    private Integer maKm; // sử dụng Integer để chấp nhận giá trị = NULL
    private BigDecimal tongTien;
    private Date ngayXuat;
    private String phuongThucTt;

    // Constructor
    public HoaDonDTO(int maHd, int maKh, Integer maKm, BigDecimal tongTien, Date ngayXuat, String phuongThucTt) {
        this.maHd = maHd;
        this.maKh = maKh;
        this.maKm = maKm;
        this.tongTien = tongTien;
        this.ngayXuat = ngayXuat;
        this.phuongThucTt = phuongThucTt;
    }

    // get
    public int getMaHd() { return maHd; }
    public int getMaKh() { return maKh; }
    public Integer getMaKm() { return maKm; }
    public BigDecimal getTongTien() { return tongTien; }
    public Date getNgayXuat() { return ngayXuat; }
    public String getPhuongThucTt() { return phuongThucTt; }

    // set
    public void setMaHd(int maHd) { this.maHd = maHd; }
    public void setMaKh(int maKh) { this.maKh = maKh; }
    public void setMaKm(Integer maKm) { this.maKm = maKm; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    public void setNgayXuat(Date ngayXuat) { this.ngayXuat = ngayXuat; }
    public void setPhuongThucTt(String phuongThucTt) { this.phuongThucTt = phuongThucTt; }

    @Override
    public String toString() {
        return "HoaDonDTO{" +
                "maHd=" + maHd +
                ", maKh=" + maKh +
                ", tongTien=" + tongTien +
                ", ngayXuat=" + ngayXuat +
                '}';
    }
}
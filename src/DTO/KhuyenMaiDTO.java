package com.mycompany.pharmacystore.DTO;

import java.math.BigDecimal;
import java.sql.Date;

public class KhuyenMaiDTO {
    private int maKm;
    private String tenKm;
    private String loaiKm;
    private BigDecimal giaTriKm;
    private String dieuKienKm;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private boolean hieuLuc;

    // Constructor
    public KhuyenMaiDTO(int maKm, String tenKm, String loaiKm, BigDecimal giaTriKm, String dieuKienKm, Date ngayBatDau, Date ngayKetThuc, boolean hieuLuc) {
        this.maKm = maKm;
        this.tenKm = tenKm;
        this.loaiKm = loaiKm;
        this.giaTriKm = giaTriKm;
        this.dieuKienKm = dieuKienKm;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.hieuLuc = hieuLuc;
    }

    // get
    public int getMaKm() { return maKm; }
    public String getTenKm() { return tenKm; }
    public String getLoaiKm() { return loaiKm; }
    public BigDecimal getGiaTriKm() { return giaTriKm; }
    public String getDieuKienKm() { return dieuKienKm; }
    public Date getNgayBatDau() { return ngayBatDau; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public boolean isHieuLuc() { return hieuLuc; }

    // set
    public void setMaKm(int maKm) { this.maKm = maKm; }
    public void setTenKm(String tenKm) { this.tenKm = tenKm; }
    public void setLoaiKm(String loaiKm) { this.loaiKm = loaiKm; }
    public void setGiaTriKm(BigDecimal giaTriKm) { this.giaTriKm = giaTriKm; }
    public void setDieuKienKm(String dieuKienKm) { this.dieuKienKm = dieuKienKm; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public void setHieuLuc(boolean hieuLuc) { this.hieuLuc = hieuLuc; }

    @Override
    public String toString() {
        return "KhuyenMaiDTO{" +
                "maKm=" + maKm +
                ", tenKm='" + tenKm + '\'' +
                ", giaTriKm=" + giaTriKm +
                ", hieuLuc=" + hieuLuc +
                '}';
    }
}
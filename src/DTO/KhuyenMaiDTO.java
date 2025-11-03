package DTO;

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
    private int trangThai;

    // Constructor
    public KhuyenMaiDTO(int maKm, String tenKm, String loaiKm, BigDecimal giaTriKm, String dieuKienKm, Date ngayBatDau,
            Date ngayKetThuc, int trangThai) {
        this.maKm = maKm;
        this.tenKm = tenKm;
        this.loaiKm = loaiKm;
        this.giaTriKm = giaTriKm;
        this.dieuKienKm = dieuKienKm;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }
    
    public KhuyenMaiDTO(String tenKm, String loaiKm, BigDecimal giaTriKm, String dieuKienKm, Date ngayBatDau,
            Date ngayKetThuc, int trangThai) {
        this.tenKm = tenKm;
        this.loaiKm = loaiKm;
        this.giaTriKm = giaTriKm;
        this.dieuKienKm = dieuKienKm;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }
    public KhuyenMaiDTO() {
        
    }
    
    // get
    public int getMaKm() {
        return this.maKm;
    }

    public String getTenKm() {
        return this.tenKm;
    }

    public String getLoaiKm() {
        return this.loaiKm;
    }

    public BigDecimal getGiaTriKm() {
        return this.giaTriKm;
    }

    public String getDieuKienKm() {
        return this.dieuKienKm;
    }

    public Date getNgayBatDau() {
        return this.ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return this.ngayKetThuc;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaKm(int maKm) {
        this.maKm = maKm;
    }

    public void setTenKm(String tenKm) {
        this.tenKm = tenKm;
    }

    public void setLoaiKm(String loaiKm) {
        this.loaiKm = loaiKm;
    }

    public void setGiaTriKm(BigDecimal giaTriKm) {
        this.giaTriKm = giaTriKm;
    }

    public void setDieuKienKm(String dieuKienKm) {
        this.dieuKienKm = dieuKienKm;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "KhuyenMaiDTO{" +
                "maKm=" + maKm +
                ", tenKm='" + tenKm + '\'' +
                ", giaTriKm=" + giaTriKm + '\'' +
                ", trangthai=" + this.trangThai + '\'' +
                '}';
    }
}
package DTO;

import java.math.BigDecimal;
import java.sql.Date;

public class KhuyenMaiDTO {
    public enum LOAI_KM{
        PHAN_TRAM,
        SAN_PHAM
    }
    
    private int maKm;
    private String tenKm;
    private String loaiKm;
    private BigDecimal giaTriKm;
    private String dieuKienKm;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int trangThai;
    
    private LOAI_KM eLoaiKM;

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
        
        setELoaiKM(loaiKm);
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
        
        setELoaiKM(loaiKm);
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
    
    public LOAI_KM getELoaiKM(){
        return this.eLoaiKM;
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
        setELoaiKM(loaiKm);
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
    
    public void setELoaiKM(String loaiKM){
        switch (loaiKM) {
            case "Phần trăm":
                this.eLoaiKM = LOAI_KM.PHAN_TRAM;
            default:
                this.eLoaiKM = LOAI_KM.SAN_PHAM;
        }
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
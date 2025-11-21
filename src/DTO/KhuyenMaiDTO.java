package DTO;

import java.math.BigDecimal;
import java.sql.Date;

public class KhuyenMaiDTO {
    public enum LOAI_KM {
        PHAN_TRAM,
        GIA_TRI
    }

    public enum DIEU_KIEN_KM {
        DU_DIEM,
        MUA_2_TANG_1
    }

    private int maKm;
    private String tenKm;
    private String loaiKm;
    private BigDecimal giaTriKm;
    private String dieuKienKm;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private int trangThai;
    private int diemCanTichLuy;

    private LOAI_KM eLoaiKM;
    private DIEU_KIEN_KM eDieuKienKM;

    // Constructor
    public KhuyenMaiDTO(int maKm, String tenKm, String loaiKm, BigDecimal giaTriKm, String dieuKienKm, Date ngayBatDau,
            Date ngayKetThuc, int trangThai, int diemCanTichLuy) {
        this.maKm = maKm;
        this.tenKm = tenKm;
        this.loaiKm = loaiKm;
        this.giaTriKm = giaTriKm;
        this.dieuKienKm = dieuKienKm;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.diemCanTichLuy = diemCanTichLuy;

        setELoaiKM(loaiKm);
        setEDieuKienKM(dieuKienKm);
    }

    public KhuyenMaiDTO(String tenKm, String loaiKm, BigDecimal giaTriKm, String dieuKienKm, Date ngayBatDau,
            Date ngayKetThuc, int trangThai, int diemCanTichLuy) {
        this.tenKm = tenKm;
        this.loaiKm = loaiKm;
        this.giaTriKm = giaTriKm;
        this.dieuKienKm = dieuKienKm;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.diemCanTichLuy = diemCanTichLuy;

        setELoaiKM(loaiKm);
        setEDieuKienKM(dieuKienKm);
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

    public LOAI_KM getELoaiKM() {
        return this.eLoaiKM;
    }

    public DIEU_KIEN_KM getEDieuKienKM() {
        return this.eDieuKienKM;
    }

    public int getDiemCanTichLuy() {
        return this.diemCanTichLuy;
    }

    // set
    public void setMaKm(int maKm) {
        this.maKm = maKm;
    }

    public void setDiemCanTichLuy(int diem) {
        this.diemCanTichLuy = diem;
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

    public void setELoaiKM(String loaiKM) {
        if (loaiKM.trim().equals("Phần trăm"))
        {
            this.eLoaiKM = LOAI_KM.PHAN_TRAM;
            return;
        }

        if (loaiKM.trim().equals("Giá trị"))
        {
            this.eLoaiKM = LOAI_KM.GIA_TRI;
            return;
        }
    }

    public void setEDieuKienKM(String dieuKien) {
        if (dieuKien != null && dieuKien.toLowerCase().contains("2 tặng 1")) {
            this.eDieuKienKM = DIEU_KIEN_KM.MUA_2_TANG_1;
        } else {
            this.eDieuKienKM = DIEU_KIEN_KM.DU_DIEM;
        }
    }

    @Override
    public String toString() {
        return "Mã KM: " + maKm + " - Tên KM: " + tenKm + " - Loại KM: " + loaiKm + " - Giá Trị KM: " + giaTriKm
                + " - Điều Kiện KM: " + dieuKienKm + " - Điểm Cần TL: " + diemCanTichLuy;
    }
}
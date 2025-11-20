package DTO;

import java.sql.Date;

public class KhachHangDTO {
    private int maKh;
    private int maTk;
    private Date ngayDangKy;
    private int diemTichLuy;
    private int trangThai;

    // Constructor
    public KhachHangDTO(int maKh, int maTk, Date ngayDangKy, int diemTichLuy, int trangThai) {
        this.maKh = maKh;
        this.maTk = maTk;
        this.ngayDangKy = ngayDangKy;
        this.diemTichLuy = diemTichLuy;
        this.trangThai = trangThai;
    }

    // get
    public int getMaKh() {
        return this.maKh;
    }

    public int getMaTk() {
        return this.maTk;
    }

    public Date getNgayDangKy() {
        return this.ngayDangKy;
    }

    public int getDiemTichLuy() {
        return this.diemTichLuy;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaKh(int maKh) {
        this.maKh = maKh;
    }

    public void setMaTk(int maTk) {
        this.maTk = maTk;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return maKh + " - " + ngayDangKy + " - " + diemTichLuy;
    }
}
package com.mycompany.pharmacystore.DTO;

import java.sql.Date; 

public class KhachHangDTO {
    private int maKh;
    private int maTk; 
    private Date ngayDangKy;
    private int diemTichLuy;

    // Constructor
    public KhachHangDTO(int maKh, int maTk, Date ngayDangKy, int diemTichLuy) {
        this.maKh = maKh;
        this.maTk = maTk;
        this.ngayDangKy = ngayDangKy;
        this.diemTichLuy = diemTichLuy;
    }

    // get
    public int getMaKh() { return maKh; }
    public int getMaTk() { return maTk; }
    public Date getNgayDangKy() { return ngayDangKy; }
    public int getDiemTichLuy() { return diemTichLuy; }

    // set
    public void setMaKh(int maKh) { this.maKh = maKh; }
    public void setMaTk(int maTk) { this.maTk = maTk; }
    public void setNgayDangKy(Date ngayDangKy) { this.ngayDangKy = ngayDangKy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    @Override
    public String toString() {
        return "KhachHangDTO{" +
                "maKh=" + maKh +
                ", maTk=" + maTk +
                ", ngayDangKy=" + ngayDangKy +
                ", diemTichLuy=" + diemTichLuy +
                '}';
    }
}
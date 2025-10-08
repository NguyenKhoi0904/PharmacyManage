package com.mycompany.pharmacystore.DTO;

public class DanhMucThuocDTO {
    private int maDmt;
    private String tenDmt;

    // Constructor
    public DanhMucThuocDTO(int maDmt, String tenDmt) {
        this.maDmt = maDmt;
        this.tenDmt = tenDmt;
    }

    // get
    public int getMaDmt() { return maDmt; }
    public String getTenDmt() { return tenDmt; }

    // set
    public void setMaDmt(int maDmt) { this.maDmt = maDmt; }
    public void setTenDmt(String tenDmt) { this.tenDmt = tenDmt; }

    @Override
    public String toString() {
        return "DanhMucThuocDTO{" +
                "maDmt=" + maDmt +
                ", tenDmt='" + tenDmt + '\'' +
                '}';
    }
}
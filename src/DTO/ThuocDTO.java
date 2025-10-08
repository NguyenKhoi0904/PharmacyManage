package com.mycompany.pharmacystore.DTO;

import java.math.BigDecimal;

public class ThuocDTO {
    private int maThuoc;
    private int maDmt; 
    private String tenThuoc;
    private BigDecimal gia;
    private String donViTinh;
    private String nhaSanXuat;
    private String xuatXu;

    // Constructor
    public ThuocDTO(int maThuoc, int maDmt, String tenThuoc, BigDecimal gia, String donViTinh, String nhaSanXuat, String xuatXu) {
        this.maThuoc = maThuoc;
        this.maDmt = maDmt;
        this.tenThuoc = tenThuoc;
        this.gia = gia;
        this.donViTinh = donViTinh;
        this.nhaSanXuat = nhaSanXuat;
        this.xuatXu = xuatXu;
    }

    // get
    public int getMaThuoc() { return maThuoc; }
    public int getMaDmt() { return maDmt; }
    public String getTenThuoc() { return tenThuoc; }
    public BigDecimal getGia() { return gia; }
    public String getDonViTinh() { return donViTinh; }
    public String getNhaSanXuat() { return nhaSanXuat; }
    public String getXuatXu() { return xuatXu; }

    // set
    public void setMaThuoc(int maThuoc) { this.maThuoc = maThuoc; }
    public void setMaDmt(int maDmt) { this.maDmt = maDmt; }
    public void setTenThuoc(String tenThuoc) { this.tenThuoc = tenThuoc; }
    public void setGia(BigDecimal gia) { this.gia = gia; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }
    public void setNhaSanXuat(String nhaSanXuat) { this.nhaSanXuat = nhaSanXuat; }
    public void setXuatXu(String xuatXu) { this.xuatXu = xuatXu; }

    @Override
    public String toString() {
        return "ThuocDTO{" +
                "maThuoc=" + maThuoc +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", gia=" + gia +
                ", nhaSanXuat='" + nhaSanXuat + '\'' +
                '}';
    }
}
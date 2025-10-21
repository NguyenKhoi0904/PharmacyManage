package DTO;

import java.math.BigDecimal;

public class ThuocDTO {
    private int maThuoc;
    private int maDmt;
    private String tenThuoc;
    private BigDecimal gia;
    private String donViTinh;
    private String nhaSanXuat;
    private String xuatXu;
    private String url_anh;
    private int trangThai;

    // Constructor
    public ThuocDTO(int maThuoc, int maDmt, String tenThuoc, BigDecimal gia, String donViTinh, String nhaSanXuat,
            String xuatXu, String url_anh, int trangThai) {
        this.maThuoc = maThuoc;
        this.maDmt = maDmt;
        this.tenThuoc = tenThuoc;
        this.gia = gia;
        this.donViTinh = donViTinh;
        this.nhaSanXuat = nhaSanXuat;
        this.xuatXu = xuatXu;
        this.url_anh = url_anh;
        this.trangThai = trangThai;
    }

    // get
    public int getMaThuoc() {
        return this.maThuoc;
    }

    public int getMaDmt() {
        return this.maDmt;
    }

    public String getTenThuoc() {
        return this.tenThuoc;
    }

    public BigDecimal getGia() {
        return this.gia;
    }

    public String getDonViTinh() {
        return this.donViTinh;
    }

    public String getNhaSanXuat() {
        return this.nhaSanXuat;
    }

    public String getXuatXu() {
        return this.xuatXu;
    }

    public String getUrlAnh() {
        return this.url_anh;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public void setMaDmt(int maDmt) {
        this.maDmt = maDmt;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public void setUrlAnh(String url_anh) {
        this.url_anh = url_anh;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "ThuocDTO{" +
                "maThuoc=" + this.maThuoc +
                ", tenThuoc='" + this.tenThuoc + '\'' +
                ", gia=" + this.gia +
                ", nhaSanXuat='" + this.nhaSanXuat + '\'' +
                ", trangthai='" + this.trangThai + '\'' +
                '}';
    }
}
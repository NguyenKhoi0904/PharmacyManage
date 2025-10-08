package com.mycompany.pharmacystore.DTO;

import java.math.BigDecimal; // DECIMAL(10,2)
import java.sql.Date;      

public class NhanVienDTO {
    private int maNv;
    private int maTk; 
    private Date ngayVaoLam;
    private BigDecimal luong; // DECIMAL(10,2)
    private String email;
    private String diaChi;
    private String gioiTinh; // ENUM('Nam', 'Nu', 'Khac')
    private Date ngaySinh;
    private String viTri;

    // Constructor
    public NhanVienDTO(int maNv, int maTk, Date ngayVaoLam, BigDecimal luong, String email, String diaChi, String gioiTinh, Date ngaySinh, String viTri) {
        this.maNv = maNv;
        this.maTk = maTk;
        this.ngayVaoLam = ngayVaoLam;
        this.luong = luong;
        this.email = email;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.viTri = viTri;
    }

    // get
    public int getMaNv() { return maNv; }
    public int getMaTk() { return maTk; }
    public Date getNgayVaoLam() { return ngayVaoLam; }
    public BigDecimal getLuong() { return luong; }
    public String getEmail() { return email; }
    public String getDiaChi() { return diaChi; }
    public String getGioiTinh() { return gioiTinh; }
    public Date getNgaySinh() { return ngaySinh; }
    public String getViTri() { return viTri; }

    // set
    public void setMaNv(int maNv) { this.maNv = maNv; }
    public void setMaTk(int maTk) { this.maTk = maTk; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }
    public void setLuong(BigDecimal luong) { this.luong = luong; }
    public void setEmail(String email) { this.email = email; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }
    public void setViTri(String viTri) { this.viTri = viTri; }

    @Override
    public String toString() {
        return "NhanVienDTO{" +
                "maNv=" + maNv +
                ", maTk=" + maTk +
                ", ngayVaoLam=" + ngayVaoLam +
                ", luong=" + luong +
                '}';
    }
}
package DTO;

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
    private int trangThai;

    // Constructor
    public NhanVienDTO(int maNv, int maTk, Date ngayVaoLam, BigDecimal luong, String email, String diaChi,
            String gioiTinh, Date ngaySinh, String viTri, int trangThai) {
        this.maNv = maNv;
        this.maTk = maTk;
        this.ngayVaoLam = ngayVaoLam;
        this.luong = luong;
        this.email = email;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.viTri = viTri;
        this.trangThai = trangThai;
    }

    // get
    public int getMaNv() {
        return this.maNv;
    }

    public int getMaTk() {
        return this.maTk;
    }

    public Date getNgayVaoLam() {
        return this.ngayVaoLam;
    }

    public BigDecimal getLuong() {
        return this.luong;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDiaChi() {
        return this.diaChi;
    }

    public String getGioiTinh() {
        return this.gioiTinh;
    }

    public Date getNgaySinh() {
        return this.ngaySinh;
    }

    public String getViTri() {
        return this.viTri;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaNv(int maNv) {
        this.maNv = maNv;
    }

    public void setMaTk(int maTk) {
        this.maTk = maTk;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public void setLuong(BigDecimal luong) {
        this.luong = luong;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "NhanVienDTO{" +
                "maNv=" + this.maNv +
                ", maTk=" + this.maTk +
                ", ngayVaoLam=" + this.ngayVaoLam +
                ", luong=" + this.luong +
                ", trangThai=" + this.trangThai +
                '}';
    }
}
package com.mycompany.pharmacystore.DTO;

public class TaiKhoanDTO {
    private int maTk;
    private String taiKhoan;
    private String matKhau;
    private String ten;
    private String sdt;
    private String vaiTro; // ENUM('admin', 'nhanvien', 'khachhang')

    // Constructor
    public TaiKhoanDTO(int maTk, String taiKhoan, String matKhau, String ten, String sdt, String vaiTro) {
        this.maTk = maTk;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.ten = ten;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
    }

    // get
    public int getMaTk() { return maTk; }
    public String getTaiKhoan() { return taiKhoan; }
    public String getMatKhau() { return matKhau; }
    public String getTen() { return ten; }
    public String getSdt() { return sdt; }
    public String getVaiTro() { return vaiTro; }

    // set
    public void setMaTk(int maTk) { this.maTk = maTk; }
    public void setTaiKhoan(String taiKhoan) { this.taiKhoan = taiKhoan; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public void setTen(String ten) { this.ten = ten; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    @Override
    public String toString() {
        return "TaiKhoanDTO{" +
                "maTk=" + maTk +
                ", taiKhoan='" + taiKhoan + '\'' +
                ", vaiTro='" + vaiTro + '\'' +
                '}';
    }
}
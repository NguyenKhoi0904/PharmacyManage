package DTO;

public class TaiKhoanDTO {
    private int maTk;
    private String taiKhoan;
    private String matKhau;
    private String ten;
    private String sdt;
    private String vaiTro; // ENUM('admin', 'nhanvien', 'khachhang')
    private int trangThai;

    // Constructor
    public TaiKhoanDTO(int maTk, String taiKhoan, String matKhau, String ten, String sdt, String vaiTro,
            int trangThai) {
        this.maTk = maTk;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.ten = ten;
        this.sdt = sdt;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }

    // get
    public int getMaTk() {
        return this.maTk;
    }

    public String getTaiKhoan() {
        return this.taiKhoan;
    }

    public String getMatKhau() {
        return this.matKhau;
    }

    public String getTen() {
        return this.ten;
    }

    public String getSdt() {
        return this.sdt;
    }

    public String getVaiTro() {
        return this.vaiTro;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaTk(int maTk) {
        this.maTk = maTk;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "TaiKhoanDTO{" +
                "maTk=" + maTk +
                ", taiKhoan='" + taiKhoan + '\'' +
                ", vaiTro='" + vaiTro + '\'' +
                ", trangthai='" + this.trangThai + '\'' +
                '}';
    }
}
package DTO;

public class DanhMucThuocDTO {
    private int maDmt;
    private String tenDmt;
    private int trangThai;

    // Constructor
    public DanhMucThuocDTO(int maDmt, String tenDmt, int trangThai) {
        this.maDmt = maDmt;
        this.tenDmt = tenDmt;
        this.trangThai = trangThai;
    }

    // get
    public int getMaDmt() {
        return this.maDmt;
    }

    public String getTenDmt() {
        return this.tenDmt;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaDmt(int maDmt) {
        this.maDmt = maDmt;
    }

    public void setTenDmt(String tenDmt) {
        this.tenDmt = tenDmt;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return this.maDmt + " - " + this.tenDmt;
    }
}
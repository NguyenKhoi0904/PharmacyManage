package DTO;

import java.sql.Timestamp;

public class PhieuNhapDTO {
    private int maPn;
    private int maNv;
    private Timestamp thoiGianNhap;
    private String diaDiem;
    private int trangThai;

    // Constructor
    public PhieuNhapDTO(int maPn, int maNv, Timestamp thoiGianNhap, String diaDiem, int trangThai) {
        this.maPn = maPn;
        this.maNv = maNv;
        this.thoiGianNhap = thoiGianNhap;
        this.diaDiem = diaDiem;
        this.trangThai = trangThai;
    }

    // get
    public int getMaPn() {
        return this.maPn;
    }

    public int getMaNv() {
        return this.maNv;
    }

    public Timestamp getThoiGianNhap() {
        return this.thoiGianNhap;
    }

    public String getDiaDiem() {
        return this.diaDiem;
    }

    public int getTrangThai() {
        return this.trangThai;
    }

    // set
    public void setMaPn(int maPn) {
        this.maPn = maPn;
    }

    public void setMaNv(int maNv) {
        this.maNv = maNv;
    }

    public void setThoiGianNhap(Timestamp thoiGianNhap) {
        this.thoiGianNhap = thoiGianNhap;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "PhieuNhapDTO{" +
                "maPn=" + this.maPn +
                ", maNv=" + this.maNv +
                ", thoiGianNhap=" + this.thoiGianNhap +
                ", diaDiem='" + this.diaDiem + '\'' +
                ", trangthai='" + this.trangThai + '\'' +
                '}';
    }
}
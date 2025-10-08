package com.mycompany.pharmacystore.DTO;

import java.sql.Timestamp;

public class PhieuNhapDTO {
    private int maPn;
    private int maNv;
    private Timestamp thoiGianNhap;
    private String diaDiem;

    // Constructor
    public PhieuNhapDTO(int maPn, int maNv, Timestamp thoiGianNhap, String diaDiem) {
        this.maPn = maPn;
        this.maNv = maNv;
        this.thoiGianNhap = thoiGianNhap;
        this.diaDiem = diaDiem;
    }

    // get
    public int getMaPn() { return maPn; }
    public int getMaNv() { return maNv; }
    public Timestamp getThoiGianNhap() { return thoiGianNhap; }
    public String getDiaDiem() { return diaDiem; }

    // set
    public void setMaPn(int maPn) { this.maPn = maPn; }
    public void setMaNv(int maNv) { this.maNv = maNv; }
    public void setThoiGianNhap(Timestamp thoiGianNhap) { this.thoiGianNhap = thoiGianNhap; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }

    @Override
    public String toString() {
        return "PhieuNhapDTO{" +
                "maPn=" + maPn +
                ", maNv=" + maNv +
                ", thoiGianNhap=" + thoiGianNhap +
                ", diaDiem='" + diaDiem + '\'' +
                '}';
    }
}
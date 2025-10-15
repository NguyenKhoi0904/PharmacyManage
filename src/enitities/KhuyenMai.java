/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

import java.util.Date;

/**
 *
 * @author aries
 */
public class KhuyenMai {
    private String id;
    private String Name;
    private String Loai;
    private double GiaTri;
    private double DieuKien;
    private Date NgayBatDau;
    private Date NgayKetThuc;
    private String HieuLuc;
    private int state;

    public KhuyenMai() {
    }

    public KhuyenMai(String id, String Name, String Loai, double GiaTri, double DieuKien, Date NgayBatDau, Date NgayKetThuc, String HieuLuc, int state) {
        this.id = id;
        this.Name = Name;
        this.Loai = Loai;
        this.GiaTri = GiaTri;
        this.DieuKien = DieuKien;
        this.NgayBatDau = NgayBatDau;
        this.NgayKetThuc = NgayKetThuc;
        this.HieuLuc = HieuLuc;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLoai() {
        return Loai;
    }

    public void setLoai(String Loai) {
        this.Loai = Loai;
    }

    public double getGiaTri() {
        return GiaTri;
    }

    public void setGiaTri(double GiaTri) {
        this.GiaTri = GiaTri;
    }

    public double getDieuKien() {
        return DieuKien;
    }

    public void setDieuKien(double DieuKien) {
        this.DieuKien = DieuKien;
    }

    public Date getNgayBatDau() {
        return NgayBatDau;
    }

    public void setNgayBatDau(Date NgayBatDau) {
        this.NgayBatDau = NgayBatDau;
    }

    public Date getNgayKetThuc() {
        return NgayKetThuc;
    }

    public void setNgayKetThuc(Date NgayKetThuc) {
        this.NgayKetThuc = NgayKetThuc;
    }

    public String getHieuLuc() {
        return HieuLuc;
    }

    public void setHieuLuc(String HieuLuc) {
        this.HieuLuc = HieuLuc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" + "id=" + id + ", Name=" + Name + ", Loai=" + Loai + ", GiaTri=" + GiaTri + ", DieuKien=" + DieuKien + ", NgayBatDau=" + NgayBatDau + ", NgayKetThuc=" + NgayKetThuc + ", HieuLuc=" + HieuLuc + ", state=" + state + '}';
    }
    
    
}

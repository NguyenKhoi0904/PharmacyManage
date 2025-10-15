/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author aries
 */
public class PhieuNhap {
    private String id;
    private Date ThoiGianNhap;
    private String DiaDiem;
    private NhanVien nhanVien;
    private List<ChiTietPhieuNhap> danhSachChiTietPhieuNhap;
    private int state;
    
    public PhieuNhap() {
        this.danhSachChiTietPhieuNhap = new ArrayList<>();
    }

    public PhieuNhap(String id, Date ThoiGianNhap, String DiaDiem, NhanVien nhanVien, int state) {
        this.id = id;
        this.ThoiGianNhap = ThoiGianNhap;
        this.DiaDiem = DiaDiem;
        this.nhanVien = nhanVien;
        this.danhSachChiTietPhieuNhap = new ArrayList<>();
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getThoiGianNhap() {
        return ThoiGianNhap;
    }

    public void setThoiGianNhap(Date ThoiGianNhap) {
        this.ThoiGianNhap = ThoiGianNhap;
    }

    public String getDiaDiem() {
        return DiaDiem;
    }

    public void setDiaDiem(String DiaDiem) {
        this.DiaDiem = DiaDiem;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public List<ChiTietPhieuNhap> getDanhSachChiTietPhieuNhap() {
        return danhSachChiTietPhieuNhap;
    }

    public void setDanhSachChiTietPhieuNhap(List<ChiTietPhieuNhap> danhSachChiTietPhieuNhap) {
        this.danhSachChiTietPhieuNhap = danhSachChiTietPhieuNhap;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PhieuNhap{" + "id=" + id + ", ThoiGianNhap=" + ThoiGianNhap + ", DiaDiem=" + DiaDiem + ", nhanVien=" + nhanVien + ", danhSachChiTietPhieuNhap=" + danhSachChiTietPhieuNhap + ", state=" + state + '}';
    }
    
    public static void main(String args[]){
        
    }
}

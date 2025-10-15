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
public class NhanVien extends User{
    private Date NgayVaoLam;
    private double Luong;
    private String Email;
    private String diaChi;
    private String GioiTinh;
    private Date ngaySinh;
    private String ViTri;

    public NhanVien() {
    }

    public NhanVien(Date NgayVaoLam, double Luong, String Email, String GioiTinh, String ViTri, String diaChi, Date ngaySinh) {
        this.NgayVaoLam = NgayVaoLam;
        this.Luong = Luong;
        this.Email = Email;
        this.diaChi = diaChi;
        this.GioiTinh = GioiTinh;
        this.ngaySinh = ngaySinh;
        this.ViTri = ViTri;
    }

    public NhanVien(Date NgayVaoLam, double Luong, String Email, String GioiTinh, String ViTri, String id, String TaiKhoan, String MatKhau, String Name, String PhoneNumber, String VaiTro, int state,  String diaChi, Date ngaySinh) {
        super(id, TaiKhoan, MatKhau, Name, PhoneNumber, VaiTro, state);
        this.NgayVaoLam = NgayVaoLam;
        this.Luong = Luong;
        this.Email = Email;
        this.GioiTinh = GioiTinh;
        this.ViTri = ViTri;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
    }


    public Date getNgayVaoLam() {
        return NgayVaoLam;
    }

    public void setNgayVaoLam(Date NgayVaoLam) {
        this.NgayVaoLam = NgayVaoLam;
    }

    public double getLuong() {
        return Luong;
    }

    public void setLuong(double Luong) {
        this.Luong = Luong;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public String getViTri() {
        return ViTri;
    }

    public void setViTri(String ViTri) {
        this.ViTri = ViTri;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    @Override
    public String toString() {
        return "NhanVien{" + "NgayVaoLam=" + NgayVaoLam + ", Luong=" + Luong + ", Email=" + Email + ", diaChi=" + diaChi + ", GioiTinh=" + GioiTinh + ", ngaySinh=" + ngaySinh + ", ViTri=" + ViTri + '}';
    }
    
    public static void main(String args[]){
        
    }
}

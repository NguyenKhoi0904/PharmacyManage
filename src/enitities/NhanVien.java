/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author aries
 */
public class NhanVien extends User{
    private Date NgayVaoLam;
    private double Luong;
    private String Email;
    private String GioiTinh;
    private String ViTri;

    public NhanVien() {
    }

    public NhanVien(Date NgayVaoLam, double Luong, String Email, String GioiTinh, String ViTri) {
        this.NgayVaoLam = NgayVaoLam;
        this.Luong = Luong;
        this.Email = Email;
        this.GioiTinh = GioiTinh;
        this.ViTri = ViTri;
    }

    public NhanVien(Date NgayVaoLam, double Luong, String Email, String GioiTinh, String ViTri, String id, String TaiKhoan, String MatKhau, String Name, String PhoneNumber, String VaiTro) {
        super(id, TaiKhoan, MatKhau, Name, PhoneNumber, VaiTro);
        this.NgayVaoLam = NgayVaoLam;
        this.Luong = Luong;
        this.Email = Email;
        this.GioiTinh = GioiTinh;
        this.ViTri = ViTri;
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

    @Override
    public String toString() {
        return "NhanVien{" + "NgayVaoLam=" + NgayVaoLam + ", Luong=" + Luong + ", Email=" + Email + ", GioiTinh=" + GioiTinh + ", ViTri=" + ViTri + '}';
    }
    
    public static void main(String args[]){
        
    }
}

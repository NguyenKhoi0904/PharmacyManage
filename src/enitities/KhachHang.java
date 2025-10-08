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
public class KhachHang extends User{
    private Date NgayDangKy;
    private int DiemTichLuy;

    public KhachHang() {
    }

    public KhachHang(Date NgayDangKy, int DiemTichLuy) {
        this.NgayDangKy = NgayDangKy;
        this.DiemTichLuy = DiemTichLuy;
    }

    public KhachHang(Date NgayDangKy, int DiemTichLuy, String id, String TaiKhoan, String MatKhau, String Name, String PhoneNumber, String VaiTro) {
        super(id, TaiKhoan, MatKhau, Name, PhoneNumber, VaiTro);
        this.NgayDangKy = NgayDangKy;
        this.DiemTichLuy = DiemTichLuy;
    }
    
    public Date getNgayDangKy() {
        return NgayDangKy;
    }

    public void setNgayDangKy(Date NgayDangKy) {
        this.NgayDangKy = NgayDangKy;
    }

    public int getDiemTichLuy() {
        return DiemTichLuy;
    }

    public void setDiemTichLuy(int DiemTichLuy) {
        this.DiemTichLuy = DiemTichLuy;
    }

    @Override
    public String toString() {
        return "KhachHang{" + "NgayDangKy=" + NgayDangKy + ", DiemTichLuy=" + DiemTichLuy + '}';
    }
    
    public static void main(String args[]){
        KhachHang a = new KhachHang();
    }
}

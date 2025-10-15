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
public class LoHang {
    private String id;
    private int SoLuongNhap;
    private int SoLuongTon; // số lượng sản phẩm còn tồn lại trong 1 lô hàng
    private Date NgaySX;
    private Date HSD; // Hạn Sử Dụng
    private double GiaNhap;
    private NhaCungCap nhaCungCap;
    private Thuoc thuoc;
    private int state;

    public LoHang() {
    }

    public LoHang(String id, int SoLuongNhap, int SoLuongTon, Date NgaySX, Date HSD, double GiaNhap, NhaCungCap nhaCungCap, Thuoc thuoc, int state) {
        this.id = id;
        this.SoLuongNhap = SoLuongNhap;
        this.SoLuongTon = SoLuongTon;
        this.NgaySX = NgaySX;
        this.HSD = HSD;
        this.GiaNhap = GiaNhap;
        this.nhaCungCap = nhaCungCap;
        this.thuoc = thuoc;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSoLuongNhap() {
        return SoLuongNhap;
    }

    public void setSoLuongNhap(int SoLuongNhap) {
        this.SoLuongNhap = SoLuongNhap;
    }

    public int getSoLuongTon() {
        return SoLuongTon;
    }

    public void setSoLuongTon(int SoLuongTon) {
        this.SoLuongTon = SoLuongTon;
    }

    public Date getNgaySX() {
        return NgaySX;
    }

    public void setNgaySX(Date NgaySX) {
        this.NgaySX = NgaySX;
    }

    public Date getHSD() {
        return HSD;
    }

    public void setHSD(Date HSD) {
        this.HSD = HSD;
    }

    public double getGiaNhap() {
        return GiaNhap;
    }

    public void setGiaNhap(double GiaNhap) {
        this.GiaNhap = GiaNhap;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "LoHang{" + "id=" + id + ", SoLuongNhap=" + SoLuongNhap + ", SoLuongTon=" + SoLuongTon + ", NgaySX=" + NgaySX + ", HSD=" + HSD + ", GiaNhap=" + GiaNhap + ", nhaCungCap=" + nhaCungCap + ", thuoc=" + thuoc + ", state=" + state + '}';
    }

    public static void main(String args[]){
        
    }
}

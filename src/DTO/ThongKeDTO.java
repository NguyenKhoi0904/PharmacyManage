/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author aries
 */
public class ThongKeDTO {
    private int thang;
    private int soLuong;
    private double doanhThu;

    public ThongKeDTO() {
    }

    
    
    public ThongKeDTO(int thang, int tongSoLuong, double doanhThu) {
        this.thang = thang;
        this.soLuong = tongSoLuong;
        this.doanhThu = doanhThu;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }



    public double getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(double doanhThu) {
        this.doanhThu = doanhThu;
    }

    @Override
    public String toString() {
        return "ThongKeDTO{" + "thang=" + thang + ", tongSoLuong=" + soLuong + ", doanhThu=" + doanhThu + '}';
    }
    
    
}

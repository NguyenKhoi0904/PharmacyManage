/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

/**
 *
 * @author aries
 */
public class NhaCungCap {
    private String id;
    private String ten;
    private String sdt;
    private String DiaChi;
    private String email;
    private int state;

    public NhaCungCap() {
    }

    public NhaCungCap(String id, String ten, String sdt, String DiaChi, String email, int state) {
        this.id = id;
        this.ten = ten;
        this.sdt = sdt;
        this.DiaChi = DiaChi;
        this.email = email;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "NhaCungCap{" + "id=" + id + ", ten=" + ten + ", sdt=" + sdt + ", DiaChi=" + DiaChi + ", email=" + email + ", state=" + state + '}';
    }
    
    public static void main(String args[]){
        
    }
}

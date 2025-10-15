/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

/**
 *
 * @author aries
 */
public class User {
    private String id;
    private String TaiKhoan;
    private String MatKhau;
    private String Name;
    private String PhoneNumber;
    private String VaiTro;
    private int state;
    
    public User() {
        
    }
    
    public User(String id, String TaiKhoan, String MatKhau, String Name, String PhoneNumber, String VaiTro, int state) {
        this.id = id;
        this.TaiKhoan = TaiKhoan;
        this.MatKhau = MatKhau;
        this.Name = Name;
        this.PhoneNumber = PhoneNumber;
        this.VaiTro = VaiTro;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaiKhoan() {
        return TaiKhoan;
    }

    public void setTaiKhoan(String TaiKhoan) {
        this.TaiKhoan = TaiKhoan;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String MatKhau) {
        this.MatKhau = MatKhau;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getVaiTro() {
        return VaiTro;
    }

    public void setVaiTro(String VaiTro) {
        this.VaiTro = VaiTro;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", TaiKhoan=" + TaiKhoan + ", MatKhau=" + MatKhau + ", Name=" + Name + ", PhoneNumber=" + PhoneNumber + ", VaiTro=" + VaiTro + ", state=" + state + '}';
    }
    
    public static void main(String args[]){
        
    }
}

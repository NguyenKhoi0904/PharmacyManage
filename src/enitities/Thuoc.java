/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

/**
 *
 * @author aries
 */
public class Thuoc {
    private String id;
    private String Name;
    private double Price;
    private String Anh;
    private DanhMucThuoc danhMucThuoc;
    private String DonViTinh;
    private String NhaSanXuat;
    private String XuatXu;
    private int state;
    

    public Thuoc() {
    }

    public Thuoc(String id, String Name, double Price, String Anh, DanhMucThuoc danhMucThuoc, String DonViTinh, String NhaSanXuat, String XuatXu, int state) {
        this.id = id;
        this.Name = Name;
        this.Price = Price;
        this.Anh = Anh;
        this.danhMucThuoc = danhMucThuoc;
        this.DonViTinh = DonViTinh;
        this.NhaSanXuat = NhaSanXuat;
        this.XuatXu = XuatXu;
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

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String Anh) {
        this.Anh = Anh;
    }

    public DanhMucThuoc getDanhMucThuoc() {
        return danhMucThuoc;
    }

    public void setDanhMucThuoc(DanhMucThuoc danhMucThuoc) {
        this.danhMucThuoc = danhMucThuoc;
    }

    public String getDonViTinh() {
        return DonViTinh;
    }

    public void setDonViTinh(String DonViTinh) {
        this.DonViTinh = DonViTinh;
    }

    public String getNhaSanXuat() {
        return NhaSanXuat;
    }

    public void setNhaSanXuat(String NhaSanXuat) {
        this.NhaSanXuat = NhaSanXuat;
    }

    public String getXuatXu() {
        return XuatXu;
    }

    public void setXuatXu(String XuatXu) {
        this.XuatXu = XuatXu;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Thuoc{" + "id=" + id + ", Name=" + Name + ", Price=" + Price + ", Anh=" + Anh + ", danhMucThuoc=" + danhMucThuoc + ", DonViTinh=" + DonViTinh + ", NhaSanXuat=" + NhaSanXuat + ", XuatXu=" + XuatXu + ", state=" + state + '}';
    }
    
    
}

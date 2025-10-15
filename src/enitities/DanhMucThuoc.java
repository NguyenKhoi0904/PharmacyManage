/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enitities;

/**
 *
 * @author aries
 */
public class DanhMucThuoc {
    private String id;
    private String Name;
    private int state;

    public DanhMucThuoc() {
    }

    public DanhMucThuoc(String id, String Name, int state) {
        this.id = id;
        this.Name = Name;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DanhMucThuoc{" + "id=" + id + ", Name=" + Name + ", state=" + state + '}';
    }
    

    
    
}

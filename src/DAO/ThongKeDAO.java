/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author aries
 */

import DTO.ThongKeDTO;
import database.JDBCUtil;

import java.sql.*;
import java.util.*;

public class ThongKeDAO {
    public ArrayList<ThongKeDTO> thongKeTheoNam(int year){
        ArrayList<ThongKeDTO> list_thongKe = new ArrayList<>();
        String sql = 
                "SELECT MONTH(hd.ngay_xuat) AS thang, " +
                "SUM(ct.so_luong) AS tongSoLuong, " +
                "SUM(ct.so_luong * ct.don_gia) AS doanhThu " +
                "FROM chitiet_hd ct " +
                "JOIN hoadon hd ON ct.ma_hd = hd.ma_hd " +
                "WHERE YEAR(hd.ngay_xuat) = ? " +
                "GROUP BY MONTH(hd.ngay_xuat) " +
                "ORDER BY MONTH(hd.ngay_xuat)";
        
        try{
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, year);
            
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                int thang = rs.getInt("thang");
                int soLuong = rs.getInt("tongSoLuong");
                double doanhThu = rs.getDouble("doanhThu");
                
                list_thongKe.add(new ThongKeDTO(thang, soLuong, doanhThu));
            }
            
            conn.close();
            
        }catch (Exception ex){
            System.out.println("DAO.ThongKeDAO.thongKeTheoNam()");
            ex.printStackTrace();
        }
        return list_thongKe;
    }
    
    // Dữ liệu vẽ biểu đồ
    public Map<Integer, Integer> thongKeSoLuongTheoThang(int year){
        Map<Integer, Integer> map = new HashMap<>();
        String sql = 
                "SELECT MONTH(hd.ngay_xuat) AS thang, SUM(ct.so_luong) AS tongSoLuong " +
                "FROM chitiet_hd ct " +
                "JOIN hoadon hd ON ct.ma_hd = hd.ma_hd " +
                "WHERE YEAR(hd.ngay_xuat) = ? " +
                "GROUP BY MONTH(hd.ngay_xuat) " +
                "ORDER BY MONTH(hd.ngay_xuat) ";
        try{
            Connection con = JDBCUtil.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, year);
            
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                map.put(rs.getInt("thang"), rs.getInt("tongSoLuong"));
            }
            
            con.close();
        }catch(Exception ex){
            System.out.println("DAO.ThongKeDAO.thongKeSoLuongTheoThang()");
            ex.printStackTrace();
        }
        
        return map;
    }
}

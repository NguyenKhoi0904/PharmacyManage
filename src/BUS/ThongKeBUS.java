/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

/**
 *
 * @author aries
 */

import DTO.ThongKeDTO;
import DAO.ThongKeDAO;

import java.util.*;

public class ThongKeBUS {
    private static ThongKeBUS instance;
    private ThongKeDAO dao;
    
    public static ThongKeBUS getInstance(){
        if(instance == null) instance = new ThongKeBUS();
        return instance;
    }
    
    public ThongKeBUS(){
        dao = new ThongKeDAO();
    }
    
    public ArrayList<ThongKeDTO> thongKeTheoNam(int year){
        return dao.thongKeTheoNam(year);
    }
    
    public Map<Integer, Integer> thongKeTheoThang(int year){
        return dao.thongKeSoLuongTheoThang(year);
    }
}

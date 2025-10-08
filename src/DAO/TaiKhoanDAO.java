package com.mycompany.pharmacystore.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mycompany.pharmacystore.DTO.TaiKhoanDTO;
import com.mycompany.pharmacystore.database.JDBCUtil;

public class TaiKhoanDAO implements DAOinterface<TaiKhoanDTO> {

    @Override
    public ArrayList<TaiKhoanDTO> selectAll() {
        return new ArrayList<>();
    }

    @Override
    public TaiKhoanDTO selectById(String id) {
        return null;
    }

    @Override
    public int insert(TaiKhoanDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO taikhoan (ma_tk,tai_khoan,mat_khau,ten,sdt,vai_tro) VALUES (?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaTk());
            ps.setString(2, data.getTaiKhoan());
            ps.setString(3, data.getMatKhau());
            ps.setString(4, data.getTen());
            ps.setString(5, data.getSdt());
            ps.setString(7, data.getVaiTro());

            // exec
            result = ps.executeUpdate();

            // close conn
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(TaiKhoanDTO data) {
        
    }

    @Override
    public void deleteById(String id) {
        
    }
    
}

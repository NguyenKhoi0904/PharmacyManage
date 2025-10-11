package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.ChiTietHdDTO; // Giả định
import database.JDBCUtil; // Giả định

public class ChiTietHdDAO implements DAOinterface<ChiTietHdDTO> {

    @Override
    public ArrayList<ChiTietHdDTO> selectAll() {
        ArrayList<ChiTietHdDTO> result = new ArrayList<ChiTietHdDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitiet_hd";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // ma_hd (int), ma_thuoc (int), don_gia (BigDecimal), so_luong (int)
                result.add(new ChiTietHdDTO(rs.getInt("ma_hd"), rs.getInt("ma_thuoc"), rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ChiTietHdDTO selectById(String id) {
        ChiTietHdDTO result = null;
        try {
            Connection conn = JDBCUtil.getConnection();
            // Lấy chi tiết dựa trên ma_hd (Primary Key)
            String sql = "SELECT * FROM chitiet_hd WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new ChiTietHdDTO(rs.getInt("ma_hd"), rs.getInt("ma_thuoc"), rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int insert(ChiTietHdDTO data) {
        int result = 0;
        try {
            String sql = "INSERT INTO chitiet_hd (ma_hd,ma_thuoc,don_gia,so_luong) VALUES (?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaHd());
            ps.setInt(2, data.getMaThuoc()); // Khóa ngoại, phải tồn tại trong thuoc
            ps.setBigDecimal(3, data.getDonGia());
            ps.setInt(4, data.getSoLuong());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(ChiTietHdDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            // Dùng ma_hd để xác định hàng cần update
            String sql = "UPDATE chitiet_hd SET ma_thuoc=?,don_gia=?,so_luong=? WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, data.getMaThuoc()); // Khóa ngoại, phải tồn tại trong thuoc
            ps.setBigDecimal(2, data.getDonGia());
            ps.setInt(3, data.getSoLuong());
            ps.setInt(4, data.getMaHd()); // Điều kiện WHERE

            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteById(String id) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM chitiet_hd WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
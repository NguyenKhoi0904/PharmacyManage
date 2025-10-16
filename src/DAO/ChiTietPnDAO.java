package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.ChiTietPnDTO; // Giả định
import database.JDBCUtil; // Giả định

public class ChiTietPnDAO implements DAOinterface<ChiTietPnDTO> {

    @Override
    public ArrayList<ChiTietPnDTO> selectAll() {
        ArrayList<ChiTietPnDTO> result = new ArrayList<ChiTietPnDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitiet_pn";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // ma_pn (int), ma_lh (int), don_gia (BigDecimal), so_luong (int)
                result.add(new ChiTietPnDTO(rs.getInt("ma_pn"), rs.getInt("ma_lh"), rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ChiTietPnDTO selectById(String id) {
        ChiTietPnDTO result = null;
        try {
            Connection conn = JDBCUtil.getConnection();
            // Lấy chi tiết dựa trên ma_pn (Primary Key)
            String sql = "SELECT * FROM chitiet_pn WHERE ma_pn=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new ChiTietPnDTO(rs.getInt("ma_pn"), rs.getInt("ma_lh"), rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int insert(ChiTietPnDTO data) {
        int result = 0;
        try {
            String sql = "INSERT INTO chitiet_pn (ma_pn,ma_lh,don_gia,so_luong) VALUES (?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaPn());
            ps.setInt(2, data.getMaLh());
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
    public int update(ChiTietPnDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            // SQL sửa lỗi: UPDATE chitiet_pn. Dùng ma_pn để xác định hàng.
            String sql = "UPDATE chitiet_pn SET ma_lh=?,don_gia=?,so_luong=? WHERE ma_pn=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, data.getMaLh()); // Khóa ngoại, phải tồn tại trong lohang
            ps.setBigDecimal(2, data.getDonGia());
            ps.setInt(3, data.getSoLuong());
            ps.setInt(4, data.getMaPn()); // Điều kiện WHERE

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
            String sql = "DELETE FROM chitiet_pn WHERE ma_pn=?";
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
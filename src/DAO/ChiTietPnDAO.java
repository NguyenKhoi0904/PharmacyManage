package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.ChiTietPnDTO;
import database.JDBCUtil;

public class ChiTietPnDAO {

    // singleton instance
    private static ChiTietPnDAO instance;

    // singleton init
    public static ChiTietPnDAO getInstance() {
        if (instance == null) {
            instance = new ChiTietPnDAO();
        }
        return instance;
    }

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

    public ArrayList<ChiTietPnDTO> selectAllByMaPn(int ma_pn) {
        ArrayList<ChiTietPnDTO> result = new ArrayList<ChiTietPnDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitiet_pn WHERE ma_pn=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, ma_pn);
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

    public ChiTietPnDTO selectById(int ma_pn, int ma_lh) {
        ChiTietPnDTO result = null;
        try {
            Connection conn = JDBCUtil.getConnection();
            // Lấy chi tiết dựa trên ma_pn (Primary Key)
            String sql = "SELECT * FROM chitiet_pn WHERE ma_pn=? AND ma_lh=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ma_pn);
            ps.setInt(2, ma_lh);
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

    public int update(ChiTietPnDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            // SQL sửa lỗi: UPDATE chitiet_pn. Dùng ma_pn để xác định hàng.
            String sql = "UPDATE chitiet_pn SET ma_lh=?,don_gia=?,so_luong=? WHERE ma_pn=? AND ma_lh=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, data.getMaLh()); // Khóa ngoại, phải tồn tại trong lohang
            ps.setBigDecimal(2, data.getDonGia());
            ps.setInt(3, data.getSoLuong());
            ps.setInt(4, data.getMaPn()); // Điều kiện WHERE
            ps.setInt(5, data.getMaLh());

            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteById(int ma_pn) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM chitiet_pn WHERE ma_pn=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ma_pn);
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteById(int ma_pn, int ma_lh) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM chitiet_pn WHERE ma_pn=? AND ma_lh=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ma_pn);
            ps.setInt(2, ma_lh);
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.ChiTietHdDTO;
import database.JDBCUtil;

public class ChiTietHdDAO {
    public static ChiTietHdDAO getInstance() {
        return new ChiTietHdDAO();
    }

    public ArrayList<ChiTietHdDTO> selectAll() {
        ArrayList<ChiTietHdDTO> result = new ArrayList<ChiTietHdDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitiet_hd";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // ma_hd (int), ma_lh(int), ma_thuoc (int), don_gia (BigDecimal), so_luong (int)
                result.add(new ChiTietHdDTO(rs.getInt("ma_hd"), rs.getInt("ma_lh"), rs.getInt("ma_thuoc"),
                        rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ChiTietHdDTO> selectAllByMa(int maHd) {
        ArrayList<ChiTietHdDTO> result = new ArrayList<ChiTietHdDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitiet_pn WHERE ma_hd=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, maHd);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(new ChiTietHdDTO(rs.getInt("ma_hd"), rs.getInt("ma_lh"), rs.getInt("ma_thuoc"),
                        rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ChiTietHdDTO selectById(int maHd, int maLh, int maThuoc) {
        ChiTietHdDTO result = null;
        try {
            Connection conn = JDBCUtil.getConnection();
            // Lấy chi tiết dựa trên ma_hd (Primary Key)
            String sql = "SELECT * FROM chitiet_hd WHERE ma_hd=? AND ma_lh=? AND ma_thuoc=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maHd);
            ps.setInt(2, maLh);
            ps.setInt(3, maThuoc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result = new ChiTietHdDTO(rs.getInt("ma_hd"), rs.getInt("ma_lh"), rs.getInt("ma_thuoc"),
                        rs.getBigDecimal("don_gia"),
                        rs.getInt("so_luong"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int insert(ChiTietHdDTO data) {
        int result = 0;
        try {
            String sql = "INSERT INTO chitiet_hd (ma_hd,ma_lh,ma_thuoc,don_gia,so_luong) VALUES (?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaHd());
            ps.setInt(2, data.getMaLh());
            ps.setInt(3, data.getMaThuoc());
            ps.setBigDecimal(4, data.getDonGia());
            ps.setInt(5, data.getSoLuong());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(ChiTietHdDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            // Dùng ma_hd để xác định hàng cần update
            String sql = "UPDATE chitiet_hd SET ma_lh=?,ma_thuoc=?,don_gia=?,so_luong=? WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, data.getMaLh());
            ps.setInt(2, data.getMaThuoc());
            ps.setBigDecimal(3, data.getDonGia());
            ps.setInt(4, data.getSoLuong());
            ps.setInt(5, data.getMaHd());

            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteById(int maHd) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM chitiet_hd WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maHd);
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
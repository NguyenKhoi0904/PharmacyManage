package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.DanhMucThuocDTO;
import database.JDBCUtil;

public class DanhMucThuocDAO implements DAOinterface<DanhMucThuocDTO> {
    @Override
    public ArrayList<DanhMucThuocDTO> selectAll() {
        ArrayList<DanhMucThuocDTO> result = new ArrayList<DanhMucThuocDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM danhmucthuoc";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new DanhMucThuocDTO(rs.getInt("ma_dmt"), rs.getString("ten_dmt"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public DanhMucThuocDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM danhmucthuoc WHERE ma_dmt=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new DanhMucThuocDTO(rs.getInt("ma_dmt"), rs.getString("ten_dmt"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(DanhMucThuocDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO danhmucthuoc (ma_dmt,ten_dmt,trang_thai) VALUES (?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaDmt());
            ps.setString(2, data.getTenDmt());
            ps.setInt(3, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(DanhMucThuocDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE danhmucthuoc SET ten_dmt=?,trang_thai=? WHERE ma_dmt=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, data.getTenDmt());
            ps.setInt(2, data.getTrangThai());
            ps.setInt(3, data.getMaDmt());
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
            String sql = "UPDATE danhmucthuoc SET trang_thai=0 WHERE ma_dmt=?";
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

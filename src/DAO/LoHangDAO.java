package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.LoHangDTO;
import database.JDBCUtil;

public class LoHangDAO implements DAOinterface<LoHangDTO> {
    @Override
    public ArrayList<LoHangDTO> selectAll() {
        ArrayList<LoHangDTO> result = new ArrayList<LoHangDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM lohang";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new LoHangDTO(rs.getInt("ma_lh"), rs.getInt("ma_ncc"), rs.getInt("sl_nhap"),
                        rs.getInt("sl_ton"), rs.getDate("ngay_sx"), rs.getDate("han_sd"),
                        rs.getBigDecimal("gia_nhap"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public LoHangDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM lohang WHERE ma_lh=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new LoHangDTO(rs.getInt("ma_lh"), rs.getInt("ma_ncc"), rs.getInt("sl_nhap"),
                        rs.getInt("sl_ton"), rs.getDate("ngay_sx"), rs.getDate("han_sd"),
                        rs.getBigDecimal("gia_nhap"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(LoHangDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO lohang (ma_lh,ma_ncc,sl_nhap,sl_ton,ngay_sx,han_sd,gia_nhap,trang_thai) VALUES (?,?,?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaLh());
            ps.setInt(2, data.getMaNcc());
            ps.setInt(3, data.getSlNhap());
            ps.setInt(4, data.getSlTon());
            ps.setDate(5, data.getNgaySx());
            ps.setDate(6, data.getHanSd());
            ps.setBigDecimal(7, data.getGiaNhap());
            ps.setInt(8, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(LoHangDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE lohang SET ma_ncc=?,sl_nhap=?,sl_ton=?,ngay_sx=?,han_sd=?,gia_nhap=?,trang_thai=? WHERE ma_lh=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaNcc());
            ps.setInt(2, data.getSlNhap());
            ps.setInt(3, data.getSlTon());
            ps.setDate(4, data.getNgaySx());
            ps.setDate(5, data.getHanSd());
            ps.setBigDecimal(6, data.getGiaNhap());
            ps.setInt(7, data.getTrangThai());
            ps.setInt(8, data.getMaLh());
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
            String sql = "UPDATE lohang SET trang_thai=0 WHERE ma_lh=?";
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

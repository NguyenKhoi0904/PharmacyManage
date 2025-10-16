package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.PhieuNhapDTO;
import database.JDBCUtil;

public class PhieuNhapDAO implements DAOinterface<PhieuNhapDTO> {
    public static PhieuNhapDAO getInstance() {
        return new PhieuNhapDAO();
    }

    @Override
    public ArrayList<PhieuNhapDTO> selectAll() {
        ArrayList<PhieuNhapDTO> result = new ArrayList<PhieuNhapDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM phieunhap WHERE trang_thai=1";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new PhieuNhapDTO(rs.getInt("ma_pn"), rs.getInt("ma_nv"), rs.getTimestamp("thoi_gian_nhap"),
                        rs.getString("dia_diem"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public PhieuNhapDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM phieunhap WHERE ma_pn=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new PhieuNhapDTO(rs.getInt("ma_pn"), rs.getInt("ma_nv"), rs.getTimestamp("thoi_gian_nhap"),
                        rs.getString("dia_diem"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(PhieuNhapDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO phieunhap (ma_pn,ma_nv,thoi_gian_nhap,dia_diem,trang_thai) VALUES (?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaPn());
            ps.setInt(2, data.getMaNv());
            ps.setTimestamp(3, data.getThoiGianNhap());
            ps.setString(4, data.getDiaDiem());
            ps.setInt(5, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(PhieuNhapDTO data) {
        // tai_khoan,mat_khau,ten,sdt,vai_tro
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE phieunhap SET ma_nv=?,thoi_gian_nhap=?,dia_diem=?,trang_thai=? WHERE ma_pn=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaNv());
            ps.setTimestamp(2, data.getThoiGianNhap());
            ps.setString(3, data.getDiaDiem());
            ps.setInt(4, data.getTrangThai());
            ps.setInt(5, data.getMaPn());
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
            String sql = "UPDATE phieunhap SET trang_thai=0 WHERE ma_kh=?";
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

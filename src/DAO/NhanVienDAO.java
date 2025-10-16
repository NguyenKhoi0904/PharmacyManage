package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.NhanVienDTO;
import database.JDBCUtil;

public class NhanVienDAO implements DAOinterface<NhanVienDTO> {

    public static NhanVienDAO getInstance() {
        return new NhanVienDAO();
    }

    @Override
    public ArrayList<NhanVienDTO> selectAll() {
        ArrayList<NhanVienDTO> result = new ArrayList<NhanVienDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhanvien WHERE trang_thai=1";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new NhanVienDTO(rs.getInt("ma_nv"), rs.getInt("ma_tk"), rs.getDate("ngay_vao_lam"),
                        rs.getBigDecimal("luong"), rs.getString("email"), rs.getString("dia_chi"),
                        rs.getString("gioi_tinh"), rs.getDate("ngay_sinh"), rs.getString("vi_tri"),
                        rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public NhanVienDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhanvien WHERE ma_nv=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new NhanVienDTO(rs.getInt("ma_nv"), rs.getInt("ma_tk"), rs.getDate("ngay_vao_lam"),
                        rs.getBigDecimal("luong"), rs.getString("email"), rs.getString("dia_chi"),
                        rs.getString("gioi_tinh"), rs.getDate("ngay_sinh"), rs.getString("vi_tri"),
                        rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(NhanVienDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO nhanvien (ma_nv,ma_tk,ngay_vao_lam,luong,email,dia_chi,gioi_tinh,ngay_sinh,vi_tri,trang_thai) VALUES (?,?,?,?,?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaTk());
            ps.setInt(2, data.getMaNv());
            ps.setDate(3, data.getNgayVaoLam());
            ps.setBigDecimal(4, data.getLuong());
            ps.setString(5, data.getEmail());
            ps.setString(6, data.getDiaChi());
            ps.setString(7, data.getGioiTinh());
            ps.setDate(8, data.getNgaySinh());
            ps.setString(9, data.getViTri());
            ps.setInt(10, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(NhanVienDTO data) {
        // tai_khoan,mat_khau,ten,sdt,vai_tro
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE nhanvien SET ngay_vao_lam=?,luong=?,email=?,dia_chi=?,gioi_tinh=?,ngay_sinh=?,vi_tri=?,trang_thai=? WHERE ma_nv=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, data.getNgayVaoLam());
            ps.setBigDecimal(2, data.getLuong());
            ps.setString(3, data.getEmail());
            ps.setString(4, data.getDiaChi());
            ps.setString(5, data.getGioiTinh());
            ps.setDate(6, data.getNgaySinh());
            ps.setString(7, data.getViTri());
            ps.setInt(8, data.getTrangThai());
            ps.setInt(9, data.getMaNv());
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
            String sql = "UPDATE nhanvien SET trang_thai=0 WHERE ma_nv=?";
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
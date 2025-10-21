package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.KhachHangDTO;
import database.JDBCUtil;

public class KhachHangDAO implements DAOinterface<KhachHangDTO> {

    // singleton instance
    private static KhachHangDAO instance;

    public static KhachHangDAO getInstance() {
        if (instance == null) {
            instance = new KhachHangDAO();
        }
        return instance;
    }

    @Override
    public ArrayList<KhachHangDTO> selectAll() {
        ArrayList<KhachHangDTO> result = new ArrayList<KhachHangDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM khachhang WHERE trang_thai=1";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new KhachHangDTO(rs.getInt("ma_kh"), rs.getInt("ma_tk"), rs.getDate("ngay_dang_ky"),
                        rs.getInt("diem_tich_luy"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public KhachHangDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM khachhang WHERE ma_kh=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new KhachHangDTO(rs.getInt("ma_kh"), rs.getInt("ma_tk"), rs.getDate("ngay_dang_ky"),
                        rs.getInt("diem_tich_luy"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(KhachHangDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO khachhang (ma_kh,ma_tk,ngay_dang_ki,diem_tich_luy,trang_thai) VALUES (?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaKh());
            ps.setInt(2, data.getMaTk());
            ps.setDate(3, data.getNgayDangKy());
            ps.setInt(4, data.getDiemTichLuy());
            ps.setInt(5, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(KhachHangDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE khachhang SET ma_tk=?,ngay_dang_ki=?,diem_tich_luy=?,trang_thai=? WHERE ma_kh=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaTk());
            ps.setDate(2, data.getNgayDangKy());
            ps.setInt(3, data.getDiemTichLuy());
            ps.setInt(4, data.getTrangThai());
            ps.setInt(5, data.getMaKh());
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
            String sql = "UPDATE khachhang SET trang_thai=0 WHERE ma_kh=?";
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

package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.KhuyenMaiDTO;
import database.JDBCUtil;

public class KhuyenMaiDAO implements DAOinterface<KhuyenMaiDTO> {

    // singleton instance
    private static KhuyenMaiDAO instance;

    // singleton init
    public static KhuyenMaiDAO getInstance() {
        if (instance == null) {
            instance = new KhuyenMaiDAO();
        }
        return instance;
    }

    @Override
    public ArrayList<KhuyenMaiDTO> selectAll() {
        ArrayList<KhuyenMaiDTO> result = new ArrayList<KhuyenMaiDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM khuyenmai";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new KhuyenMaiDTO(rs.getInt("ma_km"), rs.getString("ten_km"), rs.getString("loai_km"),
                        rs.getBigDecimal("gia_tri_km"), rs.getString("dieu_kien_km"), rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"), rs.getInt("trang_thai"), rs.getInt("diem_can_tich_luy")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public KhuyenMaiDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM khuyenmai WHERE ma_km=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new KhuyenMaiDTO(rs.getInt("ma_km"), rs.getString("ten_km"), rs.getString("loai_km"),
                        rs.getBigDecimal("gia_tri_km"), rs.getString("dieu_kien_km"), rs.getDate("ngay_bat_dau"),
                        rs.getDate("ngay_ket_thuc"), rs.getInt("trang_thai"), rs.getInt("diem_can_tich_luy"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(KhuyenMaiDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO khuyenmai (ten_km,loai_km,gia_tri_km,dieu_kien_km,ngay_bat_dau,ngay_ket_thuc,trang_thai, diem_can_tich_luy) VALUES (?,?,?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, data.getTenKm());
            ps.setString(2, data.getLoaiKm());
            ps.setBigDecimal(3, data.getGiaTriKm());
            ps.setString(4, data.getDieuKienKm());
            ps.setDate(5, data.getNgayBatDau());
            ps.setDate(6, data.getNgayKetThuc());
            ps.setInt(7, data.getTrangThai());
            ps.setInt(8, data.getDiemCanTichLuy());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(KhuyenMaiDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE khuyenmai SET ten_km=?,loai_km=?,gia_tri_km=?,dieu_kien_km=?,ngay_bat_dau=?,ngay_ket_thuc=?,trang_thai=? WHERE ma_km=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, data.getTenKm());
            ps.setString(2, data.getLoaiKm());
            ps.setBigDecimal(3, data.getGiaTriKm());
            ps.setString(4, data.getDieuKienKm());
            ps.setDate(5, data.getNgayBatDau());
            ps.setDate(6, data.getNgayKetThuc());
            ps.setInt(7, data.getTrangThai());
            ps.setInt(8, data.getDiemCanTichLuy());
            ps.setInt(9, data.getMaKm());

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
            String sql = "UPDATE khuyenmai SET trang_thai=0 WHERE ma_km=?";
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

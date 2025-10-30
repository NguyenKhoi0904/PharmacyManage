package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

import DTO.HoaDonDTO;
import database.JDBCUtil;

public class HoaDonDAO implements DAOinterface<HoaDonDTO> {

    // singleton instance
    private static HoaDonDAO instance;

    // singleton init
    public static HoaDonDAO getInstance() {
        if (instance == null) {
            instance = new HoaDonDAO();
        }
        return instance;
    }

    @Override
    public ArrayList<HoaDonDTO> selectAll() {
        ArrayList<HoaDonDTO> result = new ArrayList<HoaDonDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM hoadon";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new HoaDonDTO(rs.getInt("ma_hd"), rs.getInt("ma_nv"), rs.getInt("ma_kh"), rs.getInt("ma_km"),
                        rs.getBigDecimal("tong_tien"), rs.getDate("ngay_xuat"), rs.getString("phuong_thuc_tt"),
                        rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public HoaDonDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM hoadon WHERE ma_hd=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new HoaDonDTO(rs.getInt("ma_hd"), rs.getInt("ma_nv"), rs.getInt("ma_kh"), rs.getInt("ma_km"),
                        rs.getBigDecimal("tong_tien"), rs.getDate("ngay_xuat"), rs.getString("phuong_thuc_tt"),
                        rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(HoaDonDTO data) {
        int generatedId = -1; // ID của hóa đơn vừa thêm

        try {
            String sql = "INSERT INTO hoadon (ma_nv, ma_kh, ma_km, tong_tien, ngay_xuat, phuong_thuc_tt, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?)";
            Connection conn = JDBCUtil.getConnection();

            // Thêm RETURN_GENERATED_KEYS để JDBC biết bạn muốn lấy ID tự tăng
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, data.getMaNv());
            ps.setInt(2, data.getMaKh());
            ps.setInt(3, data.getMaKm());
            ps.setBigDecimal(4, data.getTongTien());
            ps.setDate(5, data.getNgayXuat());
            ps.setString(6, data.getPhuongThucTt());
            ps.setInt(7, data.getTrangThai());

            int rowsAffected = ps.executeUpdate(); // result cũ

            if (rowsAffected > 0) {
                // Lấy ID tự tăng vừa sinh
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        data.setMaHd(generatedId); // Gán lại vào DTO 
                    }
                }
            }

            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId; // Trả về mã hóa đơn vừa thêm
    }


    @Override
    public int update(HoaDonDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE hoadon SET ma_nv=?,ma_kh=?,ma_km=?,tong_tien=?,ngay_xuat=?,phuong_thuc_tt=?,trang_thai=? WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaNv());
            ps.setInt(2, data.getMaKh());
            ps.setInt(3, data.getMaKm());
            ps.setBigDecimal(4, data.getTongTien());
            ps.setDate(5, data.getNgayXuat());
            ps.setString(6, data.getPhuongThucTt());
            ps.setInt(7, data.getTrangThai());
            ps.setInt(8, data.getMaHd());
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
        String sql = "DELETE FROM hoadon WHERE ma_hd = ?"; // xóa bản ghi thật
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}

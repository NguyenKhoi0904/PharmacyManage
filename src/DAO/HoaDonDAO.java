package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.HoaDonDTO;
import database.JDBCUtil;

public class HoaDonDAO implements DAOinterface<HoaDonDTO> {
    @Override
    public ArrayList<HoaDonDTO> selectAll() {
        ArrayList<HoaDonDTO> result = new ArrayList<HoaDonDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM hoadon";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new HoaDonDTO(rs.getInt("ma_hd"), rs.getInt("ma_kh"), rs.getInt("ma_km"),
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
            String sql = "SELECT * FROM hoadon WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new HoaDonDTO(rs.getInt("ma_hd"), rs.getInt("ma_kh"), rs.getInt("ma_km"),
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
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO hoadon (ma_hd,ma_kh,ma_km,tong_tien,ngay_xuat,phuong_thuc_tt,trang_thai) VALUES (?,?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaHd());
            ps.setInt(2, data.getMaKh());
            ps.setInt(3, data.getMaKm());
            ps.setBigDecimal(4, data.getTongTien());
            ps.setDate(5, data.getNgayXuat());
            ps.setString(6, data.getPhuongThucTt());
            ps.setInt(9, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(HoaDonDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE hoadon SET ma_kh=?,ma_km=?,tong_tien=?,ngay_xuat=?,phuong_thuc_tt=?,trang_thai=? WHERE ma_hd=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaKh());
            ps.setInt(2, data.getMaKm());
            ps.setBigDecimal(3, data.getTongTien());
            ps.setDate(4, data.getNgayXuat());
            ps.setString(5, data.getPhuongThucTt());
            ps.setInt(6, data.getTrangThai());
            ps.setInt(7, data.getMaHd());
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
            String sql = "UPDATE hoadon SET trang_thai=0 WHERE ma_hd=?";
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

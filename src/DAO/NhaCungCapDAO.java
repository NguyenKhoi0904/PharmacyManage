package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.NhaCungCapDTO;
import database.JDBCUtil;

public class NhaCungCapDAO implements DAOinterface<NhaCungCapDTO> {

    // singleton instance
    private static NhaCungCapDAO instance;

    // singleton init
    public static NhaCungCapDAO getInstance() {
        if (instance == null) {
            instance = new NhaCungCapDAO();
        }
        return instance;
    }

    @Override
    public ArrayList<NhaCungCapDTO> selectAll() {
        ArrayList<NhaCungCapDTO> result = new ArrayList<NhaCungCapDTO>();
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhacungcap WHERE trang_thai=1";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                result.add(new NhaCungCapDTO(rs.getInt("ma_ncc"), rs.getString("ten_ncc"), rs.getString("sdt_ncc"),
                        rs.getString("dia_chi"), rs.getString("email_ncc"), rs.getInt("trang_thai")));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public NhaCungCapDTO selectById(String id) {
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhacungcap WHERE ma_ncc=? AND trang_thai=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new NhaCungCapDTO(rs.getInt("ma_ncc"), rs.getString("ten_ncc"), rs.getString("sdt_ncc"),
                        rs.getString("dia_chi"), rs.getString("email_ncc"), rs.getInt("trang_thai"));
            }
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(NhaCungCapDTO data) {
        int result = 0;
        try {
            // init connection
            String sql = "INSERT INTO nhacungcap (ma_ncc,ten_ncc,sdt_ncc,dia_chi,email_ncc,trang_thai) VALUES (?,?,?,?,?,?)";
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, data.getMaNcc());
            ps.setString(2, data.getTenNcc());
            ps.setString(3, data.getSdtNcc());
            ps.setString(4, data.getDiaChi());
            ps.setString(5, data.getEmailNcc());
            ps.setInt(6, data.getTrangThai());
            result = ps.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(NhaCungCapDTO data) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE nhacungcap SET ten_ncc=?,sdt_ncc=?,dia_chi=?,email_ncc=?,trang_thai=? WHERE ma_ncc=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, data.getTenNcc());
            ps.setString(2, data.getSdtNcc());
            ps.setString(3, data.getDiaChi());
            ps.setString(4, data.getEmailNcc());
            ps.setInt(5, data.getTrangThai());
            ps.setInt(6, data.getMaNcc());
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
            String sql = "UPDATE nhacungcap SET trang_thai=0 WHERE ma_ncc=?";
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

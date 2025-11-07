/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author aries
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportDataToExcel {
    public static void exportToExcel(JTable jTable, String filePath){
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Danh sách khách hàng");
            TableModel model = jTable.getModel();
            
            Row header = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }
        
            for(int i = 0; i < model.getRowCount(); i++){
                Row row = sheet.createRow(i+1);
                for(int j = 0; j < model.getColumnCount(); j++){
                    Object value = model.getValueAt(i, j);
                    row.createCell(j).setCellValue(value != null ? value.toString() : "");
                }
            }
            try(FileOutputStream fos = new FileOutputStream(filePath)){
                workbook.write(fos);
            }
            
            JOptionPane.showMessageDialog(null, "Xuất file Excel thành công");
        }catch(IOException  ex){
            System.err.println("LỖI Ở HÀM exportToExcel(), ExportDataToExcel.java");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi xuất file Excel");
        }
        
        
    }
    
}

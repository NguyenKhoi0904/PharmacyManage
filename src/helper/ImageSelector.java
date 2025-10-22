package helper;

import javax.swing.*;

import java.awt.Component;
import java.io.*;
import java.nio.file.*;

public class ImageSelector {

    /**
     * Mở hộp thoại chọn file ảnh, copy ảnh vào thư mục "image" trong project,
     * và trả về đường dẫn tương đối (ví dụ: "image/tenanh.png")
     */
    public static String chooseAndCopyImage(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh...");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Chỉ cho phép chọn các file ảnh
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Hình ảnh (JPG, PNG, JPEG)", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                // Tạo thư mục image nếu chưa tồn tại
                File imageDir = new File("image");
                if (!imageDir.exists()) {
                    imageDir.mkdir();
                }

                // Đặt tên file mới để tránh trùng
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path destinationPath = Paths.get(imageDir.getPath(), fileName);

                // Copy file
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // Trả về đường dẫn tương đối (để lưu DB hoặc load lại)
                return "image/" + fileName;

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent,
                        "Lỗi khi copy ảnh: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        return null; // Người dùng bấm Hủy
    }
}
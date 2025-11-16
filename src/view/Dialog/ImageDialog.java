package view.Dialog;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.net.URL;

public class ImageDialog extends JDialog {

    public ImageDialog(Frame parent, String title, String imgPath) {
        super(parent, title, true);

        setTitle(title);
        setSize(600, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblImage, BorderLayout.CENTER);

        try {
            ImageIcon icon = loadImage(imgPath);

            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(550, 550, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            } else {
                lblImage.setText("❌ Không thể tải ảnh: " + imgPath);
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblImage.setText("❌ Đã xảy ra lỗi khi tải ảnh.");
        }

        setVisible(true);
    }

    /**
     * Hàm load ảnh từ classpath hoặc từ đường dẫn tuyệt đối.
     */
    private ImageIcon loadImage(String path) {
        try {
            File file = new File(path);

            // Ảnh trên filesystem
            if (file.exists()) {
                Image img = ImageIO.read(file); // QUAN TRỌNG: dùng ImageIO.read()
                if (img != null)
                    return new ImageIcon(img);
            }

            // Thử load trong resources
            URL url = getClass().getResource(path);
            if (url != null) {
                Image img = ImageIO.read(url); // load bằng ImageIO
                if (img != null)
                    return new ImageIcon(img);
            }

            // Thử "src" nếu đường dẫn lưu như /image/xxx.webp
            file = new File("src" + path);
            if (file.exists()) {
                Image img = ImageIO.read(file);
                if (img != null)
                    return new ImageIcon(img);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

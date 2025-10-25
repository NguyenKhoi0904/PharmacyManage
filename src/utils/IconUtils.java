package utils;

import java.awt.Image;
import java.net.URL;
import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * IconUtils - Utility class for setting and scaling icons on Swing components
 * Supports JLabel, JButton, JToggleButton, JMenuItem, etc.
 */
public class IconUtils {

    /**
     * Set icon for JLabel
     * @param label target JLabel
     * @param fileName file name in /icon/ folder (e.g. "cart.png")
     * @param deleteText whether to remove text
     */
    public static void setIcon(JLabel label, String fileName, boolean deleteText) {
        setIconCommon(label, fileName, deleteText);
    }

    /**
     * Set icon for JButton, JToggleButton, JMenuItem, etc.
     * @param button target AbstractButton (e.g. JButton, JToggleButton)
     * @param fileName file name in /icon/ folder (e.g. "cart.png")
     * @param deleteText whether to remove text
     */
    public static void setIcon(AbstractButton button, String fileName, boolean deleteText) {
        button.setBorderPainted(false);      // Ẩn viền
        button.setContentAreaFilled(false);  // Ẩn nền
        button.setFocusPainted(false);       // Ẩn viền khi focus
        button.setOpaque(false);             // Làm trong suốt hoàn toàn
        setIconCommon(button, fileName, deleteText);
    }

    /**
     * Common internal method to load and scale the icon for both JLabel and AbstractButton.
     */
    private static void setIconCommon(JComponent comp, String fileName, boolean deleteText) {
        SwingUtilities.invokeLater(() -> {
            // Optional: remove text
            if (deleteText) {
                if (comp instanceof JLabel label) {
                    label.setText("");
                } else if (comp instanceof AbstractButton btn) {
                    btn.setText("");
                }
            }

            // Load image from /icon/ folder in resources
            URL imgURL = IconUtils.class.getResource("/image/" + fileName);
            if (imgURL == null) {
                System.err.println("Không tìm thấy file icon: " + fileName);
                return;
            }

            ImageIcon originalIcon = new ImageIcon(imgURL);
            int width = comp.getWidth();
            int height = comp.getHeight();

            // If component has not been rendered yet → wait for resize
            if (width <= 0 || height <= 0) {
                comp.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        setIconCommon(comp, fileName, deleteText);
                        comp.removeComponentListener(this);
                    }
                });
                return;
            }

            // Scale image smoothly
            Image scaledImage = originalIcon.getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            // Apply icon depending on component type
            if (comp instanceof JLabel label) {
                label.setIcon(scaledIcon);
            } else if (comp instanceof AbstractButton btn) {
                btn.setIcon(scaledIcon);
            }
        });
    }
}

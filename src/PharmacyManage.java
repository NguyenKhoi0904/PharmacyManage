import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import BUS.BUSManager;
import test.LoginFrame;
import view.LoginForm;

public class PharmacyManage {
    public static void main(String[] args) {
        // BUS init
        BUSManager.initAllBUS();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            FlatDarkLaf.setup();
            UIManager.put("PasswordField.showRevealButton", true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // view test
        SwingUtilities.invokeLater(() -> {
            // ========== DEBUG ==========
            // new LoginFrame().setVisible(true);

            // main app init
            new LoginForm().setVisible(true);
        });
    }
}

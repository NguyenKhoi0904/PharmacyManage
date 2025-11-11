import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import BUS.BUSManager;
import view.LoginForm;

public class PharmacyManage {
    public static void main(String[] args) {
        // BUS init
        BUSManager.initAllBUS();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            FlatLightLaf.setup();
            UIManager.put("PasswordField.showRevealButton", true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // UI init
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}

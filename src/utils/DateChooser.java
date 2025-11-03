package utils;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class DateChooser {

    /**
     * Tạo một JDateChooser có định dạng mặc định và tooltip gợi ý
     */
    public static JDateChooser createDateChooser(String tooltip) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setToolTipText(tooltip);
        dateChooser.setPreferredSize(new Dimension(140, 25));
        dateChooser.setDate(new Date()); // mặc định là hôm nay
        return dateChooser;
    }

    /**
     * Thêm label + JDateChooser vào container (panel hoặc frame)
     * @param container JPanel hoặc JFrame.getContentPane()
     * @param labelText nội dung label (vd: "Ngày bắt đầu")
     * @param x, y, w, h vị trí và kích thước
     * @return đối tượng JDateChooser (để sau này getDate)
     */
    public static JDateChooser addLabeledDateChooser(Container container,
                                                     String labelText,
                                                     int x, int y, int w, int h) {
        JLabel label = new JLabel(labelText + ":");
        label.setBounds(x, y, 100, 25);
        container.add(label);

        JDateChooser chooser = createDateChooser("Chọn " + labelText.toLowerCase());
        chooser.setBounds(x + 110, y, w, h);
        container.add(chooser);

        return chooser;
    }
}

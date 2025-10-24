import javax.swing.*;
import javax.swing.text.*;

public class NumberOnlyField extends JTextField {
    public NumberOnlyField() {
        this("0.0");
    }

    public NumberOnlyField(String defaultValue) {
        super(defaultValue);
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new DecimalFilter());
    }

    private static class DecimalFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null)
                return;
            if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string))
                super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null)
                return;
            if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), text))
                super.replace(fb, offset, length, text, attrs);
        }

        private boolean isValidInput(String currentText, String newText) {
            String combined = currentText + newText;

            // Cho phép rỗng (người dùng xóa hết)
            if (combined.isEmpty())
                return true;

            // Kiểm tra theo regex: chỉ số hoặc 1 dấu chấm duy nhất
            return combined.matches("\\d*(\\.\\d*)?");
        }
    }
}

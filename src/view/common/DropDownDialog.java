package src.view.common;

import javax.swing.JOptionPane;

public class DropDownDialog {
    public int showDropDownDialog(String title, String message, String[] options) {
        Object selectionObject = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (selectionObject == null) { return -1; }

        for (int i = 0; i <= options.length - 1; i++) {
            if (options[i].equals(selectionObject.toString())) { return i; }
        }

        return -1;
    }
}

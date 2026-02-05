package app;

import javax.swing.*;
import java.awt.*;
import ui.MainFrame;

public class AppLauncher {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

         setGlobalFont(new Font("SansSerif", Font.PLAIN, 12));

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
     private static void setGlobalFont(Font font) {
        for (var key : UIManager.getDefaults().keySet()) {
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }
}

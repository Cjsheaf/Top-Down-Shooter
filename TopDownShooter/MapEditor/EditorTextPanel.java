package TopDownShooter.MapEditor;

import javax.swing.*;
import java.awt.*;

public class EditorTextPanel extends JPanel {
    private static EditorTextPanel instance;
    
    private JTextArea textArea;
    private JScrollPane scrollPane;
    
    private EditorTextPanel() {
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(new Runnable() { //There is no way to know the size of this panel yet, so we will have to "fill" the space later
            public void run() {
                EditorTextPanel.this.resize();
            }
        });
    }
    
    public static EditorTextPanel getInstance() {
        if (instance == null) {
            instance = new EditorTextPanel();
        }
        return instance;
    }
    
    public void addText(String text) {
        final String newline = "\n";
        
        textArea.append(newline + " - " + text);
    }
    
    public void resize() {
        scrollPane.setPreferredSize(new Dimension(this.getWidth() - 10, this.getHeight() - 10));
    }
}

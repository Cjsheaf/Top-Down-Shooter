package TopDownShooter.MapEditor;
import TopDownShooter.Utility.View;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

public class LayerSelectorPanel extends JPanel implements View {
    private SelectionModel selection;
    
    private JLabel currentLayerLabel;
    
    public LayerSelectorPanel() {
        selection = SelectionModel.getInstance();
        selection.addViewer(this);
        
        loadLayerSelectorPanel();
    }
    
    private void loadLayerSelectorPanel() {
        this.setLayout(new BorderLayout());
        
        JButton plusButton = new JButton("+");
        plusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                selection.setCurrentLayer(selection.getCurrentLayer() + 1);
            }
        });
        this.add(plusButton, BorderLayout.LINE_START);
        
        currentLayerLabel = new JLabel("Layer: " + selection.getCurrentLayer());
        currentLayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(currentLayerLabel, BorderLayout.CENTER);
        
        JButton minusButton = new JButton("-");
        minusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                selection.setCurrentLayer(selection.getCurrentLayer() - 1);
            }
        });
        this.add(minusButton, BorderLayout.LINE_END);
    }
    
    public void notifyChange() {
        currentLayerLabel.setText("Layer: " + selection.getCurrentLayer());
    }
}
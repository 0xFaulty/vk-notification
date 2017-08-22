package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.components.RotatePanel;

import javax.swing.*;

/**
 * The class {@code RightUpdate} представляет собой правую панель отображающуюся
 * во время загрузки.
 */
public class RightUpdate extends Panel {

    private JLabel label;
    private boolean inProcess;
    private RotatePanel rotatePanel;

    public JPanel getPanel() {
        rotatePanel = new RotatePanel();
        label = new JLabel("..", JLabel.CENTER);
        label.setFont(design.getSecondBoldFont());
        label.setForeground(design.getThirdForeColor());

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.Y_AXIS));
        tempPanel.add(rotatePanel);
        tempPanel.add(label);

        mainPanel.add(tempPanel);

        return mainPanel;
    }

    public void updateLabel(int current, int all) {
        label.setText("Загружается.. (" + current + "/" + all + ")");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        rotatePanel.setRotateFlag(visible);
    }

    public boolean isInProcess() {
        return inProcess;
    }

    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
        label.setText("..");
    }
}

package com.defaulty.notivk.gui.panels;

import com.defaulty.notivk.gui.components.RotatePanel;

import javax.swing.*;

public class RightUpdate extends Panel {

    private JLabel label;
    private boolean inProcess;

    public JPanel getPanel() {
        RotatePanel rotatePanel = new RotatePanel();
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
    }

    public boolean isInProcess() {
        return inProcess;
    }

    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
        label.setText("..");
    }
}

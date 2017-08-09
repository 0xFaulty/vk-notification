package com.defaulty.notivk.gui.components;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.backend.threadpool.PoolImpl;
import com.defaulty.notivk.backend.threadpool.requests.GroupRequest;
import com.defaulty.notivk.backend.threadpool.requests.Request;
import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.service.ButtonConstructor;
import com.defaulty.notivk.gui.service.PanelConstructor;
import com.vk.api.sdk.objects.groups.GroupFull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class GroupAdd extends JFrame {

    private PanelConstructor previewPanel = new PanelConstructor(PanelConstructor.PanelType.GroupPreview);
    private final JTextField jTextField = new JTextField();
    private JPanel groupInfoPanel;
    private String lastGroupId;

    public GroupAdd(String title, String label, String button) {

        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        groupInfoPanel = previewPanel.getPanel();
        groupInfoPanel.setVisible(false);

        final JPanel jMainPanel = new JPanel();
        jMainPanel.setLayout(new BorderLayout(30, 0));
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(30, 0));

        JLabel jLabel = new JLabel();
        jLabel.setText(label);

        JButton jButton = new ButtonConstructor().getSimpleButton(button, e -> addGroup());

        jTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }

            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            void warn() {
                groupInfoPanel.setVisible(false);
                String string = detectGroupName(jTextField.getText());
                lastGroupId = null;
                if (string.length() > 0) PoolImpl.getInstance().addRequest(new GroupRequest(string,
                        SettingsWrapper.getInstance().getUserData(), (o) -> addGroupPreview(o)));
            }
        });

        jPanel.add(jLabel, BorderLayout.NORTH);
        jPanel.add(jTextField, BorderLayout.CENTER);
        jPanel.add(jButton, BorderLayout.SOUTH);

        jMainPanel.add(jPanel, BorderLayout.NORTH);

        jMainPanel.add(groupInfoPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(300, 185));
        getContentPane().add(jMainPanel);
    }

    private void addGroupPreview(List<Request> requestList) {
        GroupRequest groupRequest = (GroupRequest) requestList.get(0);
        GroupFull groupFull = groupRequest.getResponse();
        if (groupRequest.getGroupName().equals(detectGroupName(jTextField.getText()))) {
            if (groupFull != null) {
                lastGroupId = groupFull.getId();
                previewPanel.updatePanel(groupFull);
                groupInfoPanel.setVisible(true);
            } else
                groupInfoPanel.setVisible(false);
        }
    }

    private void addGroup() {
        if (lastGroupId != null) {
            if (!SettingsWrapper.getInstance().getGroupsIdsList().contains(lastGroupId)) {
                GUI.getInstance().guiAddNewGroupToPanel(lastGroupId);
            } else {
                JOptionPane.showMessageDialog(null, "Указанная группа уже есть в списке");
            }
        } else
            JOptionPane.showMessageDialog(null, "Группа не определена");
    }

    private String detectGroupName(String string) {
        string = string.replace("/club", "/");
        int sepPos = string.lastIndexOf("/");
        if (sepPos < 0) sepPos = 0;
        else sepPos++;
        int endPos = string.indexOf(" ", sepPos);
        if (endPos < 0) endPos = string.length();
        return string.substring(sepPos, endPos);
    }

}
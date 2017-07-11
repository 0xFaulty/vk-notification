package com.defaulty.notivk.gui.components;

import com.defaulty.notivk.backend.SettingsWrapper;
import com.defaulty.notivk.gui.GUI;
import com.defaulty.notivk.gui.service.ButtonConstructor;
import com.defaulty.notivk.gui.service.Design;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;

public class GroupSettings extends JFrame {

    private static SettingsWrapper settings = SettingsWrapper.getInstance();
    private static Design design = Design.getInstance();

    private JCheckBox checkBox = new JCheckBox();
    private JTextArea textArea = new JTextArea();
    private String groupName;

    public GroupSettings(String name) {

        super("Настройки группы: '" + name + "'");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.groupName = name;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        checkBox.setText("Показывать посты со словами (тегами)");
        checkBox.setFocusPainted(false);

        checkBox.setSelected(settings.getGroupCheckState(name));
        checkBox.addActionListener(e -> settings.setNotifyType(checkBox.isSelected()));

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BorderLayout());
        checkBoxPanel.add(checkBox, BorderLayout.WEST);

        textArea.setText(parseToString(settings.getGroupTags(name), ",\n"));
        textArea.setAutoscrolls(true);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        textArea.setBorder(border);

        JLabel label = new JLabel();
        label.setBorder(border);
        label.setText("<html>" + "квартира у озера," + "<br>" +
                "видеокарта," + "<br>" +
                "дача," + "<br>" +
                "телефон" + "<br>" +
                "</html>");

        JPanel textAndLabel = new JPanel();
        textAndLabel.setLayout(new BorderLayout(10, 0));
        textAndLabel.add(textArea, BorderLayout.CENTER);
        textAndLabel.add(label, BorderLayout.EAST);

        JButton delButt = new ButtonConstructor().getSimpleButton("Удалить группу", e -> deleteGroup());
        JButton saveButt = new ButtonConstructor().getSimpleButton("Сохранить", e -> saveGroupSettings());

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout(10, 0));
        buttons.add(delButt, BorderLayout.WEST);
        buttons.add(saveButt, BorderLayout.CENTER);

        checkBoxPanel.setBorder(design.getBorderSmall());
        textAndLabel.setBorder(design.getBorderSmall());
        buttons.setBorder(design.getBorderSmall());
        label.setBorder(design.getBorderSmall());

        mainPanel.add(checkBoxPanel);
        mainPanel.add(textAndLabel);
        mainPanel.add(buttons);

        setPreferredSize(new Dimension(400, 250));
        getContentPane().add(mainPanel);
    }

    private void deleteGroup() {
        int dialogResult = JOptionPane.showConfirmDialog(
                null,
                "Удалить группу '" + groupName + "'?", "Предупреждение",
                JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            settings.removeGroupId(groupName);
            settings.save();
            GUI.getInstance().removeGuiGroups();
            GUI.getInstance().guiAddGroup(settings.getGroupsIdsList());
            dispose();
        }
    }

    private void saveGroupSettings() {
        settings.setGroupCheckState(groupName, checkBox.isSelected());
        settings.setGroupTags(groupName, parseToList(textArea.getText(), ","));
        settings.save();
        dispose();
    }

    private java.util.List<String> parseToList(String s, String separator) {
        s = s.replace("\n", "");
        java.util.List<String> out = new ArrayList<>();
        String[] array = s.split(Pattern.quote(separator));
        for (int i = 0; i < array.length; i++)
            array[i] = array[i].trim();
        out.addAll(Arrays.asList(array));
        return out;
    }

    private String parseToString(java.util.List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(separator);
        }
        return sb.toString();
    }

}
package com.rug.energygrid.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsAgents extends JFrame {
    private JPanel panel;
    private JList agentList;
    private JScrollPane scrollPane;

    private static int DEFAULT_FRAME_WIDTH = 250;
    private static int DEFAULT_FRAME_HEIGHT = 250;


    //TODO: remove this test stuff
    private String[] data = {"element1", "element2", "Thats all"};

    public SettingsAgents() {
        super("Settings Agent");

        setContentPane(this.panel);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setVisible(true);


        DefaultListModel<String> testModel = new DefaultListModel<>();
        for(String element : data) {
            testModel.addElement(element);
        }

        agentList.setModel(testModel);
        agentList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        agentList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        agentList.setVisibleRowCount(-1);
        agentList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    System.out.println("clicked twice!!");
                }
            }
        });

        JScrollPane listScroller = new JScrollPane(agentList);
        listScroller.setPreferredSize(new Dimension(DEFAULT_FRAME_WIDTH-50, DEFAULT_FRAME_HEIGHT-100));




    }

}

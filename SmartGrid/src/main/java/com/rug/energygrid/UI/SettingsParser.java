package com.rug.energygrid.UI;

import com.rug.energygrid.parser.AgentDeserializer;
import com.rug.energygrid.parser.Initializer;
import com.rug.energygrid.parser.JSON_Grid_Deserializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsParser extends JFrame{

    private JButton confirmSettingsButton;
    private JButton showGridButton;
    private JLabel confirmedSettings;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField speedupField;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JLabel speedupLabel;
    private JPanel panel;

    private Initializer initializer;
    private JSON_Grid_Deserializer deserializer;
    private AgentDeserializer agentDeserializer;

    private static final String AGENT_CABLE_SETTING_NAME = "Houses and Cables";

    private static int DEFAULT_FRAME_WIDTH = 500;
    private static int DEFAULT_FRAME_HEIGHT = 500;

    //TODO: add/show the options to change constants like constant pricing, L from the cable energyLoss etc.
    public SettingsParser(Initializer init) {
        super("Settings Smartgrid");

        this.initializer = init;

        setContentPane(this.panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);



        showGridButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame(AGENT_CABLE_SETTING_NAME);

                //Create and set up the content pane.
                deserializer = new JSON_Grid_Deserializer();
                agentDeserializer = new AgentDeserializer();
                JComponent newContentPane =  new SettingsAgent(deserializer, agentDeserializer);
                newContentPane.setOpaque(true); //content panes must be opaque
                frame.setContentPane(newContentPane);

                frame.setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT/2);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });

        confirmSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmSettingsButton.setEnabled(false);
                try{
                    Integer speedup = Integer.parseInt(speedupField.getText());
                    initializer.setSpeedup(speedup);
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                    confirmedSettings.setText("Speedup is not a valid number, for example: 8001");
                    confirmSettingsButton.setEnabled(true);
                    return;
                }
                String value;
                value = startTimeField.getText();
                Pattern p = Pattern.compile("[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]T[0-9][0-9]\\:[0-9][0-9]\\:[0-9][0-9]\\.[0-9][0-9]Z");
                Matcher m = p.matcher(value);
                if (m.matches()) {
                    initializer.setStartTime(value);
                } else {
                    confirmedSettings.setText("Start time is not a valid value, for example: 1995-01-01T10:15:30.00Z");
                    confirmSettingsButton.setEnabled(true);
                    return;
                }
                value = endTimeField.getText();
                m = p.matcher(value);
                if (m.matches()) {
                    initializer.setEndTime(value);
                } else {
                    confirmedSettings.setText("End time is not a valid value, for example: 1995-05-01T10:15:30.00Z");
                    confirmSettingsButton.setEnabled(true);
                    return;
                }

                initializer.build();
                dispatchEvent(new WindowEvent(SettingsParser.this, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
}

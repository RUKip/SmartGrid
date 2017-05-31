package com.rug.energygrid.UI;

import com.rug.energygrid.agents.time.GlobalTimeAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsPage extends JFrame{
    private JButton startSimulationButton;
    private JButton advancedOptionsButton;
    private JProgressBar progressBar1;
    private JPanel panel;
    private JButton confirmSettingsButton;
    private JLabel settingsConfirmed;

    private JTextField endTimeField;
    private JTextField startTimeField;
    private JTextField speedupField;

    private Timer t;

    private GlobalTimeAgent globaltimeAgent;

    private static int TIMER_INTERVAL=1000; //in milliseconds, so 1 second
    private static int DEFAULT_FRAME_WIDTH = 500;
    private static int DEFAULT_FRAME_HEIGHT = 500;

    public SettingsPage(GlobalTimeAgent globaltimeAgent) {
        super("Smartgrid Simulation");

        this.globaltimeAgent = globaltimeAgent;
        setContentPane(this.panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);

        advancedOptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: here add/show the options to change constants like constant pricing, L from the cable energyLoss etc.
            }
        });
        advancedOptionsButton.setEnabled(false);//TODO: remove when button is implemented

        startSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulationButton.setEnabled(false);
                globaltimeAgent.sendGlobalTime();
                t.start();
            }
        });


        progressBar1.setValue(0);
        progressBar1.setStringPainted(true);
        Duration realSimulationDuration = Duration.between(globaltimeAgent.getStartSimulationTime(), globaltimeAgent.getEndSimulationTime()).dividedBy(globaltimeAgent.getSpeedup());
        progressBar1.setMinimum(0);
        progressBar1.setMaximum((int) realSimulationDuration.getSeconds());
        t = new Timer(TIMER_INTERVAL, new ActionListener() {
            int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(i < (int) realSimulationDuration.getSeconds()){
                    i++;
                    progressBar1.setValue(i);
                }else{
                    t.stop();
                }
            }
        });

        confirmSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer speedup =  Integer.getInteger(speedupField.getText());


                String value;
                value = startTimeField.getText();
                Pattern p = Pattern.compile("[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]T[0-9][0-9]\\:[0-9][0-9]\\:[0-9][0-9]\\.[0-9][0-9]Z");
                Matcher m = p.matcher(value);
                if(m.matches()){
                    LocalDate localDate = LocalDate.parse(value);
                    LocalDateTime localDateTime = localDate.atStartOfDay();
                    Instant startTime = localDateTime.toInstant(ZoneOffset.UTC);
                }else{
                    settingsConfirmed.setText("This is not a valid value, for example: 1995-01-01T10:15:30.00Z");
                    return;
                }
                value = endTimeField.getText();
                m = p.matcher(value);
                if(m.matches()){
                    LocalDate localDate = LocalDate.parse(value);
                    LocalDateTime localDateTime = localDate.atStartOfDay();
                    Instant endTime = localDateTime.toInstant(ZoneOffset.UTC);
                }else{
                    settingsConfirmed.setText("This is not a valid value, for example: 1995-01-01T10:15:30.00Z");
                    return;
                }




                //TODO: Call the intializer with the new variables(so create new json)
                //TODO: for each agent call the PARSEJSON fucntion
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}

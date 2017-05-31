package com.rug.energygrid.UI;

import com.rug.energygrid.agents.time.GlobalTimeAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;

public class SettingsPage extends JFrame{
    private JButton startSimulationButton;
    private JButton advancedOptionsButton;
    private JProgressBar progressBar1;
    private JPanel panel;
    private JButton confirmSettingsButton;
    private JLabel settingsConfirmed;
    private JFormattedTextField endTimeField;
    private JFormattedTextField startTimeField;
    private JFormattedTextField speedupField;

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

            }
        });

        startSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulationButton.setEnabled(false);
                globaltimeAgent.sendGlobalTime();
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
                Integer speedup = (Integer) speedupField.getValue();


                //TODO: Call the intializer with the new functions
                //TODO: for each agent call the PARSEJSON fucntion
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


}

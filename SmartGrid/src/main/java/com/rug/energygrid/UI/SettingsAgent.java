package com.rug.energygrid.UI;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.energyProducers.EnergyProducer;
import com.rug.energygrid.parser.AgentDeserializer;
import com.rug.energygrid.parser.JSON_Grid_Deserializer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class SettingsAgent extends JPanel implements ListSelectionListener {
    private JList cableList;
    private JList agentList;
    private DefaultListModel cableListModel, agentListModel;
    private JSON_Grid_Deserializer deserializer;
    private AgentDeserializer agentDeserializer;

    public SettingsAgent(JSON_Grid_Deserializer deserializer, AgentDeserializer agentDeserializer){
        super(new BorderLayout());
        this.deserializer = deserializer;
        this.agentDeserializer = agentDeserializer;


        cableListModel = new DefaultListModel();
        for(Cable cable : deserializer.getCables()){
            cableListModel.addElement(cable.getOriginNode() + " connected to " + cable.getConnectedNode() + " with cost " + cable.getCost() + " and length " + cable.getLength());
        }

        agentListModel = new DefaultListModel();
        for(String agent : agentDeserializer.getAgentList()) {
            for (EnergyProducer energyProducer : deserializer.getEnergyProducers(agent)) {
                agentListModel.addElement(agent + " has energy producer " + energyProducer.getName());
            }
        }

        //Create the cableList and put it in a scroll pane.
        cableList = new JList(cableListModel);
        cableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cableList.setSelectedIndex(0);
        cableList.addListSelectionListener(this);
        cableList.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(cableList);

        //Create the cableList and put it in a scroll pane.
        agentList = new JList(agentListModel);
        agentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        agentList.setSelectedIndex(0);
        agentList.addListSelectionListener(this);
        agentList.setVisibleRowCount(5);
        JScrollPane listScrollPane2 = new JScrollPane(agentList);

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.EAST);
        add(listScrollPane2, BorderLayout.WEST);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}

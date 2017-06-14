package com.rug.energygrid.agents.prosumerAgent.shortestPathAlgorithm;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.logging.LocalLogger;
import com.rug.energygrid.parser.AgentDeserializer;
import jade.util.Logger;

import java.util.*;

public class ShortestPath {


    public static Logger logger = LocalLogger.getLogger();
    private AgentDeserializer agentDeseriaizer = new AgentDeserializer();

    private HashMap<String, Node> createGraph(List<Cable> cables){
        HashMap<String, Node> graph = new HashMap<String, Node>(); //this is the graph of each agent and there connected cost

        List<String> prosumerAgents = agentDeseriaizer.getAgentList();
        for(String agent : prosumerAgents){
            graph.put(agent, new Node(agent));
        }

        for(Cable c : cables){
            if(graph.get(c.getOriginNode()) == null){
                logger.warning("ai caramba, cable contains an non existing agent/node");
                continue;
            }
            Node node1 = graph.get(c.getOriginNode());
            Node node2 = graph.get(c.getConnectedNode());

            node1.addConnection(graph.get(node2.getName()), c.getCost(), c.getLength());
            graph.put(c.getOriginNode(), node1);

            node2.addConnection(graph.get(node1.getName()), c.getCost(), c.getLength());
            graph.put(c.getConnectedNode(), node2);
        }

        return graph;
    }

    //fills the routingTable
    public GraphTuple calcShortestPath(String startingNode, List<Cable> cables){
        HashMap<String, Node> graph = createGraph(cables);
        PriorityQueue<Node> unvisitedNodes = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(o1.getCost()>o2.getCost()){
                    return 1;
                } else if(o1.getCost()<o2.getCost()){
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        for(Map.Entry<String, Node> entry : graph.entrySet()) {
            if(!entry.getValue().getName().equals(startingNode)){
                unvisitedNodes.add(entry.getValue());
            }
        }

        if(graph.isEmpty()){
            logger.warning("There no node in the graph at all, for the shortest path we need at least one node");
            return null;
        }

        //We use Dijkstra's algorithm, because its one of the faster path algorithm which has a reasonable understandable implementation, also we want shortest path to all.
        //step 1 set starting node to 0
        Node sNode = graph.get(startingNode);
        sNode.setCost(0.0);
        sNode.setLength(0.0);
        graph.put(startingNode, sNode);
        unvisitedNodes.add(sNode);

        //step 2, setting current node will be the first poll because cost is 0.0
        Node currentNode;

        //step 3 calculate the shortest distance compared to all the neighbours(start of loop)

        while((currentNode = unvisitedNodes.poll()) != null) {
            currentNode.setVisited(true);       //here the current node is visited
            graph.put(currentNode.getName(), currentNode);
            for (Connection c : currentNode.getConnections()) {
                if(c.getConnectedNode() == null) continue;
                Node n = graph.get(c.getConnectedNode().getName());
                if ((!n.getVisited()) && (n.getCost() > currentNode.getCost() + c.getConnectionCost())) {
                    n.setCost(currentNode.getCost() + c.getConnectionCost());
                    n.setLength(currentNode.getLength() + c.getLength());
                    unvisitedNodes.add(n); //duplicates can be added but once visited all are visited an thus never become the new current node or updated.
                }

            }
        }

        //We just convert or calculated graph to one with only a double as cost
        HashMap<String,Double> finalGraph = new HashMap<>();
        HashMap<String,Double> lenghtGraph = new HashMap<>();
        for(Map.Entry<String, Node> entry : graph.entrySet()){
            String key = entry.getKey();
            Node value = entry.getValue();
            finalGraph.put(key, value.getCost());
            lenghtGraph.put(key, value.getLength());
        }
        return new GraphTuple(finalGraph, lenghtGraph);
    }

    private class Node{
        private String name;
        private Double cost, length;
        private boolean visited = false;
        private List<Connection> connected = new ArrayList<>();

        Node(String n){
            cost = Double.MAX_VALUE;
            name = n;
        }

        boolean getVisited(){
            return visited;
        }

        void setVisited(boolean b){
            this.visited = b;
        }

        public String getName(){
            return this.name;
        }

        Double getCost(){
            return cost;
        }

        void setCost(Double c){
            cost = c;
        }

        Double getLength(){return length;}

        void setLength(Double l){ length = l;}

        void addConnection(Node node, Double cost, Double length){
            connected.add(new Connection(node, cost, length));
        }

        List<Connection> getConnections(){
            return this.connected;
        }
    }

    private class Connection{
        private Node connectedTo;
        private Double connectionCost, length;

        Connection(Node node, Double cost, Double length){
            this.connectedTo = node;
            this.connectionCost = cost;
            this.length = length;
        }

        Node getConnectedNode(){
            return connectedTo;
        }
        Double getConnectionCost(){
            return connectionCost;
        }
        Double getLength(){return length;}
    }
}


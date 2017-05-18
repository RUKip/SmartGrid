package com.rug.energygrid.agents.prosumerAgent.shortestPathAlgorithm;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;
import com.rug.energygrid.logging.LocalLogger;
import com.rug.energygrid.parser.AgentDeseriaizer;
import jade.util.Logger;

import java.util.*;

public class ShortestPath {


    public static Logger logger = LocalLogger.getLogger();
    private AgentDeseriaizer agentDeseriaizer = new AgentDeseriaizer();

    private HashMap<String, Node> createGraph(List<Cable> cables){
        HashMap<String, Node> graph = new HashMap<String, Node>(); //this is the graph of each agent and there connected cost

        //TODO: here get all AID's of the agents(the local names) and initialize the routingtable
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

            node1.addConnection(graph.get(node2.getName()), c.getCost());
            graph.put(c.getOriginNode(), node1);

            node2.addConnection(graph.get(node1.getName()), c.getCost());
            graph.put(c.getConnectedNode(), node2);
        }

        return graph;
    }

    //TODO: check for speed improvements
    //fills the routingTable
    public HashMap<String, Double> calcShortestPath(String startingNode, List<Cable> cables){
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
        }); //TODO: smallest node should be on top

        for(Map.Entry<String, Node> entry : graph.entrySet()) {
            if(!entry.getValue().getName().equals(startingNode)){
                unvisitedNodes.add(entry.getValue());
            }else{
                //logger.info("I did not insert agent: " + entry.getValue().getName()); //TODO: remove debug
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
        graph.put(startingNode, sNode);
        unvisitedNodes.add(sNode);

        //step 2, setting current node will be the first poll because cost is 0.0
        Node currentNode;

        //step 3 calculate the shortest distance compared to all the neighbours(start of loop)

        //TODO: test
        while((currentNode = unvisitedNodes.poll()) != null) {
            currentNode.setVisited(true);       //here the current node is visited
            graph.put(currentNode.getName(), currentNode);
            //logger.info("Set currentNode to: " + currentNode.getName()); //it seems to arrive at this point     TODO: remove this debug
            for (Connection c : currentNode.getConnections()) {
                //logger.info("Current connection cost is: " + c.getConnectionCost()); //TODO: remove this debug
                //logger.info("Current node" + currentNode.getName() + " is connected to: " + c.getConnectedNode().getName()); //TODO: remove debug
                if(c.getConnectedNode() == null) continue;
                Node n = graph.get(c.getConnectedNode().getName());
                //logger.info("currentNode cost is: " + currentNode.getCost() + " and other node cost: " + n.getCost()); //TODO: remove this debug
                if ((!n.getVisited()) && (n.getCost() > currentNode.getCost() + c.getConnectionCost())) {
                    //logger.info("Found a smaller cost for node: " + n.getName() + " new cost= " + (currentNode.getCost()+c.getConnectionCost())); //TODO: remove this debug
                    n.setCost(currentNode.getCost() + c.getConnectionCost());
                    unvisitedNodes.add(n); //duplicates can be added but once visited all are visited an thus never become the new current node or updated.
                }

            }
        }

        //We just convert or calculated graph to one with only a double as cost
        HashMap<String,Double> finalGraph = new HashMap<>();
        for(Map.Entry<String, Node> entry : graph.entrySet()){
            finalGraph.put(entry.getKey(), entry.getValue().getCost());
            //logger.info("For one Agent to" +  entry.getKey() + " this is the final cost: " + entry.getValue().getCost());//TODO: remove this debug
        }
        return finalGraph;
    }

    private class Node{
        private String name;
        private Double cost;
        private boolean visited = false;
        private List<Connection> connected = new ArrayList<>();

        public Node(String n){
            cost = Double.MAX_VALUE;
            name = n;
        }

        public boolean getVisited(){
            return visited;
        }

        public void setVisited(boolean b){
            this.visited = b;
        }

        public String getName(){
            return this.name;
        }

        public Double getCost(){
            return cost;
        }

        public void setCost(Double c){
            cost = c;
        }

        public void addConnection(Node node, Double cost){
            connected.add(new Connection(node, cost));
        }

        public List<Connection> getConnections(){
            return this.connected;
        }
    }

    private class Connection{
        private Node connectedTo;
        private Double connectionCost;

        public Connection(Node node, Double cost){
            this.connectedTo = node;
            this.connectionCost = cost;
        }

        public Node getConnectedNode(){
            return connectedTo;
        }
        public Double getConnectionCost(){
            return connectionCost;
        }
    }
}


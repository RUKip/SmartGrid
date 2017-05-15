package com.rug.energygrid.agents.prosumerAgent.shortestPathAlgorithm;

import com.rug.energygrid.agents.prosumerAgent.buysellEnergy.buyEnergy.Cable;

import java.util.*;

/**
 * Created by Ruben on 11-May-17.
 */
public class ShortestPath {


    private HashMap<String, Node> createGraph(List<Cable> cables){
        HashMap<String, Node> graph = new HashMap(); //this is the graph of each agent and there connected cost

        //TODO: here get all AID's of the agents(the local names) and initialize the routingtable
        List<String> prosumerAgents = new ArrayList<>(); //TODO: fill agents here
        for(String agent : prosumerAgents){
            graph.put(agent, new Node(agent));
        }

        for(Cable c : cables){
            Node node = graph.get(c.getOriginNode());
            node.addConnection(graph.get(node.getName()), c.getCost());
            graph.put(c.getOriginNode(), node);

            node = graph.get(c.getConnectedNode());
            node.addConnection(graph.get(node.getName()), c.getCost());
            graph.put(c.getConnectedNode(), node);
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
            unvisitedNodes.add(entry.getValue());
        }

        //We use Dijkstra's algorithm, because its one of the faster path algorithm which has a reasonable understandable implementation, also we want shortest path to all.
        //step 1 set starting node to 0
        Node sNode = graph.get(startingNode);
        sNode.setCost(0.0);
        graph.put(startingNode, sNode);

        //step 2, setting current node will be the first poll because cost is 0.0
        Node currentNode;

        //step 3 calculate the shortest distance compared to all the neighbours(start of loop)

        //TODO: test
        while((currentNode = unvisitedNodes.poll()) != null) {
            currentNode.setVisited(true);       //here the current node is visited
            graph.put(currentNode.getName(), currentNode);
            for (Connection c : currentNode.getConnections()) {
                if(c.getConnectedNode() == null) continue;
                Node n = graph.get(c.getConnectedNode().getName());
                if ((!n.getVisited()) && (n.getCost() > currentNode.getCost() + c.getConnectionCost())) {
                    n.setCost(currentNode.getCost() + c.getConnectionCost());
                    unvisitedNodes.add(n); //duplicates can be added but once visited all are visited an thus never become the new current node or updated.
                }

            }
        }

        //We just convert or calculated graph to one with only a double as cost
        HashMap<String,Double> finalGraph = new HashMap<>();
        for(Map.Entry<String, Node> entry : graph.entrySet()){
            finalGraph.put(entry.getKey(), entry.getValue().getCost());
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


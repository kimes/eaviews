package ph.easyaf.eaviews.views.tree.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Graph {

    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    public Graph() {}

    public int getNodeCount() { return nodes.size(); }

    public void addNode(Node node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public void addNodes(ArrayList<Node> nodes) {
        for (Node node : nodes) {
            addNode(node);
        }
    }

    public void removeNode(Node node) throws Exception {
        if (!nodes.contains(node)) throw new IllegalArgumentException("Unable to find node in graph");

        for (Node n : successorsOf(node)) {
            removeNode(n);
        }

        nodes.remove(node);

        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.getSource() == node || edge.getDestination() == node) {
                iterator.remove();
            }
        }
    }

    public void removeNodes(ArrayList<Node> nodes) throws Exception {
        for (Node node : nodes) {
            removeNode(node);
        }
    }

    public void addEdge(Edge edge) {
        addNode(edge.getSource());
        addNode(edge.getDestination());

        if (!edges.contains(edge)) {
            edges.add(edge);
        }
    }

    public void addEdge(Node source, Node destination) {
        Edge edge = new Edge(source, destination);
        addEdge(edge);
    }

    public void addEdges(ArrayList<Edge> edges) {
        for (Edge edge : edges) {
            addEdge(edge);
        }
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public void removeEdges(ArrayList<Edge> edges) {
        for (Edge edge : edges) {
            removeEdge(edge);
        }
    }

    public void removeEdge(Node predecessor, Node current) {
        edges.removeIf(edge -> edge.getSource() == predecessor && edge.getDestination() == current);
    }

    public boolean hasNodes() { return !nodes.isEmpty(); }

    public Node getNodeAtPosition(int position) throws Exception {
        if (position < 0) throw new IllegalArgumentException("Position can't be negative");

        int size = nodes.size();
        if (position >= size)
            new IndexOutOfBoundsException(String.format("Position: %d, Size: %d", position, size));

        return nodes.get(position);
    }

    public Edge getEdgeBetween(Node source, Node destination) {
        Edge retEdge = null;
        for (Edge edge : edges) {
            if (edge.getSource() == source && edge.getDestination() == destination) {
                retEdge = edge;
                break;
            }
        }
        return retEdge;
    }

    public boolean hasSuccessor(Node node) {
        return edges.stream().anyMatch(edge -> edge.getSource() == node);
    }

    public ArrayList<Node> successorsOf(Node node) {
        return (ArrayList<Node>)edges.stream()
                .filter(edge -> edge.getSource() == node)
                .map(edge -> edge.getDestination())
                .collect(Collectors.toList());
    }

    public boolean hasPredecessor(Node node) {
        return edges.stream().anyMatch(edge -> edge.getDestination() == node);
    }

    public ArrayList<Node> predecessorsOf(Node node) {
        return (ArrayList<Node>)edges.stream()
                .filter(edge -> edge.getDestination() == node)
                .map(edge -> edge.getSource())
                .collect(Collectors.toList());
    }

    public boolean contains(Node node) { return nodes.contains(node); }
    public boolean contains(Edge edge) { return edges.contains(edge); }

    public boolean containsData(Object data) {
        return nodes.stream().anyMatch(node -> node.getData() == data);
    }

    public Node getNodeAtPosition(Object data) {
        Node retNode = null;
        for (Node node : nodes) {
            if (node.getData() == data) {
                retNode = node;
                break;
            }
        }
        return retNode;
    }

    public ArrayList<Edge> getOutEdges(Node node) {
        return (ArrayList<Edge>)edges.stream()
                .filter(edge -> edge.getSource() == node)
                .collect(Collectors.toList());
    }

    public ArrayList<Edge> getInEdges(Node node) {
        return (ArrayList<Edge>)edges.stream()
                .filter(edge -> edge.getDestination() == node)
                .collect(Collectors.toList());
    }

    public ArrayList<Node> getNodes() { return nodes; }
    public ArrayList<Edge> getEdges() { return edges; }
}

package ph.easyaf.eaviews.views.tree.models;

public class Edge {

    private Node source, destination;

    public Edge(Node source, Node destination) {
        this.source = source;
        this.destination = destination;
    }

    public Node getSource() { return source; }
    public Node getDestination() { return destination; }
    public void setSource(Node source) { this.source = source; }
    public void setDestination(Node destination) { this.destination = destination; }
}

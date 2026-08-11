package ph.easyaf.eaviews.views.tree.layouts;

import ph.easyaf.eaviews.views.tree.models.Node;

public class BuchheimWalkerNodeData {

    private Node ancestor;
    private Node thread;

    private int number = 0, depth = 0;

    private double prelim = 0d, modifier = 0d, shift = 0d, change = 0d;

    public BuchheimWalkerNodeData() {}

    public Node getAncestor() { return ancestor; }
    public Node getThread() { return thread; }
    public int getNumber() { return number; }
    public int getDepth() { return depth; }
    public double getPrelim() { return prelim; }
    public double getModifier() { return modifier; }
    public double getShift() { return shift; }
    public double getChange() { return change; }

    public void setAncestor(Node ancestor) {
        this.ancestor = ancestor;
    }

    public void setThread(Node thread) { this.thread = thread; }
    public void setNumber(int number) { this.number = number; }
    public void setDepth(int depth) { this.depth = depth; }
    public void setPrelim(double prelim) { this.prelim = prelim; }
    public void setModifier(double modifier) { this.modifier = modifier; }
    public void setShift(double shift) { this.shift = shift; }
    public void setChange(double change) { this.change = change; }
}

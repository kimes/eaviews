package ph.easyaf.eaviews.views.tree.layouts;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import ph.easyaf.eaviews.views.tree.models.Graph;
import ph.easyaf.eaviews.views.tree.models.Node;
import ph.easyaf.eaviews.views.tree.utils.Size;
import ph.easyaf.eaviews.views.tree.utils.VectorF;

public class BuchheimWalkerLayoutManager extends GraphLayoutManager {

    public static final int
            SIBLING_SEPARATION = 400,
            LEVEL_SEPARATION = 50,
            SUBTREE_SEPARATION = 400;

    private HashMap<Node, BuchheimWalkerNodeData> nodeData = new HashMap<>();

    private int minNodeWidth = Integer.MAX_VALUE, minNodeHeight = Integer.MAX_VALUE,
        maxNodeWidth = Integer.MIN_VALUE, maxNodeHeight = Integer.MIN_VALUE;

    public BuchheimWalkerLayoutManager(Context context) {
        super(context);
    }

    @Override
    public Size run(Graph graph, float shiftX, float shiftY) {
        nodeData.clear();
        //graph.setAsTree();

        try {
            Node firstNode = graph.getNodeAtPosition(0);

            firstWalk(graph, firstNode, 0, 0);

            secondWalk(graph, firstNode, 0.0);

            positionNodes(graph);

            shiftCoordinates(graph, shiftX, shiftY);

            return calculateGraphSize(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Size(0, 0);
    }

    private int compare(int x, int y) {
        int c = 1;
        if (x < y) { c = -1; }
        else if (x == y) { c = 0; }
        else c = 1;
        return c;
    }

    private BuchheimWalkerNodeData createNodeData(Node node) {
        BuchheimWalkerNodeData nodeData = new BuchheimWalkerNodeData();
        nodeData.setAncestor(node);

        this.nodeData.put(node, nodeData);
        return nodeData;
    }

    private BuchheimWalkerNodeData getNodeData(Node node) {
        return nodeData.get(node);
    }

    private void firstWalk(Graph graph, Node node, int depth, int number) {
        BuchheimWalkerNodeData nodeData = createNodeData(node);
        nodeData.setDepth(depth);
        nodeData.setNumber(number);

        minNodeWidth = Math.min(minNodeWidth, node.getSize().getWidth());
        minNodeHeight = Math.min(minNodeHeight, node.getSize().getHeight());
        maxNodeWidth = Math.max(maxNodeWidth, node.getSize().getWidth());
        maxNodeHeight = Math.max(maxNodeHeight, node.getSize().getHeight());

        if (isLeaf(graph, node)) {
            if (hasLeftSibling(graph, node)) {
                Node leftSibling = getLeftSibling(graph, node);
                nodeData.setPrelim(getPrelim(leftSibling) + getSpacing(graph, leftSibling, node));
                //nodeData.setPrelim(getPrelim)
            }
        } else {
            Node leftMost = getLeftMostChild(graph, node);
            Node rightMost = getRightMostChild(graph, node);
            Node defaultAncestor = leftMost;

            Node next = leftMost;

            int i = 1;
            while (next != null) {
                firstWalk(graph, next, depth + 1, i++);
                defaultAncestor = apportion(graph, next, defaultAncestor);

                next = getRightSibling(graph, next);
            }

            executeShifts(graph, node);

            boolean isVertical = true;
            double midPoint = 0.5 * ((getPrelim(leftMost) + getPrelim(rightMost) +
                    (isVertical ? rightMost.getSize().getWidth() : rightMost.getSize().getHeight()) -
                    (isVertical ? node.getSize().getWidth() : node.getSize().getHeight())));

            if (hasLeftSibling(graph, node)) {
                Node leftSibling = getLeftSibling(graph, node);
                nodeData.setPrelim(getPrelim(leftSibling) + getSpacing(graph, leftSibling, node));
                nodeData.setModifier(nodeData.getPrelim() - midPoint);
            } else {
                nodeData.setPrelim(midPoint);
            }
        }
    }

    private void secondWalk(Graph graph, Node node, double modifier) {
        BuchheimWalkerNodeData nodeData = getNodeData(node);
        int depth = nodeData.getDepth();

        boolean vertical = true;
        node.setPosition(new VectorF(
                (float)(nodeData.getPrelim() + modifier),
                (float)(depth * (vertical ? minNodeHeight : minNodeWidth) + depth * LEVEL_SEPARATION)));

        if (node.getData() == "Child 2") {
            System.out.println("Position X: " + (nodeData.getPrelim() + modifier));
            //node.getPosition().setX(400);
        }

        ArrayList<Node> successors = graph.successorsOf(node);
        for (Node w : successors) {
            secondWalk(graph, w, modifier + nodeData.getModifier());
        }
    }

    public Size calculateGraphSize(Graph graph) {
        int left = Integer.MAX_VALUE, top = Integer.MAX_VALUE,
            right = Integer.MIN_VALUE, bottom = Integer.MIN_VALUE;

        for (Node node : graph.getNodes()) {
            left = (int)Math.min((float)left, node.getPosition().getX());
            top = (int)Math.min((float)top, node.getPosition().getY());
            right = (int)Math.max((float)right, node.getPosition().getX() + node.getSize().getWidth());
            bottom = (int)Math.max((float)bottom, node.getPosition().getY() + node.getSize().getHeight());
        }

        return new Size(right - left, bottom - top);
    }

    public void executeShifts(Graph graph, Node node) {
        double shift = 0.0;
        double change = 0.0;
        Node w = getRightMostChild(graph, node);
        while (w != null) {
            BuchheimWalkerNodeData nodeData = getNodeData(w);

            nodeData.setPrelim(nodeData.getPrelim() + shift);
            nodeData.setModifier(nodeData.getModifier() + shift);

            change += nodeData.getChange();
            shift += nodeData.getShift() + change;

            w = getLeftSibling(graph, w);
        }
    }

    public Node apportion(Graph graph, Node node, Node defaultAncestor) {
        Node ancestor = defaultAncestor;
        if (hasLeftSibling(graph, node)) {
            Node leftSibling = getLeftSibling(graph, node);

            Node vip = node;
            Node vop = node;
            Node vim = leftSibling;
            Node vom = getLeftMostChild(graph, graph.predecessorsOf(vip).get(0));

            double sip = getModifier(vip);
            double sop = getModifier(vop);
            double sim = getModifier(vim);
            double som = getModifier(vom);

            Node nextRight = nextRight(graph, vim);
            Node nextLeft = nextLeft(graph, vip);

            while (nextRight != null && nextLeft != null) {
                vim = nextRight;
                vip = nextLeft;
                vom = nextLeft(graph, vom);
                vop = nextRight(graph, vop);

                setAncestor(vop, node);

                double shift = getPrelim(vim) + sim - (getPrelim(vip) + sip) + getSpacing(graph, vim, node);
                if (shift > 0) {
                    moveSubtree(ancestor(graph, vim, node, ancestor), node, shift);
                    sip += shift;
                    sop += shift;
                }

                sim += getModifier(vim);
                sip += getModifier(vip);
                som += getModifier(vom);
                sop += getModifier(vop);

                nextRight = nextRight(graph, vim);
                nextLeft = nextLeft(graph, vip);
            }

            if (nextRight != null && nextRight(graph, vop) == null) {
                setThread(vop, nextRight);
                setModifier(vop, getModifier(vop) + sim - sop);
            }

            if (nextLeft != null && nextLeft(graph, vom) == null) {
                setThread(vom, nextLeft);
                setModifier(vom, getModifier(vom) + sip - som);
                ancestor = node;
            }
        }

        return ancestor;
    }

    private void setAncestor(Node v, Node ancestor) {
        getNodeData(v).setAncestor(ancestor);
    }

    private void setModifier(Node v, double modifier) {
        getNodeData(v).setModifier(modifier);
    }

    private void setThread(Node v, Node thread) {
        getNodeData(v).setThread(thread);
    }

    private double getPrelim(Node v) {
        return getNodeData(v).getPrelim();
    }

    private double getModifier(Node vip) {
        return getNodeData(vip).getModifier();
    }

    private void moveSubtree(Node wm, Node wp, double shift) {
        BuchheimWalkerNodeData wpNodeData = getNodeData(wp);
        BuchheimWalkerNodeData wmNodeData = getNodeData(wm);

        int subtrees = wpNodeData.getNumber() - wmNodeData.getNumber();
        wpNodeData.setChange(wpNodeData.getChange() - shift / subtrees);
        wpNodeData.setShift(wpNodeData.getShift() + shift);
        wmNodeData.setChange(wmNodeData.getChange() + shift / subtrees);
        wpNodeData.setPrelim(wpNodeData.getPrelim() + shift);
        wpNodeData.setModifier(wpNodeData.getModifier() + shift);
    }

    private Node ancestor(Graph graph, Node vim, Node node, Node defaultAncestor) {
        BuchheimWalkerNodeData vipNodeData = getNodeData(vim);

        Node ancestor = defaultAncestor;
        if (graph.predecessorsOf(vipNodeData.getAncestor()).get(0) == graph.predecessorsOf(node).get(0)) {
            ancestor = vipNodeData.getAncestor();
        }
        return ancestor;
    }

    private Node nextRight(Graph graph, Node node) {
        return graph.hasSuccessor(node) ? getRightMostChild(graph, node) : getNodeData(node).getThread();
    }

    private Node nextLeft(Graph graph, Node node) {
        return graph.hasSuccessor(node) ? getLeftMostChild(graph, node) : getNodeData(node).getThread();
    }

    private int getSpacing(Graph graph, Node leftNode, Node rightNode) {
        int separation = SUBTREE_SEPARATION;

        if (isSibling(graph, leftNode, rightNode)) {
            separation = SIBLING_SEPARATION;
        }

        boolean vertical = true;

        return separation + leftNode.getSize().getHeight();
    }

    private boolean isSibling(Graph graph, Node leftNode, Node rightNode) {
        Node leftParent = graph.predecessorsOf(leftNode).get(0);
        return graph.successorsOf(leftParent).contains(rightNode);
    }

    private boolean isLeaf(Graph graph, Node node) {
        return graph.successorsOf(node).isEmpty();
    }

    private Node getLeftSibling(Graph graph, Node node) {
        if (!hasLeftSibling(graph, node)) return null;

        Node parent = graph.predecessorsOf(node).get(0);
        ArrayList<Node> children = graph.successorsOf(parent);
        int nodeIndex = children.indexOf(node);
        return children.get(nodeIndex - 1);
    }

    private boolean hasLeftSibling(Graph graph, Node node) {
        ArrayList<Node> parents = graph.predecessorsOf(node);
        if (parents.isEmpty()) return false;

        Node parent = parents.get(0);
        int nodeIndex = graph.successorsOf(parent).indexOf(node);
        return nodeIndex > 0;
    }

    private Node getRightSibling(Graph graph, Node node) {
        if (!hasRightSibling(graph, node)) return null;

        Node parent = graph.predecessorsOf(node).get(0);
        ArrayList<Node> children = graph.successorsOf(parent);
        int nodeIndex = children.indexOf(node);
        return children.get(nodeIndex + 1);
    }

    private boolean hasRightSibling(Graph graph, Node node) {
        ArrayList<Node> parents =graph.predecessorsOf(node);
        if (parents.isEmpty()) return false;

        Node parent = parents.get(0);
        ArrayList<Node> children = graph.successorsOf(parent);
        int nodeIndex = children.indexOf(node);
        return nodeIndex < children.size() - 1;
    }

    private Node getLeftMostChild(Graph graph, Node node) {
        return graph.successorsOf(node).get(0);
    }

    private Node getRightMostChild(Graph graph, Node node) {
        ArrayList<Node> children = graph.successorsOf(node);
        return children.isEmpty() ? null : children.get(children.size() - 1);
    }

    private void positionNodes(Graph graph) {
        int globalPadding = 0, localPadding = 0;
        VectorF offset = getOffset(graph);

        ArrayList<Node> nodes = sortByLevel(graph, false);

        int firstLevel = getNodeData(nodes.get(0)).getDepth();
        Size localMaxSize = findMaxSize(filterByLevel(nodes, firstLevel));
        int currLevel = 0;

        for (Node node : nodes) {
            int depth = getNodeData(node).getDepth();
            if (depth != currLevel) {
                globalPadding += localPadding;

                localPadding = 0;
                currLevel = depth;

                localMaxSize = findMaxSize(filterByLevel(nodes, currLevel));
            }

            int width = node.getSize().getWidth(), height = node.getSize().getHeight();

            if (height > minNodeHeight) {
                int diff = height - minNodeHeight;
                localPadding = Math.max(localPadding, diff);
            }

            node.setPosition(getPosition(node, globalPadding, offset));
        }
    }

    private void shiftCoordinates(Graph graph, float shiftX, float shiftY) {
        for (Node node : graph.getNodes()) {
            node.setPosition(new VectorF(node.getPosition().getX() + shiftX, node.getPosition().getY() + shiftY));
        }
    }

    private Size findMaxSize(ArrayList<Node> nodes) {
        int width = Integer.MIN_VALUE, height = Integer.MIN_VALUE;

        for (Node node : nodes) {
            width = Math.max(width, node.getSize().getWidth());
            height = Math.max(height, node.getSize().getHeight());
        }

        return new Size(width, height);
    }

    private VectorF getOffset(Graph graph) {
        float offsetX = Float.MAX_VALUE, offsetY = Float.MAX_VALUE;

        for (Node node : graph.getNodes()) {
            offsetX = Math.min(offsetX, node.getPosition().getX());
            offsetY = Math.min(offsetY, node.getPosition().getY());
        }
        return new VectorF(offsetX, offsetY);
    }

    private VectorF getPosition(Node node, int globalPadding, VectorF offset) {
        return new VectorF(node.getPosition().getX() - offset.getX(), node.getPosition().getY() + globalPadding);
    }

    private ArrayList<Node> sortByLevel(Graph graph, boolean descending) {
        ArrayList<Node> nodes = new ArrayList<>(graph.getNodes());

        Comparator<Node> comparator = (o1, o2) -> {
            BuchheimWalkerNodeData data1 = getNodeData(o1);
            BuchheimWalkerNodeData data2 = getNodeData(o2);
            return compare(data1.getDepth(), data2.getDepth());
        };

        if (descending) { comparator = Collections.reverseOrder(comparator); }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            nodes.sort(comparator);
        }

        return nodes;
    }

    private ArrayList<Node> filterByLevel(ArrayList<Node> nodes, int level) {
        ArrayList<Node> nodeList = new ArrayList<>(nodes);

        Iterator<Node> iterator = nodeList.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            int depth = getNodeData(node).getDepth();
            if (depth != level) iterator.remove();
        }

        return nodeList;
    }
}

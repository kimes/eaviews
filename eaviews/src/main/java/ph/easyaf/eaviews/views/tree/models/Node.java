package ph.easyaf.eaviews.views.tree.models;

import ph.easyaf.eaviews.views.tree.utils.Size;
import ph.easyaf.eaviews.views.tree.utils.VectorF;

public class Node {

    private VectorF position = new VectorF();
    private Size size = new Size();

    private Object data;

    public Node(Object data) {
        this.data = data;
    }

    public VectorF getPosition() { return position; }
    public Size getSize() { return size; }
    public Object getData() { return data; }

    public void setPosition(VectorF position) { this.position = position; }
    public void setSize(Size size) { this.size = size; }
    public void setData(Object data) { this.data = data; }
}

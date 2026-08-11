package ph.easyaf.eaviews.views.tree;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import ph.easyaf.eaviews.views.tree.models.Graph;
import ph.easyaf.eaviews.views.tree.models.Node;

public abstract class AbstractGraphAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected Graph graph = null;

    @Override
    public int getItemCount() {
        return graph.getNodeCount();
    }

    public Node getNode(int position) throws Exception {
        return graph.getNodeAtPosition(position);
    }

    public Object getNodeData(int position) throws Exception {
        return graph.getNodeAtPosition(position).getData();
    }

    /**
     * Submits a new graph to be displayed
     *
     * If a graph is already being displayed, you need to dispatch Adapter.notifyItem.
     *
     * @param graph
     */
    public void submitGraph(@Nullable Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() { return graph; }
}

package ph.easyaf.eaviews.views.tree.layouts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.easyaf.eaviews.views.tree.AbstractGraphAdapter;
import ph.easyaf.eaviews.views.tree.models.Graph;
import ph.easyaf.eaviews.views.tree.models.Node;

public class TreeEdgeDecoration extends RecyclerView.ItemDecoration {

    private Paint linePaint = new Paint();
    private Path linePath = new Path();

    public TreeEdgeDecoration() {
        linePaint.setStrokeWidth(5f);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setPathEffect(new CornerPathEffect(10f));
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        // super.onDraw(c, parent, state);

        if (parent.getLayoutManager() == null || parent.getAdapter() == null) return;
        if (!(parent.getAdapter() instanceof AbstractGraphAdapter))
            throw new RuntimeException("TreeEdgeDecoration only works with " + AbstractGraphAdapter.class.getSimpleName());

        AbstractGraphAdapter adapter = (AbstractGraphAdapter)parent.getAdapter();

        if (!(parent.getLayoutManager() instanceof BuchheimWalkerLayoutManager))
            throw new RuntimeException("TreeEdgeDecoration only works with + " + BuchheimWalkerLayoutManager.class.getSimpleName());

        BuchheimWalkerLayoutManager layout = (BuchheimWalkerLayoutManager)parent.getLayoutManager();

        Graph graph = adapter.getGraph();
        if (graph != null && graph.hasNodes()) {
            ArrayList<Node> nodes = graph.getNodes();

            for (Node node : nodes) {
                ArrayList<Node> children = graph.successorsOf(node);

                for (Node child : children) {
                    linePath.reset();

                    linePath.moveTo(child.getPosition().getX() + child.getSize().getWidth() / 2f,
                            child.getPosition().getY());
                    linePath.lineTo(child.getPosition().getX() + child.getSize().getWidth() / 2f,
                            child.getPosition().getY() - BuchheimWalkerLayoutManager.LEVEL_SEPARATION / 2f);

                    linePath.lineTo(node.getPosition().getX() + node.getSize().getWidth() / 2f,
                            child.getPosition().getY() - BuchheimWalkerLayoutManager.LEVEL_SEPARATION / 2f);

                    linePath.moveTo(node.getPosition().getX() + node.getSize().getWidth() / 2f,
                            child.getPosition().getY() - BuchheimWalkerLayoutManager.LEVEL_SEPARATION / 2f);

                    linePath.lineTo(node.getPosition().getX() + node.getSize().getWidth() / 2f,
                            node.getPosition().getY() + node.getSize().getHeight());

                    c.drawPath(linePath, linePaint);
                }
            }
        }
        super.onDraw(c, parent, state);
    }
}

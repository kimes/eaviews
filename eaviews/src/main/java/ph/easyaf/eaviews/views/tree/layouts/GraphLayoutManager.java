package ph.easyaf.eaviews.views.tree.layouts;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import ph.easyaf.eaviews.views.tree.AbstractGraphAdapter;
import ph.easyaf.eaviews.views.tree.models.Graph;
import ph.easyaf.eaviews.views.tree.models.Node;
import ph.easyaf.eaviews.views.tree.utils.Size;
import ph.easyaf.eaviews.views.tree.utils.VectorF;

public abstract class GraphLayoutManager extends RecyclerView.LayoutManager {

    private boolean useMaxSize = true;
    public boolean isUseMaxSize() { return useMaxSize; }
    public void setUseMaxSize(boolean useMaxSize) {
        this.useMaxSize = useMaxSize;
        requestLayout();
    }

    private AbstractGraphAdapter adapter = null;

    public GraphLayoutManager(Context context) {}

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAdapterChanged(@Nullable @org.jetbrains.annotations.Nullable RecyclerView.Adapter oldAdapter,
                                 @Nullable @org.jetbrains.annotations.Nullable RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);

        if (!(newAdapter instanceof AbstractGraphAdapter)) {
            throw new RuntimeException("GraphLayoutManager only works with " +
                    AbstractGraphAdapter.class.getSimpleName());
        }

        adapter = (AbstractGraphAdapter)newAdapter;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        try {
            positionItems(recycler, state.getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
        AbstractGraphAdapter adapter = this.adapter;
        if (adapter == null) {
            Log.e("GraphLayoutManager", "No adapter attached; skipping layout");
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }

        Graph graph = adapter.getGraph();
        if (graph == null || !graph.hasNodes()) {
            Log.e("GraphLayoutManager", "No graph set; skipping layout");
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }

        int maxWidth = 0, maxHeight = 0;

        for (int i = 0; i < state.getItemCount(); i++) {
            View child = recycler.getViewForPosition(i);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)child.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            addView(child);

            int childWidthSpec = makeMeasureSpec(params.width);
            int childHeightSpec = makeMeasureSpec(params.height);
            child.measure(childWidthSpec, childHeightSpec);

            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();

            try {
                Node node = adapter.getNode(i);
                node.setSize(new Size(measuredWidth, measuredHeight));
            } catch (Exception e) {
                e.printStackTrace();
            }

            maxWidth = Math.max(maxWidth, measuredWidth);
            maxHeight = Math.max(maxHeight, measuredHeight);
        }

        if (useMaxSize) {
            detachAndScrapAttachedViews(recycler);

            for (int i = 0; i < state.getItemCount(); i++) {
                View child = recycler.getViewForPosition(i);

                addView(child);

                int childWidthSpec = makeMeasureSpec(maxWidth);
                int childHeightSpec = makeMeasureSpec(maxHeight);
                child.measure(childWidthSpec, childHeightSpec);

                try {
                    Node node = adapter.getNode(i);
                    node.setSize(new Size(child.getMeasuredWidth(), child.getMeasuredHeight()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Size size = run(graph, (float)getPaddingLeft(), (float)getPaddingTop());
        setMeasuredDimension(size.getWidth() + getPaddingRight() + getPaddingLeft(),
                size.getHeight() + getPaddingBottom() + getPaddingTop());
    }

    private void positionItems(RecyclerView.Recycler recycler, int itemCount) throws Exception {
        for (int i = 0; i < itemCount; i++) {
            View child = recycler.getViewForPosition(i);

            Node node = adapter.getNode(i);
            int width = node.getSize().getWidth();
            int height = node.getSize().getHeight();

            VectorF position = node.getPosition();

            addView(child);
            int childWidthSpec = makeMeasureSpec(width);
            int childHeightSpec = makeMeasureSpec(height);
            child.measure(childWidthSpec, childHeightSpec);

            int left = (int)position.getX();
            int top = (int)position.getY();
            int right = left + width;
            int bottom = top + height;

            child.layout(left, top, right, bottom);
        }
    }

    public abstract Size run(Graph graph, float shiftX, float shiftY);

    private int makeMeasureSpec(int dimension) {
        if (dimension > 0) return View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.EXACTLY);
        else return View.MeasureSpec.UNSPECIFIED;
    }
}

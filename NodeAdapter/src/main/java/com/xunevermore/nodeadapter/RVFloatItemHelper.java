package com.xunevermore.nodeadapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVFloatItemHelper extends RecyclerView.OnScrollListener {

    private static final String TAG = "RVFloatItemHelper";
    private RecyclerView mRecyclerView;
    private NodeAdapter mAdapter;
    private ViewHolder mViewHolder;
    private View mFlowView;
    private int mGroupHeight = 0;
    private LinearLayoutManager mLayoutManager;
    private INodeItem mLastItem;

    public RVFloatItemHelper(RecyclerView recyclerView, NodeAdapter adapter) {
        this.mRecyclerView = recyclerView;
        this.mAdapter = adapter;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) layoutManager;
        }
    }

    public void refresh() {
        if (mLastItem != null && mViewHolder != null) {
            mLastItem.bindData(mViewHolder);
        }
    }

    /**
     * adapter 有数据时调用
     */
    public View initFloatView() {
        int layoutRes = mAdapter.getItemViewType(0);
        mViewHolder = mAdapter.onCreateViewHolder(mRecyclerView, layoutRes);
        List<INodeItem> list = mAdapter.getList();
        if (list.size() > 0) {
            INodeItem item = list.get(0);
            mLastItem = item;
            item.bindData(mViewHolder);
        }
        mFlowView = mViewHolder.itemView;
        return mFlowView;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mFlowView == null) {
            return;
        }
        if (mViewHolder == null) {
            return;
        }
        if (mAdapter == null) {
            return;
        }
        if (mLayoutManager == null) {
            return;
        }
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
        if (mGroupHeight == 0) {
            mGroupHeight = mFlowView.getMeasuredHeight();
        }
        INodeItem n = mAdapter.getList().get(firstVisibleItemPosition);
        INodeItem rootNode = n.getRootNode();
        if (mLastItem != rootNode) {
            rootNode.bindData(mViewHolder);
            mLastItem = rootNode;
        }
        mFlowView.setTranslationY(0);

        int poi = firstVisibleItemPosition + 1;
        if (poi <= lastVisibleItemPosition) {
            int childCount = recyclerView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = recyclerView.getChildAt(i);
                int position = recyclerView.getChildAdapterPosition(child);
                if (position == poi) {
                    INodeItem nodeItem = mAdapter.getList().get(position);
                    int top = child.getTop();
                    if (nodeItem != null && nodeItem.isRootNode() && top > 0 && top < mGroupHeight) {
                        mFlowView.setTranslationY(top - mGroupHeight);
                    }
                    break;
                }
            }
        }
    }
}

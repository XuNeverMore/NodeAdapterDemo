package com.xunevermore.nodeadapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NodeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<INodeItem> mList = new ArrayList<>();

    public NodeAdapter(List<? extends INodeItem> list) {
        mList.addAll(list);
    }

    public NodeAdapter() {
        mList = new ArrayList<>();
    }

    public List<INodeItem> getList() {
        return mList;
    }

    public void replaceAll(List<? extends INodeItem> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(INodeItem item) {
        if (item == null) {
            return;
        }
        item.getParentNode().getChildNodes().remove(item);
        getList().remove(item);

        INodeItem parentNode = item.getParentNode();
        if (parentNode != null && !parentNode.isRootNode() && parentNode.getChildCount() == 0) {
            parentNode.getParentNode().getChildNodes().remove(parentNode);
            getList().remove(parentNode);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        INodeItem item = mList.get(position);
        if (item != null) {
            item.bindData(holder);
            item.setAdapter(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        INodeItem item = mList.get(position);
        if (item != null) {
            return item.getItemLayoutRes();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

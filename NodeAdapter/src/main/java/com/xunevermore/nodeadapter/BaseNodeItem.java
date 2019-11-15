package com.xunevermore.nodeadapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseNodeItem implements INodeItem {

    protected boolean mIsExpand = false;
    private NodeAdapter mNodeAdapter;
    private List<INodeItem> mChildList;
    private INodeItem mParent;
    private int mSelectState = INodeItem.STATE_SELECT_ALL;
    private INodeItem mRootNode;

    public BaseNodeItem() {
        mChildList = new ArrayList<>();
    }


    @Override
    public INodeItem getParentNode() {
        return mParent;
    }

    @Override
    public void setParentNode(INodeItem parentNode) {
        mParent = parentNode;
    }

    @Override
    public void cacuSelectedSize() {

    }

    @Override
    public List<INodeItem> getChildNodes() {
        return mChildList;
    }

    @Override
    public INodeItem getRootNode() {
        return mRootNode;
    }

    @Override
    public boolean isRootNode() {
        return this == getRootNode();
    }

    @Override
    public void setRootNode(INodeItem rootNode) {
        this.mRootNode = rootNode;
        for (INodeItem childNode : getChildNodes()) {
            if (childNode == null) {
                continue;
            }
            childNode.setRootNode(rootNode);
        }
    }

    @Override
    public void addNodeChild(@NonNull INodeItem nodeItem) {
        mChildList.add(nodeItem);
    }

    @Override
    public void addNodeChildren(List<? extends INodeItem> list) {
        mChildList.addAll(list);
    }

    @Override
    public int getChildCount() {
        return mChildList.size();
    }


    @Override
    public int getSelectState() {
        return mSelectState;
    }

    @Override
    public void setSelectState(int selectState) {
        mSelectState = selectState;
    }

    @Override
    public void setSelectState(boolean selected, boolean applyToChildren) {
        if (applyToChildren) {
            for (INodeItem childNode : getChildNodes()) {
                if (childNode == null) {
                    continue;
                }
                childNode.setSelectState(selected, applyToChildren);
            }
        }
        mSelectState = selected ? INodeItem.STATE_SELECT_ALL : INodeItem.STATE_SELECT_NONE;
    }

    @Override
    public void refreshParentSelectState() {
        INodeItem parentNode = getParentNode();
        if (parentNode != null) {
            parentNode.updateSelectState();
            if (!parentNode.isRootNode()) {
                parentNode.refreshParentSelectState();
            } else {
                parentNode.cacuSelectedSize();
            }
        }

    }

    @Override
    public void updateSelectState() {
        boolean isAllSelected = true;
        boolean isNoneSelected = true;
        for (INodeItem childNode : getChildNodes()) {
            if (childNode == null) {
                continue;
            }
            if (childNode.getSelectState() != INodeItem.STATE_SELECT_ALL) {
                isAllSelected = false;
            }
            if (childNode.getSelectState() != INodeItem.STATE_SELECT_NONE) {
                isNoneSelected = false;
            }
        }
        if (!isAllSelected && !isNoneSelected) {
            setSelectState(INodeItem.STATE_SELECT_PART);
        } else if (isAllSelected) {
            setSelectState(INodeItem.STATE_SELECT_ALL);
        } else {
            setSelectState(INodeItem.STATE_SELECT_NONE);
        }
    }

    @Override
    public boolean isExpand() {
        return mIsExpand;
    }

    @Override
    public void setExpand(boolean expand) {
        if (!isExpandable() || mIsExpand == expand) {
            return;
        }
        NodeAdapter adapter = getAdapter();
        if (adapter != null) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                List<INodeItem> list = adapter.getList();
                if (expand) {
                    list.addAll(adapterPosition + 1, getChildNodes());
                } else {
                    List<INodeItem> childNodes = getChildNodes();
                    for (INodeItem childNode : childNodes) {
                        childNode.setExpand(false);
                    }
                    list.removeAll(getChildNodes());
                }
                mIsExpand = expand;
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setExpandSimple(boolean expand) {
        mIsExpand = expand;
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }

    @Override
    public int getAdapterPosition() {
        int index = -1;
        NodeAdapter adapter = getAdapter();
        if (adapter != null) {
            List<INodeItem> list = mNodeAdapter.getList();
            index = list.indexOf(this);
        }
        return index;
    }

    @Override
    public NodeAdapter getAdapter() {
        return mNodeAdapter;
    }

    @Override
    public void setAdapter(NodeAdapter adapter) {
        this.mNodeAdapter = adapter;
    }
}

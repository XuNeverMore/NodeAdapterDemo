package com.xunevermore.nodeadapter;

import androidx.annotation.NonNull;

import java.util.List;

public interface INodeItem {

    int STATE_SELECT_ALL = 0;
    int STATE_SELECT_NONE = 1;
    int STATE_SELECT_PART = 2;

    int getItemLayoutRes();

    void bindData(ViewHolder viewHolder);

    INodeItem getParentNode();

    void setParentNode(INodeItem parentNode);

    List<INodeItem> getChildNodes();

    void addNodeChild(@NonNull INodeItem nodeItem);

    void addNodeChildren(List<? extends INodeItem> list);

    void cacuSelectedSize();

    int getChildCount();

    int getSelectState();

    void setSelectState(int selectState);

    long getSelectedSize();

    long getTotalSize();

    void init();

    INodeItem getRootNode();

    boolean isRootNode();

    void setRootNode(INodeItem rootNode);

    /**
     * @param selected        选中状态
     * @param applyToChildren
     */
    void setSelectState(boolean selected, boolean applyToChildren);

    boolean isExpand();

    void setExpand(boolean expand);

    void setExpandSimple(boolean expand);

    void refreshParentSelectState();

    void updateSelectState();

    boolean isExpandable();

    NodeAdapter getAdapter();

    void setAdapter(NodeAdapter adapter);

    int getAdapterPosition();


}

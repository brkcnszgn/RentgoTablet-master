package com.creatifsoftware.filonova.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.model.Branch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerembalaban on 11.03.2019 at 14:09.
 */
public class ExpandableBranchListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final String expandableListTitle;
    private List<? extends Branch> expandableListDetail = new ArrayList<>();

    public ExpandableBranchListAdapter(Context context, String expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;

    }

    public void setDetailList(final List<? extends Branch> expandableListDetail) {
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Branch expandedItem = (Branch) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_branch_list_view_item, null);
        }
        TextView expandedItemTitle = convertView
                .findViewById(R.id.list_title);
        expandedItemTitle.setText(expandedItem.branchName);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_branch_list_view_title, null);
        }
        TextView selectedBranchTitle = convertView
                .findViewById(R.id.selected_branch);

        for (Branch item : expandableListDetail) {
            if (item.isSelected) {
                selectedBranchTitle.setText(item.branchName);
                break;
            }
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}

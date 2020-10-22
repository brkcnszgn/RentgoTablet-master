package com.creatifsoftware.filonova.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.creatifsoftware.filonova.R;
import com.creatifsoftware.filonova.model.AdditionalProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kerembalaban on 11.03.2019 at 14:09.
 */
public class ExpandableExtraPaymentListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final String expandableListTitle;
    private List<? extends AdditionalProduct> expandableListDetail = new ArrayList<>();

    public ExpandableExtraPaymentListAdapter(Context context, String expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;

    }

    public void setDetailList(final List<? extends AdditionalProduct> expandableListDetail) {
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
        final AdditionalProduct expandedItem = (AdditionalProduct) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_view_item, null);
        }
        TextView expandedItemTitle = convertView
                .findViewById(R.id.list_title);
        TextView expandedItemPrice = convertView
                .findViewById(R.id.list_price);
        expandedItemTitle.setText(expandedItem.productName);
        expandedItemPrice.setText(String.format(Locale.getDefault(), "%.2f TL", expandedItem.tobePaidAmount));

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
            convertView = layoutInflater.inflate(R.layout.expandable_list_view_title, null);
        }
        TextView listItemTitle = convertView
                .findViewById(R.id.list_title);
        TextView listItemAmount = convertView
                .findViewById(R.id.list_price);
        listItemTitle.setTypeface(null, Typeface.BOLD);
        listItemTitle.setText(listTitle);
        double totalPrice = 0;
        for (AdditionalProduct item : expandableListDetail) {
            totalPrice = totalPrice + item.tobePaidAmount;
        }
        listItemAmount.setText(String.format(Locale.getDefault(), "%.02f TL", totalPrice));
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

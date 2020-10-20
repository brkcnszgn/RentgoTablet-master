package com.creatifsoftware.rentgoservice.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.model.HgsItem;
import com.creatifsoftware.rentgoservice.utils.DateUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by kerembalaban on 11.03.2019 at 14:09.
 */
public class ExpandableTollListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final String expandableListTitle;
    private List<? extends HgsItem> expandableListDetail;

    public ExpandableTollListAdapter(Context context, String expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
    }

    public void setDetailList(final List<? extends HgsItem> expandableListDetail) {
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
        final HgsItem expandedItem = (HgsItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_view_item, null);
        }
        TextView expandedItemTitle = convertView
                .findViewById(R.id.list_title);
        TextView expandedItemPrice = convertView
                .findViewById(R.id.list_price);

        String entryDateTimeString = DateUtils.convertTimestampToStringDateTimeWithoutTimezone(expandedItem.entryDateTime);
        String exitDateTimeString = DateUtils.convertTimestampToStringDateTimeWithoutTimezone(expandedItem.exitDateTime);

        expandedItemTitle.setText(String.format(Locale.getDefault(), "%s - %s\n%s - %s",
                entryDateTimeString,
                expandedItem.entryLocation,
                exitDateTimeString,
                expandedItem.exitLocation));

        expandedItemPrice.setText(String.format(Locale.getDefault(), "%.2f TL", expandedItem.amount));

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
        LayoutInflater layoutInflater = (LayoutInflater) this.context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.expandable_list_view_title, null);
        TextView listItemTitle = convertView
                .findViewById(R.id.list_title);
        TextView listItemAmount = convertView
                .findViewById(R.id.list_price);
        listItemTitle.setTypeface(null, Typeface.BOLD);
        listItemTitle.setText(listTitle);
        double totalPrice = 0;
        if (expandableListDetail != null) {
            for (HgsItem item : expandableListDetail) {
                totalPrice = totalPrice + item.amount;
            }
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

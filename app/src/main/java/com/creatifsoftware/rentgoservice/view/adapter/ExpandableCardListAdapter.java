package com.creatifsoftware.rentgoservice.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.model.CreditCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kerembalaban on 11.03.2019 at 14:09.
 */
public class ExpandableCardListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final String expandableListTitle;
    private final double depositAmount;
    private List<? extends CreditCard> expandableListDetail = new ArrayList<>();

    public ExpandableCardListAdapter(Context context, String expandableListTitle, double depositAmount) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.depositAmount = depositAmount;

    }

    public List<? extends CreditCard> getDetailList() {
        return this.expandableListDetail;
    }

    public void setDetailList(final List<? extends CreditCard> expandableListDetail) {
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
        final CreditCard expandedItem = (CreditCard) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_card_list_view_item, null);
        }
        TextView expandedItemTitle = convertView
                .findViewById(R.id.list_title);
        expandedItemTitle.setText(expandedItem.creditCardNumber);

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
            convertView = layoutInflater.inflate(R.layout.expandable_card_list_view_title, null);
        }

        TextView listItemTitle = convertView
                .findViewById(R.id.list_title);
        TextView listItemAmount = convertView
                .findViewById(R.id.deposit_amount);
        listItemTitle.setTypeface(null, Typeface.BOLD);
        listItemTitle.setText(listTitle);

        listItemTitle.setText((String) getGroup(listPosition));
        listItemAmount.setText(String.format(Locale.getDefault(), "%.02f", depositAmount));

        for (CreditCard item : expandableListDetail) {
            if (item.isSelected) {
                listItemTitle.setText(item.creditCardNumber);
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

package com.teamproject.plastikproject.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.helpers.ContentHelper;
import com.teamproject.plastikproject.helpers.SqlDbHelper;

/**
 * Created by rage on 2/22/15.
 */
public class PurchaseItemAdapter extends CursorAdapter {
    private static final String TAG = PurchaseItemAdapter.class.getSimpleName();
    private int resource;
    private LayoutInflater inflater;
    private OnItemInteractionListener onItemInteractionListener;
    private boolean isItemEnable = true;

    @TargetApi(8)
    public PurchaseItemAdapter(Context context, Cursor c, int resource) {
        super(context, c);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @TargetApi(11)
    public PurchaseItemAdapter(Context context, Cursor c, int flags, int resource) {
        super(context, c, flags);
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemInteractionListener(OnItemInteractionListener onItemInteractionListener) {
        this.onItemInteractionListener = onItemInteractionListener;
    }

    public boolean isItemEnable() {
        return isItemEnable;
    }

    public void setItemEnable(boolean isItemEnable) {
        this.isItemEnable = isItemEnable;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(resource, parent, false);
        Holder holder = new Holder();
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.description = (TextView) view.findViewById(R.id.description);
        holder.count = (TextView) view.findViewById(R.id.count);
        holder.count_container = view.findViewById(R.id.count_container);
        holder.checkBox = (CheckBox) view.findViewById(R.id.check_bought);
        view.setTag(holder);

        populateHolder(cursor, holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = (Holder) view.getTag();

        populateHolder(cursor, holder);
    }

    private class Holder {
        TextView title, description, count;
        CheckBox checkBox;
        View count_container;
    }

    private void populateHolder(final Cursor cursor, final Holder holder) {
        final long dbId = ContentHelper.getDbId(cursor);
        int indexName = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_LABEL);
        int indexDescription = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_DESCRIPTION);
        int indexBought = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_BOUGHT);
        int indexCancel = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_IS_CANCEL);
        int indexQuantity = cursor.getColumnIndex(SqlDbHelper.PURCHASE_ITEM_COLUMN_GOODS_QUANTITY);

        String description = cursor.getString(indexDescription);
        boolean isCancel = cursor.getInt(indexCancel) > 0;
        float quantity = cursor.getFloat(indexQuantity);

        holder.title.setText(cursor.getString(indexName));
        holder.title.setEnabled(isItemEnable && !isCancel);
        holder.checkBox.setChecked(cursor.getInt(indexBought) > 0);
        holder.checkBox.setEnabled(isItemEnable && !isCancel);
        holder.checkBox.setClickable(!isCancel);
        if (!TextUtils.isEmpty(description)) {
            holder.description.setText(description);
            holder.description.setEnabled(isItemEnable && !isCancel);
            holder.description.setVisibility(View.VISIBLE);
        }
        if (quantity > 0) {
            holder.count.setText(Float.toString(quantity));
            holder.count.setEnabled(isItemEnable && !isCancel);
            holder.count_container.setVisibility(View.VISIBLE);
        }

        if (isItemEnable && onItemInteractionListener != null) {
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemInteractionListener.onCheckBoxClick(dbId, holder.checkBox.isChecked());
                }
            });
        }
    }

    public interface OnItemInteractionListener {
        void onCheckBoxClick(long dbId, boolean checked);
    }
}

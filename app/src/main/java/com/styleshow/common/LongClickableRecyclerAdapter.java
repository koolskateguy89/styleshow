package com.styleshow.common;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A RecyclerView adapter that supports item long click events.
 *
 * @param <VH>   The type of ViewHolder.
 * @param <Item> The type of item in the list.
 * @see ItemLongClickListener
 */
public abstract class LongClickableRecyclerAdapter<VH extends RecyclerView.ViewHolder, Item> extends RecyclerView.Adapter<VH> {

    protected @Nullable ItemLongClickListener<Item> itemLongClickListener;

    public void setItemLongClickListener(@Nullable ItemLongClickListener<Item> itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    protected void onItemLongClick(int index, @NonNull Item item) {
        if (itemLongClickListener != null)
            itemLongClickListener.onLongClick(index, item);
    }

    public abstract void setItems(@NonNull List<Item> items);
}

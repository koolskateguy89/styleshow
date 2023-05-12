package com.styleshow.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

/**
 * A RecyclerView adapter that supports item click events.
 *
 * @param <VH>  The type of ViewHolder.
 * @param <Item> The type of item in the list.
 * @see ItemClickListener
 */
public abstract class ClickableRecyclerAdapter<VH extends ViewHolder, Item> extends RecyclerView.Adapter<VH> {

    protected @Nullable ItemClickListener<Item> itemClickListener;

    public void setItemClickListener(@Nullable ItemClickListener<Item> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    protected void onItemClick(@NonNull Item item) {
        if (itemClickListener != null)
            itemClickListener.onItemClick(item);
    }
}

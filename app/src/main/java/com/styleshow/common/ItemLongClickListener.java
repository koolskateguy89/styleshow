package com.styleshow.common;

import androidx.annotation.NonNull;

/**
 * Long click listener for RecyclerView items.
 *
 * @see <a href="https://medium.com/@amsavarthan/the-modern-approach-to-handle-item-click-on-recyclerview-6292cca3178d">Medium article</a>
 * @see LongClickableRecyclerAdapter
 */
@FunctionalInterface
public interface ItemLongClickListener<T> {

    /**
     * Called when an item is long clicked.
     *
     * @param item the item represented by the view that was clicked
     */
    void onLongClick(int index, @NonNull T item);
}

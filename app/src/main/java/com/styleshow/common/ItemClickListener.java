package com.styleshow.common;

import androidx.annotation.NonNull;

// TODO: ItemLongClickListener

/**
 * Click listener for RecyclerView items.
 *
 * @see <a href="https://medium.com/@amsavarthan/the-modern-approach-to-handle-item-click-on-recyclerview-6292cca3178d">Medium article</a>
 * @see ClickableRecyclerAdapter
 */
@FunctionalInterface
public interface ItemClickListener<T> {

    /**
     * Called when an item is clicked.
     *
     * @param item the item represented by the view that was clicked
     */
    void onClick(int index, @NonNull T item);
}

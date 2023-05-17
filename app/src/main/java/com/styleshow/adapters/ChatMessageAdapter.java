package com.styleshow.adapters;

import java.util.List;
import java.util.function.IntConsumer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.styleshow.common.LongClickableRecyclerAdapter;
import com.styleshow.databinding.ItemChatMessageReceivedBinding;
import com.styleshow.databinding.ItemChatMessageSentBinding;
import com.styleshow.domain.model.ChatMessage;

/**
 * The adapter for the chat messages.
 *
 * @see com.styleshow.R.layout#item_chat_message_sent
 * @see com.styleshow.R.layout#item_chat_message_received
 */
public class ChatMessageAdapter extends
        LongClickableRecyclerAdapter<ChatMessageAdapter.MessageHolder, ChatMessage> {

    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_RECEIVED = 1;

    private @NonNull List<ChatMessage> messages;

    public ChatMessageAdapter(@NonNull List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public void setItems(@NonNull List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        var message = messages.get(position);

        return message.isMyMessage() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IntConsumer onItemLongClick = index -> {
            this.onItemLongClick(index, messages.get(index));
        };

        switch (viewType) {
            case VIEW_TYPE_SENT -> {
                var binding = ItemChatMessageSentBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

                return new SentMessageHolder(binding, onItemLongClick);
            }
            case VIEW_TYPE_RECEIVED -> {
                var binding = ItemChatMessageReceivedBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

                return new ReceivedMessageHolder(binding, onItemLongClick);
            }
            default -> throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        var message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * Common view holder superclass for both sent and received messages.
     */
    static abstract class MessageHolder extends RecyclerView.ViewHolder {
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(ChatMessage message);
    }

    /**
     * The holder for message sent by the user.
     *
     * @see com.styleshow.R.layout#item_chat_message_sent
     */
    static class SentMessageHolder extends MessageHolder {

        final @NonNull ItemChatMessageSentBinding binding;

        public SentMessageHolder(@NonNull ItemChatMessageSentBinding binding,
                                 @NonNull IntConsumer onItemLongClick) {
            super(binding.getRoot());
            this.binding = binding;

            this.itemView.setOnLongClickListener(v -> {
                onItemLongClick.accept(getLayoutPosition());
                return true;
            });
        }

        @Override
        public void bind(ChatMessage message) {
            binding.setMessage(message);
        }
    }

    /**
     * The holder for a message not sent by the user.
     *
     * @see com.styleshow.R.layout#item_chat_message_received
     */
    static class ReceivedMessageHolder extends MessageHolder {

        final @NonNull ItemChatMessageReceivedBinding binding;

        public ReceivedMessageHolder(@NonNull ItemChatMessageReceivedBinding binding,
                                     @NonNull IntConsumer onItemLongClick) {
            super(binding.getRoot());
            this.binding = binding;

            this.itemView.setOnLongClickListener(v -> {
                onItemLongClick.accept(getLayoutPosition());
                return true;
            });
        }

        @Override
        public void bind(ChatMessage message) {
            binding.setMessage(message);
        }
    }
}

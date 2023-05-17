package com.styleshow.domain.model;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * A model class representing a chat message.
 */
public class ChatMessage {

    private final @NonNull String id;

    private final @NonNull String content;

    private final @NonNull Date sentAt;

    private final boolean isMyMessage;

    public ChatMessage(
            @NonNull String id,
            @NonNull String content,
            @NonNull Date sentAt,
            boolean isMyMessage
    ) {
        this.id = id;
        this.content = content;
        this.sentAt = sentAt;
        this.isMyMessage = isMyMessage;
    }

    public @NonNull String getId() {
        return id;
    }

    public @NonNull String getContent() {
        return content;
    }

    public @NonNull Date getSentAt() {
        return sentAt;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    @Override
    public @NonNull String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", isMyMessage=" + isMyMessage +
                '}';
    }
}

package com.styleshow.domain.model;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * A model class representing a chat message.
 */
public class ChatMessage {

    private final @NonNull String content;

    private final @NonNull Date sentAt;

    private final boolean isMyMessage;

    public ChatMessage(@NonNull String content, @NonNull Date sentAt, boolean isMyMessage) {
        this.content = content;
        this.sentAt = sentAt;
        this.isMyMessage = isMyMessage;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public Date getSentAt() {
        return sentAt;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    @Override
    public @NonNull String toString() {
        return "ChatMessage{" +
                "content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", isMyMessage=" + isMyMessage +
                '}';
    }
}

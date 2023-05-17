package com.styleshow.domain.model;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * A model class representing a chat message.
 */
public class ChatMessage {

    private final @NonNull String id;

    public final @NonNull String senderUid;

    private final @NonNull String content;

    private final @NonNull Date sentAt;

    private final boolean isMyMessage;

    public ChatMessage(
            @NonNull String id,
            @NonNull String senderUid,
            @NonNull String content,
            @NonNull Date sentAt,
            boolean isMyMessage
    ) {
        this.id = id;
        this.senderUid = senderUid;
        this.content = content;
        this.sentAt = sentAt;
        this.isMyMessage = isMyMessage;
    }

    public @NonNull String getId() {
        return id;
    }

    public @NonNull String getSenderUid() {
        return senderUid;
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
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof ChatMessage that) {
            return id.equals(that.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public @NonNull String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", senderUid='" + senderUid + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", isMyMessage=" + isMyMessage +
                '}';
    }
}

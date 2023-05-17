package com.styleshow.domain.model;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * A model class representing a chat message.
 */
public class ChatMessage {

    private final @NonNull String senderUid;

    private final @NonNull String receiverUid;

    private final @NonNull String content;

    private final @NonNull Date sentAt;

    public ChatMessage(@NonNull String senderUid, @NonNull String receiverUid, @NonNull String content, @NonNull Date sentAt) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.content = content;
        this.sentAt = sentAt;
    }

    public @NonNull String getSenderUid() {
        return senderUid;
    }

    public @NonNull String getReceiverUid() {
        return receiverUid;
    }

    public @NonNull String getContent() {
        return content;
    }

    public @NonNull Date getSentAt() {
        return sentAt;
    }

    @Override
    public @NonNull String toString() {
        return "ChatMessage{" +
                "senderUid='" + senderUid + '\'' +
                ", receiverUid='" + receiverUid + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}

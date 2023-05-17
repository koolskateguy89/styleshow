package com.styleshow.data.remote.dto;

import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.styleshow.domain.model.ChatMessage;

/**
 * DTO for {@link ChatMessage}.
 *
 * @see com.styleshow.data.remote.ChatDataSource
 */
public class ChatMessageDto {

    @DocumentId
    public String id;

    public String senderUid;
    public String receiverUid;
    public String content;
    public Timestamp sentAt;

    public ChatMessage toChatMessage(@NonNull String currentUserId) {
        boolean isMyMessage = currentUserId.equals(senderUid);

        return new ChatMessage(id, content, sentAt.toDate(), isMyMessage);
    }

    @Override
    public @NonNull String toString() {
        return "ChatMessageDto{" +
                "id='" + id + '\'' +
                ", senderUid='" + senderUid + '\'' +
                ", receiverUid='" + receiverUid + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}

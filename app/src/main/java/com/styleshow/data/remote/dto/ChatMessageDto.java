package com.styleshow.data.remote.dto;

import java.util.Date;

import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;
import com.styleshow.domain.model.ChatMessage;
import timber.log.Timber;

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

    @ServerTimestamp
    public Date sentAt;

    public ChatMessage toChatMessage(@NonNull String currentUserId) {
        boolean isMyMessage = currentUserId.equals(senderUid);

        return new ChatMessage(id, content, sentAt, isMyMessage);
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

package com.styleshow.data.remote.dto;

import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;
import com.styleshow.domain.model.ChatMessage;

/**
 * DTO for {@link ChatMessage}.
 *
 * @see com.styleshow.data.remote.ChatDataSource
 */
public class ChatMessageDto {

    public String senderUid;
    public String receiverUid;
    public String content;
    public Timestamp sentAt;

    public ChatMessage toChatMessage(@NonNull String currentUserId) {
        boolean isMyMessage = currentUserId.equals(senderUid);

        return new ChatMessage(content, sentAt.toDate(), isMyMessage);
    }
}

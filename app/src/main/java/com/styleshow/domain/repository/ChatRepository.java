package com.styleshow.domain.repository;

import java.util.List;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.ChatMessage;
import io.reactivex.rxjava3.core.Observable;

public interface ChatRepository {

    void sendMessage(@NonNull String receiverUid, @NonNull String content);

    Task<Void> deleteMessage(@NonNull String messageId);

    /**
     * For use in activities.
     *
     * @see #listenForMessageEvents(String)
     */
    void listenForMessagesBetween(@NonNull Activity activity, @NonNull String receiverId,
                                  @NonNull ChatListener listener);

    /**
     * For use in services.
     *
     * @see #listenForMessagesBetween(Activity, String, ChatListener)
     */
    Observable<MessageEvent> listenForMessageEvents(@NonNull String currentUserId);

    /**
     * An interface to listen to chat messages updates.
     */
    interface ChatListener {

        void onNewMessages(@NonNull List<ChatMessage> messages);

        void onMessageDeleted(@NonNull ChatMessage message);
    }

    sealed abstract class MessageEvent permits MessageEvent.MessageSentEvent, MessageEvent.MessageDeletedEvent {

        public static final class MessageSentEvent extends MessageEvent {

            public final @NonNull ChatMessage message;

            public MessageSentEvent(@NonNull ChatMessage message) {
                this.message = message;
            }
        }

        public static final class MessageDeletedEvent extends MessageEvent {

            public final @NonNull ChatMessage message;

            public MessageDeletedEvent(@NonNull ChatMessage message) {
                this.message = message;
            }
        }
    }
}

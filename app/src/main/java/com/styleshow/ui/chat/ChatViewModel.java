package com.styleshow.ui.chat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntConsumer;

import javax.inject.Inject;

import android.app.Activity;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.ChatRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class ChatViewModel extends ViewModel {

    private final @NonNull ChatRepository chatRepository;

    private final MutableLiveData<UserProfile> mReceiver = new MutableLiveData<>();
    private final MutableLiveData<String> mNewMessage = new MutableLiveData<>("");
    private final List<ChatMessage> messages = new ArrayList<>();

    @Inject
    public ChatViewModel(@NonNull ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public LiveData<UserProfile> getReceiver() {
        return mReceiver;
    }

    @MainThread
    public void setReceiver(UserProfile receiver) {
        mReceiver.setValue(receiver);
    }

    public MutableLiveData<String> getNewMessage() {
        return mNewMessage;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void listenForMessage(@NonNull Activity activity, RecyclerView.Adapter<?> adapter,
                                 @NonNull IntConsumer scrollToBottom) {
        chatRepository.listenForMessagesBetween(activity, mReceiver.getValue().getUid(), new ChatRepository.ChatListener() {

            @Override
            public void onNewMessages(@NonNull List<ChatMessage> newMessages) {
                int oldSize = messages.size();
                messages.addAll(newMessages);

                // Sort messages by timestamp
                messages.sort(Comparator.comparing(ChatMessage::getSentAt));

                // Notify adapter of changes
                adapter.notifyItemRangeInserted(oldSize, messages.size());

                // Scroll to bottom
                scrollToBottom.accept(messages.size() - 1);
            }

            @Override
            public void onMessageDeleted(@NonNull ChatMessage message) {
                int indexRemoved = messages.indexOf(message);

                messages.remove(message);
                adapter.notifyItemRemoved(indexRemoved);
            }
        });
    }

    @MainThread
    public void trySendMessage() {
        var receiver = mReceiver.getValue();
        if (receiver == null) return;
        String receiverUid = receiver.getUid();

        String content = mNewMessage.getValue();
        Timber.i("(sendMessage) content = %s", content);
        if (content == null || content.isBlank()) return;
        content = content.trim();

        // Send the message
        chatRepository.sendMessage(receiverUid, content);

        // Reset the input field
        mNewMessage.setValue("");
    }

    public boolean canDeleteMessage(@NonNull ChatMessage message) {
        return message.isMyMessage();
    }

    public Task<Void> deleteMessage(@NonNull ChatMessage message) {
        return chatRepository.deleteMessage(message.getId());
    }
}

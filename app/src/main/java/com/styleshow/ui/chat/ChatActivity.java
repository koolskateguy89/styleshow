package com.styleshow.ui.chat;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.styleshow.R;
import com.styleshow.adapters.ChatMessageAdapter;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityChatBinding;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.model.UserProfile;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * Screen for chatting with another user.
 */
@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var receiver = (UserProfile) getIntent().getSerializableExtra(Constants.NAME_PROFILE);
        Timber.i("receiver = %s", receiver);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.setReceiver(receiver);
        viewModel.loadMessages();

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        // Send message on DONE
        binding.etNewMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.trySendMessage();
            }
            return false;
        });

        // Setup message recycler view
        var adapter = new ChatMessageAdapter(viewModel.getMessages().getValue());

        // On long click on message, confirm message deletion
        adapter.setItemLongClickListener((index, message) -> {
            if (viewModel.canDeleteMessage(message))
                confirmDeleteMessageWithDialog(message);
        });

        binding.rvMessages.setAdapter(adapter);
        viewModel.getMessages().observe(this, messages -> {
            adapter.setItems(messages);
            // Scroll to bottom on messages loaded
            binding.rvMessages.smoothScrollToPosition(messages.size());

            // Start listening for message updates
            // Smooth scroll to bottom on new message
            viewModel.listenForMessage(this, adapter, binding.rvMessages::smoothScrollToPosition);
        });

        // Handle initial messages loading state
        viewModel.getLoadingState().observe(this, loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display messages
                    binding.viewSwitcher.setDisplayedChild(0);
                }
            }
        });
    }

    private void confirmDeleteMessageWithDialog(ChatMessage message) {
        var builder = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_message_dialog_title)
                .setMessage(R.string.delete_message_dialog_message)
                // Add the buttons
                .setPositiveButton(R.string.dialog_ok, (dialog, id) -> {
                    // User clicked OK button
                    // Delete the comment
                    viewModel.deleteMessage(message).addOnSuccessListener(ignore -> {
                        // Inform user of deletion
                        Snackbar.make(binding.getRoot(), R.string.delete_message_success,
                                        Snackbar.LENGTH_SHORT)
                                .show();
                    });
                })
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> {
                    // User cancelled the dialog (do nothing)
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

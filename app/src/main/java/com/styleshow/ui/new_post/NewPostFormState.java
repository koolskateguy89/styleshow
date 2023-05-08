package com.styleshow.ui.new_post;

class NewPostFormState {

    final String caption;
    final String imageUrl;

    NewPostFormState(String caption, String imageUrl) {
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    NewPostFormState withCaption(String caption) {
        return new NewPostFormState(caption, imageUrl);
    }
}

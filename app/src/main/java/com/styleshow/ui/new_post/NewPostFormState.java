package com.styleshow.ui.new_post;

class NewPostFormState {

    final String caption;
    final String imageUrl;
    final String shoeUrl;

    NewPostFormState(String caption, String imageUrl, String shoeUrl) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.shoeUrl = shoeUrl;
    }

    NewPostFormState withCaption(String caption) {
        return new NewPostFormState(caption, imageUrl, shoeUrl);
    }

    // TODO: withImageUrl & withShoeUrl?
}

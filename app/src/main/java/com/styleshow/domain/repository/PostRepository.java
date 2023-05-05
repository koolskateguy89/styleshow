package com.styleshow.domain.repository;

import java.util.List;

import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.Post;

public interface PostRepository {

    // TODO: getAllPosts (maybe paginated)

    Task<List<Post>> getPostsByUser(String uid);
}

package com.styleshow.common;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirebaseUtils {
    private FirebaseUtils() {
    }

    /**
     * Delete a collection, in batches of batchSize. Though Firestore docs doesn't recommend
     * doing this on Android on the client, it's the only way for me to do it.
     * <p>
     * Slightly adapted from the source below.
     *
     * @see <a href="https://firebase.google.com/docs/firestore/manage-data/delete-data#java_5">Source</a>
     */
    public static void deleteCollection(CollectionReference collection, int batchSize) throws Exception {
        // retrieve a small batch of documents to avoid out-of-memory errors
        var task = collection.limit(batchSize).get();
        int deleted = 0;

        // Tasks.await(task) waits for the document delete to complete
        var documents = Tasks.await(task).getDocuments();

        for (DocumentSnapshot document : documents) {
            document.getReference().delete();
            deleted++;
        }

        // If there are still more documents left, continue
        if (deleted >= batchSize) {
            // retrieve and delete another batch
            deleteCollection(collection, batchSize);
        }
    }
}

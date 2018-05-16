package com.mikalh.purchaseorderonline.Model;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatRoomRepository {
    private static final String TAG = "ChatRoomRepo";

    private FirebaseFirestore db;

    public ChatRoomRepository(FirebaseFirestore db) {
        this.db = db;
    }
    public void getChats(String roomId, EventListener<QuerySnapshot> listener) {
        db.collection("Conversasion")
                .whereEqualTo("roomId", roomId)
                .orderBy("timeStamp")
                .addSnapshotListener(listener);
    }
}

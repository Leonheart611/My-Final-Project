package com.mikalh.purchaseorderonline.FCM;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        Log.d("@@@@", "onTokenRefresh: " + instanceId);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            /*FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(firebaseUser.getUid())
                    .child("instanceId")
                    .setValue(instanceId);*/
            HashMap<String,Object> instance = new HashMap<>();
            instance.put("notificationId",instanceId);
            FirebaseFirestore.getInstance().collection("Users")
                    .document(firebaseUser.getUid()).update(instance)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                Log.d("Add InstanseId",task.toString());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Error add Instanse ID",e.getLocalizedMessage());
                }
            });
        }
    }
}

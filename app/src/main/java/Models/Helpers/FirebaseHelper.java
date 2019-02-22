package Models.Helpers;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;

import Models.User;

public class FirebaseHelper {
    FirebaseDatabase firebaseDatabase;
    User user;


    public FirebaseHelper(FirebaseDatabase firebaseDatabase)
    {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void InitializeUser(FirebaseUser firebaseUser)
    {
        String userId = firebaseUser.getUid();
        DatabaseReference usersReference = firebaseDatabase.getReference("users");
        DatabaseReference thisUserReference = usersReference.child(userId);
            User user = new User(firebaseUser.getDisplayName(), "aaaa", "jpg");
        thisUserReference.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError!=null)
                {
                    String error = databaseError.getMessage();
                }
            }
        });

    }

    private void getUserFromDb(String uid)
    {
        DatabaseReference userReference = firebaseDatabase.getReference("users/" + uid);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

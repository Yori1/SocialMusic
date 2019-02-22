package Services;

import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class UserPersistencyService {
    private FirebaseDatabase firebaseDatabase;

    public void EnsureUserInstantiated(String userKey)
    {
        firebaseDatabase.getReference("users/" + userKey);
    }
}

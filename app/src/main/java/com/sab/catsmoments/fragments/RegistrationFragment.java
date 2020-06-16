package com.sab.catsmoments.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sab.catsmoments.R;
import com.sab.catsmoments.databinding.FragmentRegistrationBinding;
import com.sab.catsmoments.models.User;

public class RegistrationFragment extends Fragment {

    FragmentRegistrationBinding registrationBinding;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    User userObject;

    private FirebaseFirestore database;
    private CollectionReference collectionReference;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        collectionReference = database.collection("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registrationBinding = FragmentRegistrationBinding.inflate(inflater, container,
                false);

        registrationBinding.createAccountBtn.setOnClickListener(v -> {

            if(!TextUtils.isEmpty(registrationBinding.catNameText.getText().toString())
            && !TextUtils.isEmpty(registrationBinding.emailText.getText().toString())
            && !TextUtils.isEmpty(registrationBinding.passwordText.getText().toString()))
            {
                String email = registrationBinding.emailText.getText().toString();
                String password = registrationBinding.passwordText.getText().toString();
                String catName = registrationBinding.catNameText.getText().toString();

                createNewUser(email, password, catName);
                registrationBinding.progressBar.setVisibility(View.VISIBLE);
            }
            else
                Toast.makeText(getContext(), "These fields cannot be empty!",
                        Toast.LENGTH_SHORT).show();
        });

        return registrationBinding.getRoot();
    }

    private void createNewUser(String email, String password, String catName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();

                        userObject = new User(user.getUid(), catName);
                        collectionReference.add(userObject);
                        registrationBinding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Account was created",
                                Toast.LENGTH_SHORT).show();

                        moveToSettings(getView(), user);


                    } else{
                        registrationBinding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Whoops! Something went wrong!",
                                Toast.LENGTH_SHORT).show();
                        FirebaseAuthException e = (FirebaseAuthException ) task.getException();
                    Toast.makeText(getContext(), "Failed Registration: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;}
                });
    }

    private void moveToSettings(View view, FirebaseUser user){
        Bundle bundle = new Bundle();
        bundle.putParcelable("firebaseUser", user);
        bundle.putString("catName", userObject.getCatName());
        Navigation.findNavController(view).navigate(R.id.action_registrationFragment_to_momentSettingsFragment, bundle);
    }
}

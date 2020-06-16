package com.sab.catsmoments.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sab.catsmoments.R;
import com.sab.catsmoments.databinding.FragmentMomentSettingsBinding;
import com.sab.catsmoments.models.Moment;
import com.sab.catsmoments.models.User;

import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class MomentSettingsFragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_INTENT_CODE = 777;
    private static final String NO_CAT_NAME = "CAT_404";
    private static final String TAG = "MomentSettingsFragment";

    FragmentMomentSettingsBinding settingsBinding;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore database;

    private CollectionReference collectionReference;
    private CollectionReference userCollectionReference;
    private StorageReference storageReference;
    private String catName;
    private Uri imageUri;
    Moment moment;

    public MomentSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("firebaseUser", user);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        collectionReference = database.collection("Moments");
        userCollectionReference = database.collection("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        catName = bundle.getString("catName", NO_CAT_NAME);
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = getArguments().getParcelable("firebaseUser");
        Log.d(TAG, "onCreateView: " + user.getUid());
        settingsBinding = FragmentMomentSettingsBinding.inflate(inflater, container, false);
        settingsBinding.cameraImage.setOnClickListener(this::onClick);
        settingsBinding.saveMomentButton.setOnClickListener(this::onClick);
        if(catName.equals(NO_CAT_NAME) && user!=null)
        {   user = mAuth.getCurrentUser();
            userCollectionReference.whereEqualTo("userId", user.getUid())
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                            catName = snapshot.getString("catName");
                        settingsBinding.catNameSettings.setText(catName);
                    });
        }
        else {settingsBinding.catNameSettings.setText(catName);}
        return settingsBinding.getRoot();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_image:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_INTENT_CODE);
                break;
            case R.id.saveMomentButton:
                if (!TextUtils.isEmpty(settingsBinding.titleText.getText().toString())
                        && !TextUtils.isEmpty(settingsBinding.descriptionText.getText().toString())
                        && imageUri != null) {

                    settingsBinding.progressBarSettings.setVisibility(View.VISIBLE);

                    StorageReference imagePath = storageReference.child("moment_images")
                            .child("moment_" + Timestamp.now().getSeconds());
                    imagePath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imagePath.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                        moment = new Moment();
                        moment.setUserId(user.getUid());
                        moment.setCatName(catName);
                        moment.setTitle(settingsBinding.titleText.getText().toString());
                        moment.setDescription(settingsBinding.descriptionText.getText()
                                .toString());
                        moment.setImageUrl(uri.toString());
                        moment.setTimeAdded(new Timestamp(new Date()));
                        collectionReference.add(moment);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("moment", moment);
                        bundle.putParcelable("firebaseUser", user);
                        Navigation.findNavController(v).
                                navigate(R.id.action_momentSettingsFragment_to_momentsListFragment, bundle);

                    }));
                }
                else Toast.makeText(getContext(), "Something's missisng", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT_CODE
                && resultCode == RESULT_OK
                && data != null)
            imageUri = data.getData();
        settingsBinding.momentImage.setImageURI(imageUri);
    }
}

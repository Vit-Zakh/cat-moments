package com.sab.catsmoments.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sab.catsmoments.R;
import com.sab.catsmoments.adapters.MomentsListAdapter;
import com.sab.catsmoments.databinding.FragmentMomentsListBinding;
import com.sab.catsmoments.models.Moment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MomentsListFragment extends Fragment {

    FragmentMomentsListBinding fragmentMomentsListBinding;
    MomentsListAdapter adapter;
    FirebaseFirestore database;
    FirebaseUser user;
    FirebaseAuth mAuth;
    CollectionReference collectionReference;
    List<Moment> momentList = new ArrayList<>();
    private static final String TAG = "MomentsListFragment";


    public MomentsListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        collectionReference = database.collection("Moments");
        user = getArguments().getParcelable("firebaseUser");
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.logout_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mAuth.signOut();
        Navigation.findNavController(getView())
                .navigate(R.id.action_momentsListFragment_to_authorizationFragment);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", user.getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Moment moment = snapshot.toObject(Moment.class);
                            momentList.add(moment);
                        }

                        Collections.sort(momentList, (o1, o2) -> o2.getTimeAdded().compareTo(o1.getTimeAdded()));

                        adapter = new MomentsListAdapter();
                        adapter.setMomentList(momentList);
                        fragmentMomentsListBinding.wordsList.setLayoutManager(new LinearLayoutManager(getContext()));
                        fragmentMomentsListBinding.wordsList.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), user.getUid() + " " + momentList.size(), Toast.LENGTH_LONG).show();
                    }

                }).addOnFailureListener(e -> {

                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMomentsListBinding = FragmentMomentsListBinding.inflate(inflater, container, false);
        fragmentMomentsListBinding.addFloatingButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            FirebaseUser mUser = mAuth.getCurrentUser();
            bundle.putParcelable("firebaseUser", mUser);
            Log.d(TAG, "onClick: " + mUser.getUid());
            Navigation.findNavController(v).navigate(R.id.action_momentsListFragment_to_momentSettingsFragment, bundle);
        });
        return fragmentMomentsListBinding.getRoot();
    }
}

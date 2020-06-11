package com.sab.catsmoments.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sab.catsmoments.R;
import com.sab.catsmoments.databinding.FragmentAuthorizationBinding;

public class AuthorizationFragment extends Fragment {
    FragmentAuthorizationBinding authorizationBinding;


    public AuthorizationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authorizationBinding = FragmentAuthorizationBinding.inflate(inflater, container,
                false);
        authorizationBinding.registrationBtn.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_authorizationFragment_to_registrationFragment));
        return authorizationBinding.getRoot();
    }
}

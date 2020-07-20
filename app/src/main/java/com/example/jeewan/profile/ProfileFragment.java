package com.example.jeewan.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeewan.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding profileBinding;
    ProfileViewModel viewModel;
    FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        viewModel=new ViewModelProvider(this).get(ProfileViewModel.class);
        auth=FirebaseAuth.getInstance();
        return profileBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getDataFetched(auth.getUid()).observe(this, new Observer<ProfileModel>() {
            @Override
            public void onChanged(ProfileModel profileModel) {
               profileBinding.nameText.setText(profileModel.name);
            }
        });

    }
}
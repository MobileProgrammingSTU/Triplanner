package com.seoultech.triplanner.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.seoultech.triplanner.R;

public class StorageFragmentLikes extends Fragment {

    public StorageFragmentLikes() {}

    public static StorageFragmentLikes newInstance() {
        StorageFragmentLikes storageFragment = new StorageFragmentLikes();
        Bundle bundle = new Bundle();
        return storageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.storage_fragment_likes, container, false);
        return view;
    }
}

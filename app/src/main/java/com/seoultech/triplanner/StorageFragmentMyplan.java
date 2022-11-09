package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StorageFragmentMyplan extends Fragment {

    public StorageFragmentMyplan() {}

    public static StorageFragmentMyplan newInstance() {
        StorageFragmentMyplan storageFragment = new StorageFragmentMyplan();
        Bundle bundle = new Bundle();
        return storageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.storage_fragment_myplan, container, false);
        return view;
    }
}

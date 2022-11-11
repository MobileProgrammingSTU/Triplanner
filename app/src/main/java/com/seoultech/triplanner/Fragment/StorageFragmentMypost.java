package com.seoultech.triplanner.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.seoultech.triplanner.R;

public class StorageFragmentMypost extends Fragment {

    public StorageFragmentMypost () {}

    public static StorageFragmentMypost newInstance() {
        StorageFragmentMypost storageFragment = new StorageFragmentMypost();
        Bundle bundle = new Bundle();
        return storageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.storage_fragment_mypost, container, false);
        return view;
    }
}

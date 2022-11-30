package com.seoultech.triplanner.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PlanItem;
import com.seoultech.triplanner.R;
import com.seoultech.triplanner.bannerPlanAdapter;

import java.util.ArrayList;

public class StorageFragmentMyplan extends Fragment {
    ListView listView;
    bannerPlanAdapter adapter;
    ArrayList<PlanItem> listMyPlan = new ArrayList<>();  // post 배너 리스트뷰 아이템 리스트

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    public StorageFragmentMyplan() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.storage_fragment_myplan, container, false);

        listView = (ListView) view.findViewById(R.id.listPlan);
        adapter = new bannerPlanAdapter(getContext(), R.layout.place_selected_banner_item, listMyPlan, false);
        adapter.useBtnDelete(true);
        listView.setAdapter(adapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Triplanner");
        DatabaseReference dbRefUserPlans = mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).child("Plans");

        dbRefUserPlans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PlanItem fbPlanData = dataSnapshot.getValue(PlanItem.class);
                    listMyPlan.add(fbPlanData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}

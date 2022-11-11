package com.seoultech.triplanner.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.seoultech.triplanner.R;

public class PostFragment extends Fragment {
    ImageButton btnBack;
    ImageView post_image, like, save;
    LinearLayout indicators;

    TextView title, subtitle, publisher, content;
    Button loacation, typePlace, typeRegion;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        post_image = (ImageView) view.findViewById(R.id.post_image);
        indicators = (LinearLayout) view.findViewById(R.id.layoutIndicators);
        like = (ImageView) view.findViewById(R.id.like);
        save = (ImageView) view.findViewById(R.id.save);
        title = (TextView) view.findViewById(R.id.postTitle);
        subtitle = (TextView) view.findViewById(R.id.postSubtitle);
        publisher = (TextView) view.findViewById(R.id.postPublisher);
        content = (TextView) view.findViewById(R.id.postContent);
        loacation = (Button) view.findViewById(R.id.postLocation);
        typePlace = (Button) view.findViewById(R.id.postPlaceType);
        typeRegion = (Button) view.findViewById(R.id.postRegionType);

        return view;
    }

}
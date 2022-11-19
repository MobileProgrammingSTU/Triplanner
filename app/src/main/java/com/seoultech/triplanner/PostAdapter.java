package com.seoultech.triplanner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public Context mContext;
    public List<PostItem> mPost; // 포스트 아이템의 리스트

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    public PostAdapter(Context mContext, List<PostItem> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Triplanner");    // DB table connect
    }

    //아이템 클릭 리스너 인터페이스
    interface OnItemClickListener{
        void onItemClicked(int position, String data);
    }
    //리스너 객체 참조 변수
    private OnItemClickListener itemClickListener;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        PostAdapter.ViewHolder viewHolder = new PostAdapter.ViewHolder(view);

        //피드 아이템(포스트) 클릭 이벤트
        view.setFocusable(true);
        view.setOnClickListener(new View.OnClickListener() {
            String postId = "";
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    postId = viewHolder.pid;
                }
                Intent intent = new Intent(mContext, PostMain.class);

                Toast.makeText(mContext, postId, Toast.LENGTH_SHORT).show();
                intent.putExtra("pid", postId);
                mContext.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    // 뷰홀더에서 선언한 아이템에 DB값 입력
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem post = mPost.get(position);

        holder.images.clear();
        holder.indicators.clear();
        for (String imgurl : post.getImages().values()) {
            holder.images.add(imgurl);
        }
        // 슬라이드 인디케이터
        holder.setupIndicators(holder.indicators);

        // 이미지 좌/우 슬라이드
        holder.sliderViewPager.setOffscreenPageLimit(3);
        holder.sliderViewPager.setAdapter(new ImageSliderAdapter(mContext, holder.images));
        holder.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                holder.setCurrentIndicator(position);
            }
        });

        holder.pid = post.getPid();

        holder.title.setText(post.getTitle());
        holder.subtitle.setText(post.getSubtitle());
        holder.publisher.setText(post.getPublisher() + " 님");

        isLiked(post.getPid(), holder.like);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getTag().equals("like")) {
                    mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).child("Likes")
                            .child(post.getPid()).setValue(true);
                            //.child(firebaseUser.getUid()).setValue(true);
                    Toast.makeText(mContext, "좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).child("Likes")
                            .child(post.getPid()).removeValue();
                            //.child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    // 뷰홀더 : 아이템 선언
    public class ViewHolder extends RecyclerView.ViewHolder {
        public String pid;
        public ViewPager2 sliderViewPager;
        public ImageView like, save;
        public TextView title, subtitle, publisher;
        public LinearLayout layoutIndicator;
        public ArrayList<String> images;
        public ArrayList<ImageView> indicators;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            images = new ArrayList<String>(); // imgUrl 리스트
            indicators = new ArrayList<>();
            sliderViewPager = itemView.findViewById(R.id.sliderViewPager);
            like = itemView.findViewById(R.id.like);
            save = itemView.findViewById(R.id.save);
            title = itemView.findViewById(R.id.post_title);
            subtitle = itemView.findViewById(R.id.post_subtitle);
            publisher = itemView.findViewById(R.id.publisher);
            layoutIndicator = itemView.findViewById(R.id.layoutIndicators);
        }

        //게시물 슬라이드 인디케이터(점) 생성
        public void setupIndicators(ArrayList<ImageView> indicators) {
            layoutIndicator.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(16, 8, 16, 8);

            for (int i = 0; i < images.size(); i++) {
                ImageView newView = new ImageView(mContext);
                indicators.add(newView);
                indicators.get(i).setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.activity_main_layoutindicators_inactive));
                indicators.get(i).setLayoutParams(params);
                layoutIndicator.addView(indicators.get(i));
            }
            setCurrentIndicator(0);
        }
        //현재 게시물 슬라이드 인디케이터(점)
        public void setCurrentIndicator(int position) {
            int childCount = layoutIndicator.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
                if (i == position) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            mContext, R.drawable.activity_main_layoutindicators_active));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            mContext, R.drawable.activity_main_layoutindicators_inactive));
                }
            }
        }
    }

    private void isLiked(String postid, ImageView imageView) {
        DatabaseReference userDatabaseRef =  mDatabaseRef.child("UserAccount").child(fbCurrentUserUID);

        userDatabaseRef.child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if (snapshot.child(firebaseUser.getUid()).exists()) {
                if (snapshot.exists()) {
                    imageView.setImageResource(R.drawable.ic_heart_filled);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
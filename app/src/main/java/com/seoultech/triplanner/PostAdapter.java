package com.seoultech.triplanner;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    private GestureDetector gestureDetector = null;
    private Boolean clickEventFlag = false; // 클릭 플래그 (기본값 : false)

    public PostAdapter(Context mContext, List<PostItem> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Triplanner");    // DB table connect
        
        // viewPager2 터치 제스쳐 구분
        gestureDetector = new GestureDetector(mContext, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                clickEventFlag = true; // 클릭 감지시 플래그 세움
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });
    }

    //인터페이스 선언
    public interface OnItemClickListener{
        //클릭시 동작할 함수
        void onItemClick(View v, int position);
    }
    private OnItemClickListener itemListener = null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        PostAdapter.ViewHolder viewHolder = new PostAdapter.ViewHolder(view);

//        String postId = "";
//        ArrayList<String> postImages = new ArrayList<>();
        //피드 아이템(포스트) 클릭 이벤트
//        view.setOnClickListener(new View.OnClickListener() {
//            String postId = "";
//            ArrayList<String> postImages = new ArrayList<>();
//            @Override
//            public void onClick(View view) {
//                int position = viewHolder.getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    postId = viewHolder.pid;
//                    postImages = viewHolder.images;
//                }
//                Intent intent = new Intent(mContext, PostMain.class);
//
//                Toast.makeText(mContext, postId, Toast.LENGTH_SHORT).show();
//                intent.putExtra("pid", postId);
//
//                // 이미지 url 데이터를 리스트 형태로 보냄 : DB 에서 찾아서하면 로드 오류남
//                intent.putExtra("images", postImages);
//
//                mContext.startActivity(intent);
//            }
//        });

        return viewHolder;
    }

    @Override
    // 뷰홀더에서 선언한 아이템에 DB값 입력
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem post = mPost.get(position);

        holder.images.clear();
        holder.indicators.clear();
        holder.images.addAll(post.getImages().values());
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
        // viewPager2의 recyclerView 터치 이벤트 (recyclerView item 클릭 이벤트 오류로 해당 방식으로 해결)
        holder.sliderViewPager.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);

                if (clickEventFlag) {
                    clickEventFlag = false;
                    Intent intent = new Intent(mContext, PostMain.class);

                    intent.putExtra("pid", holder.pid);

                    // 이미지 url 데이터를 리스트 형태로 보냄 : DB 에서 찾아서하면 로드 오류남
                    intent.putExtra("images", holder.images);

                    mContext.startActivity(intent);
                }

                return false;
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
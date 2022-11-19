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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;

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
        view.setOnClickListener(new View.OnClickListener() {
            String postId = "";
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    postId = viewHolder.pid;
                }
                Intent intent = new Intent(mContext, PostMain.class);

                //Toast.makeText(mContext, postId, Toast.LENGTH_SHORT).show();
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

        holder.pid = post.getPid();

        Glide.with(mContext).load(post.getImgurl()).placeholder(R.drawable.noimg).dontAnimate().into(holder.post_image);

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
        public ImageView post_image, like, save;
        public TextView title, subtitle, publisher;
        public LinearLayout indicators;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            save = itemView.findViewById(R.id.save);
            title = itemView.findViewById(R.id.post_title);
            subtitle = itemView.findViewById(R.id.post_subtitle);
            publisher = itemView.findViewById(R.id.publisher);
            indicators = itemView.findViewById(R.id.layoutIndicators);
        }
    }

    private void isLiked(String postid, ImageView imageView) {

        DatabaseReference reference = mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).child("Likes").child(postid);
        //DatabaseReference reference = mDatabaseRef.child("Likes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
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
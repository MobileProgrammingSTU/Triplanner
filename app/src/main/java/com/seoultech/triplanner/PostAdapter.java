package com.seoultech.triplanner;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;
import com.seoultech.triplanner.Model.UserAccount;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public Context mContext;
    public List<PostItem> mPost; // 포스트 아이템의 리스트

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<PostItem> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        PostItem post = mPost.get(position);

        Glide.with(mContext).load(post.getImgurl()).placeholder(R.drawable.img_activity_main_cafe_1_bw).dontAnimate().into(holder.post_image);
        holder.title.setText(post.getTitle());
        holder.subtitle.setText(post.getSubtitle());
        holder.publisher.setText(post.getPublisher());

        //publisherInfo(holder.publisher, post.getPublisher());

        isLiked(post.getPid(), holder.like);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference("Triplanner").child("Likes")
                            .child(post.getPid()).setValue(true);
                            //.child(firebaseUser.getUid()).setValue(true);
                    Toast.makeText(mContext, "좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference("Triplanner").child("Likes")
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

    public class ViewHolder extends RecyclerView.ViewHolder {
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

    private void publisherInfo (TextView publisher, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Triplanner").child("Post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount user = snapshot.getValue(UserAccount.class);
                publisher.setText(user.getEmailId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLiked(String postid, ImageView imageView) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Triplanner")
                .child("Likes").child(postid);

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
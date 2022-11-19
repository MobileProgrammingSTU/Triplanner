package com.seoultech.triplanner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;

//PlaceBannerAdapter와 구조는 동일합니다. 다만 여기에서는 필터를 사용하지 않는다는 차이가 있습니다

public class bannerPostAdapter extends BaseAdapter{
    public final static String ATT = "att";
    public final static String REST = "rest";
    public final static String CAFE = "cafe";

    private ArrayList<PostItem> bannerList = new ArrayList<PostItem>();
    public Context mContext;
    private LayoutInflater inflater;
    private int layout;

    public bannerPostAdapter(Context context, int layout, ArrayList<PostItem> dataArray) {
        this.bannerList = dataArray;
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return bannerList.size();
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return bannerList.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        // LayoutInflater를 통해 place_banner_item 메모리에 객체화
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        PostItem bannerItem = bannerList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ImageView bannerImg = (ImageView) convertView.findViewById(R.id.bannerImg);
        Glide.with(mContext).load(bannerItem.getImgurl()).placeholder(R.drawable.noimg).dontAnimate().into(bannerImg);
        TextView bannerTitle = (TextView) convertView.findViewById(R.id.bannerTitle);
        bannerTitle.setText(bannerItem.getTitle());

        Button bannerTag = (Button) convertView.findViewById(R.id.bannerTag);
        String type = bannerItem.getTypePlace();
        if(type.contains("cafe") || type.contains("카페")) {
            bannerTag.setText("카 페");
            bannerTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2BD993")));
        }
        else if(type.contains("rest") || type.contains("맛집")) {
            bannerTag.setText("맛 집");
            bannerTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F26C73")));
        }
        else if(type.contains("att") || type.contains("명소")) {
            bannerTag.setText("명 소");
            bannerTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD37A")));
        }
        else{
            bannerTag.setText("기 타");
        }

        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String pid, String imgurl, String title, String subtitle, String publisher) {
        PostItem item = new PostItem();
        item.setPid(pid);
        item.setImgurl(imgurl);
        item.setTitle(title);
        item.setSubtitle(subtitle);
        item.setPublisher(publisher);
        bannerList.add(item);
        this.notifyDataSetChanged();
    }

    public void removeItem(int position) {
        bannerList.remove(position);
        this.notifyDataSetChanged(); // 데이터 변경사항이 어댑터에 적용, 리스트뷰에 나타남
    }

}

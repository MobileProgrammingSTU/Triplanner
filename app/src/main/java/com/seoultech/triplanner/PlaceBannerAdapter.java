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

import java.util.ArrayList;

public class PlaceBannerAdapter extends BaseAdapter {
    public final static String ATT = "att";
    public final static String REST = "rest";
    public final static String CAFE = "cafe";

    private ArrayList<PlaceBannerItem> bannerList = new ArrayList<PlaceBannerItem>();
    private LayoutInflater inflater;
    private int layout;

    private ArrayList<PlaceBannerItem> filteredItemList = bannerList;

    public PlaceBannerAdapter(Context context, int layout, ArrayList<PlaceBannerItem> dataArray) {
        this.bannerList = dataArray;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // LayoutInflater를 통해 place_banner_item 메모리에 객체화
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        PlaceBannerItem bannerItem = filteredItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ImageView bannerImg = (ImageView) convertView.findViewById(R.id.bannerImg);
        bannerImg.setImageResource(bannerItem.getImg());

        TextView bannerTitle = (TextView) convertView.findViewById(R.id.bannerTitle);
        bannerTitle.setText(bannerItem.getTitle());

        Button bannerTag = (Button) convertView.findViewById(R.id.bannerTag);
        String type = bannerItem.getType();
        if(type.contains("cafe") || type.contains("카페")) {
            bannerTag.setText("카 페");
            bannerTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6CE4B4")));
        }
        else if(type.contains("rest") || type.contains("맛집")) {
            bannerTag.setText("맛 집");
            bannerTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F26C73")));
        }
        else if(type.equals("att") || type.equals("명소")) {
            bannerTag.setText("명 소");
            bannerTag.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD37A")));
        }
        else{
            bannerTag.setText("기 타");
        }

        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int img, String title, String type) {
        PlaceBannerItem item = new PlaceBannerItem(img, title, type);
        bannerList.add(item);
    }

    public void removeItem(int position) {
        bannerList.remove(position);
        this.notifyDataSetChanged();
    }

    public void addFilterType(String type) {
        for (PlaceBannerItem item : bannerList) {
            if(item.getType().equals(type)) {
                filteredItemList.add(0, item);
            }
        }
        notifyDataSetChanged();
    }

    public void removeFilterType(String type) {
        for (PlaceBannerItem item : bannerList) {
            if(item.getType().equals(type)) {
                filteredItemList.remove(item);
            }
        }
        notifyDataSetChanged();
    }
}

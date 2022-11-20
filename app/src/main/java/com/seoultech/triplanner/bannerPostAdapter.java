package com.seoultech.triplanner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoultech.triplanner.Model.PlaceIntent;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;

/*
    모든 포스트(장소) 배너의 리스트뷰에 적용할 adapter 입니다.
    적용 페이지 : StorageFragmentLikes, PlacePlanner(필터), SelectedPlanner(삭제버튼)
    
    [기능 : 기존의 형태를 그대로 가져가되 각기 흩어져있던 어댑터들의 기능을 합쳤습니다]
    1. 필터
        생성자의 제일 마지막 인자에서 필터의 사용 여부를 결정합니다.
        false 입력시 필터 사용을 안합니다.
    2. 삭제 버튼
        배너의 오른쪽 상단의 삭제 버튼을 사용할지 결정합니다.
        기본값은 false 이며, 사용을 원할경우 useBtnDelete() 인자를 true 입력하면 됩니다.
*/

public class bannerPostAdapter extends BaseAdapter{
    public final static String ATT = "att";
    public final static String REST = "rest";
    public final static String CAFE = "cafe";

    public Context mContext;
    private LayoutInflater inflater;
    private int layout;

    // 데이터 리스트
    private ArrayList<PostItem> bannerList = new ArrayList<PostItem>();

    //필터리스트
    private ArrayList<PostItem> filteredItemList = bannerList;

    private Boolean btnDeleteFlag = false;

    public bannerPostAdapter(Context context, int layout, ArrayList<PostItem> dataArray, Boolean filterFlag) {
        if (filterFlag) //필터 사용 여부
            this.bannerList = dataArray;
        else
            this.filteredItemList = dataArray;
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        final int pos = position;

        // LayoutInflater를 통해 place_banner_item 메모리에 객체화
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        PostItem bannerItem = filteredItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ImageView bannerImg = (ImageView) convertView.findViewById(R.id.bannerImg);
        Glide.with(mContext).load(bannerItem.getThumbnail()).placeholder(R.drawable.noimg).dontAnimate().into(bannerImg);
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

        ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
        btnDelete.setFocusable(false); // 이걸해야 리스트뷰의 아이템 클릭, 이미지버튼 클릭 둘다 가능해진다
        if(btnDeleteFlag) {
            btnDelete.setVisibility(convertView.VISIBLE);
            btnDelete.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(pos); // 리스트 뷰에서 지우기
                    PlaceIntent.daySelectedPlace.remove(pos); // static 리스트 지우기
                }
            });
        }
        else {
            btnDelete.setVisibility(convertView.GONE);
        }

        return convertView;
    }

    // 아이템 삭제
    public void removeItem(int position) {
        filteredItemList.remove(position);
        this.notifyDataSetChanged(); // 데이터 변경사항이 어댑터에 적용, 리스트뷰에 나타남
    }

    // 삭제 버튼을 사용할지 결정(default : false)
    public void useBtnDelete(Boolean flag) {
        this.btnDeleteFlag = flag;
    }

    //필터 : 타입에 해당하는 아이템(배너)을 리스트에 추가합니다
    public void addFilterType(String type) {
        for (PostItem item : bannerList) {
            if(item.getTypePlace().equals(type)) {
                filteredItemList.add(0, item);
            }
        }
        notifyDataSetChanged();
    }

    //필터 : 타입에 해당하는 아이템(배너)을 리스트에서 제거합니다
    public void removeFilterType(String type) {
        for (PostItem item : bannerList) {
            if(item.getTypePlace().equals(type)) {
                filteredItemList.remove(item);
            }
        }
        notifyDataSetChanged();
    }
}

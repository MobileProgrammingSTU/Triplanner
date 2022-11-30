package com.seoultech.triplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;

public class bannerPlanInfoAdapter extends BaseAdapter {
    public Context mContext;
    private LayoutInflater inflater;
    private int layout;

    // 데이터 리스트
    private ArrayList<ArrayList<PostItem>> bannerList = new ArrayList<>();

    public bannerPlanInfoAdapter(Context context, int layout, ArrayList<ArrayList<PostItem>> dataArray) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;

        bannerList = dataArray;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public Object getItem(int position) {
        return bannerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // LayoutInflater를 통해 place_banner_item 메모리에 객체화
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        // 데이터 리스트로부터 받아오기 : 한 day 밑의 장소들(0, 1, ...) 정보
        ArrayList<PostItem> itemPlacesByDay = bannerList.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.planDay);

        TextView places = (TextView) convertView.findViewById(R.id.planPlace);
        TextView hours = (TextView) convertView.findViewById(R.id.planHour);
        TextView minutes = (TextView) convertView.findViewById(R.id.planMin);
        String txtPlaces = "";
        String txtHours = "";
        String txtMins = "";

        for (int i=0; i<itemPlacesByDay.size(); i++) {
            if (i == 0) {
                txtPlaces += itemPlacesByDay.get(i).getTitle();
                String time = itemPlacesByDay.get(i).getPlanTime();
                txtHours += time.split(" ")[0];
                txtMins += time.split(" ")[1];
            }
            else {
                txtPlaces += "\n" + itemPlacesByDay.get(i).getTitle();
                String time = itemPlacesByDay.get(i).getPlanTime();
                txtHours += "\n" + time.split(" ")[0];
                txtMins += "\n" + time.split(" ")[1];
            }
        }

        title.setText((position+1) + "일차");
        places.setText(txtPlaces);
        hours.setText(txtHours);
        minutes.setText(txtMins);

        return convertView;
    }
}

package com.seoultech.triplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.seoultech.triplanner.Model.PlaceBannerItem;

import java.util.ArrayList;

public class ImgSliderListAdapter extends BaseAdapter {

    public final static String ATT = "att";
    public final static String REST = "rest";
    public final static String CAFE = "cafe";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<PlaceBannerItem> sliderList = new ArrayList<PlaceBannerItem>();
    private ArrayList<PlaceBannerItem> filteredItemList = sliderList;

    public ImgSliderListAdapter(Context context, int layout, ArrayList<PlaceBannerItem> dataArray) {
        super();
        this.context = context;
        this.sliderList = dataArray;
        //this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater = LayoutInflater.from(context);
        this.layout = layout;
    }

    @Override
    public int getCount()  {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        PlaceBannerItem sliderItem = filteredItemList.get(position);

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(layout, parent, false);

            holder.sliderViewPager = (ViewPager2) convertView.findViewById(R.id.imgSliderViewPager);
            holder.layoutIndicator = (LinearLayout) convertView.findViewById(R.id.layoutIndicators);
            holder.btnSave = (ImageButton) convertView.findViewById(R.id.postBtnSave);
            holder.btnHeart = (ImageButton) convertView.findViewById(R.id.postBtnHeart);
            holder.title = (TextView) convertView.findViewById(R.id.postTitle);
            holder.subtitle = (TextView) convertView.findViewById(R.id.postSubtitle);
            holder.username = (TextView) convertView.findViewById(R.id.postUsername);

            holder.numOfIndicators = sliderItem.getImg().length;
            setupIndicators(holder.numOfIndicators, holder.layoutIndicator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sliderViewPager.setOffscreenPageLimit(4);
        holder.sliderViewPager.setAdapter(new ImageSliderAdapter(context, sliderItem.getImg()));
        holder.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int pos) {
                super.onPageSelected(pos);
                setCurrentIndicator(pos, holder.layoutIndicator);
            }
        });
        holder.sliderViewPager.setFocusable(false);

        holder.title.setText(sliderItem.getTitle());

        holder.btnSave.setFocusable(false);
        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((ImageButton)v).isSelected()) {
                    ((ImageButton)v).setImageResource(R.drawable.ic_save);
                }
                else {
                    ((ImageButton)v).setImageResource(R.drawable.ic_save_selected);
                    Toast.makeText(context,"플랜을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                ((ImageButton)v).setSelected(!((ImageButton)v).isSelected());
            }
        });
        holder.btnHeart.setFocusable(false);
        holder.btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((ImageButton)v).isSelected()) {
                    ((ImageButton)v).setImageResource(R.drawable.ic_heart);
                }
                else {
                    ((ImageButton)v).setImageResource(R.drawable.ic_heart_filled);
                    Toast.makeText(context,"게시물을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                ((ImageButton)v).setSelected(!((ImageButton)v).isSelected());
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private ViewPager2 sliderViewPager;
        private LinearLayout layoutIndicator;
        private TextView title;
        private TextView subtitle;
        private TextView username;
        private ImageButton btnSave;
        private ImageButton btnHeart;
        public int numOfIndicators;
    }

    // 아이템 데이터 추가
    public void addItem(Integer[] img, String title, String type) {
        PlaceBannerItem item = new PlaceBannerItem(img, title, type);
        sliderList.add(item);
    }

    public void removeItem(int position) {
        sliderList.remove(position);
        this.notifyDataSetChanged();
    }

    //필터 : 타입에 해당하는 아이템(배너)을 리스트에 추가합니다
    public void addFilterType(String type) {
        for (PlaceBannerItem item : sliderList) {
            if(item.getType().equals(type)) {
                filteredItemList.add(0, item);
            }
        }
    }
    //필터 : 타입에 해당하는 아이템(배너)을 리스트에서 제거합니다
    public void removeFilterType(String type) {
        for (PlaceBannerItem item : sliderList) {
            if(item.getType().equals(type)) {
                filteredItemList.remove(item);
            }
        }
        notifyDataSetChanged();
    }

    //게시물 슬라이드 인디케이터(점) 생성
    public void setupIndicators(int count, LinearLayout layoutIndicator) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < count; i++) {
            indicators[i] = new ImageView(context);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.activity_main_layoutindicators_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0, layoutIndicator);
    }
    //현재 게시물 슬라이드 인디케이터(점)
    public void setCurrentIndicator(int position, LinearLayout layoutIndicator) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        context,
                        R.drawable.activity_main_layoutindicators_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        context,
                        R.drawable.activity_main_layoutindicators_inactive
                ));
            }
        }
    }
}

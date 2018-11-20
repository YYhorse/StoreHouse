package com.storehouse.www.Adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.storehouse.www.Activity.MainActivity;
import com.storehouse.www.R;
import com.storehouse.www.Utils.Json.ProductJson.ProductJson;
import com.storehouse.www.Utils.PopMessage.PopMessageUtil;
import com.storehouse.www.Utils.VoiceService.VoiceService;

import java.util.List;

public class GoodsCategoryAdapter extends BaseAdapter {
    private MainActivity mContext = null;
    private List<ProductJson.GoodsCategory> goodsCategoryList;
    ValueAnimator colorAnim;

    public GoodsCategoryAdapter(MainActivity mainActivity, List<ProductJson.GoodsCategory> GoodsCategory) {
        mContext = mainActivity;
        goodsCategoryList = GoodsCategory;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != goodsCategoryList) {
            count = goodsCategoryList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        ProductJson.GoodsCategory item = null;
        if (null != goodsCategoryList) {
            item = goodsCategoryList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView itemName_txt;
        LinearLayout item_layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_goodscategory, null);
            viewHolder.itemName_txt = (TextView) convertView.findViewById(R.id.GoodsCategray_txt);
            viewHolder.item_layout = (LinearLayout) convertView.findViewById(R.id.GoodsCategray_layout);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        //---------------获取最新数据--------------//
        viewHolder.itemName_txt.setText(goodsCategoryList.get(position).getGoods_category_name());
        //===========选中行变色=============//
        if (position == mContext.selectGoodsCategory) {
            viewHolder.itemName_txt.setTextColor(0xffffffff);
            final View finalConvertView = convertView;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    colorAnim = ObjectAnimator.ofInt(finalConvertView, "backgroundColor", 0x8fffffff, 0xffFF8080);
                    colorAnim.setDuration(300);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.setRepeatCount(0);
                    colorAnim.start();
                }
            });
        } else {
            viewHolder.itemName_txt.setTextColor(0xff666666);
            convertView.setBackgroundColor(0x8fffffff);
        }

        //----------------点击事件-----------------//
        viewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopMessageUtil.Log("点击" + goodsCategoryList.get(position).getGoods_category_name());
                if (mContext.selectGoodsCategory != position) {
                    mContext.selectGoodsCategory = position;
                    notifyDataSetChanged();
                    mContext.UpdataGoodsShow(goodsCategoryList.get(position).getGoods_category_id());
                }
            }
        });
        return convertView;
    }

    public void UpdataGoodsCategoryList(List<ProductJson.GoodsCategory> GoodsCategory) {
        this.goodsCategoryList = GoodsCategory;
        notifyDataSetChanged();
    }
}

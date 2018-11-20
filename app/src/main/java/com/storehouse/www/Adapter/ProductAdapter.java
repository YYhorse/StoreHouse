package com.storehouse.www.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.storehouse.www.Activity.BarcodeActivity;
import com.storehouse.www.Activity.MainActivity;
import com.storehouse.www.R;
import com.storehouse.www.Utils.Json.GoodsJson.GoodsJson;
import com.storehouse.www.Utils.Json.ProductJson.ProductJson;
import com.storehouse.www.Utils.PopMessage.PopMessageUtil;
import com.storehouse.www.Utils.SwitchPage.SwitchUtil;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private MainActivity mContext = null;
    private List<GoodsJson.Product_Info> productItemList;

    public ProductAdapter(MainActivity mainActivity, List<GoodsJson.Product_Info> ProductItem) {
        mContext = mainActivity;
        productItemList = ProductItem;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != productItemList) {
            count = productItemList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        GoodsJson.Product_Info item = null;
        if (null != productItemList) {
            item = productItemList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView itemName_txt, itemPrice_txt, itemQuantity_txt;
        LinearLayout item_layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_goods, null);
            viewHolder.itemName_txt = (TextView) convertView.findViewById(R.id.Item_goodsname_txt);
            viewHolder.itemPrice_txt = (TextView) convertView.findViewById(R.id.Item_goodsprice_txt);
            viewHolder.itemQuantity_txt = (TextView) convertView.findViewById(R.id.Item_goodsquantity_txt);
            viewHolder.item_layout = (LinearLayout) convertView.findViewById(R.id.Item_goods_layout);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        //---------------获取最新数据--------------//
        viewHolder.itemName_txt.setText(productItemList.get(position).getProduct_name());
        viewHolder.itemPrice_txt.setText("S$ " + productItemList.get(position).getProduct_price());
        viewHolder.itemQuantity_txt.setText("剩" + productItemList.get(position).getProduct_quantity());
        //----------------点击事件-----------------//
        viewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchUtil.switchActivity(mContext, BarcodeActivity.class)
                        .addString("ProductName",productItemList.get(position).getProduct_name())
                        .addString("ProductId",String.valueOf(productItemList.get(position).getProduct_id()))
                        .switchTo();
            }
        });
        return convertView;
    }

    public void UpdataGoodsList(List<GoodsJson.Product_Info> ProductItem) {
        this.productItemList = ProductItem;
        notifyDataSetChanged();
    }
}
package com.example.kadyr.tumar;

/**
 * Created by Archimed on 22/02/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kadyr.tumar.DataRepository.RoomPrice;
import com.example.kadyr.tumar.DataRepository.RoomType;

import java.util.List;

/**
 * Created by Archimed on 22/02/2018.
 */

public class RoomPriceAdapter extends BaseAdapter {
    private List<RoomPrice> data = null;
    private LayoutInflater mInflater;

    public RoomPriceAdapter(Context context, List<RoomPrice> prices){
        data=prices;

        mInflater = LayoutInflater.from(context);
    }



    public int getCount() {
        return data.size();
    }


    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void SetPrices(List<RoomPrice> newPrices){
        data=newPrices;
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = mInflater.inflate(R.layout.room_price_item, parent, false);
        TextView date =  rowView.findViewById(R.id.dateOperation);
        TextView roomTypeName =  rowView.findViewById(R.id.roomType);
        ImageView imageRoomType =  rowView.findViewById(R.id.roomImage);
        TextView price =  rowView.findViewById(R.id.price);

        RoomPrice roomPrice = data.get(position);
        RoomType roomType = RoomType.GetRoomType(roomPrice.IdRoomType);

        date.setText(String.valueOf(roomPrice.DateOperation.getDate()) + "/" +
                                 (roomPrice.DateOperation.getMonth()+1) + "/" +
                                 (roomPrice.DateOperation.getYear()+1900));
        roomTypeName.setText(roomType.getName());
        imageRoomType.setImageResource(roomType.getBedsCount()==2?R.drawable.ic_two_beds_horizontal_green:R.drawable.ic_three_beds_horizontal_green);
        price.setText(String.valueOf(roomPrice.Price));
        return rowView;
    }




}

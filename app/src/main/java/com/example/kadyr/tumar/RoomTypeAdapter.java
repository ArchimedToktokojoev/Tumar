package com.example.kadyr.tumar;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kadyr.tumar.DataRepository.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archimed on 23/02/2018.
 */

public class RoomTypeAdapter extends BaseAdapter implements Filterable {

    private List<String> originalData = null;
    private List<String> filteredData = null;
    private LayoutInflater mInflater;
    private RoomTypeAdapter.ItemFilter mFilter = new RoomTypeAdapter.ItemFilter();



    public RoomTypeAdapter(Context context, List<String> roomTypeNames){
        originalData=roomTypeNames;
        filteredData=roomTypeNames;
        mInflater = LayoutInflater.from(context);
    }
    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void SetItems(List<String> items){
        originalData=items;
        filteredData=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = mInflater.inflate(R.layout.simple_list_item_1, parent, false);
        TextView typeName =  rowView.findViewById(R.id.text1);
        String name = filteredData.get(position);

        typeName.setText(name);

        return rowView;
    }

    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = originalData;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }
    }

}

package com.example.kadyr.tumar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.DrawableUtils;
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
 * Created by Kadyr on 2/16/2018.
 */

public class ClientAdapter extends BaseAdapter implements Filterable {


    private List<Client> originalData = null;
    private List<Client> filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();



    public ClientAdapter(Context context, List<Client> clients){
        originalData=clients;
        filteredData=clients;
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

    public void SetClients(List<Client> newClients){
        originalData=newClients;
        filteredData=newClients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = mInflater.inflate(R.layout.client_list, parent, false);
        TextView name =  rowView.findViewById(R.id.name);
        TextView telephone =  rowView.findViewById(R.id.telephone);
        ImageView imageView =  rowView.findViewById(R.id.avatar);
        Client client = filteredData.get(position);

        name.setText(client.getName());
        telephone.setText(client.getTelephone());
        Bitmap avatar = client.getPicture();
        if(imageView==null) Log.e("axa", "here is null");
        else{
            if(avatar!=null){
                Log.e("axa", "here is not null");
                imageView.setImageBitmap(avatar);
            }
        }

        return rowView;
    }

    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Client>) results.values;
            notifyDataSetChanged();
                }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Client> list = originalData;

            int count = list.size();
            final ArrayList<Client> nlist = new ArrayList<Client>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
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

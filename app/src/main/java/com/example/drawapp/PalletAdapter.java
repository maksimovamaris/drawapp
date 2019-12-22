package com.example.drawapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class PalletAdapter extends BaseAdapter {
    private List<Pallet> pallets;
    private Context context;

    public PalletAdapter(Context context, List<Pallet> pallets) {
        this.context = context;
        this.pallets = pallets;
    }


    public static class ViewHolder {
        ImageView color;
    }

    @Override
    public int getCount() {
        return pallets.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View customView = convertView;
        final Pallet pallet = pallets.get(position);
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            customView = li.inflate(R.layout.pallet_item, null);
            holder.color = (ImageView) customView.findViewById(R.id.color);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        holder.color.setBackgroundColor(Color.parseColor(pallet.getColor()));

        return customView;

    }


}
package com.example.st_fashion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<ItemsDb> itemsList;

    public ItemListAdapter(Context context, int layout, ArrayList<ItemsDb> itemsList) {
        this.context = context;
        this.layout = layout;
        this.itemsList = itemsList;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtType, txtColor, txtSeason, txtName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.txtName = (TextView) row.findViewById(R.id.textName);
            holder.txtType = (TextView) row.findViewById(R.id.textType);
            holder.txtColor = (TextView) row.findViewById(R.id.textColor);
            holder.txtSeason = (TextView) row.findViewById(R.id.textSeason);
            holder.imageView = (ImageView) row.findViewById(R.id.imageItem);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

        ItemsDb items =itemsList.get(position);

        holder.txtName.setText(context.getResources().getString(R.string.list_name) + items.getName());
        holder.txtType.setText(context.getResources().getString(R.string.list_type) + items.getType());
        holder.txtColor.setText(context.getResources().getString(R.string.list_color) + items.getColor());
        holder.txtSeason.setText(context.getResources().getString(R.string.list_season) + items.getSeason());

        byte[] itemImage = items.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}

package com.example.st_fashion;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UniformListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<UniformsDb> uniformList;

    public UniformListAdapter(Context context, int layout, ArrayList<UniformsDb> uniformList) {
        this.context = context;
        this.layout = layout;
        this.uniformList = uniformList;
    }

    @Override
    public int getCount() {
        return uniformList.size();
    }

    @Override
    public Object getItem(int position) {
        return uniformList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageTop,imageBottom,imageShoes;
        TextView uniformNum,uniformSeason,uniformColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        UniformListAdapter.ViewHolder holder = new UniformListAdapter.ViewHolder();

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.uniformNum = (TextView) row.findViewById(R.id.textViewUniformNum);
            holder.uniformSeason = (TextView) row.findViewById(R.id.textViewUniformSeason);
            holder.uniformColor = (TextView) row.findViewById(R.id.textViewUniformColor);
            holder.imageTop = (ImageView) row.findViewById(R.id.imageViewUniTop);
            holder.imageBottom = (ImageView) row.findViewById(R.id.imageViewUniBottom);
            holder.imageShoes = (ImageView) row.findViewById(R.id.imageViewUniShoes);
            row.setTag(holder);
        }
        else
        {
            holder = (UniformListAdapter.ViewHolder) row.getTag();
        }

        UniformsDb uniforms = uniformList.get(position);

        byte[] itemImageTop = uniforms.getImageTop();
        byte[] itemImageBottom = uniforms.getImageBottom();
        byte[] itemImageShoes = uniforms.getImageShoes();

        int number = position + 1;
        String num = context.getResources().getString(R.string.choose_num);
        String season = context.getResources().getString(R.string.choose_season);
        String color = context.getResources().getString(R.string.choose_color);

        holder.uniformNum.setText(num + number);
        holder.uniformSeason.setText(season + uniforms.season);
        holder.uniformColor.setText(color + uniforms.color);

        Bitmap bitmap = BitmapFactory.decodeByteArray(itemImageTop,0,itemImageTop.length);
        holder.imageTop.setImageBitmap(bitmap);

        bitmap = BitmapFactory.decodeByteArray(itemImageBottom,0,itemImageBottom.length);
        holder.imageBottom.setImageBitmap(bitmap);

        bitmap = BitmapFactory.decodeByteArray(itemImageShoes,0,itemImageShoes.length);
        holder.imageShoes.setImageBitmap(bitmap);

        return row;
    }
}

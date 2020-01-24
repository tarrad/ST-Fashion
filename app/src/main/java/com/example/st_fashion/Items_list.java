package com.example.st_fashion;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.st_fashion.ui.create.CreateFragment;
import com.example.st_fashion.ui.create.UpdateItems;

import java.util.ArrayList;

public class Items_list extends Fragment {

    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_ITEMS_USERNAME = "username";
    private static final String TABLE_ITEMS_TYPE = "type";

    private static String sqlQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + TABLE_ITEMS_USERNAME +
            " = ? AND " + TABLE_ITEMS_TYPE + " = ?";

    GridView gridView;
    ArrayList<ItemsDb> list;
    ItemListAdapter adapter = null;

    private static byte[] photo;
    private static String type;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_items_list, container, false);

        gridView = (GridView) root.findViewById(R.id.gridViewItems);
        list = new ArrayList<>();
        adapter = new ItemListAdapter(getActivity(),R.layout.items_design,list);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsDb item = list.get(position);
                photo = item.image;
                type = item.type;

                if(type.equals("TOP"))
                {
                    UpdateItems.setPhotoTop(photo);
                }
                else if(type.equals("BOTTOM"))
                {
                    UpdateItems.setPhotoBottom(photo);
                }
                else if(type.equals("SHOES"))
                {
                    UpdateItems.setPhotoShoes(photo);
                }

                CreateFragment cf = new CreateFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.nav_host_fragment,cf,cf.getTag()).commit();
            }
        });

        //get data from db
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getData(sqlQuery);
        list.clear();
        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                String username = cursor.getString(1);
                String name = cursor.getString(2);
                String type = cursor.getString(3);
                String color = cursor.getString(4);
                String season = cursor.getString(5);
                byte[] image = cursor.getBlob(6);

                list.add(new ItemsDb(username,name,type,color,season,image));
                cursor.moveToNext();
            }
            adapter.notifyDataSetChanged();
        }

        if(list.size() == 0)
        {
            CreateFragment cf = new CreateFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.nav_host_fragment,cf,cf.getTag()).commit();
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.items_noitems),Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        return root;
    }

}

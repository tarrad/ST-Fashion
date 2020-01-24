package com.example.st_fashion.ui.choose;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.st_fashion.DatabaseHelper;

import com.example.st_fashion.R;
import com.example.st_fashion.UniformListAdapter;
import com.example.st_fashion.UniformsDb;

import java.util.ArrayList;

public class ChooseFragment extends Fragment {

    private static final String TABLE_UNIFORMS = "uniforms";
    private static final String TABLE_UNIFORMS_USERNAME = "username";

    private static String sqlQuery = "SELECT * FROM " + TABLE_UNIFORMS + " WHERE "
            + TABLE_UNIFORMS_USERNAME + " = ?";

    GridView gridView;
    ArrayList<UniformsDb> list;
    UniformListAdapter adapter = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_choose, container, false);

        gridView = (GridView) root.findViewById(R.id.gridViewUniforms);
        list = new ArrayList<>();
        adapter = new UniformListAdapter(getActivity(),R.layout.uniforms_design,list);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UniformsDb uniform = list.get(position);
            }
        });

        //get data from db
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getUniforms(sqlQuery);
        list.clear();
        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                String username = cursor.getString(1);
                byte[] top = cursor.getBlob(2);
                byte[] bottom = cursor.getBlob(3);
                byte[] shoes = cursor.getBlob(4);
                String season = cursor.getString(5);
                String color = cursor.getString(6);

                list.add(new UniformsDb(username,top,bottom,shoes,season.toUpperCase(),color.toUpperCase()));
                cursor.moveToNext();
            }
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        return root;
    }

}
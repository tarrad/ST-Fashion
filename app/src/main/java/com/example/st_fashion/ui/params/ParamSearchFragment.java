package com.example.st_fashion.ui.params;

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
import androidx.fragment.app.FragmentManager;

import com.example.st_fashion.DatabaseHelper;

import com.example.st_fashion.R;
import com.example.st_fashion.UniformListAdapter;
import com.example.st_fashion.UniformsDb;

import java.util.ArrayList;

public class ParamSearchFragment extends Fragment {

    private static final String TABLE_UNIFORMS = "uniforms";
    private static final String TABLE_UNIFORMS_USERNAME = "username";
    private static final String TABLE_UNIFORMS_SEASON = "season";
    private static final String TABLE_UNIFORMS_COLOR = "color";

    private static String sqlParam = "SELECT * FROM " + TABLE_UNIFORMS + " WHERE " + TABLE_UNIFORMS_USERNAME +
            " = ? AND " + TABLE_UNIFORMS_SEASON + " = ? AND " + TABLE_UNIFORMS_COLOR + " = ?";

    GridView gridView;
    ArrayList<UniformsDb> list;
    UniformListAdapter adapter = null;

    String season;
    String color;
    byte[] top;
    byte[] bottom;
    byte[] shoes;

    public ParamSearchFragment(String season,String color) {
        this.season = season;
        this.color = color;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_param_search, container, false);

        gridView = (GridView) root.findViewById(R.id.gridViewParamUniforms);
        list = new ArrayList<>();
        adapter = new UniformListAdapter(getActivity(),R.layout.uniforms_design,list);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UniformsDb uniform = list.get(position);
                top = uniform.getImageTop();
                bottom = uniform.getImageBottom();
                shoes = uniform.getImageShoes();
                UpdateUniforms.setPhotoTop(top);
                UpdateUniforms.setPhotoBottom(bottom);
                UpdateUniforms.setPhotoShoes(shoes);
                UpdateUniforms.setSeason(season);
                UpdateUniforms.setColor(color);

                ParamsFragment pf = new ParamsFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.nav_host_fragment,pf,pf.getTag()).commit();
            }
        });

        //get data from db
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getParamUniforms(sqlParam,season,color);
        list.clear();
        if(cursor.getCount() > 0)
        {
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
        }
        else
        {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.param_noresults),Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        return root;
    }

}
package com.example.st_fashion.ui.create;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.st_fashion.DatabaseHelper;
import com.example.st_fashion.Menu;
import com.example.st_fashion.R;
import com.example.st_fashion.Items_list;
import com.example.st_fashion.UniformsDb;
import com.example.st_fashion.ui.home.HomeFragment;

import java.io.ByteArrayOutputStream;

public class CreateFragment extends Fragment {

    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_ITEMS_USERNAME = "username";
    private static final String TABLE_ITEMS_TYPE = "type";

    private static String sqlRandomItem = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + TABLE_ITEMS_USERNAME +
            " = ? AND " + TABLE_ITEMS_TYPE + " = ?";

    private static String itemClicked;

    private Button btnShowTops,btnShowBottoms,btnShowShoes,btnSave,btnRandom;
    private ImageView imageViewTop,imageViewBottom,imageViewShoes;
    private EditText Season,Color;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create, container, false);

        btnShowTops = root.findViewById(R.id.btnTop);
        btnShowBottoms = root.findViewById(R.id.btnBottom);
        btnShowShoes = root.findViewById(R.id.btnShoes);
        btnSave = root.findViewById(R.id.btnSave);
        btnRandom = root.findViewById(R.id.btnRandom);
        imageViewTop = root.findViewById(R.id.imageViewTop);
        imageViewBottom = root.findViewById(R.id.imageViewBottom);
        imageViewShoes = root.findViewById(R.id.imageViewShoes);
        Season = root.findViewById(R.id.editTextOutSeason);
        Color = root.findViewById(R.id.editTextOutColor);

        if(UpdateItems.getPhotoTop() != null)
        {
            byte[] itemImage = UpdateItems.getPhotoTop();
            Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
            imageViewTop.setImageBitmap(bitmap);
        }
        if(UpdateItems.getPhotoBottom() != null)
        {
            byte[] itemImage = UpdateItems.getPhotoBottom();
            Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
            imageViewBottom.setImageBitmap(bitmap);
        }
        if(UpdateItems.getPhotoShoes() != null)
        {
            byte[] itemImage = UpdateItems.getPhotoShoes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
            imageViewShoes.setImageBitmap(bitmap);
        }

        btnShowTops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showItems("TOP");
            }
        });
        btnShowBottoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showItems("BOTTOM");
            }
        });
        btnShowShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showItems("SHOES");
            }
        });
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setRandomUniform();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String season = Season.getText().toString().toUpperCase();
                String color = Color.getText().toString().toUpperCase();
                if(!season.equals("SPRING") && !season.equals("SUMMER") && !season.equals("AUTUMN") && !season.equals("WINTER") )
                {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.create_format),Toast.LENGTH_SHORT).show();
                }
                else if(!color.equals("BLACK") && !color.equals("WHITE") && !color.equals("COLOR") )
                {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.create_format),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(hasImage(imageViewTop) && hasImage(imageViewBottom) && hasImage(imageViewShoes) )
                    {
                        saveItemsToUniform();
                    }
                    else
                    {
                        Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.create_chooseall),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }

    public void showItems(String string)
    {
        itemClicked = string;
        Items_list il = new Items_list();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.nav_host_fragment,il,il.getTag()).commit();
    }

    public static String getItemClicked()
    {
        return itemClicked;
    }

    public void saveItemsToUniform()
    {
        DatabaseHelper db = new DatabaseHelper(getContext());
        int result = 0;
        UniformsDb ud = new UniformsDb(Menu.getUsername(),imageToByte(imageViewTop),
                imageToByte(imageViewBottom),imageToByte(imageViewShoes),Season.getText().toString().toUpperCase(),
                Color.getText().toString().toUpperCase());
        result = db.addUniform(ud);
        if(result == 1)
        {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.create_addeduniform),Toast.LENGTH_SHORT).show();
            UpdateItems.setPhotoTop(null);
            UpdateItems.setPhotoBottom(null);
            UpdateItems.setPhotoShoes(null);
            HomeFragment home = new HomeFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.nav_host_fragment,home,home.getTag()).commit();
        }
        else
        {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.create_errorupload),Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasImage(ImageView view) {
        Drawable drawable = view.getDrawable();
        if(drawable instanceof BitmapDrawable)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public byte[] imageToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void setRandomUniform()
    {
        DatabaseHelper db = new DatabaseHelper(getContext());
        byte[] top = db.getRandomData(sqlRandomItem,"TOP");
        byte[] bottom = db.getRandomData(sqlRandomItem,"BOTTOM");
        byte[] shoes = db.getRandomData(sqlRandomItem,"SHOES");

        if(top != null && bottom != null && shoes != null)
        {
            UpdateItems.setPhotoTop(top);
            UpdateItems.setPhotoBottom(bottom);
            UpdateItems.setPhotoShoes(shoes);

            Bitmap bitmap = BitmapFactory.decodeByteArray(top,0,top.length);
            imageViewTop.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeByteArray(bottom,0,bottom.length);
            imageViewBottom.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeByteArray(shoes,0,shoes.length);
            imageViewShoes.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.create_errorgenerate),Toast.LENGTH_SHORT).show();
        }
    }

}
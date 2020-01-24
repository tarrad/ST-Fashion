package com.example.st_fashion.ui.add_items;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.st_fashion.DatabaseHelper;
import com.example.st_fashion.ItemsDb;
import com.example.st_fashion.Menu;
import com.example.st_fashion.R;
import com.example.st_fashion.ui.home.HomeFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {

    private static final int IMAGE_PICK_PHONE = 1;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_FROM_CAMERA = 1001;
    private static boolean cameraOn = true;

    private Button btnAddDevice,btnAddCamera,btnSave;
    private ImageView Photo;
    private Uri imageFromCamera;
    private EditText EditTextName,EditTextType,EditTextColor,EditTextSeason;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add, container, false);

        btnAddDevice = root.findViewById(R.id.btnAddDevice);
        btnAddCamera = root.findViewById(R.id.btnAddCamera);
        btnSave = root.findViewById(R.id.btnSaveItem);
        Photo = root.findViewById(R.id.imageViewItem);
        EditTextName = root.findViewById(R.id.editTextName);
        EditTextType = root.findViewById(R.id.editTextType);
        EditTextColor = root.findViewById(R.id.editTextColor);
        EditTextSeason = root.findViewById(R.id.editTextSeason);

        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cameraOn = false;
                pickImageFromGallery();
            }
        });

        btnAddCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cameraOn = true;
                pickImageFromCamera();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveItemToDB();
            }
        });

        return root;
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Pick Item"), IMAGE_PICK_PHONE);
    }

    public void pickImageFromCamera() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED)
            {
                //request permission
                String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_CODE);
            }
            else
            {
                //got permission already
                openCamera();
            }
        }
        else
        {
            // no need to ask
            openCamera();
        }
    }

    public void saveItemToDB() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        // check if everything is initialized
        boolean okToUpload = true;
        String name,type,color,season;
        name = EditTextName.getText().toString().toUpperCase();
        type = EditTextType.getText().toString().toUpperCase();
        color = EditTextColor.getText().toString().toUpperCase();
        season =  EditTextSeason.getText().toString().toUpperCase();

        if(name.equals("") || name.length() > 20)
        {
            okToUpload = false;
        }
        else if(!type.equals("TOP") && !type.equals("BOTTOM") && !type.equals("SHOES") )
        {
            okToUpload = false;
        }
        else if(!color.equals("BLACK") && !color.equals("WHITE") && !color.equals("COLOR") )
        {
            okToUpload = false;
        }
        else if(!season.equals("SPRING") && !season.equals("SUMMER") && !season.equals("AUTUMN") && !season.equals("WINTER") )
        {
            okToUpload = false;
        }
        else if(!hasImage(Photo))
        {
            okToUpload = false;
        }

        if(okToUpload)
        {
            int result = 0;
            ItemsDb item = new ItemsDb(Menu.getUsername(),name,
                                    type,color,season,imageToByte(Photo));
            result = db.addItem(item);
            if(result == 1)
            {
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.add_added),Toast.LENGTH_SHORT).show();
                HomeFragment home = new HomeFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.nav_host_fragment,home,home.getTag()).commit();
            }
            else
            {
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.add_uploadfailed),Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.add_wrongformat),Toast.LENGTH_LONG).show();
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

    public void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Pic from camera");
        imageFromCamera = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageFromCamera);
        startActivityForResult(intent,IMAGE_FROM_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else{
                    Toast.makeText(getActivity(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(!cameraOn)
        {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_PHONE)
            {
                Uri imageFromPhone = data.getData();
                try
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageFromPhone);
                    Photo.setImageBitmap(bitmap);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && requestCode == IMAGE_FROM_CAMERA)
            {
                try
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageFromCamera);
                    Photo.setImageBitmap(bitmap);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
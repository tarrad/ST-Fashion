package com.example.st_fashion.ui.params;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.st_fashion.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ParamsFragment extends Fragment {

    final static int SEASON_CODE = 200;
    final static int COLOR_CODE = 201;

    EditText Eseason,Ecolor;
    Button search,share;
    ImageView micSeason,micColor,top,bottom,shoes;
    TextView Tseason,Tcolor;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_params, container, false);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());

        Eseason = (EditText) root.findViewById(R.id.editTextOutSeason);
        Ecolor = (EditText) root.findViewById(R.id.editTextOutColor);
        search = (Button) root.findViewById(R.id.btnSeach);
        share = (Button) root.findViewById(R.id.btnShare);
        micSeason = (ImageView) root.findViewById(R.id.imageViewMic1);
        micColor = (ImageView) root.findViewById(R.id.imageViewMic2);
        top = (ImageView) root.findViewById(R.id.imageViewUniTop);
        bottom = (ImageView) root.findViewById(R.id.imageViewUniBottom);
        shoes = (ImageView) root.findViewById(R.id.imageViewUniShoes);
        Tseason = (TextView) root.findViewById(R.id.textViewParSeason);
        Tcolor = (TextView) root.findViewById(R.id.textViewParColor);

        if(UpdateUniforms.getPhotoTop() != null)
        {
            byte[] itemImage = UpdateUniforms.getPhotoTop();
            Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
            top.setImageBitmap(bitmap);
        }
        if(UpdateUniforms.getPhotoBottom() != null)
        {
            byte[] itemImage = UpdateUniforms.getPhotoBottom();
            Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
            bottom.setImageBitmap(bitmap);
        }
        if(UpdateUniforms.getPhotoShoes() != null)
        {
            byte[] itemImage = UpdateUniforms.getPhotoShoes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
            shoes.setImageBitmap(bitmap);
        }
        if(UpdateUniforms.getSeason() != null)
        {
            String season = getActivity().getResources().getString(R.string.choose_season);
            Tseason.setText(season + UpdateUniforms.getSeason());
        }
        if(UpdateUniforms.getColor() != null)
        {
            String color = getActivity().getResources().getString(R.string.choose_color);
            Tcolor.setText(color + UpdateUniforms.getColor());
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                searchForParams();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                share();
            }
        });

        micSeason.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                VoiceToText(SEASON_CODE);
            }
        });

        micColor.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                VoiceToText(COLOR_CODE);
            }
        });

        return root;
    }

    public void searchForParams()
    {
        String season = Eseason.getText().toString().toUpperCase();
        String color = Ecolor.getText().toString().toUpperCase();

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
            ParamSearchFragment param = new ParamSearchFragment(season,color);
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.nav_host_fragment,param,param.getTag()).commit();
        }
    }

    public void share()
    {
        // set null
        UpdateUniforms.setPhotoTop(null);
        UpdateUniforms.setPhotoBottom(null);
        UpdateUniforms.setPhotoShoes(null);
        UpdateUniforms.setSeason(null);
        UpdateUniforms.setColor(null);

        //start share
        if(hasImage(top) && hasImage(bottom) && hasImage(shoes))
        {
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.param_sharesucces),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.param_sharecancel),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.param_sharefail),Toast.LENGTH_SHORT).show();
                }
            });

            Bitmap bitTop = ((BitmapDrawable)top.getDrawable()).getBitmap();
            Bitmap bitBottom = ((BitmapDrawable)bottom.getDrawable()).getBitmap();
            Bitmap bitShoes = ((BitmapDrawable)shoes.getDrawable()).getBitmap();

            SharePhoto shareTop = new SharePhoto.Builder()
                    .setBitmap(bitTop)
                    .build();
            SharePhoto shareBottom = new SharePhoto.Builder()
                    .setBitmap(bitBottom)
                    .build();
            SharePhoto shareShoes = new SharePhoto.Builder()
                    .setBitmap(bitShoes)
                    .build();

            List<SharePhoto> sharePhotos = new ArrayList<>();
            sharePhotos.add(shareTop);
            sharePhotos.add(shareBottom);
            sharePhotos.add(shareShoes);

            if(ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhotos(sharePhotos)
                        .build();
                shareDialog.show(content);
            }
        }
        else
        {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.param_selectout),Toast.LENGTH_SHORT).show();
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

    public void VoiceToText(int code)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, code);
        }
        else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.param_device), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SEASON_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Eseason.setText(result.get(0));
                }
                break;
            case COLOR_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Ecolor.setText(result.get(0));
                }
                break;
        }
    }
}
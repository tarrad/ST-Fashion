package com.example.st_fashion;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.st_fashion.ui.create.UpdateItems;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Menu extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    static String navUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each Menu ID as a set of Ids because each
        // Menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add_items, R.id.nav_create, R.id.nav_choose,
                R.id.nav_params,R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.TextViewUsername);
        TextView navEmail = (TextView) headerView.findViewById(R.id.TextViewEmail);
        navUserName = getIntent().getExtras().getString("username");
        navUsername.setText(navUserName);
        navEmail.setText(getIntent().getExtras().getString("email"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void Logout(MenuItem item) {
        UpdateItems.setPhotoTop(null);
        UpdateItems.setPhotoBottom(null);
        UpdateItems.setPhotoShoes(null);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static String getUsername()
    {
        return navUserName;
    }
}

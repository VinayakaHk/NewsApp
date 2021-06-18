package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsapp.Model.ApiClient;
import com.example.newsapp.Model.Articles;
import com.example.newsapp.Model.Headlines;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


public class HomeActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etQuery;
    Button btnSearch,btnAboutUs;
    Dialog dialog;
    final String API_KEY = "e35e8615714048f1a297be3deab00eec";
    Adapter adapter;
    List<Articles>  articles = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);

        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);
        btnAboutUs = findViewById(R.id.aboutUs);
        dialog = new Dialog(HomeActivity.this);





        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String country = getCountry();
        final String catagory= getCatagory();


        swipeRefreshLayout.setOnRefreshListener(() -> retrieveJson("","in","",API_KEY));
        retrieveJson("","in","",API_KEY);

        etQuery.setOnKeyListener((v, keyCode, event) -> {
            etQuery.setImeActionLabel("Enter key", KeyEvent.KEYCODE_ENTER);
            etQuery.setImeActionLabel("Action down key", KeyEvent.ACTION_DOWN);
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                btnSearch.callOnClick();

            }
            return false;
        });
        btnSearch.setOnClickListener(v -> {

            if (!etQuery.getText().toString().equals("")){
                swipeRefreshLayout.setOnRefreshListener(() -> retrieveJson(etQuery.getText().toString(),country,"",API_KEY));
                retrieveJson(etQuery.getText().toString(),country,"",API_KEY);
            }else{
                swipeRefreshLayout.setOnRefreshListener(() -> retrieveJson("",country,"",API_KEY));
                retrieveJson("",country,"",API_KEY);
            }
        });



        btnAboutUs.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(),RecentActivity.class);
            startActivity(intent);
        });
        //hamburger.setOnClickListener(v -> {
            //Intent intent = new Intent(getApplicationContext(),RecentActivity.class);
            //startActivity(intent);
        //
    }

    public void retrieveJson(String query ,String country, String catagory, String apiKey){
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if (!etQuery.getText().toString().equals("")){
            call= ApiClient.getInstance().getApi().getSpecificData(query,apiKey);
        }else{
            call= ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(@NotNull Call<Headlines> call, @NotNull Response<Headlines> response) {
                try {
                    if (response.isSuccessful() && Objects.requireNonNull(response.body()).getArticles() != null) {
                        swipeRefreshLayout.setRefreshing(false);
                        articles.clear();
                        articles = response.body().getArticles();
                        adapter = new Adapter(HomeActivity.this, articles);
                        recyclerView.setAdapter(adapter);
                    } else
                        Toast.makeText(HomeActivity.this, "There are no Articles", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    System.out.println("Exception e " + e);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Headlines> call, @NotNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(HomeActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }


    public String getCatagory ()
    {
        String catagory="default";
        return catagory;
    }

    /*public void showDialog(){
        Button btnClose;
        dialog.setContentView(R.layout.about_us_pop_up);
        dialog.show();
        btnClose = dialog.findViewById(R.id.close);

        btnClose.setOnClickListener(v -> dialog.dismiss());

    }*/


}
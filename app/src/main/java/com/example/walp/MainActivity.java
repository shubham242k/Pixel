package com.example.walp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Images> imagesList;
    private int pageno = 1;
    boolean isScrolling = false;
    ProgressBar pBar;
    private int currentItems,totalItems,scrollOutItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        pBar = findViewById(R.id.pBar);
        imagesList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling & (currentItems+scrollOutItems) == totalItems){
                    isScrolling = false;
                    pBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            fetchImages();
                        }
                    },2000);

                }
            }
        });

        imageAdapter = new ImageAdapter(imagesList,getApplicationContext());
        recyclerView.setAdapter(imageAdapter);
        fetchImages();

    }

    public void fetchImages(){
        String listner = "https://api.unsplash.com/collections?page="+ pageno +"&per_page=20&client_id=IfduC4Ae1iQSzdllwCObyVqX86rTGak4cwGZjZzliTY";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, listner, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//
                try {
                    JSONArray wholeImages = new JSONArray(response);
//
                    for(int i = 0;i<wholeImages.length();i++){
                        JSONObject image = wholeImages.getJSONObject(i);
//
                        JSONObject cover_photo = image.getJSONObject("cover_photo");
                        JSONObject imageUrls = cover_photo.getJSONObject("urls");

                        String url = imageUrls.getString("raw");
                        imagesList.add(new Images(url));
                    }

                    imageAdapter.notifyDataSetChanged();
                    pageno++;
                } catch (JSONException e) {
                    Log.d("error12","Starts");
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error12",error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        pBar.setVisibility(View.GONE);

    }
}
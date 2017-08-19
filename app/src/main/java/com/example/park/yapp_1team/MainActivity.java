package com.example.park.yapp_1team;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.park.yapp_1team.network.movie_info_crawling;
import com.example.park.yapp_1team.items.movieListItem;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.park.yapp_1team.utils.URL.NAVER_SELECT;
import static com.example.park.yapp_1team.utils.URL.NAVER_URL;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    MainRecyclerviewAdapter mainRecyclerviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, SplashActivity.class));

        ArrayList<movieListItem> data_array = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_start);

        movie_info_crawling test = new movie_info_crawling(NAVER_URL, NAVER_SELECT);

        try{
            data_array = (ArrayList<movieListItem>)test.execute().get();
        } catch (ExecutionException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        mainRecyclerviewAdapter = new MainRecyclerviewAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        for(int i=0;i<data_array.size();i++) {
            mainRecyclerviewAdapter.add(data_array.get(i));
        }

        recyclerView.setAdapter(mainRecyclerviewAdapter);

    }


    public class MainRecyclerviewAdapter extends RecyclerView.Adapter<MainRecyclerviewAdapter.ViewHolder>
    {

        ArrayList<movieListItem> datas = new ArrayList<>();

        public void add(movieListItem movieListItem)
        {
            datas.add(movieListItem);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
                ImageView imageView;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image1_recyclerview2);
            }
        }

        @Override
        public MainRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_main,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MainRecyclerviewAdapter.ViewHolder holder, int position) {
            movieListItem movieListItem = datas.get(position);
            Glide.with(getApplicationContext()).load(movieListItem.getURL()).into(holder.imageView);
            final Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
            intent.putExtra("MOVIENAME",movieListItem.getName());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }


}

package com.march.quickrv;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.march.quickrvlibs.RvAdapter;
import com.march.quickrvlibs.TypeRvAdapter;
import com.march.quickrvlibs.SimpleRvAdapter;
import com.march.quickrvlibs.module.LoadMoreModule;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context self = MainActivity.this;
    Handler handler = new Handler();
    private List<Demo> demos;
    private TypeRvAdapter<Demo> quickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rvquick_activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        demos = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            demos.add(new Demo(i, i + " <- this is"));
        }
//        singleTypeTest();
//        multiTypeTest();
        hfTest();
//        preLoadTest();
    }

    private void singleTypeTest() {
        SimpleRvAdapter simpleAdapter = new SimpleRvAdapter<Demo>(self, demos, R.layout.rvquick_item_a) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos) {
                holder.setText(R.id.item_a_tv, data.title);
            }
        };

        simpleAdapter.setOnItemClickListener(new OnItemClickListener<RvViewHolder>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder) {
                Toast.makeText(self, "click " + pos, Toast.LENGTH_SHORT).show();
            }
        });
        simpleAdapter.addHeaderOrFooter(R.layout.rvquick_header, R.layout.rvquick_footer, recyclerView);
        addManager();
        recyclerView.setAdapter(simpleAdapter);
    }

    private void multiTypeTest() {

        quickAdapter = new TypeRvAdapter<Demo>(self, demos) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                if (type == 0)
                    holder.setText(R.id.item_a_tv, data.title);
                else
                    holder.setText(R.id.item_b_tv, data.title + "www");
            }
        };
        quickAdapter.addType(0, R.layout.rvquick_item_a)
                .addType(1, R.layout.rvquick_item_b);

        quickAdapter.setOnItemClickListener(new OnItemClickListener<RvViewHolder>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder) {
                Toast.makeText(self, "click " + pos, Toast.LENGTH_SHORT).show();
            }
        });
        addManager();
        recyclerView.setAdapter(quickAdapter);
    }


    private void hfTest() {
        addManager();
        quickAdapter = new TypeRvAdapter<Demo>(self, demos) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                if (type == 0)
                    holder.setText(R.id.item_a_tv, data.title);
                else
                    holder.setText(R.id.item_b_tv, data.title + "www");
            }

            @Override
            public void bindLisAndData4Header(RvViewHolder header) {
                super.bindLisAndData4Header(header);
                header.setText(R.id.header_tv, "Header");
            }

            @Override
            public void bindLisAndData4Footer(RvViewHolder footer) {
                super.bindLisAndData4Footer(footer);
                footer.setText(R.id.footer_tv, "Footer");
            }
        };

        quickAdapter.addType(0, R.layout.rvquick_item_a)
                .addType(1, R.layout.rvquick_item_b);

        getLayoutInflater().inflate(R.layout.rvquick_header, null);
        getLayoutInflater().inflate(R.layout.rvquick_footer, null);

        quickAdapter.addHeaderOrFooter(
                getLayoutInflater().inflate(R.layout.rvquick_header, recyclerView, false)
                , getLayoutInflater().inflate(R.layout.rvquick_footer, recyclerView, false)
        );
//        quickAdapter.addHeaderOrFooter(R.layout.rvquick_header, R.layout.rvquick_footer,recyclerView);

        quickAdapter.setOnItemClickListener(new OnItemClickListener<RvViewHolder>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder) {
                Toast.makeText(self, "click " + pos, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(quickAdapter);
    }

    private void preLoadTest() {

        quickAdapter = new TypeRvAdapter<Demo>(self, demos) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                if (type == 0)
                    holder.setText(R.id.item_a_tv, data.title);
                else
                    holder.setText(R.id.item_b_tv, data.title + "www");
            }

            @Override
            public void bindListener4View(RvViewHolder holder, int type) {
                super.bindListener4View(holder, type);
            }

            @Override
            public void bindLisAndData4Header(RvViewHolder header) {
                super.bindLisAndData4Header(header);
                header.setText(R.id.header_tv, "Header");
            }

            @Override
            public void bindLisAndData4Footer(RvViewHolder footer) {
                super.bindLisAndData4Footer(footer);
                footer.setText(R.id.footer_tv, "Footer");
            }
        };

        quickAdapter.addType(0, R.layout.rvquick_item_a)
                .addType(1, R.layout.rvquick_item_b);

        quickAdapter.addHeaderOrFooter(R.layout.rvquick_header, R.layout.rvquick_footer, recyclerView);

        quickAdapter.setOnItemClickListener(new OnItemClickListener<RvViewHolder>() {
            @Override
            public void onItemClick(int pos, RvViewHolder holder) {
                Toast.makeText(self, "click " + pos, Toast.LENGTH_SHORT).show();
            }
        });
        quickAdapter.addLoadMoreModule(2, new LoadMoreModule.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("chendong", "4秒后加载新的数据");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            demos.add(new Demo(i, "new " + i));
                        }
                        quickAdapter.notifyDataSetChanged();
                        quickAdapter.finishLoad();
                    }
                }, 4000);
            }
        });
        addManager();
        recyclerView.setAdapter(quickAdapter);
    }

    private void addManager() {
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

    }
}

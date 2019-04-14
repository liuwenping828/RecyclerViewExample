package com.lwp.horizontalcarouselrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ItemAdapter adapter = new ItemAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HorizontalCarouselRecyclerView recyclerView = findViewById(R.id.item_list);
        recyclerView.initialize(adapter);

        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(R.id.list_item_background);
        ids.add(R.id.list_item_text);
        recyclerView.setViewsToChangeColor(ids);

        adapter.setItems(getLargeListOfItems());
    }

    private List<Item> getLargeListOfItems() {

        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(new Item("girl:" + i, R.drawable.a));
        }

        return items;
    }


}

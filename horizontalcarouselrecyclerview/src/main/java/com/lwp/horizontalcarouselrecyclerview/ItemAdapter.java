package com.lwp.horizontalcarouselrecyclerview;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<Item> items = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}


class ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private ImageView icon;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.list_item_text);
        icon = itemView.findViewById(R.id.list_item_icon);
    }

    public void bind(Item item) {
        title.setText(item.title);
        icon.setImageResource(item.icon);
    }
}

class Item {
    public String title;
    @DrawableRes
    public int icon;

    public Item(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }
}

package com.example.wiprodemo.mainactivitymodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wiprodemo.R;
import com.example.wiprodemo.model.Row;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private List<Row> imageList;
    private Context context;

    public ImageListAdapter(List<Row> imageList) {
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.image_list_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Row row = imageList.get(position);
        holder.title.setText(row.getTitle());
        holder.titleDescription.setText(row.getDescription());


        Picasso.get()
                .load(row.getImageHref())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .transform(new CropSquareTransformation())
                .into(holder.image);

    }
    public void setItemModelList(List<Row> itemModels) {
        imageList.clear();
        imageList = itemModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public AppCompatTextView title, titleDescription;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.imageView);
            titleDescription = view.findViewById(R.id.title_description);
            title = view.findViewById(R.id.title);
        }
    }
}
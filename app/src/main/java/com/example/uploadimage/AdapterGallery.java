package com.example.uploadimage;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.MyViewHolder> {

    private ArrayList<Uri> arrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView iv_gallery, iv_close;

        MyViewHolder(View view) {
            super(view);
            iv_gallery = view.findViewById(R.id.iv_gallery);
            iv_close = view.findViewById(R.id.iv_gallery_close);
        }
    }

    public AdapterGallery(ArrayList<Uri> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gallery, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        Picasso.get()
                .load(arrayList.get(position))
                .into(holder.iv_gallery);

        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position <= arrayList.size()) {
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, arrayList.size());
                }
            }
        });
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
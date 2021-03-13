package com.example.walp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private List<Images> imagesList;
    private Context context;
    private  PopUpImageClass popUpImageClass;

    public ImageAdapter(List<Images> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImageView(imagesList.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpImageClass = new PopUpImageClass() {
                    @Override
                    public void onPopup() {
                        this.showPopupWindow(v,context,imagesList.get(position).getImageUrl());
                    }

                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.cancelRoomButton){
                            popUpImageClass.dismissPopup(v);
                        }
                    }
                };

                popUpImageClass.onPopup();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }

        public void setImageView(Images image) {
            Glide.with(context).load(image.getImageUrl()).into(imageView);
        }
    }
}

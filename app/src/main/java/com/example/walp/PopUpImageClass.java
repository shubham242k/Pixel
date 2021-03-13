package com.example.walp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public abstract class PopUpImageClass implements View.OnClickListener {


    protected PopupWindow popupWindow;
    protected ImageView cancelRoomButton;
    private ImageView bigImageView;
    private String imageUrl ;
    private  Context c;
    public abstract void onPopup();


    //PopupWindow display method
    public void showPopupWindow(final View view,Context c,String url) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        this.c =c;
        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;
        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        //Set the location of the window on the screen
        imageUrl = url;
        initializeUI(popupView,view);

    }

    private void initializeUI(View popupView, View parentView) {
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
//        popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        dimBehind(popupWindow);

        bigImageView = popupView.findViewById(R.id.bigImageView);
        Glide.with(c).load(imageUrl).into(bigImageView);
        cancelRoomButton = popupView.findViewById(R.id.cancelRoomButton);
        cancelRoomButton.setOnClickListener(this);
    }

    protected void dismissPopup(View v){
        popupWindow.dismiss();
//        Toast.makeText(v.getContext(), "Wow, cancel action button", Toast.LENGTH_SHORT).show();
    }

    private static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }

}


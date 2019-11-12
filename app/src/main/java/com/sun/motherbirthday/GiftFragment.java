package com.sun.motherbirthday;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.graphics.Color.*;
import static com.sun.motherbirthday.R.color.colorAccent;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftFragment extends Fragment{

    AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private Animator textanimator;
    DisplayMetrics displayMetrics;
    LinearLayout.LayoutParams textlayoutParams;

    RecyclerView recyclerView;
    TextView tv_gift_title;
    List<Gift> giftDataList;
    DataAdapter dataAdapter;

    boolean isClick = false;
    int isClickNumber;
    private Interpolator interpolator = new LinearInterpolator ();

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_gift , container , false );
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view , savedInstanceState );

        WindowManager windowManager=(WindowManager) getContext ().getSystemService ( Context.WINDOW_SERVICE );
        Display display =windowManager.getDefaultDisplay ();
        displayMetrics = new DisplayMetrics ();
        display.getMetrics ( displayMetrics );

        giftDataList = new ArrayList<>();
        giftDataList.add ( new Gift (1,"iPhone11",R.drawable.iphone11) );
        giftDataList.add ( new Gift (2,"黃金",R.drawable.gold) );
        giftDataList.add ( new Gift (3,"新台幣",R.drawable.money) );
        giftDataList.add ( new Gift (4,"豪華度假飯店度假",R.drawable.house) );
        giftDataList.add ( new Gift (5,"A5和牛大餐",R.drawable.wagyu) );
        recyclerView = view.findViewById ( R.id.recyclerView );
        tv_gift_title = view.findViewById ( R.id.tv_gift_title );
        textlayoutParams =(LinearLayout.LayoutParams) tv_gift_title.getLayoutParams ();
        textanimator = getAnimSet(tv_gift_title);
        textanimator.start ();
//        Log.i( "textlayoutParams", String.valueOf ( textlayoutParams.height ) );

        recyclerView.setLayoutManager ( new LinearLayoutManager(getContext ()) );
        dataAdapter = new DataAdapter(getContext (), giftDataList);
        recyclerView.setAdapter (dataAdapter );
        recyclerView.setOnFlingListener(null);

        if (Userset.ISNOTFIRST){
            isClick = true;
        }

        if (audioManager == null) {
            audioManager = (AudioManager) getActivity ().getSystemService(AUDIO_SERVICE);
        }
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create( getActivity(), R.raw.happybirthday);
        }
        if (Userset.ISNOTFIRST){
            mediaPlayer.start();
        }

    }

    public int getStatusBarHeight(){
        int result = 0;
        int resourceId = getResources ().getIdentifier ( "status_bar_height","dimen","android" );
        if (resourceId > 0){
            result = getResources ().getDimensionPixelSize ( resourceId );
        }

        return result;
    }
    private AnimatorSet getAnimSet(TextView textView) {
        /* 建立集合動畫物件以加入多種動畫 */
        AnimatorSet animatorSet = new AnimatorSet();
        /* 播放完after()的動畫後再播放play()的動畫 */
        animatorSet.playTogether ( new ObjectAnimator[]{getTranslateAnim ( ) , getColorAnim ( textView)} );
        return animatorSet;
    }

    /**
     * 建立位移動畫並套用當前特效
     */
    private ObjectAnimator getTranslateAnim() {
        /* 第1個參數為套用動畫的對象 - ivSoccer物件；
           第2個參數為要套用的屬性 - TRANSLATION_X代表水平位移；
           第3個參數為屬性設定值 - 從原點向右移動800像素 */
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(tv_gift_title, View.TRANSLATION_X, 0, displayMetrics.widthPixels - textlayoutParams.width);
        /* 設定播放時間(預設300毫秒)為500毫秒，就是0.5秒 */
        objectAnimator.setDuration(1500);
        /* 1代表重複播放1次；預設為0代表不重播；INFINITE代表無限重播 */
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        /* REVERSE代表播放完畢後會反向播放；重複播放至少要設定1次才會反向播放 */
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        /* 套用特效 */
        objectAnimator.setInterpolator(interpolator);
        return objectAnimator;
    }

    private ObjectAnimator getColorAnim(TextView textView) {
        ObjectAnimator objectAnimator;
        /* 設定背景色從白色變成紅色再還原，API 21開始可呼叫ofArgb()設定 */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            objectAnimator = ObjectAnimator.ofArgb( textView, "TextColor",
                                                    BLUE, RED);
        } else {
            objectAnimator =
                    ObjectAnimator.ofObject( textView, "TextColor",
                                             new ArgbEvaluator (), BLUE, RED);
        }
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount( ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setInterpolator(interpolator);
        return objectAnimator;
    }

    private ObjectAnimator getScaleAnim(View view) {
        /* 設定水平縮放：從原大小(1.0f)縮小至10%(0.1f)後再放大至2倍(2.0f)後再縮小至原大小 */
        PropertyValuesHolder holderX =
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 0.1f, 2.0f, 1.0f);
        /* 設定垂直縮放跟水平縮放效果一樣 */
        PropertyValuesHolder holderY =
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 0.1f, 2.0f, 1.0f);
        /* 將水平與垂直縮放等2個屬性設定套用在同一個ivSoccer物件上 */
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofPropertyValuesHolder(view, holderX, holderY);
        objectAnimator.setDuration(2000);
        objectAnimator.setInterpolator(interpolator);
        objectAnimator.setRepeatCount( ValueAnimator.INFINITE);
        return objectAnimator;
    }
    private AnimatorSet getAnimSet_title(TextView textView) {
        /* 建立集合動畫物件以加入多種動畫 */
        AnimatorSet animatorSet = new AnimatorSet();
        /* 播放完after()的動畫後再播放play()的動畫 */
        animatorSet.playTogether ( new ObjectAnimator[]{getScaleAnim(textView) , getColorAnim ( textView)} );
        return animatorSet;
    }
    private void clickDialog(Gift gift ,Boolean isgift){
        Animator titleanimator;
        final Dialog dialog = new Dialog( getContext ());

        dialog.setContentView(R.layout.dialog_gift);
        dialog.setCancelable(false);
        TextView tv_dialog_title = dialog.findViewById ( R.id.tv_dialog_title );
        if (isgift){
            tv_dialog_title.setVisibility ( View.VISIBLE );
            titleanimator = getAnimSet_title ( tv_dialog_title );
            titleanimator.start ();
        }

        TextView tv_Giftname = dialog.findViewById ( R.id.tv_Giftname );
        ImageView iv_gift = dialog.findViewById ( R.id.iv_gift );
        Button bt_ok = dialog.findViewById ( R.id.bt_ok );
        bt_ok.setOnClickListener ( new View.OnClickListener ( ){
            @Override
            public void onClick(View view) {
                dialog.dismiss ();
            }
        } );

        if(gift != null){
            tv_Giftname.setText ( gift.name );
            iv_gift.setImageResource ( gift.image );
        }
        dialog.show ();
    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.giftViewHolder>{

        Context context;
        List<Gift> giftDataList;
        private Animator animator;

        DataAdapter(Context context, List<Gift> giftDataList){
            this.context = context;
            this.giftDataList = giftDataList;
        }

        private class giftViewHolder extends RecyclerView.ViewHolder{
            LinearLayout linearlayout;
            ImageView ivGift;
            ImageView ivGiftShow;

            public giftViewHolder(@NonNull View itemView) {
                super ( itemView );
                linearlayout = itemView.findViewById ( R.id.linearlayout );
                ivGift = itemView.findViewById ( R.id.ivGift );
                ivGiftShow =itemView.findViewById ( R.id.ivGiftShow);

                ViewGroup.LayoutParams layoutParams =linearlayout.getLayoutParams ();
                layoutParams.height = (displayMetrics.heightPixels- textlayoutParams.height -getStatusBarHeight()) / 5;
            }
        }
        @Override
        public int getItemCount() {
            return giftDataList.size ();
        }
        @NonNull
        @Override
        public giftViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
            View itemView=LayoutInflater.from(context).inflate( R.layout.item_view, parent, false);
            return new giftViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final giftViewHolder holder , final int position) {
            final Gift giftData = giftDataList.get ( position );
            holder.ivGiftShow.setImageResource ( giftData.image );
            if (position == Userset.ISCLICKkNUMBER){
                holder.itemView.setBackgroundColor ( RED );
            }
            if (isClick && !Userset.ISNOTFIRST){
                animator = getAnimSet ( holder.ivGift );
                animator.start ();
                Handler handler=new Handler ( );
                handler.postDelayed (new Runnable ( ){
                    @Override
                    public void run() {

                        holder.ivGiftShow.setVisibility ( View.VISIBLE );

                    }
                }, 2000);

            }else if (Userset.ISNOTFIRST){
                holder.ivGift.setVisibility ( View.INVISIBLE );
                holder.ivGiftShow.setVisibility ( View.VISIBLE );
            }

            holder.itemView.setOnClickListener ( new View.OnClickListener ( ){
                @Override
                public void onClick(View view) {
                    if(!mediaPlayer.isPlaying ()){
                        mediaPlayer.start();
                    }
                    if(isClick){
                        if (position == Userset.ISCLICKkNUMBER){
                            clickDialog ( giftData ,true);
                        }else{
                            clickDialog ( giftData ,false);
                        }

                    }
                    if(!isClick && !Userset.ISNOTFIRST){
                        isClick = true;
                        animator = getAnimSet(holder.ivGift);
                        animator.start ();
                        holder.itemView.setBackgroundColor ( RED );
                        notifyDataSetChanged ();
                        Handler handler=new Handler ( );
                        handler.postDelayed (new Runnable ( ){
                            @Override
                            public void run() {
                                clickDialog ( giftData ,true);
                                Userset.ISCLICKkNUMBER = position;
                                Userset.ISNOTFIRST = true;
                                Userset.savePersonalInfo ( getActivity () );
                            }
                        }, 2000);
                    }



//                    Navigation.findNavController ( view ).navigate ( R.id.action_giftFragment_to_detilFragment );

                }
            } );

        }



    }
    private AnimatorSet getAnimSet(View view) {
        /* 建立集合動畫物件以加入多種動畫 */
        AnimatorSet animatorSet = new AnimatorSet();
        /* 播放完after()的動畫後再播放play()的動畫 */
//            animatorSet.play(getRotateAnim()).after(getTranslateAnim());
        /* 播放完play()的動畫後再播放before()的動畫 */
        animatorSet.play(getShakeAnim(view)).before(getAlphaAnim(view));
        return animatorSet;
    }
    private ObjectAnimator getAlphaAnim(View view) {
        /* 設定ivSoccer物件從完全不透明(1)至完全透明(0) */
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0);
        objectAnimator.setDuration(1000);
//            objectAnimator.setRepeatCount(1);
//            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setInterpolator(interpolator);
        return objectAnimator;
    }
    private ObjectAnimator getShakeAnim(View view) {
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, 10);
        objectAnimator.setDuration(1000);
        /* 設定CycleInterpolator特效以重複播放7次 */
        CycleInterpolator cycleInterpolator = new CycleInterpolator( 7);
        objectAnimator.setInterpolator(cycleInterpolator);
        return objectAnimator;
    }
}

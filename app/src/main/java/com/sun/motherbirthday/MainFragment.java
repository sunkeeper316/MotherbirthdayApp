package com.sun.motherbirthday;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment{
    AnimationDrawable animationDrawable;

    boolean isNotFirst = false ;

    private Interpolator interpolator = new LinearInterpolator ();
    private Animator animator;
    private Animator textanimator;
    private TextView tv_title;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_main , container , false );
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view , savedInstanceState );
        Userset.loadPersonalInfo ( getActivity () );
        tv_title = view.findViewById ( R.id.tv_gift_title );
        tv_title.setText ( "生日快樂！" );


        List<Drawable> drawables = new ArrayList<> ();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /* Resources.getDrawable()並指定圖片ID可取得對應圖片。
               圖片資料類型必須為Drawable方可用於動畫 */

            drawables.add(getResources().getDrawable(R.drawable.gift_1, null));
            drawables.add(getResources().getDrawable(R.drawable.gift_2, null));
            drawables.add(getResources().getDrawable(R.drawable.gift_3, null));
            drawables.add(getResources().getDrawable(R.drawable.gift_4, null));
            drawables.add(getResources().getDrawable(R.drawable.gift_5, null));
            drawables.add(getResources().getDrawable(R.drawable.gift_6, null));
            drawables.add(getResources().getDrawable(R.drawable.gift_7, null));
            drawables.add(getResources().getDrawable(R.drawable.gift_8, null));
        } else {
            drawables.add(getResources().getDrawable(R.drawable.gift_1));
            drawables.add(getResources().getDrawable(R.drawable.gift_2));
            drawables.add(getResources().getDrawable(R.drawable.gift_3));
            drawables.add(getResources().getDrawable(R.drawable.gift_4));
            drawables.add(getResources().getDrawable(R.drawable.gift_5));
            drawables.add(getResources().getDrawable(R.drawable.gift_6));
            drawables.add(getResources().getDrawable(R.drawable.gift_7));
            drawables.add(getResources().getDrawable(R.drawable.gift_8));
        }
        animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);
        int duration = 300;
        for (Drawable drawable : drawables) {
            animationDrawable.addFrame(drawable, duration);
        }

        ImageView ivPicture = view.findViewById( R.id.ivAnimation);
        /* 呼叫View.setBackground()，該ImageView即可套用動畫設定 */
        ivPicture.setBackground(animationDrawable);
        animator = getColorAnim();
        animator.start ();
        animationDrawable.start ();
        ivPicture.setOnClickListener ( new View.OnClickListener ( ){
            @Override
            public void onClick(View view) {
                Navigation.findNavController ( view ).navigate ( R.id.action_mainFragment_to_giftFragment );
            }
        } );
    }

    private ObjectAnimator getColorAnim() {
        ObjectAnimator objectAnimator;
        /* 設定背景色從白色變成紅色再還原，API 21開始可呼叫ofArgb()設定 */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            objectAnimator = ObjectAnimator.ofArgb( tv_title, "TextColor",
                                                    Color.BLUE, Color.RED);
        } else {
            objectAnimator =
                    ObjectAnimator.ofObject( tv_title, "TextColor",
                                             new ArgbEvaluator (), Color.BLUE, Color.RED);
        }
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount( ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setInterpolator(interpolator);
        return objectAnimator;
    }
}

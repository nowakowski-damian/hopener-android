package com.thirteendollars.hopener.activity;

import android.Manifest;
import android.animation.Animator;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.thirteendollars.hopener.adapter.DevicePageAdapter;
import com.thirteendollars.hopener.R;
import com.thirteendollars.hopener.fragment.ControlFragment;
import com.thirteendollars.hopener.fragment.ScannerFragment;
import com.thirteendollars.hopener.util.ObscuredSharedPreferences;
import com.thirteendollars.hopener.util.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 0;
    private  Preferences mPreferences;

    @BindView(R.id.info_window)
    TextView mInfoWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPreferences = new Preferences(getApplicationContext());
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},REQUEST_CODE_CAMERA);
        }
        else {
            setProperFragment();
        }
    }

    private void setProperFragment() {

        if( mPreferences.isUuidSet() ) {
            replaceFragment( ControlFragment.newInstance(mPreferences.getUuid()) );
        }
        else {
            replaceFragment( ScannerFragment.newInstance() );
        }
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.container,fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_CAMERA) {
            if( grantResults[0]!=PackageManager.PERMISSION_GRANTED ) {
                finish();
            }
            else {
                setProperFragment();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public Preferences getPreferences() {
        return mPreferences;
    }

    public void showInfoWindow(String message, boolean isError) {
        final int height = mInfoWindow.getMeasuredHeight();
        mInfoWindow.setText(message);
        if( isError ) {
            mInfoWindow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.redTextColor) );
        }
        else {
            mInfoWindow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.greenTextColor) );
        }
        mInfoWindow.animate()
                .translationY(height-1)
                .setStartDelay(0)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mInfoWindow.animate()
                                .translationY(-height)
                                .setListener(null)
                                .setStartDelay(1000)
                                .setDuration(1500)
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }

}

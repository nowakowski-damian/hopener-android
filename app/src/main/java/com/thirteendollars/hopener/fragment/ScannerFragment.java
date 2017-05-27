package com.thirteendollars.hopener.fragment;


import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.thirteendollars.hopener.R;
import com.thirteendollars.hopener.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener{

    @BindView(R.id.qrcode_scanner_view)
    QRCodeReaderView mQrReaderView;

    private MainActivity mActivity;

    public ScannerFragment() {
        // Required empty public constructor
    }

    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this,view);
        mActivity = (MainActivity) getActivity();
        mQrReaderView.setOnQRCodeReadListener(this);
        mQrReaderView.setAutofocusInterval(2000L);
        mQrReaderView.setQRDecodingEnabled(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mQrReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mQrReaderView.stopCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mQrReaderView.setQRDecodingEnabled(false);
        mQrReaderView.stopCamera();
        mActivity.getPreferences().setUuid(text);
        mActivity.replaceFragment(ControlFragment.newInstance(text));
        mActivity.showInfoWindow(getString(R.string.qr_scanned_successfully),false);
    }
}

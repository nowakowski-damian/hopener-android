package com.thirteendollars.hopener.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thirteendollars.hopener.R;
import com.thirteendollars.hopener.activity.MainActivity;
import com.thirteendollars.hopener.adapter.DevicePageAdapter;
import com.thirteendollars.hopener.model.ControlService;
import com.thirteendollars.hopener.model.Request;
import com.thirteendollars.hopener.model.Response;
import com.thirteendollars.hopener.util.App;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class ControlFragment extends Fragment implements ViewPager.OnPageChangeListener {

    public static final String ARG_DEVICE_UUID = "ARG_DEVICE_UUID";

    private DevicePageAdapter mAdapter;
    private MainActivity mActivity;
    private ControlService mControlService;
    private String mDeviceUuid;
    private String mChosenDevice;
    private Disposable mSubscription;
    private Action onCompleteAction = new Action() {
        @Override
        public void run() throws Exception {
            Log.d(getClass().getCanonicalName(), "onComplete()");
        }
    };
    private Consumer onNextConsumer = new Consumer<Response>() {
        @Override
        public void accept(@NonNull Response response) throws Exception {
            mActivity.showInfoWindow(response.getMessage(), false);
        }
    };

    private Consumer onErrorConsumer = new Consumer<Exception>() {
        @Override
        public void accept(@NonNull Exception exception) throws Exception {
            Log.e(getClass().getCanonicalName(),exception.toString() );
            if(exception instanceof HttpException) {
                HttpException httpException = (HttpException) exception;
                switch ( httpException.code() ) {
                    case 400:
                    case 404:
                    case 405:
                    case 415:
                        mActivity.showInfoWindow(getString(R.string.rest_415),true);
                        break;
                    case 401:
                        mActivity.showInfoWindow(getString(R.string.rest_401),true);
                        break;
                    case 403:
                        mActivity.showInfoWindow(getString(R.string.rest_403),true);
                        break;
                    case 500:
                        mActivity.showInfoWindow(getString(R.string.rest_500),true);
                        break;
                    default:
                        mActivity.showInfoWindow(getString(R.string.rest_unknown),true);
                }
            }
            else if(exception instanceof ConnectException || exception instanceof SocketTimeoutException) {
                mActivity.showInfoWindow(getString(R.string.connection_error),true);
            }
        }
    };

    @BindView(R.id.device_pager)
    ViewPager mPager;

    @OnClick(R.id.open_button)
    public void onOpen() {
        mSubscription = buildRequest(Request.ACTIVITY_OPEN)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextConsumer,onErrorConsumer,onCompleteAction);
    }

    @OnClick(R.id.pause_button)
    public void onAbort() {
        mSubscription = buildRequest(Request.ACTIVITY_STOP)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextConsumer,onErrorConsumer,onCompleteAction);
    }

    @OnClick(R.id.close_button)
    public void onClose() {
        mSubscription = buildRequest(Request.ACTIVITY_CLOSE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNextConsumer,onErrorConsumer,onCompleteAction);
    }

    public ControlFragment() {
        // Required empty public constructor
    }

    public static ControlFragment newInstance(String deviceUuid) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_UUID, deviceUuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDeviceUuid = getArguments().getString(ARG_DEVICE_UUID);
        }
        mChosenDevice = Request.DEVICE_FENCE;
        mActivity = (MainActivity) getActivity();
        mControlService = ((App) mActivity.getApplication()).getControlService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        ButterKnife.bind(this,view);
        mAdapter = new DevicePageAdapter(getContext());
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(this);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if( mSubscription!=null && !mSubscription.isDisposed() ) {
            mSubscription.dispose();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position==0) {
            mChosenDevice = Request.DEVICE_FENCE;
        }
        else {
            mChosenDevice = Request.DEVICE_GARAGE;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private Observable<Response> buildRequest(String ACTIVITY) {
        return mControlService.control( new Request(mDeviceUuid, mChosenDevice, ACTIVITY) );
    }

}

package de.kai_morich.simple_usb_terminal;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.kai_morich.simple_usb_terminal.databinding.FragmentAppBinding;

public class AppFragment extends Fragment implements ServiceConnection, SerialListener{

    private FragmentAppBinding appBinding;
    private SerialService service;
    private Intent intent;

    public AppFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(getActivity(), SerialService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appBinding = FragmentAppBinding.inflate(inflater, container, false);
        return appBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appBinding.resetBiggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appBinding.resetBiggest.setText(Long.toString(service.resetBiggestBothFloatTime()));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(service != null)
            service.attach(this);
        else
            getActivity().startService(intent);
    }

    @Override
    public void onStop() {
        if(service != null && !getActivity().isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        service = ((SerialService.SerialBinder) iBinder).getService();

        service.getIsFloatL().observe(getViewLifecycleOwner(), aBoolean -> {
            appBinding.leftFootFloat.setText(Long.toString(service.footFloatTimeL));
        });

        service.getIsFloatR().observe(getViewLifecycleOwner(), aBoolean -> {
            appBinding.rightFootFloat.setText(Long.toString(service.footFloatTimeR));
        });

        service.getTouchGround().observe(getViewLifecycleOwner(), aBoolean -> {
            appBinding.bothFeetFloat.setText(Long.toString(service.bothFeetFloatTime));
            appBinding.resetBiggest.setText(Long.toString(service.getBiggestBothFloatTime()));
        });

        service.getStepCount().observe(getViewLifecycleOwner(), integer -> {
            appBinding.totalStep.setText(Integer.toString(service.getStepCount().getValue()));
            appBinding.stepFreq.setText(Long.toString((int) 120000/service.oneStepTime));
        });

        service.getHeelImpactL().observe(getViewLifecycleOwner(), aBoolean -> {
            appBinding.heelImpactL.setText(Long.toString(service.heelImpactTimeL));
        });

        service.getHeelImpactR().observe(getViewLifecycleOwner(), aBoolean -> {
            appBinding.heelImpactR.setText(Long.toString(service.heelImpactTimeR));
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public void onSerialConnect() {

    }

    @Override
    public void onSerialConnectError(Exception e) {

    }

    @Override
    public void onSerialRead(byte[] data) {

    }

    @Override
    public void onSerialIoError(Exception e) {

    }
}
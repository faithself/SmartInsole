package de.kai_morich.simple_usb_terminal;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.Random;

import de.kai_morich.simple_usb_terminal.databinding.FragmentMedicalBinding;

public class MedicalFragment extends Fragment implements ServiceConnection, SerialListener {

    private FragmentMedicalBinding medicalBinding;
    private SerialService service;
    private Intent intent;
    private LinearLayout layoutL, layoutR;
    private GraphicalView chartviewL, chartviewR;
    private XYMultipleSeriesDataset datasetL, datasetR;
    private XYSeries xyseriesL, xyseriesR;
    private XYMultipleSeriesRenderer rendererL, rendererR;
    private short addXL, addYL, addXR, addYR;

    public MedicalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(getActivity(), SerialService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);
        datasetL = new XYMultipleSeriesDataset();
        xyseriesL = new XYSeries("fsr????????????");
        datasetL.addSeries(xyseriesL);
        datasetR = new XYMultipleSeriesDataset();
        xyseriesR = new XYSeries("fsr????????????");
        datasetR.addSeries(xyseriesR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        medicalBinding = FragmentMedicalBinding.inflate(inflater, container, false);
        return medicalBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutL = medicalBinding.fsrCurveL;
        layoutR = medicalBinding.fsrCurveR;
        chartviewL = ChartFactory.getLineChartView(getContext(), datasetL, getDemoRendererL());
        layoutL.addView(chartviewL, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
        chartviewR = ChartFactory.getLineChartView(getContext(), datasetR, getDemoRendererR());
        layoutR.addView(chartviewR, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (service != null)
            service.attach(this);
        else
            getActivity().startService(intent);
    }

    @Override
    public void onStop() {
        if (service != null && !getActivity().isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        service = ((SerialService.SerialBinder) iBinder).getService();

        service.getLameL().observe(getViewLifecycleOwner(), aBoolean -> {
            medicalBinding.lameL.setText(Boolean.toString(service.getLameL().getValue()));
        });

        service.getLameR().observe(getViewLifecycleOwner(), aBoolean -> {
            medicalBinding.lameR.setText(Boolean.toString(service.getLameR().getValue()));
        });

        service.getMainSuportL().observe(getViewLifecycleOwner(), aBoolean -> {
            medicalBinding.mainSupportL.setText(Boolean.toString(service.getMainSuportL().getValue()));
        });

        service.getMainSuportR().observe(getViewLifecycleOwner(), aBoolean -> {
            medicalBinding.mainSupportR.setText(Boolean.toString(service.getMainSuportR().getValue()));
        });

        service.getFsrValueCountL().observe(getViewLifecycleOwner(), Integer ->{
            addYL = (short) (service.totalFsrL >> 2);
            datasetL.removeSeries(xyseriesL);
            xyseriesL.add(addXL++, addYL);
            if (addXL > 255 && rendererL != null) {//???????????????????????????,????????????????????????????????????
                rendererL.setXAxisMin(addXL - 256);//???????????????100
                rendererL.setXAxisMax(addXL);
            }
            datasetL.addSeries(xyseriesL);
            chartviewL.invalidate();
        });

        service.getFsrValueCountR().observe(getViewLifecycleOwner(), Integer ->{
            addYR = (short) (service.totalFsrR >> 2);
            datasetR.removeSeries(xyseriesR);
            xyseriesR.add(addXR++, addYR);
            if (addXR > 255 && rendererR != null) {//???????????????????????????,????????????????????????????????????
                rendererR.setXAxisMin(addXR - 256);//???????????????100
                rendererR.setXAxisMax(addXR);
            }
            datasetR.addSeries(xyseriesR);
            chartviewR.invalidate();
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

    private XYMultipleSeriesRenderer getDemoRendererL() {
        rendererL = new XYMultipleSeriesRenderer();
//		    rendererL.setChartTitle("????????????");//??????
//		    rendererL.setChartTitleTextSize(20);
//        rendererL.setAxisTitleTextSize(24);
        rendererL.setAxesColor(Color.BLACK);
        rendererL.setLabelsTextSize(16);    //????????????????????????
        rendererL.setLabelsColor(Color.BLACK);
        rendererL.setLegendTextSize(22);    //????????????
        rendererL.setXLabelsColor(Color.BLACK);
        rendererL.setXAxisMin(0);//??????x???????????????
        rendererL.setXAxisMax(256);//???????????????????????????
        rendererL.setYLabelsColor(0, Color.BLACK);
        rendererL.setShowCustomTextGrid(true); // ??????X??????Y?????????????????????.

        rendererL.setGridColor(Color.WHITE);// LTGRAY); //?????????????????????
        rendererL.setMarginsColor(Color.WHITE); // ??????????????????
        rendererL.setBackgroundColor(Color.WHITE); //?????????????????????
//
        rendererL.setShowLegend(true);
        rendererL.setMargins(new int[]{20, 30, 30, 0});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.RED);
        r.setChartValuesTextSize(20);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(true);
//		    r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        rendererL.addSeriesRenderer(r);
        rendererL.setPanEnabled(true, true);
        rendererL.setShowGrid(true);
        rendererL.setYAxisMax(4096);
        rendererL.setYAxisMin(0);
        rendererL.setZoomEnabled(false, false);
        rendererL.setPanEnabled(false, false);
        return rendererL;
    }

    private XYMultipleSeriesRenderer getDemoRendererR() {
        rendererR = new XYMultipleSeriesRenderer();
//		    rendererR.setChartTitle("????????????");//??????
//		    rendererR.setChartTitleTextSize(20);
//        rendererR.setAxisTitleTextSize(24);
        rendererR.setAxesColor(Color.BLACK);
        rendererR.setLabelsTextSize(16);    //????????????????????????
        rendererR.setLabelsColor(Color.BLACK);
        rendererR.setLegendTextSize(22);    //????????????
        rendererR.setXLabelsColor(Color.BLACK);
        rendererR.setXAxisMin(0);//??????x???????????????
        rendererR.setXAxisMax(256);//???????????????????????????
        rendererR.setYLabelsColor(0, Color.BLACK);
        rendererR.setShowCustomTextGrid(true); // ??????X??????Y?????????????????????.

        rendererR.setGridColor(Color.WHITE);// LTGRAY); //?????????????????????
        rendererR.setMarginsColor(Color.WHITE); // ??????????????????
        rendererR.setBackgroundColor(Color.WHITE); //?????????????????????
//
        rendererR.setShowLegend(true);
        rendererR.setMargins(new int[]{20, 30, 30, 0});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.GREEN);
        r.setChartValuesTextSize(20);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(true);
//		    r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        rendererR.addSeriesRenderer(r);
        rendererR.setPanEnabled(true, true);
        rendererR.setShowGrid(true);
        rendererR.setYAxisMax(4096);
        rendererR.setYAxisMin(0);
        rendererR.setZoomEnabled(false, false);
        rendererR.setPanEnabled(false, false);
        return rendererR;
    }
}
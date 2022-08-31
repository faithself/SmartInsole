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
        xyseriesL = new XYSeries("fsr左足变化");
        datasetL.addSeries(xyseriesL);
        datasetR = new XYMultipleSeriesDataset();
        xyseriesR = new XYSeries("fsr右足变化");
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
            if (addXL > 255 && rendererL != null) {//如果超出了屏幕边界,实现坐标轴自动移动的方法
                rendererL.setXAxisMin(addXL - 256);//显示范围为100
                rendererL.setXAxisMax(addXL);
            }
            datasetL.addSeries(xyseriesL);
            chartviewL.invalidate();
        });

        service.getFsrValueCountR().observe(getViewLifecycleOwner(), Integer ->{
            addYR = (short) (service.totalFsrR >> 2);
            datasetR.removeSeries(xyseriesR);
            xyseriesR.add(addXR++, addYR);
            if (addXR > 255 && rendererR != null) {//如果超出了屏幕边界,实现坐标轴自动移动的方法
                rendererR.setXAxisMin(addXR - 256);//显示范围为100
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
//		    rendererL.setChartTitle("实时曲线");//标题
//		    rendererL.setChartTitleTextSize(20);
//        rendererL.setAxisTitleTextSize(24);
        rendererL.setAxesColor(Color.BLACK);
        rendererL.setLabelsTextSize(16);    //数轴刻度字体大小
        rendererL.setLabelsColor(Color.BLACK);
        rendererL.setLegendTextSize(22);    //曲线说明
        rendererL.setXLabelsColor(Color.BLACK);
        rendererL.setXAxisMin(0);//设置x轴的起始点
        rendererL.setXAxisMax(256);//设置一屏有多少个点
        rendererL.setYLabelsColor(0, Color.BLACK);
        rendererL.setShowCustomTextGrid(true); // 设置X轴和Y轴网格是否显示.

        rendererL.setGridColor(Color.WHITE);// LTGRAY); //网格颜色，灰色
        rendererL.setMarginsColor(Color.WHITE); // 设置四边颜色
        rendererL.setBackgroundColor(Color.WHITE); //设置中间背景色
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
//		    rendererR.setChartTitle("实时曲线");//标题
//		    rendererR.setChartTitleTextSize(20);
//        rendererR.setAxisTitleTextSize(24);
        rendererR.setAxesColor(Color.BLACK);
        rendererR.setLabelsTextSize(16);    //数轴刻度字体大小
        rendererR.setLabelsColor(Color.BLACK);
        rendererR.setLegendTextSize(22);    //曲线说明
        rendererR.setXLabelsColor(Color.BLACK);
        rendererR.setXAxisMin(0);//设置x轴的起始点
        rendererR.setXAxisMax(256);//设置一屏有多少个点
        rendererR.setYLabelsColor(0, Color.BLACK);
        rendererR.setShowCustomTextGrid(true); // 设置X轴和Y轴网格是否显示.

        rendererR.setGridColor(Color.WHITE);// LTGRAY); //网格颜色，灰色
        rendererR.setMarginsColor(Color.WHITE); // 设置四边颜色
        rendererR.setBackgroundColor(Color.WHITE); //设置中间背景色
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
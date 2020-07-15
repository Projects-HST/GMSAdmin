package com.gms.admin.activity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.gms.admin.R;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity implements IServiceListener, DialogClickListener {

    private static final String TAG = GraphActivity.class.getName();

    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private String paguthiId = "";

    PieChart pieChart;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paguthiId = getIntent().getStringExtra("paguthi");

        pieChart = findViewById(R.id.pie_chart);
        lineChart = findViewById(R.id.line_chart);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        getDashboard();
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(GMSConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("Success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    private void getDashboard() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GMSConstants.PAGUTHI, paguthiId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = PreferenceStorage.getClientUrl(this) + GMSConstants.GET_DASHBOARD_DETAIL;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                JSONObject getData = response.getJSONObject("grievance_graph");
                JSONArray getLineData = response.getJSONArray("meeting_graph");
                ArrayList<PieEntry> entries = new ArrayList<>();
                int count = getLineData.length();

//                entries.add(new PieEntry(Float.parseFloat((getData.getString("gerv_ecount"))),"Enquiry"));
//                entries.add(new PieEntry(Float.parseFloat((getData.getString("gerv_ppcount"))),"Processing"));
//                entries.add(new PieEntry(Float.parseFloat((getData.getString("gerv_pccount"))),"Completed"));

                entries.add(new PieEntry(125f, "Enquiry"));
                entries.add(new PieEntry(230f, "Processing"));
                entries.add(new PieEntry(50f, "Completed"));

                PieDataSet dataSet = new PieDataSet(entries, "");

                PieData data = new PieData(dataSet);
                pieChart.setData(data);

//                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setColors(new int[]{ContextCompat.getColor(this, R.color.enquiry_pie), ContextCompat.getColor(this, R.color.processing_pie), ContextCompat.getColor(this, R.color.completed_pie)});
// dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.setValueLinePart1OffsetPercentage(80.f);
                dataSet.setValueLinePart1Length(0.3f);
                dataSet.setValueLinePart2Length(0.6f);
//                dataSet.setUsingSliceColorAsValueLineColor(true);
                pieChart.animateXY(1000, 1000);

                // undo all highlights
                pieChart.highlightValues(null);
                pieChart.setDrawHoleEnabled(false);
                pieChart.getLegend().setWordWrapEnabled(true);

                pieChart.getDescription().setEnabled(false);
//                pieChart.setHoleRadius(10.0f);
//                pieChart.setTransparentCircleRadius(12f);
                pieChart.setDrawSlicesUnderHole(true);
                pieChart.setEntryLabelColor(dataSet.getValueLineColor());
                pieChart.getLegend().setEnabled(false);
                pieChart.invalidate();



                ArrayList<Entry> values = new ArrayList<>();
                ArrayList year = new ArrayList();
                for (int i = 0; i < count; i++) {

                    float val = Float.parseFloat(getLineData.getJSONObject(i).getString("meeting_request"));
                    values.add(new Entry(i, val));
                    year.add(getLineData.getJSONObject(i).getString("month_year"));
                }

                LineDataSet lineDataSet;
                lineDataSet = new LineDataSet(values, "Meeting Data");
                lineDataSet.setDrawIcons(false);
                lineDataSet.enableDashedLine(10f, 5f, 0f);
                lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
                lineDataSet.setColor(Color.DKGRAY);
                lineDataSet.setCircleColor(Color.DKGRAY);
                lineDataSet.setLineWidth(1f);
                lineDataSet.setCircleRadius(3f);
                lineDataSet.setDrawCircleHole(false);
                lineDataSet.setValueTextSize(9f);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFormLineWidth(1f);
                lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                lineDataSet.setFormSize(15.f);
                if (Utils.getSDKInt() >= 18) {
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                    lineDataSet.setFillDrawable(drawable);
                } else {
                    lineDataSet.setFillColor(Color.DKGRAY);
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);
                LineData lineData = new LineData(dataSets);
                lineChart.setData(lineData);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f); // only intervals of 1 day
                xAxis.setLabelCount(12);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(year));


                lineChart.getDescription().setEnabled(false);
                lineChart.getLegend().setEnabled(false);
                lineChart.invalidate();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }
}
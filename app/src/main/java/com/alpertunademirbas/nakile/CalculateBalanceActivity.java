package com.alpertunademirbas.nakile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalculateBalanceActivity extends AppCompatActivity {

    private PieChart pieChart;
    private DatabaseHelper dbHelper;
    private TextView balanceTextView, textViewMonth, textViewTotalTransferNumber, textViewSelectTransferName, textViewSelectTransferPrice;
    String monthName;
    List<Transfer> currentMonthTransfers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_balance);

        dbHelper = new DatabaseHelper(this);
        balanceTextView = findViewById(R.id.textViewTotal);
        pieChart = findViewById(R.id.pieChart);
        textViewMonth = findViewById(R.id.textViewMonth);
        textViewTotalTransferNumber = findViewById(R.id.textViewTotalTransferNumber);
        textViewSelectTransferName = findViewById(R.id.textViewSelectTransferName);
        textViewSelectTransferPrice = findViewById(R.id.textViewSelectTransferPrice);

        ImageView menuImageView = findViewById(R.id.menu);
        menuImageView.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(CalculateBalanceActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout);

            Button button = dialog.findViewById(R.id.button);
            button.setOnClickListener(v1 -> {
                startActivity(new Intent(CalculateBalanceActivity.this, MainScreenActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button1 = dialog.findViewById(R.id.button1);
            button1.setOnClickListener(v1 -> {
                startActivity(new Intent(CalculateBalanceActivity.this, CalculateFuelPriceActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button3 = dialog.findViewById(R.id.button3);
            button3.setOnClickListener(v13 -> {
                dialog.dismiss();
            });

            Button button4 = dialog.findViewById(R.id.button4);
            button4.setOnClickListener(v13 -> {
                startActivity(new Intent(CalculateBalanceActivity.this, SettingActivity.class));
                dialog.dismiss();
                finish();
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            int[] location = new int[2];
            menuImageView.getLocationOnScreen(location);
            layoutParams.x = location[0]; // x position
            layoutParams.y = location[1] + menuImageView.getHeight(); // y position
            dialog.getWindow().setAttributes(layoutParams);

            dialog.show();
        });

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                textViewSelectTransferName.setText(pe.getLabel());
                textViewSelectTransferPrice.setText(String.valueOf(pe.getValue()+" TL"));
            }

            @Override
            public void onNothingSelected() {}
        });


        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;

        List<Transfer> transfers = dbHelper.getAllTransfers();
        currentMonthTransfers = new ArrayList<>();

        float totalTransferPrice = 0;
        for (Transfer transfer : transfers) {
            String transferDate = transfer.getTransferDate();

            int transferMonth = Integer.parseInt(transferDate.split("/")[1]);

            if (transferMonth == month) {
                currentMonthTransfers.add(transfer);
                totalTransferPrice += Float.parseFloat(transfer.getTransferPrice());
            }
        }

        monthName = new DateFormatSymbols().getMonths()[month-1];
        balanceTextView.setText(monthName + " Ayı Toplam Geliriniz " + totalTransferPrice + " TL");


        List<PieEntry> entries = new ArrayList<>();
        for (Transfer transfer : currentMonthTransfers) {
            float transferPrice = Float.parseFloat(transfer.getTransferPrice());
            entries.add(new PieEntry(transferPrice, transfer.getNameSurname()));
        }

        PieDataSet set = new PieDataSet(entries, "Transfer Prices");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setValueTextSize(14f);


        PieData data = new PieData(set);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);



        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(43f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setCenterText("Transferler");
        pieChart.setCenterTextSize(18f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setUsePercentValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.invalidate();
        int totalTransfers = currentMonthTransfers.size();
        textViewMonth.setText(monthName);
        textViewTotalTransferNumber.setText(String.valueOf(totalTransfers));
    }

}
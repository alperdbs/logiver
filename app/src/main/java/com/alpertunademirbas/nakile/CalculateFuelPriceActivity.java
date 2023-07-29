package com.alpertunademirbas.nakile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class CalculateFuelPriceActivity extends AppCompatActivity {

    EditText totalDistance,fuelConsumption,fuelPrice;
    TextView textView4, textView5;
    Button calculate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_fuel_price);

        totalDistance = findViewById(R.id.totalDistance);
        fuelConsumption = findViewById(R.id.fuelConsumption);
        fuelPrice = findViewById(R.id.fuelPrice);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        calculate = findViewById(R.id.calculate);
        ImageView menuImageView = findViewById(R.id.menu);
        menuImageView.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(CalculateFuelPriceActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout);

            Button button = dialog.findViewById(R.id.button);
            button.setOnClickListener(v1 -> {
                startActivity(new Intent(CalculateFuelPriceActivity.this, MainScreenActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button1 = dialog.findViewById(R.id.button1);
            button1.setOnClickListener(v1 -> {
                dialog.dismiss();
            });


            Button button3 = dialog.findViewById(R.id.button3);
            button3.setOnClickListener(v13 -> {
                startActivity(new Intent(CalculateFuelPriceActivity.this, CalculateBalanceActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button4 = dialog.findViewById(R.id.button4);
            button4.setOnClickListener(v13 -> {
                startActivity(new Intent(CalculateFuelPriceActivity.this, SettingActivity.class));
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

        textView5.setVisibility(View.GONE);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView5.setVisibility(View.GONE);
                try {
                    double totalDistanceVal = Double.parseDouble(totalDistance.getText().toString());
                    double fuelConsumptionVal = Double.parseDouble(fuelConsumption.getText().toString());
                    double fuelPriceVal = Double.parseDouble(fuelPrice.getText().toString());

                    double A = totalDistanceVal / 100;
                    double Y = A * fuelConsumptionVal;
                    double Z = Y * fuelPriceVal;
                    textView4.setText(String.format("%.2f", Z));

                } catch (NumberFormatException e) {
                    // Kullanıcı geçerli bir sayı girmediyse, bir hata mesajı göster.
                    textView5.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
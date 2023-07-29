package com.alpertunademirbas.nakile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class SettingActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dbHelper = new DatabaseHelper(this);
        ImageView menuImageView = findViewById(R.id.menu);
        Button clear = findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("Verileri Temizle")
                        .setMessage("Tüm verileri temizlemek istediğinize emin misiniz? Bu işlem geri alınamaz.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteAllTransfers();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        menuImageView.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(SettingActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout);
            Button button = dialog.findViewById(R.id.button);
            button.setOnClickListener(v1 -> {
                startActivity(new Intent(SettingActivity.this, MainScreenActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button1 = dialog.findViewById(R.id.button1);
            button1.setOnClickListener(v1 -> {
                startActivity(new Intent(SettingActivity.this, CalculateFuelPriceActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button3 = dialog.findViewById(R.id.button3);
            button3.setOnClickListener(v13 -> {
                startActivity(new Intent(SettingActivity.this, CalculateBalanceActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button4 = dialog.findViewById(R.id.button4);
            button4.setOnClickListener(v13 -> {

                dialog.dismiss();
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


    }
}
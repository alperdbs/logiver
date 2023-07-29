package com.alpertunademirbas.nakile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

public class MainScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransferAdapter transferAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            if (!notificationHelper.areNotificationsEnabled()) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName())
                        .putExtra(Settings.EXTRA_CHANNEL_ID, NotificationHelper.CHANNEL_ID);
                startActivity(intent);
            }
        } else {
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DatabaseHelper db = new DatabaseHelper(MainScreenActivity.this);
        List<Transfer> transfers = db.getAllTransfers();
        transferAdapter = new TransferAdapter(transfers);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(transferAdapter);

        ImageView menuImageView = findViewById(R.id.menu);
        menuImageView.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(MainScreenActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout);

            Button button = dialog.findViewById(R.id.button);
            button.setOnClickListener(v1 -> {
                startActivity(new Intent(MainScreenActivity.this, MainScreenActivity.class));
                dialog.dismiss();
                finish();
            });

            Button button1 = dialog.findViewById(R.id.button1);
            button1.setOnClickListener(v1 -> {
                startActivity(new Intent(MainScreenActivity.this, CalculateFuelPriceActivity.class));
                dialog.dismiss();
            });

            Button button3 = dialog.findViewById(R.id.button3);
            button3.setOnClickListener(v13 -> {
                startActivity(new Intent(MainScreenActivity.this, CalculateBalanceActivity.class));
                dialog.dismiss();
            });

            Button button4 = dialog.findViewById(R.id.button4);
            button4.setOnClickListener(v13 -> {
                startActivity(new Intent(MainScreenActivity.this, SettingActivity.class));
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

        ImageView searchImageView = findViewById(R.id.searchImage);
        searchImageView.setOnClickListener(v -> {
            final Dialog dialogSearch = new Dialog(MainScreenActivity.this);
            dialogSearch.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSearch.setContentView(R.layout.search_layout);

            EditText searchEditText = dialogSearch.findViewById(R.id.search_view_edit_text);

            Button searchButton = dialogSearch.findViewById(R.id.search_button);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query = searchEditText.getText().toString().trim();

                    if (!query.isEmpty()) {
                        DatabaseHelper db = new DatabaseHelper(MainScreenActivity.this);
                        List<Transfer> transfers = db.searchTransfers(query);

                        transferAdapter = new TransferAdapter(transfers);
                        recyclerView.setAdapter(transferAdapter);
                        dialogSearch.dismiss();
                    } else {
                        searchEditText.setError("Arama için bir girdi girin.");
                    }
                }
            });

            dialogSearch.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialogSearch.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.TOP | Gravity.START;
            int[] location = new int[2];
            searchImageView.getLocationOnScreen(location);
            layoutParams.x = location[0]; // x position
            layoutParams.y = location[1] + menuImageView.getHeight(); // y position
            dialogSearch.getWindow().setAttributes(layoutParams);

            dialogSearch.show();
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Transfer deletedTransfer = transferAdapter.getTransfer(position);

                DatabaseHelper db = new DatabaseHelper(MainScreenActivity.this);
                db.deleteTransfer(deletedTransfer);

                transferAdapter.removeTransfer(position);
            }
        }).attachToRecyclerView(recyclerView);


        Button addTransfer = findViewById(R.id.addTransfer);
        addTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainScreenActivity.this, AddTransferActivity.class);
                MainScreenActivity.this.startActivity(mainIntent);
            }
        });
    }

}


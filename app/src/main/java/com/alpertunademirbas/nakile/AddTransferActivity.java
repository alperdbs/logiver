package com.alpertunademirbas.nakile;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTransferActivity extends AppCompatActivity {

    String phone;
    PendingIntent pendingIntent;
    DatabaseHelper db;
    private List<Integer> imageIds = Arrays.asList(
            R.drawable.character1,
            R.drawable.character2,
            R.drawable.character3,
            R.drawable.character4,
            R.drawable.character5,
            R.drawable.character6,
            R.drawable.character7,
            R.drawable.character8,
            R.drawable.character9
    );

    private int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transfer);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button addTransfer = findViewById(R.id.addTransfer);
        EditText nameView = findViewById(R.id.name);
        EditText surnameView = findViewById(R.id.surname);
        EditText transferLocationView = findViewById(R.id.transferLocation);
        EditText transferPriceView = findViewById(R.id.transferPrice);
        EditText phoneNumberView = findViewById(R.id.phoneNumber);
        EditText transferDateView = findViewById(R.id.transferDate);
        ImageView imageView = findViewById(R.id.imageView9);

        imageView.setImageResource(imageIds.get(currentIndex));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % imageIds.size();
                imageView.setImageResource(imageIds.get(currentIndex));
            }
        });




        phoneNumberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                phone = s.toString().replaceAll(" ", "");
                if (phone.length() > 10) {
                    phone = phone.substring(0, 10);
                }

                String formattedPhone = formatPhoneNumber(phone);
                if (!s.toString().equals(formattedPhone)) {
                    phoneNumberView.removeTextChangedListener(this);
                    phoneNumberView.setText(formattedPhone);
                    phoneNumberView.setSelection(formattedPhone.length());
                    phoneNumberView.addTextChangedListener(this);
                }
            }

            private String formatPhoneNumber(String phone) {
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < phone.length(); ++i) {
                    if (i == 3 || i == 6 || i == 8) {
                        formatted.append(" ");
                    }
                    formatted.append(phone.charAt(i));
                }
                return formatted.toString();
            }

        });

        transferDateView.setFocusable(false);
        transferDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTransferActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                transferDateView.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });




        addTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isBatteryOptimizationEnabled()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTransferActivity.this);
                    builder.setTitle("Bildirimler için izin gerekli");

                    builder.setMessage("Uygulamanın arka planda bildirim göndermesi için lütfen batarya optimizsyonunu kapatın!");

                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                            startActivity(intent);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    String name = nameView.getText().toString();
                    String surname = surnameView.getText().toString();
                    String phoneNumber = phoneNumberView.getText().toString();
                    String transferDate = transferDateView.getText().toString();
                    String nameSurname = name+" "+surname;
                    String transferLocation = transferLocationView.getText().toString();
                    String transferPrice = transferPriceView.getText().toString();
                    String activeImageName = "character" + (currentIndex + 1);




                    String transferDatee = transferDateView.getText().toString();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname)) {
                        Toast.makeText(AddTransferActivity.this, "Ad ve Soyad Kısımları Boş!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Transfer newTransfer = new Transfer(name, surname, phoneNumber, transferDate, nameSurname, transferLocation, transferPrice, activeImageName);

                    db = new DatabaseHelper(AddTransferActivity.this);
                    if (db.doesDateExist(transferDate)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTransferActivity.this);
                        builder.setTitle("Tarih Zaten Var");
                        builder.setMessage("Bu tarih zaten veritabanında var!");
                        builder.setPositiveButton("Yine de Ekle", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.addTransfer(newTransfer);

                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                Date date = null;
                                try {
                                    date = sdf.parse(transferDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (date != null) {
                                    long millis = date.getTime();
                                    Intent intent = new Intent(AddTransferActivity.this, AlarmReceiver.class);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        pendingIntent = PendingIntent.getBroadcast(AddTransferActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                                    } else {
                                        pendingIntent = PendingIntent.getBroadcast(AddTransferActivity.this, 0, intent, 0);
                                    }


                                    if (alarmManager != null) {
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
                                    }
                                }


                                Intent mainIntent = new Intent(AddTransferActivity.this, MainScreenActivity.class);
                                AddTransferActivity.this.startActivity(mainIntent);
                            }
                        });
                        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }
                    db.addTransfer(newTransfer);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = null;
                    try {
                        date = sdf.parse(transferDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date != null) {
                        long millis = date.getTime();
                        Intent intent = new Intent(AddTransferActivity.this, AlarmReceiver.class);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            pendingIntent = PendingIntent.getBroadcast(AddTransferActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                        } else {
                            pendingIntent = PendingIntent.getBroadcast(AddTransferActivity.this, 0, intent, 0);
                        }


                        if (alarmManager != null) {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
                        }
                    }

                    Intent mainIntent = new Intent(AddTransferActivity.this, MainScreenActivity.class);
                    AddTransferActivity.this.startActivity(mainIntent);
                    finish();
                }


            }
        });
    }

    private boolean isBatteryOptimizationEnabled() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String packageName = getPackageName();
                return !powerManager.isIgnoringBatteryOptimizations(packageName);
            } else {
                return false;
            }
        }
        return false;
    }
}

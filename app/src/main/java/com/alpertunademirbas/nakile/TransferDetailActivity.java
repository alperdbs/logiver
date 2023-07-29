package com.alpertunademirbas.nakile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class TransferDetailActivity extends AppCompatActivity {
    Button call, route;
    String location;
    ImageView characterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_detail);

        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        String phone = getIntent().getStringExtra("phone");
        String date = getIntent().getStringExtra("date");
        location = getIntent().getStringExtra("location");
        String price = getIntent().getStringExtra("price");
        String character = getIntent().getStringExtra("character");

        EditText nameView = findViewById(R.id.name);
        EditText surnameView = findViewById(R.id.surname);
        EditText transferLocationView = findViewById(R.id.transferLocation);
        EditText transferPriceView = findViewById(R.id.transferPrice);
        EditText phoneNumberView = findViewById(R.id.phoneNumber);
        EditText transferDateView = findViewById(R.id.transferDate);
        characterImageView = findViewById(R.id.imageView9);
        call = findViewById(R.id.call);
        route = findViewById(R.id.route);

        nameView.setText(name);
        surnameView.setText(surname);
        phoneNumberView.setText(phone);
        transferDateView.setText(date);
        transferLocationView.setText(location);
        transferPriceView.setText(price);
        int drawableResourceId = this.getResources().getIdentifier(character, "drawable", this.getPackageName());

        characterImageView.setImageResource(drawableResourceId);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = "+90" + phone;
                Uri dialIntentUri = Uri.parse("tel:" + Uri.encode(phoneNumber));
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, dialIntentUri);
                if (dialIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(dialIntent);
                }
            }
        });

        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });



    }


}
package com.example.appointmentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AppointmentPage extends AppCompatActivity {
    //views references
    Intent intent;
    EditText noteEditText;
    TextView titleTV;
    ImageView beltIMV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_page);
        noteEditText = findViewById(R.id.noteEditText);
        titleTV = findViewById(R.id.titleTV);
        beltIMV = findViewById(R.id.beltIMV);

        intent = getIntent();

        //set views text
        titleTV.setText(intent.getStringExtra("title"));
        noteEditText.setText(intent.getStringExtra("note"));
        beltIMV.setColorFilter(intent.getIntExtra("color",Color.WHITE));

    }



    public void saveNotes(View view) {
        //if note changed
        if(!( noteEditText.getText().toString().equals(intent.getStringExtra("note"))))
        {

            AppointmentManagerDatabase.
                    getInstance(this).
                    appointmentDAO().
                    updateAppointment(intent.getIntExtra("id",-1),
                                     noteEditText.getText().toString());

        }
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        onBackPressed();//back to main
    }
}
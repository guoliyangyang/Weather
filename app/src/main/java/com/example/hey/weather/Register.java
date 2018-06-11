package com.example.hey.weather;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Register extends Activity {
    Button b_register;
    Button b_back;
    EditText e_userin;
    EditText e_passwordin;
    EditText e_emailin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.print("hey\n");
        setContentView(R.layout.activity_register);

        b_register = (Button)findViewById(R.id.register_register);
        b_back = (Button)findViewById(R.id.back_register);
        e_userin = (EditText)findViewById(R.id.userin_register);
        e_passwordin = (EditText)findViewById(R.id.passwordin_register);
        e_emailin = (EditText)findViewById(R.id.emailin_register);

        final MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "User.db", null, 2);

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("USER", e_userin.getText().toString());
                values.put("PASSWORD", e_passwordin.getText().toString());
                values.put("EMAIL", e_emailin.getText().toString());
                if(db.insert("WEATHER", null, values) == -1) {
                    e_userin.setText("");
                    e_passwordin.setText("");
                    e_emailin.setText("");
                    Toast.makeText(Register.this, "Input Invalid", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Register.this, "Register Success!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Register.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}

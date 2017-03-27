package com.example.johnbad.access_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Email extends AppCompatActivity {
    private Button buttonSend;
    private EditText textSubject;
    private EditText textMessage;
    final String TO = "accessmap.team@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        getSupportActionBar().setTitle("Report Problem");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        buttonSend = (Button) findViewById(R.id.bSend);
        textSubject = (EditText) findViewById(R.id.editTextSubject);
        textMessage = (EditText) findViewById(R.id.editTextMessage);


        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String subject = textSubject.getText().toString();
                String message = textMessage.getText().toString();

                if (subject.length() == 0) {
                    Toast.makeText(Email.this, "Please fill in your Subject!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (message.length() == 0) {
                    Toast.makeText(Email.this, "Please fill in your Message!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }

        if(id == R.id.clear_textview) {
            textMessage.setText("");
            textSubject.setText("");
        }

        return super.onOptionsItemSelected(item);
    }
}

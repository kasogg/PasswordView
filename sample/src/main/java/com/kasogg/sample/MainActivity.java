package com.kasogg.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kasogg.passwordview.PasswordView;

public class MainActivity extends AppCompatActivity {
    PasswordView mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPasswordView = (PasswordView) findViewById(R.id.password_view);
        mPasswordView.setInputCallback(new PasswordView.InputCallback() {
            @Override
            public void onFinish(String password) {
                //Do something when finish input
                Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
            }
        });

        Button button = (Button) findViewById(R.id.btn_show_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, mPasswordView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

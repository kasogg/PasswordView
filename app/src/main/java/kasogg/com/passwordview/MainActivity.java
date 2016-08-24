package kasogg.com.passwordview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PasswordView passwordInputView = (PasswordView) findViewById(R.id.passwordView);
        passwordInputView.setInputCallback(new PasswordView.InputCallback() {
            @Override
            public void onFinish(String password) {
                Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, passwordInputView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

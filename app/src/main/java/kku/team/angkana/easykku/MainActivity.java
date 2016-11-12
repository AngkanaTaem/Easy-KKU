package kku.team.angkana.easykku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private Button signInButton, signUpButton; //Shift+Ctrl+Enter ->End process


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Ctrl+คลิก วาปหน้า

        //Bind Widget
        signInButton = (Button) findViewById(R.id.button2); //Alt + enter  -> Catch
        signUpButton = (Button) findViewById(R.id.button);

        //Sing Up Controller
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SingUpActivity.class));
            }
        });

    } // Main Method
} // Main Class

package unitec.ac.nz.myunitec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private TextView txtWelcome;
    private Button btnLogout;
    private Button btnProgramme;
    private Button btnEnroll;
    private Button btnTimetable;
    private String firstName;
    private String lastName;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        btnProgramme = (Button) findViewById(R.id.btnProgramme);
        btnProgramme.setOnClickListener(this);
        btnEnroll = (Button) findViewById(R.id.btnEnroll);
        btnEnroll.setOnClickListener(this);
        btnTimetable = (Button) findViewById(R.id.btnTimetable);
        btnTimetable.setOnClickListener(this);
        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        username = intent.getStringExtra("username");
        txtWelcome.setText("Welcome\n" + firstName + " " + lastName);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            startActivity(intent);
        } else if (v == btnProgramme) {
            Intent intent = new Intent(MainActivity.this, ProgrammeActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            startActivity(intent);
        }
    }
}

package c346.rp.edu.sg.mybmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText w, h;
    Button calc, reset;
    TextView lastDate, lastBMI, outcome;

    String datetime, bmiOutcome;
    Double BMI = 0.0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        w = findViewById(R.id.weightEt);
        h = findViewById(R.id.heightEt);

        calc = findViewById(R.id.calculateBtn);
        reset = findViewById(R.id.resetDataBtn);

        lastDate = findViewById(R.id.lastCalcDateTv);
        lastBMI = findViewById(R.id.lastCalcBMITv);
        outcome = findViewById(R.id.outcomeTv);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        Calendar now = Calendar.getInstance();  //Create a Calendar object with current date and time
        datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                (now.get(Calendar.MONTH)+1) + "/" +
                now.get(Calendar.YEAR) + " " +
                now.get(Calendar.HOUR_OF_DAY) + ":" +
                now.get(Calendar.MINUTE);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((h.getText().length() > 0) && (w.getText().length() > 0)) {
                    double height = Double.parseDouble(h.getText().toString());
                    double weight = Double.parseDouble(w.getText().toString());

                    calcBMI(height, weight);
                    lastDate.setText("Last Calculated Date: " + datetime);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w.setText("");
                h.setText("");

                lastDate.setText("Last Calculated Date: ");
                lastBMI.setText("Last Calculated BMI: ");
                outcome.setText("");
                editor.clear();
                editor.commit();
            }
        });
    }

    private void calcBMI(double height, double weight) {

        height = height / 100;

        BMI = weight / (height * height);

        if (BMI < 18.5) {
            bmiOutcome = "Your are underweight";
        }
        else if (BMI >= 18.5 && BMI <= 24.9) {
            bmiOutcome = "Your BMI is normal";
        }
        else if (BMI >= 25 && BMI <= 29.9) {
            bmiOutcome = "Your are overweight";
        }
        else {
            bmiOutcome = "You are obese";
        }

        outcome.setText(bmiOutcome);
        lastBMI.setText("Last Calculated BMI: " + String.format("%.1f", BMI));
    }

    @Override
    protected void onPause() {
        super.onPause();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        editor = sharedPreferences.edit();

        editor.putString("lastDate", datetime);
        editor.putString("lastBMI", String.format("%.1f", BMI));
        editor.putString("outcome", bmiOutcome);

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String lastCalcDate = sharedPreferences.getString("lastDate", "");
        String lastCalcBMI = sharedPreferences.getString("lastBMI", "");
        String lastOutcome = sharedPreferences.getString("outcome", "");

        lastDate.setText("Last Calculated Date: " + lastCalcDate);
        lastBMI.setText("Last Calculated BMI: " + lastCalcBMI);
        outcome.setText(lastOutcome);

    }
}

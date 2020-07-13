package sg.edu.rp.c346.id19004731.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etHeight;
    Button btnCalculate, btnResetData;
    TextView tvDate, tvBMI, tvBMIRange;
    RadioGroup rgWeight, rgHeight;
    float ftBMI = 0;
    String datetime = "";
    String BMIRange = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnResetData = findViewById(R.id.buttonResetData);
        tvDate = findViewById(R.id.textViewDate);
        tvBMI = findViewById(R.id.textViewBMI);
        tvBMIRange = findViewById(R.id.textViewBMIRange);
        rgWeight = findViewById(R.id.radioGroupWeight);
        rgHeight = findViewById(R.id.radioGroupHeight);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //=== Calculate BMI ===

                float ftWeight = Float.parseFloat(etWeight.getText().toString());
                //=== convert weight to kg ===
                ftWeight = rgWeight.getCheckedRadioButtonId() == R.id.radioButtonLBS ?
                        (float) (ftWeight / 2.205) : ftWeight;

                float ftHeight = Float.parseFloat(etHeight.getText().toString());
                //=== convert height to m ===
                ftHeight = rgHeight.getCheckedRadioButtonId() == R.id.radioButtonCM ?
                        (float) (ftHeight / 100) : ftHeight;

                ftBMI = ftWeight / (ftHeight*ftHeight);
                tvBMI.setText(getText(R.string.bmi) + " " + ftBMI);

                //=== Date Calculated ===
                Calendar now = Calendar.getInstance();
                datetime = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH)+1)  + "/" +
                        now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
                tvDate.setText(getText(R.string.date) + " " + datetime);

                //=== BMI Range ===
                if (ftBMI < 18.5)
                    BMIRange = "You are underweight";
                else if (ftBMI < 25)
                    BMIRange = "Your BMI is normal";
                else if (ftBMI < 30)
                    BMIRange = "Your are overweight";
                else
                    BMIRange = "Your are obese";
                tvBMIRange.setText(BMIRange);

                //=== Save ===
                save();
            }
        });

        btnResetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftBMI = 0;
                datetime = "";
                BMIRange = "";
                tvDate.setText(getText(R.string.date) + " " + datetime);
                tvBMI.setText(getText(R.string.bmi) + " " +ftBMI);
                tvBMIRange.setText(BMIRange);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    private void save() {
        //store date, bmi, range to Shared Preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putFloat("bmi", ftBMI);
        prefEdit.putString("date", datetime);
        prefEdit.putString("bmiRange", BMIRange);
        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //retrieve date, bmi, range from Shared Preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        float bmi = prefs.getFloat("bmi", ftBMI);
        String date = prefs.getString("date", datetime);
        String range = prefs.getString("bmiRange", BMIRange);
        //set date, bmi, range
        tvDate.setText(getText(R.string.date) + " " + date);
        tvBMI.setText(getText(R.string.bmi) + " " + bmi);
        tvBMIRange.setText(range);
    }


}

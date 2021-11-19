package com.example.rentalz_coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.Date;
import java.util.List;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements Validator.ValidationListener, AdapterView.OnItemSelectedListener {

    private Spinner propType;

    private Spinner bedroom;

    private Spinner furniture;

    @NotEmpty
    private EditText date;

    @NotEmpty
    private EditText price;

    private EditText notes;

    @NotEmpty
    private EditText reporter;

    private Button buttonSave;

    private TextView tvw;

    private Validator validator;

    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);

        Spinner spinner1 = (Spinner) findViewById(R.id.propType);
        Spinner spinner2 = (Spinner) findViewById(R.id.bedroom);
        Spinner spinner3 = (Spinner) findViewById(R.id.furniture);

        ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(this,
                R.array.propType, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> room_adapter = ArrayAdapter.createFromResource(this,
                R.array.bedroom, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> furniture_adapter = ArrayAdapter.createFromResource(this,
                R.array.furniture, android.R.layout.simple_spinner_item);

        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        furniture_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(type_adapter);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setAdapter(room_adapter);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setAdapter(furniture_adapter);
        spinner3.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private void initView() {
        propType = findViewById(R.id.propType);
        bedroom = findViewById(R.id.bedroom);
        date = findViewById(R.id.editDate);
        price = findViewById(R.id.editPrice);
        furniture = findViewById(R.id.furniture);
        notes = findViewById(R.id.editNotes);
        reporter = findViewById(R.id.editReporter);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar sDate = Calendar.getInstance();
                int day = sDate.get(Calendar.DAY_OF_MONTH);
                int month = sDate.get(Calendar.MONTH);
                int year = sDate.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(sDate.getTimeInMillis());
                picker.show();
            }
        });
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog);
                builder.setTitle("Confirm your information");
                builder.setMessage("Type: " + propType.getSelectedItem().toString() + "\n" +
                        "Room: " + bedroom.getSelectedItem().toString() + "\n" +
                        "Date: " + date.getText().toString() + "\n" +
                        "Price: " + price.getText().toString() + "\n" +
                        "Furniture: " + furniture.getSelectedItem().toString() + "\n" +
                        "Notes: " + notes.getText().toString() + "\n" +
                        "Reporter: " + reporter.getText().toString());
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buttonSave_onClick(view);
                    };
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    };
                });
                builder.show();
            }
        });
        tvw=(TextView)findViewById(R.id.textView1);
    }

    private void buttonSave_onClick(View view) {
        validator.validate();
    }


    @Override
    public void onValidationSucceeded() {
        String type = propType.getSelectedItem().toString();
        String bedrooms = bedroom.getSelectedItem().toString();
        String startDay = date.getText().toString();
        String prices = price.getText().toString();
        String furnitures = furniture.getSelectedItem().toString();
        String note = notes.getText().toString();
        String reporters = reporter.getText().toString();
        tvw.setText("Type:\t" + type + "\nBedrooms:\t" + bedrooms + "\nStartDay:\t" + startDay
                + "\nPrice:\t" + prices + "\nFurniture:\t" + furnitures
                + "\nNotes:\t" + note + "\nReporters:\t" + reporters);
        Toast.makeText(this, "Successfully submit!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
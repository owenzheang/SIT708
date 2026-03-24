package com.example.a21p;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button button;
    TextView value2;

    String[] categories = {"Currency", "Fuel", "Temperature"};
    String[] currency = {"USD", "AUD", "EUR", "JPY", "GBP"};
    String[] fuel = {"mpg", "km/L"};
    String[] tem = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextValue = findViewById(R.id.editTextValue);
        value2 = findViewById(R.id.value2);
        button = findViewById(R.id.button);

        ArrayAdapter<String>categoriesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoriesAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categorySelected = categories[position];

                if(categorySelected.equals("Currency")){
                    updateUnit(currency);
                }else if(categorySelected.equals("Fuel")){
                    updateUnit(fuel);
                }else if(categorySelected.equals("Temperature")){
                    updateUnit(tem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateUnit(String[] units){
        ArrayAdapter<String>UnitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        UnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(UnitAdapter);
        spinnerTo.setAdapter(UnitAdapter);
    }

    private void Result(){
        String inputText = editTextValue.getText().toString().trim();

        if(inputText.isEmpty()){
            Toast.makeText(this,"Please enter a value",Toast.LENGTH_SHORT).show();
            return;
        }

        double inputValue;

        try{
            inputValue = Double.parseDouble(inputText);
        }catch (NumberFormatException e){
            Toast.makeText(this, "Please enter a valid number",Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();
        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();

        if(from.equals(to)){
            value2.setText("Result:" + inputValue);
            Toast.makeText(this,"Can't select same unit", Toast.LENGTH_SHORT).show();
            return;
        }

        double result = 0.0;

        if(category.equals("Currency")){
            if(inputValue < 0){
                Toast.makeText(this,"Currency value cannot be negative",Toast.LENGTH_SHORT).show();
                return;
            }
            result = convertCurrency(from, to, inputValue);
        }else if(category.equals("Fuel")){
            if(inputValue < 0){
                Toast.makeText(this,"Fuel value cannot be negative",Toast.LENGTH_SHORT).show();
                return;
            }
            result = convertFuel(from, to, inputValue);
        }else if(category.equals("Temperature")){
            result = convertTem(from, to, inputValue);
        }

        value2.setText("Result: " + String.format("%.2f", result));
    }

    private double convertCurrency(String from, String to, double value){
        double usdValue;

        if(from.equals("USD")){
            usdValue = value;
        }else if(from.equals("AUD")){
            usdValue = value / 1.55;
        }else if(from.equals("EUR")){
            usdValue = value / 0.92;
        }else if(from.equals("JPY")){
            usdValue = value / 148.50;
        }else if(from.equals("GBP")){
            usdValue = value / 0.78;
        }else {
            usdValue = value;
        }

        if(to.equals("USD")){
            return usdValue;
        }else if(to.equals("AUD")){
            return  usdValue * 1.55;
        }else if(to.equals("EUR")){
            return  usdValue * 0.92;
        }else if(to.equals("JPY")){
            return  usdValue * 148.50;
        }else if(to.equals("GBP")){
            return  usdValue * 0.78;
        }else{
            return usdValue;
        }
    }

    private double convertFuel(String from, String to, double value){
        if(from.equals("mpg") && to.equals("km/L")){
            return value * 0.425;
        }else if(from.equals("km/L") && to.equals("mpg")){
            return value / 0.425;
        }else {
            return value;
        }
    }

    private double convertTem(String from, String to, double value){
        if(from.equals("Celsius") && to.equals("Fahrenheit")){
            return (value * 1.8) + 32;
        }else if(from.equals("Fahrenheit") && to.equals("Celsius")){
            return (value - 32) / 1.8;
        }else if(from.equals("Celsius") && to.equals("Kelvin")){
            return value + 273.15;
        }else if(from.equals("Kelvin") && to.equals("Celsius")){
            return value - 273.15;
        }else if(from.equals("Fahrenheit") && to.equals("Kelvin")) {
            return (value - 32) / 1.8 + 273.15;
        }else if(from.equals("Kelvin") && to.equals("Fahrenheit")){
            return (value - 273.15) * 1.8 + 32;
        }else{
            return value;
        }

    }
}
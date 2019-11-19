package com.groupassignment2019.bartertraderappgithuballsetup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.InputValidator;
import com.groupassignment2019.bartertraderappgithuballsetup.models.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostNewItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView imageViewNewItemImage;
    private Button addVideoButton;
    private EditText editTextVideoDescription;
    private EditText editTextNewItemTitle;
    private Button PostItemButton;


    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViewNewItemImage = (ImageView) findViewById(R.id.imageViewNewItemImage);
        addVideoButton = (Button) findViewById(R.id.addVideoButton);
        editTextVideoDescription = (EditText) findViewById(R.id.editTextVideoDescription);
        editTextNewItemTitle = (EditText) findViewById(R.id.editTextNewItemTitle);
        PostItemButton = (Button) findViewById(R.id.PostItemButton);

        setContentView(R.layout.activity_post_new_item);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Spinner spinner = findViewById(R.id.spinner);

        final List<String> categoriesList = new ArrayList<>();
        categoriesList.add("Please select Category");
        categoriesList.addAll(Arrays.asList(Category.CATEGORIES));


        //ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.Cathegories,android.R.layout.simple_spinner_item );
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinnerstyle, categoriesList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinnerstyle);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void validateAndSubmitItem(View view) {
        String imageViewNewItemImageValue;
        InputValidator iv = new InputValidator(this);
        boolean ok = iv.notEmpty(editTextNewItemTitle) && iv.notEmpty(editTextVideoDescription);

        String addVideoButtonValue = addVideoButton.getText().toString();
        String editTextVideoDescriptionValue = editTextVideoDescription.getText().toString();
        String editTextNewItemTitleValue = editTextNewItemTitle.getText().toString();
        //String PostItemButtonValue =  PostItemButton.getText().toString();
    }

    public void openVideoFileSelection(View view) {
    }
}

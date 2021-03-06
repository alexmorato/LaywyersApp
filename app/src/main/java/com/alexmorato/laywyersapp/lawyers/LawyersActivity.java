package com.alexmorato.laywyersapp.lawyers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alexmorato.laywyersapp.R;
import com.alexmorato.laywyersapp.data.LawyersDbHelper;

public class LawyersActivity extends AppCompatActivity {

    public static final String EXTRA_LAWYER_ID = "extra_lawyer_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LawyersFragment fragment = (LawyersFragment)
                getSupportFragmentManager().findFragmentById(R.id.content_lawyers);

        if (fragment == null) {
            fragment = LawyersFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_lawyers, fragment)
                    .commit();
        }

        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fabDB = (FloatingActionButton) findViewById(R.id.fabDB);
        fabDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopiarDB();
            }
        });

    }

    private void CopiarDB() {
        LawyersDbHelper dbHelper = new LawyersDbHelper(this);
        dbHelper.BackupDB(this);
    }

}

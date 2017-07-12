package com.housey.aeiton.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.housey.aeiton.R;

/**
 * Created by JAR on 25-06-2017.
 */

public class TestActivity extends NammaTvMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.test_activity, constraintLayout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return super.onNavigationItemSelected(item);
    }
}

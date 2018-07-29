package com.example.mycalendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;


public class MainActivity extends AppCompatActivity{

    private  Intent intent;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setSupportActionBar(toolbar);
        //intent = new Intent();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        intent = getIntent();

        if (intent!= null){
            int id = intent.getIntExtra("fragmentId",0);
            if (id == 1){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.fragment_container, new CalendarFragment());
                ft.commit();
                bottomNav.setSelectedItemId(R.id.bottom_calendar);
            }
            else if(id == 2){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.fragment_container, new ScheduleFragment());
                ft.commit();
                bottomNav.setSelectedItemId(R.id.bottom_schedule);
            }
            else if (id == 3){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.fragment_container, new CourseFragment());
                ft.commit();
                bottomNav.setSelectedItemId(R.id.bottom_course);
            }
            else if (id == 4){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.replace(R.id.fragment_container, new SettingFragment());
                ft.commit();
                bottomNav.setSelectedItemId(R.id.bottom_setting);
            }
            else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalendarFragment()).commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                break;
            default:
        }
        Log.i("123645320","1111111");
        return super.onOptionsItemSelected(item);
    }
        private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.bottom_calendar:
                            selectedFragment = new CalendarFragment();
                            break;
                        case R.id.bottom_schedule:
                            selectedFragment = new ScheduleFragment();
                            break;
                        case R.id.bottom_course:
                            selectedFragment = new CourseFragment();
                            break;
                        case R.id.bottom_setting:
                            selectedFragment = new SettingFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}

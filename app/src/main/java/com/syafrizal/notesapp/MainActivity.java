package com.syafrizal.notesapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.syafrizal.notesapp.Fragments.LoginFragment;
import com.syafrizal.notesapp.Fragments.NoteFragment;
import com.syafrizal.notesapp.Fragments.SettingsFragment;
import com.syafrizal.notesapp.Models.Session;
import com.syafrizal.notesapp.Models.Settings;
import com.syafrizal.notesapp.Models.User;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentListener
        , NoteFragment.OnNoteFragmentListener
        , NoteFragment.OnDisplayListener{

    private Settings settings;
    private Session session;

//    private LoginFragment.OnLoginFragmentListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = new Settings(this);
        session = new Session(settings);
        addFragment();


    }

    private void addFragment() {
        Fragment fragment = null;
        if (session.isLogin()) {
            fragment = new NoteFragment();
            ((NoteFragment) fragment).setListener(this);
            ((NoteFragment) fragment).setDisplayListener(this);
        } else {
            fragment = new LoginFragment();
            ((LoginFragment) fragment).setListener(this);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            createSettingFragment();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoginClicked(View view, String username, String password) {
        User user = session.doLogin(username, password);
        String message = "Authentication failed";
        if (user != null) {
            message = "Welcome " + username;
            session.setUser(username);
            addFragment();
        }
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLogoutClick() {
        session.doLogout();

        addFragment();
    }

    private void createSettingFragment() {
        Fragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public String getLastList() {
        if (settings.getListMode() == null){
            return  "list";
        }else{
            return settings.getListMode();
        }
    }

    @Override
    public void setLastList(String last) {
        settings.setListMode(last);
    }
}

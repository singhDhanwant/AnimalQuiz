package com.example.animalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String GUESSES = "settings_numberOfGuesses";
    public static final String ANIMALS_TYPE = "settings_animals_type";
    public static final String QUIZ_BACKGROUND_COLOR = "settings_quiz_background_color";
    public static final String QUIZ_FONT = "settings_quiz_font";
    private boolean isSettingsChanged = false;

    static Typeface chunkfive;
    static Typeface fontleroybrown;
    static Typeface wonderbardemo;

    MainActivityFragment myAnimalQuizFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chunkfive = Typeface.createFromAsset(getAssets(), "fonts/Chunkfive.otf");
        fontleroybrown = Typeface.createFromAsset(getAssets(), "fonts/FontleroyBrown.ttf");
        wonderbardemo = Typeface.createFromAsset(getAssets(), "fonts/Wonderbar Demo.otf");

        PreferenceManager.setDefaultValues(this, R.xml.quiz_preferences, false);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(settingsChangeListener);

        myAnimalQuizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.animalQuizFragment);

        myAnimalQuizFragment.modifyAnimalsguessRows(PreferenceManager.getDefaultSharedPreferences(this));
        myAnimalQuizFragment.modifyTypeOfAnimalsInQuiz(PreferenceManager.getDefaultSharedPreferences(this));
        myAnimalQuizFragment.modifyQuizFont(PreferenceManager.getDefaultSharedPreferences(this));
        myAnimalQuizFragment.modifyBackgroundColor(PreferenceManager.getDefaultSharedPreferences(this));
        myAnimalQuizFragment.resetAnimalQuiz();
        isSettingsChanged = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent preferenceIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferenceIntent);
        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener settingsChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            isSettingsChanged = true;

            if (key.equals(GUESSES)){
                myAnimalQuizFragment.modifyAnimalsguessRows(sharedPreferences);
                myAnimalQuizFragment.resetAnimalQuiz();
                Toast.makeText(MainActivity.this, R.string.change_message, Toast.LENGTH_SHORT).show();

            } else if (key.equals(ANIMALS_TYPE)){
                Set<String> animalTypes = sharedPreferences.getStringSet(ANIMALS_TYPE, null);

                if (animalTypes != null && animalTypes.size() > 0){
                    myAnimalQuizFragment.modifyTypeOfAnimalsInQuiz(sharedPreferences);
                    myAnimalQuizFragment.resetAnimalQuiz();
                    Toast.makeText(MainActivity.this, R.string.change_message, Toast.LENGTH_SHORT).show();

                } else {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    animalTypes.add(getString(R.string.default_animal_type));
                    editor.putStringSet(ANIMALS_TYPE, animalTypes);
                    editor.apply();

                    Toast.makeText(MainActivity.this, R.string.toast_message, Toast.LENGTH_SHORT).show();
                }
            } else if (key.equals(QUIZ_FONT)){
                myAnimalQuizFragment.modifyQuizFont(sharedPreferences);
                myAnimalQuizFragment.resetAnimalQuiz();
                Toast.makeText(MainActivity.this, R.string.change_message, Toast.LENGTH_SHORT).show();

            } else if (key.equals(QUIZ_BACKGROUND_COLOR)) {
                myAnimalQuizFragment.modifyBackgroundColor(sharedPreferences);
                myAnimalQuizFragment.resetAnimalQuiz();
                Toast.makeText(MainActivity.this, R.string.change_message, Toast.LENGTH_SHORT).show();

            }


        }
    };
}

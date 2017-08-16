package app.teacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditChallengeActivity extends Activity implements OnItemClickListener{

    Test test;
    final int MAX_VALUE_CHALLENGES = 200;
    ListView list;
    final String DATA_FOR_TEST = "data_for_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_challenge);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        list = (ListView) findViewById(R.id.challenges);

        test = getTest();
        Bundle extras = getIntent().getExtras();
        String testName = "";
        if (extras != null)
            testName = extras.getString("test_name");
        if(test == null)
            test = new Test(testName);
        setTitle(test.getName());
        updateListView(list, test);

   }

    public void choiceChallenge() {
        String title = getString(R.string.addquestion);
        String[] types = getResources().getStringArray(R.array.types);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(title);
        ad.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Challenge challenge = null;
                switch (item) {
                    case 0:
                        challenge = new OnlyChoiceQuestion();
                        break;
                    case 1:
                        challenge = new MultipleChoiceQuestion();
                        break;
                    case 2:
                        challenge = new InputQuestion();
                        break;
                }
                test.addChallenge(challenge);
                updateListView(list, test);
                }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Utils.showToast(EditChallengeActivity.this, getString(R.string.nochoose));
            }
        });
        ad.show();

        saveTest();
    }

    public void updateListView(ListView list, Test test){
        list.setAdapter(new TestAdapter(EditChallengeActivity.this, test));
        list.setOnItemClickListener(EditChallengeActivity.this);
    }


    public void newChallenge(View v) {
        if (test.size() <= MAX_VALUE_CHALLENGES)
            choiceChallenge();
        else
            Utils.showToast(this, getString(R.string.cannotadd));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(EditChallengeActivity.this, EditTestActivity.class);
        switch(test.getChallenge(position).getType()){
            case 0:
                intent = new Intent(EditChallengeActivity.this, EditChoiceActivity.class);
                break;
            case 1:
                intent = new Intent(EditChallengeActivity.this, EditMultipleActivity.class);
                break;
            case 2:
                intent = new Intent(EditChallengeActivity.this, EditInputActivity.class);
                break;
        }

        saveTest();
        intent.putExtra("position", position);
        intent.putExtra("challenge", test.getChallenge(position));
        startActivity(intent);

    }

    void saveTest(){
        SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Test.class, new TestConverter());
        final Gson gson = builder.create();
        String json = gson.toJson(test);
        editor.putString("test", json);
        editor.apply();
    }

    Test getTest(){
        SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Test.class, new TestConverter());
        final Gson gson = builder.create();
        String json = sharedPref.getString("test", "");
        test = gson.fromJson(json, Test.class);
        return test;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveTest();
    }
}



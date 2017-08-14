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

public class EditChallengeActivity extends Activity implements OnItemClickListener{

    Test test;
    final int MAX_VALUE_CHALLENGES = 200;
    ListView list;
    final String DATA_FOR_TEST = "data_for_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.newtest);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        test = getTest();
        if(test == null)
            test = new Test();
    }

    public void choiceChallenge() {
        String title = getString(R.string.addquestion);
        String[] types = getResources().getStringArray(R.array.types);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(title);
        ad.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Challenge challenge = new Challenge();
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
                list.setAdapter(new TestAdapter(EditChallengeActivity.this, test));
                list.setOnItemClickListener(EditChallengeActivity.this);
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


    public void newChallenge(View v) {
        if (test.size() <= MAX_VALUE_CHALLENGES)
            choiceChallenge();
        else
            Utils.showToast(this, getString(R.string.cannotadd));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(EditChallengeActivity.this, EditTestActivity.class);
        if (test.getChallenge(position) instanceof OnlyChoiceQuestion) {
            intent = new Intent(EditChallengeActivity.this, EditChoiceActivity.class);
        } else if (test.getChallenge(position) instanceof MultipleChoiceQuestion) {
            intent = new Intent(EditChallengeActivity.this, EditMultipleActivity.class);
        } else if (test.getChallenge(position) instanceof InputQuestion) {
            intent = new Intent(EditChallengeActivity.this, EditInputActivity.class);
        }

        intent.putExtra("position", position);
        //intent.putExtra("type", listAnswers.get(position).getType());
        startActivity(intent);

    }

    void saveTest(){
        SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(test);
        editor.putString("test", json);
        editor.apply();
    }

    Test getTest(){
        SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_TEST, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("test", null);
        Test test = gson.fromJson(json,Test.class);
        return test;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}



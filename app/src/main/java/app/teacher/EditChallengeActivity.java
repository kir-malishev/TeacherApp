package app.teacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditChallengeActivity extends Activity{

    Test test;
    final int MAX_VALUE_CHALLENGES = 200;
    RecyclerView list;
    final String DATA_FOR_TEST = "data_for_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_challenge);

        list = (RecyclerView) findViewById(R.id.challenges);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);

        test = getTest();
        Bundle extras = getIntent().getExtras();
        if(test == null)
            test = new Test(getString(R.string.nonameyet));
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

    public void updateListView(RecyclerView list, Test test){
        TestAdapter adapter = new TestAdapter(EditChallengeActivity.this, test, new TestAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Challenge challenge, int position) {
                Intent intent = new Intent(EditChallengeActivity.this, EditTestActivity.class);
                switch(challenge.getType()){
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
                intent.putExtra("challenge", challenge);
                startActivity(intent);
            }
        });
        ItemTouchHelper.Callback callback = new RecyclerItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(list);
        list.setAdapter(adapter);
    }


    public void newChallenge(View v) {
        if (test.size() <= MAX_VALUE_CHALLENGES)
            choiceChallenge();
        else
            Utils.showToast(this, getString(R.string.cannotadd));
    }

    public void setTestName(View v){
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        ad.setTitle(R.string.create);
        ad.setMessage(R.string.nametest);

        ad.setView(editText);

        ad.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String testName = editText.getText().toString().trim();
                if (testName.isEmpty()) {
                    Utils.showToast(EditChallengeActivity.this, getString(R.string.noname));
                    return;
                }
                test.setName(testName);
                setTitle(testName);
            }
        });

        ad.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });

        ad.show();
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /** Закрывает клавиатуру, если произедено касание какого-либо из полей. */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }
}



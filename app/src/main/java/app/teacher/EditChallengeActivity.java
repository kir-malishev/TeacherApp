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
import android.widget.EditText;

public class EditChallengeActivity extends Activity {

    Test test;
    final int MAX_VALUE_CHALLENGES = 200;
    RecyclerView list;
    TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_challenge);
        test = Test.getTest(this);
        if(test == null) {
            test = new Test();
            setTitle(getString(R.string.nonameyet));
        } else if (test.isEmpty()) {
            setTitle(getString(R.string.nonameyet));
        } else {
            if (test.getName().isEmpty())
                setTitle(getString(R.string.nonameyet));
            else
                setTitle(test.getName());
            if(!getIntent().getBooleanExtra("isContinueEditing", false))
                checkOfDesireToContinue();
        }

        list = (RecyclerView) findViewById(R.id.challenges);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        adapter = new TestAdapter(EditChallengeActivity.this, test, new TestAdapter.OnItemClickListener(){
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
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
            }
        });
        ItemTouchHelper.Callback callback = new RecyclerItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(list);
        list.setAdapter(adapter);
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
                test.saveTest(EditChallengeActivity.this);
                adapter.notifyItemInserted(test.size() - 1);
                }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Utils.showToast(EditChallengeActivity.this, getString(R.string.nochoose));
            }
        });
        ad.show();

        test.saveTest(this);
    }


    public void newChallenge(View v) {
        if (test.size() <= MAX_VALUE_CHALLENGES)
            choiceChallenge();
        else
            Utils.showToast(this, getString(R.string.cannotadd));
    }

    public void inMenu(View v){
        test.saveTest(this);
        Intent intent = new Intent(this, ActivityMenu.class);
        startActivity(intent);
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

        ad.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    void checkOfDesireToContinue(){
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.edittest);
        ad.setMessage(R.string.youwanttocontinue);
        ad.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        adapter.notifyItemRangeRemoved(0, test.size());
                        test.clear();
                        adapter.notifyDataSetChanged();
                        setTitle(getString(R.string.nonameyet));
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

    /*@Override
    public void saveTest(){
        SharedPreferences sharedPref = getSharedPreferences(Test.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        CompatibleWithJSON<Test> converter = new TestConverter();
        String json = converter.getJSON(test);
        editor.putString("test", json);
        editor.apply();
    }

    @Override
    public Test getTest(){
        SharedPreferences sharedPref = getSharedPreferences(Test.FILE_FOR_SAVE, Context.MODE_PRIVATE);
        String json = sharedPref.getString("test", "");
        CompatibleWithJSON<Test> converter = new TestConverter();
        test = converter.getFromJSON(json);
        return test;
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        test.saveTest(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        test.saveTest(this);
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



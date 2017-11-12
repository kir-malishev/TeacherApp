package app.teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditChallengeActivity extends BaseActivity {

    Test test;
    final int MAX_VALUE_CHALLENGES = 200;
    RecyclerView list;
    EditTestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_challenge);
        test = Test.getTest(this);
        if (test == null) {
            test = new Test();
            setTitle(getString(R.string.nonameyet));
        } else if (test.isEmpty()) {
            setTitle(getString(R.string.nonameyet));
        } else {
            if (test.getName().isEmpty())
                setTitle(getString(R.string.nonameyet));
            else
                setTitle(test.getName());
            if (getIntent().getBooleanExtra("isNotFinishedSinceLastTime", true))
                checkOfDesireToContinue();
        }

        list = (RecyclerView) findViewById(R.id.challenges);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        adapter = new EditTestAdapter(EditChallengeActivity.this, test, new EditTestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Challenge challenge, int position) {
                Intent intent = new Intent(EditChallengeActivity.this, EditTestActivity.class);
                switch (challenge.getType()) {
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
            }
        });
        ItemTouchHelper.Callback callback = new EditTestTouchHelper(adapter);
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
                showToast(getString(R.string.nochoose));
            }
        });
        ad.show();

        test.saveTest(this);
    }


    public void newChallenge(View v) {
        if (test.size() <= MAX_VALUE_CHALLENGES)
            choiceChallenge();
        else
            showToast(getString(R.string.cannotadd));
    }



    public void setTestName(View v) {
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
                    showToast(getString(R.string.noname));
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

    void clearRecyclerView() {
        adapter.notifyItemRangeRemoved(0, test.size());
        test.clear();
        Test.clearTestFromMemory(this);
        adapter.notifyDataSetChanged();
        setTitle(getString(R.string.nonameyet));
    }

    void checkOfDesireToContinue() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.edittest);
        ad.setMessage(R.string.youwanttocontinue);
        ad.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clearRecyclerView();
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

    public boolean checkTest() {
        int code = test.status();
        switch (code) {
            case -1:
                return true;
            case -2:
                showToast("Нет названия теста");
                return false;
            case -3:
                showToast("В тесте не ни одного вопроса");
                return false;
            case -4:
                showToast("Неизвестная ошибка");
                return false;
        }
        if (code >= 0) {
            showToast("Вы не завершили редактирование задания №" + (code + 1));
            return false;
        }
        showToast("Совсем неизвестная ошибка");
        return false;

    }

    public void saveTest(View v) {
        if (!checkTest()) return;

        String json = test.getJSON();

        showProgress("Сохраняю тест");
        API api = Request.getApi();
        Call<SaveTestResponse> call = api.sendTest(json, LoginActivity.LOGIN);
        //Utils.showToast(this, json);
        call.enqueue(new Callback<SaveTestResponse>() {
            @Override
            public void onResponse(Call<SaveTestResponse> call, Response<SaveTestResponse> response) {
                dismissProgress();
                if (response.isSuccessful()) {
                    SaveTestResponse saveTestResponse = response.body();
                    int status = saveTestResponse.getStatus();
                    switch (status) {
                        case 1:
                            showToast("Номер созданного теста: " + saveTestResponse.getTestID());
                            clearRecyclerView();
                            break;
                        case 2:
                            showToast("Тест успешно обновлён!");
                            clearRecyclerView();
                            break;
                        default:
                            showToast("Произошла какая-то ошибка!");
                    }
                } else
                    showToast("Произошла ошибка! Попробуйте в другой раз!");
            }

            @Override
            public void onFailure(Call<SaveTestResponse> call, Throwable t) {
                dismissProgress();
                showToast("Произошла ошибка! Попробуйте в другой раз!");
            }
        });
    }

    public void inMenu(View v) {
        back();
    }

    void back() {
        test.saveTest(this);
        Intent intent = new Intent(this, ActivityMenu.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

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

    /**
     * Закрывает клавиатуру, если произедено касание какого-либо из полей.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

}



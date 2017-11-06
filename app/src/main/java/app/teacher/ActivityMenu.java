package app.teacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Меню приложения.
 *
 * @version 1.0
 * @autor Кирилл Малышев
 */
public class ActivityMenu extends Activity {
    /**
     * Всплывающее диалоговое окно
     */
    AlertDialog.Builder ad;

    /**
     * Устанавливает разметку, если пользователь перешёл в активность после
     * создания теста, показывается его номер
     * {@link ActivityMenu#showID(String)}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        EditTestActivity.data_for_test = new ArrayList<HashMap<String, String>>();
        EditTestActivity.testName = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("id");
            showID(id);
        }

    }

    /**
     * Показывает всплывающее окно с номером вновь созданного теста.
     *
     * @param id Номер теста.
     */
    public void showID(String id) {
        ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.createOK);
        ad.setMessage(getString(R.string.testID) + "\n" + id);
        ad.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        ad.setCancelable(true);
        ad.show();
    }

    /**
     * Показывает окно для ввода названия теста и переводит в активность для
     * создания теста.
     *
     * @param view Кнопка "Создать тест".
     * @see EditTestActivity
     */
    public void create(View view) {
        Intent intent = new Intent(getApplicationContext(), EditChallengeActivity.class);
        startActivity(intent);
    }

    /**
     * Отправляет в активнсть для просмотра созданных тестов.
     *
     * @param view Кнопка "Мои тесты".
     * @see MyTestsActivity
     */
    public void myTests(View view) {
        API api = Request.getApi();
        Call<ResponseBody> call = api.getTestReview(LoginActivity.LOGIN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String jsonTestDescription = response.body().string();
                        TestDescription.saveJSON(ActivityMenu.this, jsonTestDescription);
                        Intent intent = new Intent(ActivityMenu.this, MyTestsActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException e) {
                        Utils.showToast(ActivityMenu.this, "Произошла ошибка! Попробуйте в другой раз!");
                        e.printStackTrace();
                    }
                } else
                    Utils.showToast(ActivityMenu.this, "Произошла ошибка! Попробуйте в другой раз!");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.showToast(ActivityMenu.this, "Произошла ошибка! Попробуйте в другой раз!");
            }
        });
    }

    /**
     * Отправляет в окно авторизации.
     *
     * @param v Кнопка "Выйти".
     * @see LoginActivity
     */
    public void exit(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
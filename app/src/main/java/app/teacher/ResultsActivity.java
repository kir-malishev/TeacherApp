package app.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

/**
 * Активность просмотра списка результатов прохождений теста.
 *
 * @version 1.0
 * @autor Кирилл Малышев
 */
public class ResultsActivity extends BaseActivity {

    /**
     * Объект для запуска потока, отправляющего запрос на сервер.
     *
     * @see ResultsActivity.Task
     */
    RequestTask task;

    /**
     * Тип запроса
     */
    String mode;

    /**
     * Идентификтор результата
     */
    String uuid;

    /**
     * Массив индентификаторов результатов {@link ResultsActivity#uuid}
     */
    ArrayList<String> uuidList = new ArrayList<String>();

    /**
     * Массив элементов ListView.
     *
     * @see ResultsActivity#list
     */
    final ArrayList<Item> results = new ArrayList<Item>();

    RecyclerView list;
    ReviewResultAdapter adapter;

    ArrayList<ResultDescription> descriptions;

    /**
     * Устанавливает разметку, отправляет запрос на сервер и выводит список
     * результатов прохождений теста.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_review);

        descriptions = ResultDescription.getResultDescriptions(this);

        if (descriptions.size() == 0)
            showToast("Никто ещё не прошёл ваш тест");

        //descriptions = new ArrayList<ResultDescription>();
        list = (RecyclerView) findViewById(R.id.results);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        adapter = new ReviewResultAdapter(descriptions, new ReviewResultAdapter.OnItemClickListener() {


            @Override
            public void onItemClick(ResultDescription resultDescription, int position) {
                //////
            }
        });

        list.setAdapter(adapter);

    }

    /**
     * Переводит пользователя в раздел "Мои тесты".
     *
     * @param v Кнопка "Назад".
     * @see MyTestsActivity
     */
    public void back(View v) {
        Intent intent = new Intent(getApplicationContext(), MyTestsActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Отправляет запрос на сервер для получения выбранного результата и
     * открывает окно для его просмотра.
     *
     * @param parent   Адаптер для ListView {@link MyTestsActivity#list}
     * @param view     Нажатый элемент списка.
     * @param position Индекс теста.
     * @param id       Идентификатор элемента.
     * @see ResultViewActivity
     */
   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        try {
            if (Utils.isConnected()) {
                mode = "inresult";
                uuid = uuidList.get(position);
                task = new Task();
                task.setContext(this);
                task.addParam("uuid", uuid);
                task.execute(RequestTask.DOMAIN + "/api/scripts/getresult.php");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
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

    /**
     * Класс для обработки ответа от сервера. Отправка запроса происходит в
     * классе {@link RequestTask}
     */
    class Task extends RequestTask {

        /**
         * Обрабатывает ответ тем или иным образом в зависимости от типа запроса
         * {@link ResultsActivity#mode}.
         *
         * @param result Ответ сервера.
         */
        /*@Override
        protected void onPostExecute(String result) {
            super.dialog.dismiss();
            try {
                if (mode.equals("getresultreview")) {
                    if (Boolean.valueOf(result))
                        showToast(getString(R.string.noresults));
                    else
                        parsingJSON(result);
                } else if (mode.equals("inresult")) {
                    Intent intent = new Intent(getApplicationContext(), ResultViewActivity.class);
                    intent.putExtra("result", result);
                    intent.putExtra("uuid", uuid);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);

        }*/
    }

}

package app.teacher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Активность просмотра списка результатов прохождений теста.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class ResultsActivity extends BaseActivity implements OnItemClickListener {

	/**
	 * Объект для запуска потока, отправляющего запрос на сервер.
	 * 
	 * @see ResultsActivity.Task
	 */
	RequestTask task;

	/** Тип запроса */
	String mode;

	/** Идентификтор результата */
	String uuid;

	/** Массив индентификаторов результатов {@link ResultsActivity#uuid} */
	ArrayList<String> uuidList = new ArrayList<String>();

	/**
	 * Массив элементов ListView.
	 * 
	 * @see ResultsActivity#list
	 */
	final ArrayList<Item> results = new ArrayList<Item>();

	/** Список вопросов теста. */
	ListView list;

	/**
	 * Устанавливает разметку, отправляет запрос на сервер и выводит список
	 * результатов прохождений теста.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_review);

		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		Bundle extras = getIntent().getExtras();
		try {
			if (extras != null && Utils.isConnected()) {
				String id;
				String testName;
				id = extras.getString("id");
				testName = extras.getString("testname");
				setTitle(testName);
				list = (ListView) findViewById(R.id.results);
				mode = "getresultreview";
				task = new Task();
				task.setContext(this);
				task.addParam("login", LoginActivity.LOGIN);
				task.addParam("id", id.toUpperCase());
				task.execute(RequestTask.DOMAIN + "/api/scripts/getresultreview.php");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Переводит пользователя в раздел "Мои тесты".
	 * 
	 * @see MyTestsActivity
	 * @param v
	 *            Кнопка "Назад".
	 */
	public void back(View v) {
		Intent intent = new Intent(getApplicationContext(), MyTestsActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * Парсит json-объект и выводит список результатов прохождений теста.
	 * 
	 * @param review
	 *            Json-объект со списком результатов теста.
	 */
	public void parsingJSON(String review) {
		try {
			JSONObject json = new JSONObject(review);
			JSONArray data = json.getJSONArray("review");
			int size = data.length();
			String studentName;
			String date;
			String uuid;
			for (int i = 0; i < size; i++) {

				studentName = data.getJSONObject(i).getString("studentname").toString();
				date = data.getJSONObject(i).getString("date").toString();
				uuid = data.getJSONObject(i).getString("uuid").toString();
				uuidList.add(uuid);
				addResult(studentName, date, "test");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Добавляет в ListView {@link ResultsActivity#list} новый элемент
	 * 
	 * @param header
	 *            Заголовок большего размера.
	 * @param subHeader
	 *            Заголовок меньшего размера.
	 * @param type
	 *            Тип вопроса теста.
	 */
	public void addResult(String header, String subHeader, String type) {

		results.add(new Item(header, subHeader, type));
		list.setAdapter(new MyAdapter(this, results));
		list.setOnItemClickListener(this);

	}

	/**
	 * Отправляет запрос на сервер для получения выбранного результата и
	 * открывает окно для его просмотра.
	 * 
	 * @see ResultViewActivity
	 * 
	 * @param parent
	 *            Адаптер для ListView {@link MyTestsActivity#list}
	 * @param view
	 *            Нажатый элемент списка.
	 * @param position
	 *            Индекс теста.
	 * @param id
	 *            Идентификатор элемента.
	 */
	@Override
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

	/**
	 * Класс для обработки ответа от сервера. Отправка запроса происходит в
	 * классе {@link RequestTask}
	 */
	class Task extends RequestTask {

		/**
		 * Обрабатывает ответ тем или иным образом в зависимости от типа запроса
		 * {@link ResultsActivity#mode}.
		 * 
		 * @param result
		 *            Ответ сервера.
		 */
		@Override
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

		}
	}

}

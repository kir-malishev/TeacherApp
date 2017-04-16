package app.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * Активность создания и редактирования теста.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
@SuppressLint({ "CommitPrefEdits", "UseValueOf" })
public class EditTestActivity extends Activity implements OnItemClickListener {

	/** Всплывающее диалоговое окно. */
	AlertDialog.Builder ad;

	/**
	 * Массив элементов ListView.
	 * 
	 * @see EditTestActivity#list
	 */
	public static ArrayList<Item> listAnswers = new ArrayList<Item>();

	/** Список вопросов теста. */
	ListView list;

	/** Название теста. */
	public static String testName;

	/** Номер теста. */
	String testId;


	/** Информация о тесте в виде json-объекта. */
	JSONObject resultJson;

	/**
	 * Кнопка "Сохранить".
	 * 
	 * @see EditTestActivity#saveTest(View)
	 */
	Button button;

	/** Если создаётся новый тест: false, иначе: true. */
	boolean isOldTest = false;

	/** Массив с информацией о вопросах теста. */
	public static ArrayList<HashMap<String, String>> data_for_test = new ArrayList<HashMap<String, String>>();

	/** Максимальной количество вариантов ответа. */
	final int MAX_VALUE_ANS = 8;

	/**
	 * Объект для запуска потока, отправляющего запрос на сервер.
	 * 
	 * @see EditTestActivity.Task
	 */
	RequestTask task;

	/**
	 * Устанавливает разметку. Если активность открыта для редактирования
	 * существующего теста, то ListView {@link EditTestActivity#list}
	 * заполняется вопросами.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtest);

		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		listAnswers = new ArrayList<Item>();
		list = (ListView) findViewById(R.id.answers);
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.getBoolean("mode")) {
			isOldTest = true;
			testId = extras.getString("id");
			addQuestions();
		}
		setTitle(testName);

		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				remove(position);
				return true;
			}
		});

	}

	/**
	 * Заполняет ListView {@link EditTestActivity#list} вопросами из массива
	 * {@link EditTestActivity#data_for_test}
	 */
	public void addQuestions() {
		String type;
		for (int position = 0; position < data_for_test.size(); position++) {
			if (data_for_test.get(position).containsKey("type"))
				type = data_for_test.get(position).get("type");
			else
				type = Item.CHOICE;
			if (type.equals(Item.CHOICE))
				addQuestion(getString(R.string.numberqq) + (listAnswers.size() + 1), getString(R.string.abouthoice),
						Item.CHOICE);
			else if (type.equals(Item.MULTIPLE))
				addQuestion(getString(R.string.numberqq) + (listAnswers.size() + 1), getString(R.string.aboutmultiple),
						Item.MULTIPLE);
			else if (type.equals(Item.INPUT))
				addQuestion(getString(R.string.numberqq) + (listAnswers.size() + 1), getString(R.string.aboutinput),
						Item.INPUT);
		}
	}

	/**
	 * Добавляет в ListView {@link EditTestActivity#list} новый элемент
	 * 
	 * @param header
	 *            Заголовок большего размера.
	 * @param subHeader
	 *            Заголовок меньшего размера.
	 * @param type
	 *            Тип вопроса теста.
	 */
	public void addQuestion(String header, String subHeader, String type) {
		Item item = new Item(header, subHeader, type);
		listAnswers.add(item);
		list.setAdapter(new MyAdapter(EditTestActivity.this, listAnswers));
		list.setOnItemClickListener(EditTestActivity.this);
	}

	/**
	 * Добавляет новый вопрос.
	 * 
	 * @param v
	 *            Кнопка "Добавить вопрос".
	 */
	public void newAns(View v) {
		final int MAX_VALUE_QQ = 200;
		if (listAnswers.size() <= MAX_VALUE_QQ)
			choiceTypeAns();
		else
			Utils.showToast(this, getString(R.string.cannotadd));
	}

	/**
	 * Удаляет вопрос.
	 * 
	 * @param position
	 *            Индекс вопроса (номер пороса - 1).
	 */
	public void remove(final int position) {
		ad = new AlertDialog.Builder(this);
		ad.setTitle(getString(R.string.remove) + " №" + (position + 1) + " ?");
		ad.setMessage(R.string.confirm);

		ad.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				data_for_test.remove(position);
				listAnswers.remove(position);
				for (int i = position; i < listAnswers.size(); i++)
					listAnswers.get(i).setHeader(getString(R.string.numberqq) + (i + 1));
				list.setAdapter(new MyAdapter(EditTestActivity.this, listAnswers));
				list.setOnItemClickListener(EditTestActivity.this);

			}
		});

		ad.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		ad.setCancelable(true);
		ad.show();
	}

	/**
	 * Отправляет пользователя в окно, из которого он попал в эту активность.
	 * 
	 * @param v
	 *            Кнопка "Назад"
	 */
	public void inMenu(View v) {

		ad = new AlertDialog.Builder(this);
		ad.setTitle(R.string.back);
		final Intent intent;
		if (isOldTest) {
			ad.setMessage(R.string.warning1);
			intent = new Intent(getApplicationContext(), MyTestsActivity.class);
		} else {
			ad.setMessage(R.string.warning);
			intent = new Intent(getApplicationContext(), ActivityMenu.class);
		}
		ad.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				data_for_test = new ArrayList<HashMap<String, String>>();
				if (isOldTest) {
					startActivity(intent);
					finish();
				} else {
					startActivity(intent);
					finish();
				}
			}
		});

		ad.setNegativeButton(R.string.stay, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		ad.setCancelable(true);
		ad.show();
	}

	/**
	 * Открывает окно редактирования выбранного вопроса.
	 * 
	 * @param parent
	 *            Адаптер для ListView {@link EditTestActivity#list}
	 * @param view
	 *            Нажатый элемент списка.
	 * @param position
	 *            Индекс вопроса (номер вопроса - 1).
	 * @param id
	 *            Идентификатор элемента.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String type = listAnswers.get(position).getType();

		Intent intent = new Intent(EditTestActivity.this, EditTestActivity.class);
		if (type.equals(Item.CHOICE)) {
			intent = new Intent(EditTestActivity.this, EditChoiceActivity.class);
		} else if (type.equals(Item.MULTIPLE)) {
			intent = new Intent(EditTestActivity.this, EditMultipleActivity.class);
		} else if (type.equals(Item.INPUT)) {
			intent = new Intent(EditTestActivity.this, EditInputActivity.class);
		}

		intent.putExtra("position", position);
		intent.putExtra("type", listAnswers.get(position).getType());
		startActivity(intent);

	}

	/**
	 * Отправляет на сервер информацию в виде json о созданном тесте.
	 * 
	 * @param v
	 *            Кнопка "Сохранить".
	 * @throws JSONException
	 */
	public void saveTest(View v) throws JSONException {
		try {
			if (Utils.isConnected()) {
				if (toJSON()) {
					task = new Task();
					task.setContext(this);
					task.addParam("testname", testName);
					if (testId != null) task.addParam("id", testId);
					task.addParam("mode", Boolean.toString(isOldTest));
					task.addParam("test", resultJson.toString());
					task.addParam("login", LoginActivity.LOGIN);
					button = (Button) findViewById(R.id.createnewtest);
					button.setEnabled(false);
					task.execute(RequestTask.DOMAIN + "/api/scripts/savetest.php");
				}
			} else
				Utils.showToast(this, getString(R.string.noconnect));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Переводит информацию о теста в json-объект для отправки на сервер.
	 * 
	 * @return false - если редактирование вопросов не завершено, иначе true.
	 * @throws JSONException
	 */
	public boolean toJSON() throws JSONException {
		int size = listAnswers.size();
		if (size < 1) {
			Utils.showToast(this, getString(R.string.noans));
			return false;
		}
		String type;
		int points;
		String quest;
		String[] ans;
		JSONArray test = new JSONArray();
		resultJson = new JSONObject();
		for (int position = 0; position < size; position++) {
			JSONObject answer = new JSONObject();
			if (data_for_test.get(position).containsKey("type"))
				type = data_for_test.get(position).get("type");
			else
				type = "";
			if (data_for_test.get(position).containsKey("points"))
				points = Integer.parseInt(data_for_test.get(position).get("points"));
			else
				points = 1;
			if (data_for_test.get(position).containsKey("qq"))
				quest = data_for_test.get(position).get("qq");
			else
				quest = "";
			ans = new String[MAX_VALUE_ANS];
			for (int g = 0; g < MAX_VALUE_ANS; g++) {
				if (data_for_test.get(position).containsKey("ans_" + g))
					ans[g] = data_for_test.get(position).get("ans_" + g);
				else
					ans[g] = "";
			}
			boolean isRight = false;
			if (quest.equals("") || (type.equals(Item.CHOICE) && (ans[0].equals("") || ans[1].equals(""))
					|| (type.equals(Item.INPUT) && ans[0].equals("")))) {
				Utils.showToast(this,
						getString(R.string.numberqq) + " " + (position + 1) + " " + getString(R.string.noend));
				return false;
			} else if (type.equals(Item.MULTIPLE) && !isAtLeastOneRight(position)) {
				Utils.showToast(this,
						getString(R.string.numberqq) + " " + (position + 1) + " " + getString(R.string.noright));
				return false;
			} else {
				answer.put("type", type);
				answer.put("points", points);
				answer.put("question", quest);
				int k = 1;
				if (type.equals(Item.MULTIPLE)) {
					for (int g = 0; g < MAX_VALUE_ANS; g++) {
						if (!ans[g].equals("")) {
							isRight = Boolean.valueOf(data_for_test.get(position).get("isright_" + g));
							answer.put("isright" + k, isRight);
							answer.put("answer" + k++, ans[g]);
						}
					}
				} else {
					for (int g = 0; g < MAX_VALUE_ANS; g++) {
						if (!ans[g].equals(""))
							answer.put("answer" + k++, ans[g]);
					}
				}
				answer.put("size", k - 1);
				test.put(answer);
			}

		}
		resultJson.put("test", test);
		resultJson.put("testname", testName);
		resultJson.put("username", LoginActivity.LOGIN);
		data_for_test = new ArrayList<HashMap<String, String>>();
		return true;

	}

	/**
	 * Проверяет, есть ли в вопросе с множественным выбором ответа хотя бы один
	 * верный ответ.
	 * 
	 * @param position
	 *            Индекс вопроса (номер вопроса - 1).
	 * @return true - если в вопросе есть хотя бы один верный ответ, иначе
	 *         false.
	 */
	public boolean isAtLeastOneRight(int position) {
		boolean atLeastOneRight = false;
		for (int i = 0; i < MAX_VALUE_ANS; i++)
			atLeastOneRight |= Boolean.valueOf(data_for_test.get(position).get("isright_" + i));
		return atLeastOneRight;
	}

	/**
	 * Открывает диалоговое окно для выбора вида вопроса и создает новый элемент
	 * в ListView {@link EditTestActivity#list} в зависимости от выбора.
	 * 
	 */
	public void choiceTypeAns() {

		String title = getString(R.string.addquestion);
		String[] types = getResources().getStringArray(R.array.types);

		ad = new AlertDialog.Builder(this);
		ad.setTitle(title);
		ad.setItems(types, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					addQuestion(getString(R.string.numberqq) + (listAnswers.size() + 1), getString(R.string.abouthoice),
							Item.CHOICE);
					data_for_test.add(new HashMap<String, String>());
					break;
				case 1:
					addQuestion(getString(R.string.numberqq) + (listAnswers.size() + 1),
							getString(R.string.aboutmultiple), Item.MULTIPLE);
					data_for_test.add(new HashMap<String, String>());
					break;
				case 2:
					addQuestion(getString(R.string.numberqq) + (listAnswers.size() + 1), getString(R.string.aboutinput),
							Item.INPUT);
					data_for_test.add(new HashMap<String, String>());
					break;
				}
			}
		});
		ad.setCancelable(true);
		ad.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Utils.showToast(EditTestActivity.this, getString(R.string.nochoose));
			}
		});
		ad.show();

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
		 * Переводит пользователя на предыдущее открытое у него окно. Если
		 * создан новый тест, запоминает его номер.
		 * 
		 * @param result
		 *            Номер теста.
		 */
		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();
			try {
				button.setEnabled(true);
				Intent intent;
				if (isOldTest)
					intent = new Intent(EditTestActivity.this, MyTestsActivity.class);
				else
					intent = new Intent(EditTestActivity.this, ActivityMenu.class);
				final int VALUE_ID = 6;
				if (result.length() >= VALUE_ID)
					intent.putExtra("id", result.toLowerCase());
				startActivity(intent);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}
}
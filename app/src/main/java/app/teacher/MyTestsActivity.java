package app.teacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

/**
 * Активность просмотра созданных тестов.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class MyTestsActivity extends Activity implements OnItemClickListener {


	/** Номер теста */
	String id;

	/** Название теста */
	String testName;

	/** Адрес электронной почты для отправки сообщений. */
	public static String email;

	/**
	 * Массив элементов ListView.
	 * 
	 * @see MyTestsActivity#list
	 */
	ArrayList<Item> tests = new ArrayList<Item>();


	/** Всплывающее диалоговое окно. */
	AlertDialog.Builder ad;

	/** Тип запроса */
	String mode;


	RecyclerView list;
	ReviewTestAdapter adapter;

	ArrayList<TestDescription> descriptions;

	/**
	 * Устанавливает разметку, отправляет запрос на сервер для получения списка
	 * созданных пользователем тестов и отображает этот список.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_review);

		email = LoginActivity.LOGIN;


		descriptions = TestDescription.getTestDescriptions(this);

		if (descriptions.size() == 0)
			Utils.showToast(this, "Вы пока не создали ни одного теста(");

		list = (RecyclerView) findViewById(R.id.tests);
		LinearLayoutManager manager = new LinearLayoutManager(MyTestsActivity.this);
		list.setLayoutManager(manager);
		adapter = new ReviewTestAdapter(descriptions, new ReviewTestAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(TestDescription testDescription, int position) {
				choiceOfAction(position);
			}
		});

		list.setAdapter(adapter);


		//mode = "gettestreview";




		/*Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String id = extras.getString("id");
			showID(id);
		}*/

	}


	/**
	 * Показывает всплывающее окно с номером вновь созданного теста.
	 * 
	 * @param id
	 *            Номер теста.
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
	 * Отправляет пользователя в меню приложения
	 * 
	 * @see ActivityMenu
	 * 
	 * @param v
	 *            Кнопка "В меню".
	 */
	public void inMenu(View v) {
		Intent intent = new Intent(this, ActivityMenu.class);
		startActivity(intent);
		finish();
	}

	/**
	 * Открывает диалоговое окно для ввода имени нового теста, после чего
	 * отправляет пользователя в окно создания теста.
	 * 
	 * @see MyTestsActivity#setTestName(int)
	 * 
	 * @see EditTestActivity
	 * 
	 * @param v
	 *            Кнопка "Создать тест".
	 */
	public void create(View v) {
		setTestName(-1);
	}


	void choiceOfAction(int position) {
		String title = getString(R.string.choiceoption);
		String[] options = getResources().getStringArray(R.array.options);
		ad = new AlertDialog.Builder(this);
		ad.setTitle(title);

		ad.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
					case 0:                // Редактировать вопросы


						break;
					case 1:                // Результаты прохождений


						break;
					case 2:                // Отправить тест на e-mail


						break;
					case 3:                // Удалить тест


						break;
				}
			}
		});
		ad.setCancelable(true);
		ad.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
			}
		});
		ad.show();

	}


	/**
	 * Открывает окно выбора действия над тестом и выполняет выбранное дествие.
	 * 
	 * @param parent
	 *            Адаптер для ListView {@link MyTestsActivity#list}
	 * @param view
	 *            Нажатый элемент списка.
	 * @param position
	 *            Индекс теста.
	 * @param ID
	 *            Идентификатор элемента.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long ID) {
		/*testName = tests.get(position).getHeader();
		id = tests.get(position).getSubHeader();
		String title = getString(R.string.choiceoption);
		String[] options = getResources().getStringArray(R.array.options);
		ad = new AlertDialog.Builder(this);
		ad.setTitle(title);
		ad.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					setTestName(position);
					break;
				case 1:
					try {
						if (Utils.isConnected()) {
							mode = "gettest";
							task = new Task();
							task.setContext(MyTestsActivity.this);
							task.addParam("login", LoginActivity.LOGIN);
							task.addParam("id", id);
							task.execute(RequestTask.DOMAIN + "/api/scripts/gettest.php");
							break;
						} else
							Utils.showToast(MyTestsActivity.this, getString(R.string.noconnect));
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 2:
					try {
						if (Utils.isConnected()) {
							Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
							intent.putExtra("id", tests.get(position).getSubHeader());
							intent.putExtra("testname", tests.get(position).getHeader());
							startActivity(intent);
							finish();
							break;
						} else
							Utils.showToast(MyTestsActivity.this, getString(R.string.noconnect));
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 3:
					toMail();
					break;
				}
			}
		});
		ad.setCancelable(true);
		ad.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
			}
		});
		ad.show();*/

	}

	/**
	 * Посылает запрос на сервер, посылающий на введённый e-mail выбранный тест
	 * в формате docx файла.
	 */
	public void toMail() {
		/*ad = new AlertDialog.Builder(this);
		final EditText editText = new EditText(this);
		editText.setSingleLine(true);
		editText.setText(email);
		ad.setTitle(R.string.mailresult);
		ad.setMessage(R.string.noemail);

		ad.setView(editText);

		ad.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				email = editText.getText().toString().trim();
				if (email.equals("")) {
					Utils.showToast(MyTestsActivity.this, getString(R.string.noname));
					return;
				} else if (!Utils.isValidEmail(email)) {
					Utils.showToast(MyTestsActivity.this, getString(R.string.erremail));
					return;
				} else
					try {
						if (!Utils.isConnected()) {
							Utils.showToast(MyTestsActivity.this, getString(R.string.noconnect));
						} else

						{
							mode = "mailtest";
							task = new Task();
							task.setContext(MyTestsActivity.this);
							task.addParam("login", LoginActivity.LOGIN);
							task.addParam("id", id);
							task.addParam("email", email);
							task.addParam("testname", testName);
							task.execute(RequestTask.DOMAIN + "/api/scripts/mailtest.php");

						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}

		});

		ad.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		ad.setCancelable(true);
		ad.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
			}
		});

		ad.show();*/
	}

	/**
	 * Открывает окно для ввода имени для нового теста и переводит пользователя
	 * в окно создания теста.
	 * 
	 * @see EditTestActivity
	 * @param position
	 *            Индекс элемента.
	 */
	public void setTestName(final int position) {
		/*ad = new AlertDialog.Builder(this);
		final EditText editText = new EditText(this);
		editText.setSingleLine(true);
		if (position == -1)
			ad.setTitle(R.string.create);
		else {
			ad.setTitle(R.string.rename);
			editText.setText(testName);
		}
		ad.setMessage(R.string.nametest);

		ad.setView(editText);

		ad.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String text = editText.getText().toString().trim();
				if (text.equals("")) {
					Utils.showToast(MyTestsActivity.this, getString(R.string.noname));
					return;
				} else if (text.equals(testName))
					return;
				try {
					if (position == -1) {
						EditTestActivity.testName = text;
						EditTestActivity.data_for_test = new ArrayList<HashMap<String, String>>();
						Intent intent = new Intent(getApplicationContext(), EditTestActivity.class);
						startActivity(intent);
						finish();
					} else if (Utils.isConnected()) {
						mode = "renametest";
						testName = text;
						tests.get(position).setHeader(testName);
						list.setAdapter(new MyAdapter(getApplicationContext(), tests));
						task = new Task();
						task.setContext(MyTestsActivity.this);
						task.addParam("login", LoginActivity.LOGIN);
						task.addParam("id", id);
						task.addParam("testname", testName);
						task.execute(RequestTask.DOMAIN + "/api/scripts/renametest.php");
					} else
						Utils.showToast(MyTestsActivity.this, getString(R.string.noconnect));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		ad.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		ad.setCancelable(true);
		ad.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Utils.showToast(MyTestsActivity.this, getString(R.string.noname));
			}
		});

		ad.show();*/
	}

	/**
	 * Записывает информацию по выбранному для редактирования тесту в массив
	 * {@link EditTestActivity#data_for_test} и отправляет пользователя в окно
	 * редактирования теста.
	 * 
	 * @see EditTestActivity
	 * @param review
	 *            Json-объект с информацией по тесту.
	 */
	public void getTest(String review) {

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

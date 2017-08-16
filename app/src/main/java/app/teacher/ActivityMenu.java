package app.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Меню приложения.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class ActivityMenu extends Activity {
	/** Всплывающее диалоговое окно */
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
	 * Показывает окно для ввода названия теста и переводит в активность для
	 * создания теста.
	 * 
	 * @param view
	 *            Кнопка "Создать тест".
	 * @see EditTestActivity
	 */
	public void create(View view) {
		ad = new AlertDialog.Builder(this);
		final EditText editText = new EditText(this);
		editText.setSingleLine(true);
		ad.setTitle(R.string.create);
		ad.setMessage(R.string.nametest);

		ad.setView(editText);

		ad.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String testName = editText.getText().toString().trim();
				if (testName.equals("")) {
					Utils.showToast(ActivityMenu.this, getString(R.string.noname));
					return;
				}
				//EditTestActivity.testName = testName;
				//Intent intent = new Intent(getApplicationContext(), EditTestActivity.class);
				Intent intent = new Intent(getApplicationContext(), EditChallengeActivity.class);
				intent.putExtra("test_name", testName);
				startActivity(intent);
				finish();
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

		ad.show();
	}

	/**
	 * Отправляет в активнсть для просмотра созданных тестов.
	 * 
	 * @param view
	 *            Кнопка "Мои тесты".
	 * @see MyTestsActivity
	 */
	public void myTests(View view) {

		try {
			if (Utils.isConnected()) {
				Intent intent = new Intent(this, MyTestsActivity.class);
				startActivity(intent);
				finish();
			} else
				Utils.showToast(this, getString(R.string.noconnect));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Отправляет в окно авторизации.
	 * 
	 * @param v
	 *            Кнопка "Выйти".
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

	/** Закрывает клавиатуру, если произедено касание какого-либо из полей. */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
			hideKeyboard();
		return super.dispatchTouchEvent(ev);
	}

}
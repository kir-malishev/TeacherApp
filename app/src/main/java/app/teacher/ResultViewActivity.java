package app.teacher;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Активность просмотра результата прохождения теста.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class ResultViewActivity extends BaseActivity {

	/** Резульатат прохождения в виде html-кода */
	String result;

	/** Идентификатор результата */
	String uuid;

	/**
	 * Объект для запуска потока, отправляющего запрос на сервер.
	 * 
	 * @see ResultsActivity.Task
	 */
	RequestTask task;

	/**
	 * Устанавливает разметку. Выводит результат прохождение в текстовое поле.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		TextView text = (TextView) findViewById(R.id.result);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			result = extras.getString("result");
			uuid = extras.getString("uuid");
			text.setText(Html.fromHtml(result));
		}
	}

	/**
	 * Делает запрос на сервер для отправки резльтата прохождения теста на
	 * электронную почту {@link MyTestsActivity#email}.
	 * 
	 * @param v
	 *            Кнопка "E-mail"
	 */
	/*public void toMail(View v) {
		AlertDialog.Builder ad;
		ad = new AlertDialog.Builder(this);
		final EditText editText = new EditText(this);
		editText.setSingleLine(true);
		editText.setText(MyTestsActivity.email);
		ad.setTitle(R.string.mailresult);
		ad.setMessage(R.string.noemail);

		ad.setView(editText);

		ad.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				MyTestsActivity.email = editText.getText().toString().trim();
				if (MyTestsActivity.email.equals("")) {
					showToast(getString(R.string.noname));
					return;
				} else if (!Utils.isValidEmail(MyTestsActivity.email)) {
					showToast(getString(R.string.erremail));
					return;
				} else
					try {
						if (!Utils.isConnected()) {
							showToast(getString(R.string.noconnect));
						} else {

							task = new Task();
							task.setContext(ResultViewActivity.this);
							task.addParam("result", result);
							task.addParam("email", MyTestsActivity.email);
							task.addParam("uuid", uuid);
							task.execute(RequestTask.DOMAIN + "/api/scripts/mailresult.php");

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

		ad.show();
	}

	/**
	 * Закрывает окно просмотра результата и возваращает пользователя к списку
	 * результатов {@link ResultsActivity}.
	 * 
	 * @param v
	 *            Кнопка "Назад".
	 */
	public void back(View v) {
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

	/**
	 * Класс для обработки ответа от сервера. Отправка запроса происходит в
	 * классе {@link RequestTask}
	 */
	class Task extends RequestTask {

		/**
		 * Обрабатывает ответ от сервера. Выводит сообщение об удачной либо
		 * неудочной отправке сообщения на электронную почту.
		 * 
		 * @param result
		 *            Ответ сервера. true - при удачной отправке e-mail, false -
		 *            при неудачной.
		 */
		@Override
		protected void onPostExecute(String result) {

			super.dialog.dismiss();
			if (Boolean.valueOf(result))
				showToast(getString(R.string.succesfulmail));
			else
				showToast(getString(R.string.errormail));

			super.onPostExecute(result);

		}
	}
}

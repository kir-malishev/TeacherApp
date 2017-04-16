package app.teacher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/** Активность для регистрации пользователей. 
 * @autor Кирилл Малышев
 * @version 1.0
*/
public class RegActivity extends Activity {

	/**Поле для ввода логина (e-mail).*/
	public EditText editLogin;
	
	/**Поле для ввода пароля.*/
	public EditText editPass;
	
	/**Поле для подтверждения пароля.*/
	public EditText editPass1;
	
	/**Объект для запуска потока, отправляющего запрос на сервер.
	 * @see RegActivity.Task*/
	RequestTask task;
	
	/**Строка с логином из поля {@link RegActivity#editLogin}*/
	String email;
	
	/**Строка с паролем из поля {@link RegActivity#editPass}*/
	String password;
	
	/**Строка с подтверждением пароля из поля {@link RegActivity#editPass1*/
	String password1;
	
	/**Кнопка "Отправить".
	 * @see RegActivity#registration(View)*/
	Button button;

	/**Выводит разметку на экран*/
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		editLogin = (EditText) findViewById(R.id.editemail);
		editPass = (EditText) findViewById(R.id.editpass);
		editPass1 = (EditText) findViewById(R.id.editpass1);
	}

	/**Отвравляет пользователя в окно авторизации.
	 * @param view Кнопка "Назад".
	 * @see LoginActivity
	 */
	public void back(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	/**Проверяет корректность введённых данных и отпраляет запрос на сервер для регистрации.
	 * @param view Кнопка "Отправить".
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void registration(View view) throws InterruptedException, IOException {
		final int MIN_LENGTH_PASS = 6;
		email = editLogin.getText().toString().toLowerCase();
		password = editPass.getText().toString();
		password1 = editPass1.getText().toString();
		if (email.equals("")) {
			Utils.showToast(this, getString(R.string.noemail));
		} else if (!Utils.isValidEmail(editLogin.getText().toString())) {
			Utils.showToast(this, getString(R.string.erremail));
		} else if (password.equals("")) {
			Utils.showToast(this, getString(R.string.nopass));
		} else if (password.length() < MIN_LENGTH_PASS) {
			Utils.showToast(this, getString(R.string.veryshortpass));
		} else if (password1.equals("")) {
			Utils.showToast(this, getString(R.string.nopass1));
		} else if (!password.equals(password1)) {
			Utils.showToast(this, getString(R.string.otherpass));
		} else if (!Utils.isConnected()) {
			Utils.showToast(this, getString(R.string.noconnect));
		} else {
			task = new Task();
			task.setContext(this);
			task.addParam("login", email);
			task.addParam("pass", password);
			button = (Button) findViewById(R.id.buttonreg);
			button.setEnabled(false);
			task.execute(RequestTask.DOMAIN + "/api/scripts/getreg.php");
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

	/**Класс для обработки ответа от сервера.
	 * Отправка запроса происходит в классе {@link RequestTask}
	 */
	class Task extends RequestTask {

		/**Обрабатывает ответ сервера.
		 * При удачной регистрации отправляет пользователя в окно авторизации.
		 * @see LoginActivity
		 * @param result Ответ от сервера:
		 * "true" - удачная авторизация,
		 * "false" - неудачная авторизация.
		 */
		@Override
		protected void onPostExecute(String result) {
			super.dialog.dismiss();
			button.setEnabled(true);
			if (Boolean.valueOf(result)) {
				Intent intent = new Intent(RegActivity.this, LoginActivity.class);
				intent.putExtra("login", email);
				intent.putExtra("password", password);
				startActivity(intent);
				finish();
			} else {
				Utils.showToast(RegActivity.this, getString(R.string.emailalreadywas));
			}
			super.onPostExecute(result);
		}

	}
}
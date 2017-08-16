package app.teacher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


/** Активность для авторицации пользователей. 
 * @autor Кирилл Малышев
 * @version 1.0
*/


@SuppressLint("CommitPrefEdits")
public class LoginActivity extends Activity {
	/**Поле для ввода логина (e-mail)*/
	EditText editLogin;
	
	/**Поле для ввода пароля*/
	EditText editPass;
	
	/**Флажок для подтверждения запоминания
	 *  в памяти устройства логина и пароля.*/
	CheckBox isRemember;
	
	/**Объект для запуска потока, отправляющего запрос на сервер.
	 * @see LoginActivity.Task*/
	RequestTask task;
	
	/**Строка из поля для ввода логина {@link LoginActivity#editLogin}.*/
	String loginFromEdit;
	
	/**Логин пользователя.*/
	public static String LOGIN;
	
	/**Пароль пользователя.*/
	private String PASSWORD;
	
	/**Кнопка "Войти".
	 * @see LoginActivity#toAuth(View)*/
	Button buttonToAuth;
	
	/**Имя файла для хранения логина и пароля*/
	private static final String DATA_FOR_AUTH = "DataForAuth";

	/**Выводит разметру на экран
	 * и вставляет логин и пароль,
	 * если они были сохранены,
	 * в соответствующие поля.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		editLogin = (EditText) findViewById(R.id.authemail);
		editPass = (EditText) findViewById(R.id.authpass);
		isRemember = (CheckBox) findViewById(R.id.remember);
		
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		LOGIN = sharedPref.getString("Login", "");
		PASSWORD = sharedPref.getString("Password", "");
		
		Intent intent = getIntent();
		if(intent.hasExtra("login") && intent.hasExtra("password")){
			LOGIN = intent.getStringExtra("login");
			PASSWORD = intent.getStringExtra("password");
		}


		if (!LOGIN.equals(""))
			isRemember.setChecked(true);
		editLogin.setText(LOGIN);
		editPass.setText(PASSWORD);

	}

    
    /** Запоминает логин и пароль в память устройства.
     * @param email Логин.
     * @param password Пароль.
    */
	public void saveAuth(String email, String password) {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putString("Login", email);
		editor.putString("Password", password);
		editor.apply();
	}

    /** Удаляет логин и пароль из памяти устройства.*/
	public void forgetAuth() {
		SharedPreferences sharedPref = getSharedPreferences(DATA_FOR_AUTH, Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.clear();
		editor.apply();
	}

	/**Закрывает текущую активностьти отправляет пользователя в окно регистрации.
	 * @param view Кнопка "Регистрация".
	 * @see RegActivity
	 */
	public void toFormReg(View view) {
		Intent intent = new Intent(this, RegActivity.class);
		startActivity(intent);
		finish();
	}

	/**Проверяет корректность введённых данных и отправляет запрос на авторизацию на сервер.
	 * @param view Кнопка "Войти".
	 * @throws InterruptedException
	 * @throws IOException
	 * @see LoginActivity.Task
	 */
	public void toAuth(View view) throws InterruptedException, IOException {
		loginFromEdit = editLogin.getText().toString().toLowerCase();
		PASSWORD = editPass.getText().toString();
		if (loginFromEdit.equals("")) {
			Utils.showToast(this, getString(R.string.noemail));
		} else if (PASSWORD.equals("")) {
			Utils.showToast(this, getString(R.string.nopass));
		} else if (!Utils.isConnected()) {
			Utils.showToast(this, getString(R.string.noconnect));
		} else {
			task = new Task();
			task.addParam("login", loginFromEdit);
			task.addParam("pass", PASSWORD);
			task.setContext(this);
			buttonToAuth = (Button) findViewById(R.id.toauth);
			buttonToAuth.setEnabled(false);
			task.execute(RequestTask.DOMAIN + "/api/scripts/getauth.php");
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
		 * При удачной авторизации отправляет пользователя в меню.
		 * @see ActivityMenu
		 * @param result Ответ от сервера:
		 * "true" - удачная авторизация,
		 * "false" - неудачная авторизация.
		 */
		@Override
		protected void onPostExecute(String result) {
			super.dialog.dismiss();
			buttonToAuth.setEnabled(true);
			if (Boolean.valueOf(result)) {
				LOGIN = loginFromEdit;
				if (isRemember.isChecked())
					saveAuth(LOGIN, PASSWORD);
				else
					forgetAuth();
				Intent intent = new Intent(LoginActivity.this, ActivityMenu.class);
				startActivity(intent);
				finish();

			} else {
				Utils.showToast(LoginActivity.this, getString(R.string.errorlogin));
			}
			super.onPostExecute(result);
		}

	}
}
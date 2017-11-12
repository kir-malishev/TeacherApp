package app.teacher;

import android.text.TextUtils;

import java.io.IOException;

/**
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class Utils {

	/**
	 * Проверяет адрес электронной почты на корректность.
	 * 
	 * @param target
	 *            E-mail.
	 * @return true, если e-mail корректный, иначе false.
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	/**
	 * Отправляет ping запрос на сервер для проверки соединения.
	 * 
	 * @return true, если ответ получен, иначе false.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static boolean isConnected() throws InterruptedException, IOException {
		String command = "ping -c 1 -w 1 www.malishevkir.fvds.ru";
		return (Runtime.getRuntime().exec(command).waitFor() == 0);
	}


}

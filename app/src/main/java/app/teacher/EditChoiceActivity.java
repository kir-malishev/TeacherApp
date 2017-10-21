package app.teacher;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Активность редактирования вопроса с выбором одного ответа.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class EditChoiceActivity extends Activity {

	/** Индекс вопроса (номер вопроса - 1). */
	int position;

	/** Тип вопроса. */
	String type;

	/** Окно для добавления вариантов ответа. */
	LinearLayout list;

	/**
	 * Парметры для тестовых полей с вариантами ответа
	 * {@link EditChoiceActivity#editTextList}.
	 */
	LinearLayout.LayoutParams editParams;

	/** Список с текстовыми полями для добавления вариантов ответа. */
	private List<EditText> editTextList = new ArrayList<EditText>();

	/** Текстовое поле с правильным ответом. */
	EditText rightAnswer;

	/** Текстовое поле с вопросом. */
	EditText qq;

	/** Поле для выбора количества очков за верный ответ. */
	Spinner spinner;

	/** Основное окно разметки. */
	ScrollView view;


	Test test;

	OnlyChoiceQuestion onlyChoiceQuestion;


	final int MAX_SIZE = 6;


	/**
	 * Устанавливает разметку. Отображает информацию о вопросе, если она есть.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.qq_with_choice);

		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		test = Test.getTest(this);

		final int position = getIntent().getIntExtra("position", 0);  // номер вопроса в тесте

		onlyChoiceQuestion = (OnlyChoiceQuestion) test.getChallenge(position);


		TextView label = (TextView) findViewById(R.id.questionlabel);
		label.setText(getString(R.string.numberqq) + (position + 1));

		rightAnswer = (EditText) findViewById(R.id.ans0);
		qq = (EditText) findViewById(R.id.editqq);

		list = (LinearLayout) findViewById(R.id.answers);
		editParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		spinner = (Spinner) findViewById(R.id.pointsforchoice);
		String[] points = getResources().getStringArray(R.array.points);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_for_spinner, R.id.spinnertext,
				points);

		spinner.setAdapter(adapter);


		setData();

		view = (ScrollView) findViewById(R.id.scrollView3);
		view.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
			public void onSwipeLeft() {
				if (position <= test.size()) {
					swipe(position + 1);
				}
			}
			public void onSwipeRight() {
				if (position > 0) {
					swipe(position - 1);
				}
			}
		});

	}

	/**
	 * Обрабатывает свайп вправо или влево. Открывает предыдущий или следующий
	 * вопрос, если он есть.
	 * 
	 * @param position
	 *            Индекс вопроса (номер вопроса - 1), на который нужно перейти.
	 */
	public void swipe(int position) {
		saveQuestion();
		Intent intent = new Intent(this, EditChoiceActivity.class);
		switch(test.getChallenge(position).getType()){
			case 0:
				intent = new Intent(this, EditChoiceActivity.class);
				break;
			case 1:
				intent = new Intent(this, EditMultipleActivity.class);
				break;
			case 2:
				intent = new Intent(this, EditInputActivity.class);
				break;
		}
		intent.putExtra("position", position);
		startActivity(intent);
		finish();
		/*String type = EditTestActivity.listAnswers.get(position).getType();
		Intent intent = new Intent(EditChoiceActivity.this, EditTestActivity.class);
		if (type.equals("choice")) {
			intent = new Intent(EditChoiceActivity.this, EditChoiceActivity.class);
		} else if (type.equals("multiple")) {
			intent = new Intent(EditChoiceActivity.this, EditMultipleActivity.class);
		} else if (type.equals("input")) {
			intent = new Intent(EditChoiceActivity.this, EditInputActivity.class);
		}

		intent.putExtra("position", position);
		intent.putExtra("type", EditTestActivity.listAnswers.get(position).getType());
		startActivity(intent);
		finish();*/
	}

	/**
	 * Сохраняет информацию о вопросе в массив
	 * {@link EditTestActivity#data_for_test}
	 */
	public void saveQuestion() {
		onlyChoiceQuestion.clear();
		onlyChoiceQuestion.setPoints(Integer.parseInt(spinner.getSelectedItem().toString()));
		onlyChoiceQuestion.setQuestion(qq.getText().toString().trim());
		onlyChoiceQuestion.setRightAnswer(rightAnswer.getText().toString().trim());
		for (int i = 0; i < editTextList.size(); i++) {
			String text = editTextList.get(i).getText().toString().trim();
			if (!text.equals("")) {
				onlyChoiceQuestion.addAnswer(text);
			}
		}
		test.saveTest(this);
	}

	/**
	 * Получает информацию о вопросе из массива
	 * {@link EditTestActivity#data_for_test} и выводит её на экран.
	 * 
	 */
	public void setData() {

		qq.setText(onlyChoiceQuestion.getQuestion());

		rightAnswer.setText(onlyChoiceQuestion.getRightAnswer());

		ArrayList<String> incorrectAnswers = onlyChoiceQuestion.getIncorrectAnswers();

		if(incorrectAnswers.size() == 0){
				addEdit("");
		}
		else {
			for (String answer : incorrectAnswers) {
				addEdit(answer);
			}
		}
		spinner.setSelection(onlyChoiceQuestion.getPoints() - 1);
	}

	/**
	 * Добавляет новое текстовое поле для ввода варианта ответа.
	 * 
	 */
	public void addEdit(String text) {

		EditText editTxt;
		int size = editTextList.size();
		editTxt = new EditText(this);
		editTxt.setLayoutParams(editParams);
		editTxt.setHint(getString(R.string.numbervar) + (size + 2));
		editTxt.setSingleLine(true);
		editTxt.setText(text);
		editTextList.add(editTxt);
		list.addView(editTxt);
	}

	/**
	 * Добавляет новое текстовое поле для ввода варианта ответа.
	 * 
	 * @param v
	 *            Кнопка "Добавить".
	 */
	public void add(View v) {
		addEdit("");
	}

	/**
	 * Удаляет нижнее поле для ввода варианта ответа.
	 * 
	 * @param v
	 *            Кнопка "Удалить".
	 */
	public void remove(View v) {
		final int MIN_VALUE_ANSWERS = 1;
		int size = editTextList.size();
		if (size >= MIN_VALUE_ANSWERS + 1) {
			editTextList.get(size - 1).setVisibility(View.GONE);
			editTextList.remove(size - 1);
			saveQuestion();
		} else {
			Utils.showToast(this, getString(R.string.lessnot));
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
	 * Сохраняет информацию о вопросе в массив
	 * {@link EditTestActivity#data_for_test}, закрывает активность и возвращает
	 * пользователя к списку вопросов.
	 * 
	 * @param v
	 *            Кнопка "Назад".
	 */
	public void back(View v) {
		saveQuestion();
		Intent intent = new Intent(this, EditChallengeActivity.class);
		intent.putExtra("isContinueEditing", true);
		startActivity(intent);
		finish();
	}


	@Override
	protected void onPause() {
		super.onPause();
		saveQuestion();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveQuestion();
	}
}
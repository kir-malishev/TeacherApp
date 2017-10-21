package app.teacher;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Активность редактирования вопроса с вводом ответа.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class EditInputActivity extends Activity {

	/** Индекс вопроса (номер вопроса - 1). */
	int position;

	/** Тип вопроса. */
	String type;

	/** Окно для добавления вариантов ответа. */
	LinearLayout list;

	/**
	 * Парметры для тестовых полей с вариантами ответа
	 * {@link EditInputActivity#editTextList}.
	 */
	LinearLayout.LayoutParams editParams;

	/** Список с текстовыми полями для добавления вариантов ответа. */
	private List<EditText> editTextList = new ArrayList<EditText>();

	/** Текстовое поле с вопросом. */
	EditText qq;

	/** Поле для выбора количества очков за верный ответ. */
	Spinner spinner;

	/** Основное окно разметки. */
	ScrollView view;

	Test test;

	InputQuestion inputQuestion;

	/**
	 * Устанавливает разметку. Отображает информацию о вопросе, если она есть.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*Bundle extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt("position");
			type = extras.getString("type");
		}*/

		setContentView(R.layout.qq_with_input);

		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		test = Test.getTest(this);

		final int position = getIntent().getIntExtra("position", 0);  // номер вопроса в тесте

		inputQuestion = (InputQuestion) test.getChallenge(position);

		TextView label = (TextView) findViewById(R.id.questionlabel);
		label.setText(getString(R.string.numberqq) + (position + 1));

		qq = (EditText) findViewById(R.id.editqq);

		list = (LinearLayout) findViewById(R.id.questions);
		editParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		spinner = (Spinner) findViewById(R.id.pointsforinput);
		String[] points = getResources().getStringArray(R.array.points);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.style_for_spinner, R.id.spinnertext,
				points);

		spinner.setAdapter(adapter);

		//getQuestion();

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
		Intent intent = new Intent(this, EditInputActivity.class);
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


		/*saveQuestion();
		String type = EditTestActivity.listAnswers.get(position).getType();
		Intent intent = new Intent(EditInputActivity.this, EditTestActivity.class);
		if (type.equals(Item.CHOICE)) {
			intent = new Intent(EditInputActivity.this, EditChoiceActivity.class);
		} else if (type.equals(Item.MULTIPLE)) {
			intent = new Intent(EditInputActivity.this, EditMultipleActivity.class);
		} else if (type.equals(Item.INPUT)) {
			intent = new Intent(EditInputActivity.this, EditInputActivity.class);
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
		inputQuestion.clear();
		inputQuestion.setPoints(Integer.parseInt(spinner.getSelectedItem().toString()));
		inputQuestion.setQuestion(qq.getText().toString().trim());
		for (int i = 0; i < editTextList.size(); i++) {
			String text = editTextList.get(i).getText().toString().trim();
			if (!text.equals("")) {
				inputQuestion.addRightAnswer(text);
			}
		}
		test.saveTest(this);

		/*String points = spinner.getSelectedItem().toString();
		EditTestActivity.data_for_test.get(position).put("points", points);
		EditTestActivity.data_for_test.get(position).put("type", type);
		EditTestActivity.data_for_test.get(position).put("qq", qq.getText().toString().trim());
		EditTestActivity.data_for_test.get(position).put("size", Integer.toString(editTextList.size()));
		String text;
		int k = 0;
		for (int i = 0; i < editTextList.size(); i++) {
			text = editTextList.get(i).getText().toString().trim();
			if (!text.equals(""))
				EditTestActivity.data_for_test.get(position).put("ans_" + (k++), text);
		}*/
	}

	public void setData(){
		qq.setText(inputQuestion.getQuestion());
		if(inputQuestion.numberOfRightAnswers() == 0){
			addEdit("");
		} else{
			for(String rightAnswer: inputQuestion.getRightAnswer()){
				addEdit(rightAnswer);
			}
		}
		spinner.setSelection(inputQuestion.getPoints() - 1);
	}



	/**
	 * Добавляет новое текстовое поле для ввода варианта ответа.
	 * 
	 */
	public void addEdit(String text) {
		EditText editTxt;
		final int MAX_VALUE_ANSWERS = 8;
		int size = editTextList.size();
		if (size <= MAX_VALUE_ANSWERS - 1) {
			editTxt = new EditText(this);
			editTxt.setLayoutParams(editParams);
			editTxt.setHint(getString(R.string.numbervar) + (size + 1));
			editTxt.setText(text);
			editTxt.setSingleLine(true);
			editTextList.add(editTxt);
			list.addView(editTxt);
		} else {
			Utils.showToast(this, getString(R.string.mustnot));
		}
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
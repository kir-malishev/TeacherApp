package app.teacher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Активность редактирования вопроса с выбором одного или нескольких ответов.
 * 
 * @autor Кирилл Малышев
 * @version 1.0
 */
public class EditMultipleActivity extends BaseActivity {

	/** Индекс вопроса (номер вопроса - 1). */
	int position;

	/** Тип вопроса. */
	String type;

	/** Окно для добавления вариантов ответа. */
	LinearLayout list;

	/**
	 * Парметры для тестовых полей с вариантами ответа
	 * {@link EditMultipleActivity#editTextList}.
	 */
	LinearLayout.LayoutParams editParams;

	/**
	 * Парметры для разделительный линий между вариантами ответа
	 * {@link EditMultipleActivity#viewList}.
	 */
	LinearLayout.LayoutParams viewParams;

	/** Список с чекбоксами для указания верности вариантов ответа. */
	private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();

	/** Список с текстовыми полями для добавления вариантов ответа. */
	private List<EditText> editTextList = new ArrayList<EditText>();

	/** Список с разделительными линиями между вариантами ответа. */
	private List<View> viewList = new ArrayList<View>();

	/** Текстовое поле с вопросом. */
	EditText qq;

	/** Поле для выбора количества очков за верный ответ. */
	Spinner spinner;

	/** Основное окно разметки. */
	ScrollView view;

	Test test;

	MultipleChoiceQuestion multipleChoiceQuestion;

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

		setContentView(R.layout.qq_with_multiple);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		test = Test.getTest(this);

		final int position = getIntent().getIntExtra("position", 0);  // номер вопроса в тесте

		multipleChoiceQuestion = (MultipleChoiceQuestion) test.getChallenge(position);

		TextView label = (TextView) findViewById(R.id.questionlabel);
		label.setText(getString(R.string.numberqq) + (position + 1));

		qq = (EditText) findViewById(R.id.editqq);

		list = (LinearLayout) findViewById(R.id.questions);
		editParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);

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
		Intent intent = new Intent(this, EditMultipleActivity.class);
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
		Intent intent = new Intent(EditMultipleActivity.this, EditTestActivity.class);
		if (type.equals(Item.CHOICE)) {
			intent = new Intent(EditMultipleActivity.this, EditChoiceActivity.class);
		} else if (type.equals(Item.MULTIPLE)) {
			intent = new Intent(EditMultipleActivity.this, EditMultipleActivity.class);
		} else if (type.equals(Item.INPUT)) {
			intent = new Intent(EditMultipleActivity.this, EditInputActivity.class);
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
		multipleChoiceQuestion.clear();
		multipleChoiceQuestion.setPoints(Integer.parseInt(spinner.getSelectedItem().toString()));
		multipleChoiceQuestion.setQuestion(qq.getText().toString().trim());
		for (int i = 0; i < editTextList.size(); i++) {
			String text = editTextList.get(i).getText().toString().trim();
			if (!text.equals("")) {
				multipleChoiceQuestion.addAnswer(text, checkBoxes.get(i).isChecked());
			}
		}
		test.saveTest(this);

		/*String points = spinner.getSelectedItem().toString();
		EditTestActivity.data_for_test.get(position).put("points", points);
		EditTestActivity.data_for_test.get(position).put("type", type);
		EditTestActivity.data_for_test.get(position).put("qq", qq.getText().toString().trim());
		EditTestActivity.data_for_test.get(position).put("size", Integer.toString(editTextList.size()));
		boolean isRight;
		String text;
		int k = 0;
		for (int i = 0; i < editTextList.size(); i++) {
			text = editTextList.get(i).getText().toString().trim();
			if (!text.equals("")) {
				EditTestActivity.data_for_test.get(position).put("ans_" + (k), text);
				isRight = checkBoxes.get(i).isChecked();
				EditTestActivity.data_for_test.get(position).put("isright_" + (k++), Boolean.toString(isRight));
			}
		}*/
	}

	/**
	 * Получает информацию о вопросе из массива
	 * {@link EditTestActivity#data_for_test} и выводит её на экран.
	 * 
	 */
	/*public void getQuestion() {
		int size;
		String question = "";
		int points;
		if (EditTestActivity.data_for_test.get(position).containsKey("size"))
			size = Integer.parseInt(EditTestActivity.data_for_test.get(position).get("size"));
		else
			size = 1;
		if (EditTestActivity.data_for_test.get(position).containsKey("qq"))
			question = EditTestActivity.data_for_test.get(position).get("qq");
		else
			question = "";
		qq.setText(question);
		if (EditTestActivity.data_for_test.get(position).containsKey("points"))
			points = Integer.parseInt(EditTestActivity.data_for_test.get(position).get("points"));
		else
			points = 1;
		spinner.setSelection(points - 1);
		String ans;
		int j = 0;
		boolean isRight;
		for (int i = 0; i < size; i++) {
			isRight = Boolean.valueOf(EditTestActivity.data_for_test.get(position).get("isright_" + i));
			if (EditTestActivity.data_for_test.get(position).containsKey("ans_" + i))
				ans = EditTestActivity.data_for_test.get(position).get("ans_" + i);
			else
				ans = "";
			if (!ans.equals("")) {
				addCheckBox();
				checkBoxes.get(j).setChecked(isRight);
				editTextList.get(j++).setText(ans);
			}
		}
		if (editTextList.size() == 0) {
			addCheckBox();
			addCheckBox();
		}
	}*/

	/**
	 * Получает информацию о вопросе из массива
	 * {@link EditTestActivity#data_for_test} и выводит её на экран.
	 *
	 */
	public void setData() {
		qq.setText(multipleChoiceQuestion.getQuestion());
		for(String answer : multipleChoiceQuestion.getAnswers()){
			addEdit(multipleChoiceQuestion.isContainsToRight(answer), answer);
		}
		for(int i = multipleChoiceQuestion.size(); i < 2; i++){
			addEdit(false, "");
		}
		spinner.setSelection(multipleChoiceQuestion.getPoints() - 1);
	}

	/**
	 * Добавляет новый чекбокс для выбора верности варианта ответа.
	 * 
	 */
	/*public void addCheckBox() {

	}*/

	/**
	 * Добавляет новое текстовое поле для ввода варианта ответа.
	 * 
	 */
	public void addEdit(boolean isRight, String answer) {
		CheckBox checkBox;
		final int MAX_VALUE_ANSWERS = 8;
		int size = checkBoxes.size();
		if (size <= MAX_VALUE_ANSWERS - 1) {
			if (size > 0)
				addLine();
			checkBox = new CheckBox(this);
			checkBox.setLayoutParams(editParams);
			checkBox.setTextColor(getResources().getColor(R.color.light_color));
			checkBox.setText(R.string.rightans);
			checkBox.setChecked(isRight);
			checkBoxes.add(checkBox);
			list.addView(checkBox);

			EditText editTxt;
			editTxt = new EditText(this);
			editTxt.setLayoutParams(editParams);
			editTxt.setHint(getString(R.string.numbervar) + (size + 1));
			editTxt.setText(answer);
			editTxt.setSingleLine(true);
			editTextList.add(editTxt);
			list.addView(editTxt);
		} else {
			showToast(getString(R.string.mustnot));
		}
	}

	/**
	 * Добавляет новую разделительную линию между вариантами ответа.
	 * 
	 */
	@SuppressLint("ResourceAsColor")
	public void addLine() {
		View line = new View(this);
		line.setLayoutParams(viewParams);
		line.setBackgroundColor(getResources().getColor(R.color.light_color));
		viewList.add(line);
		list.addView(line);
	}

	/**
	 * Добавляет новое текстовое поле для ввода варианта ответа.
	 * 
	 * @param v
	 *            Кнопка "Добавить".
	 */
	public void add(View v) {
		addEdit(false, "");
	}

	/**
	 * Удаляет нижнее поле для ввода варианта ответа.
	 * 
	 * @param v
	 *            Кнопка "Удалить".
	 */
	public void remove(View v) {
		final int MIN_VALUE_ANSWERS = 2;
		int size = editTextList.size();
		if (size >= MIN_VALUE_ANSWERS + 1) {
			checkBoxes.get(size - 1).setVisibility(View.GONE);
			checkBoxes.remove(size - 1);
			editTextList.get(size - 1).setVisibility(View.GONE);
			editTextList.remove(size - 1);
			viewList.get(size - 2).setVisibility(View.GONE);
			viewList.remove(size - 2);
			saveQuestion();
		} else {
			showToast(getString(R.string.lessnot));
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
		intent.putExtra("isNotFinishedSinceLastTime", false);
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
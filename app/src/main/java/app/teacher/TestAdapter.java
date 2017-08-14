package app.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Кирилл on 04.07.2017.
 */
public class TestAdapter extends BaseAdapter {

    Test test = new Test();
    Context context;

    public TestAdapter(Context context, Test test) {
        if (test != null) {
            this.test = test;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return test.size();
    }

    @Override
    public Object getItem(int num) {
        return test.getChallenge(num);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    /** Создаёт элемент ListView */
    @Override
    public View getView(int i, View someView, ViewGroup arg2) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (someView == null) {
            someView = inflater.inflate(R.layout.list_view_item, arg2, false);
        }
        TextView header = (TextView) someView.findViewById(R.id.item_headerText);
        TextView subHeader = (TextView) someView.findViewById(R.id.item_subHeaderText);
        if (test.getChallenge(i) instanceof OnlyChoiceQuestion) {
            header.setTextColor(context.getResources().getColor(R.color.orange_color));
            subHeader.setText(context.getString(R.string.abouthoice));
        } else if (test.getChallenge(i) instanceof MultipleChoiceQuestion) {
            header.setTextColor(context.getResources().getColor(R.color.white_color));
            subHeader.setText(context.getString(R.string.aboutmultiple));
        } else if (test.getChallenge(i) instanceof InputQuestion) {
            header.setTextColor(context.getResources().getColor(R.color.yellow_color));
            subHeader.setText(context.getString(R.string.aboutinput));
        }
        header.setText(context.getString(R.string.numberqq) + test.size());
        return someView;
    }

}

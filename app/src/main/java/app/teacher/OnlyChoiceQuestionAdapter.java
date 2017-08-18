package app.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Кирилл on 18.08.2017.
 */
public class OnlyChoiceQuestionAdapter extends RecyclerView.Adapter<OnlyChoiceQuestionAdapter.OnlyChoiceQuestionViewHolder> implements ItemTouchHelperAdapter {




    OnlyChoiceQuestion onlyChoiceQuestion;
    Context context;
    RadioButton lastCheckedRadioButton = null;
    boolean update;




    OnlyChoiceQuestionAdapter(Context context, OnlyChoiceQuestion onlyChoiceQuestion){
        this.context = context;
        this.onlyChoiceQuestion = onlyChoiceQuestion;
    }

    void clear(int size){
        notifyItemRangeRemoved(0, size);
    }

    public void addItem(){
        notifyItemInserted(onlyChoiceQuestion.size() - 1);
    }

    @Override
    public boolean onItemMove(int firstPos, int secondPos) {
        onlyChoiceQuestion.swap(firstPos, secondPos);
        notifyItemMoved(firstPos, secondPos);
        update = true;
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        onlyChoiceQuestion.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public OnlyChoiceQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_with_radio_button, parent, false);
        OnlyChoiceQuestionViewHolder testViewHolder = new OnlyChoiceQuestionViewHolder(view);
        return testViewHolder;
    }



    @Override
    public void onBindViewHolder(OnlyChoiceQuestionViewHolder holder, int position) {
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(id);
                if(lastCheckedRadioButton != null) {
                    lastCheckedRadioButton.setChecked(false);
                }
                lastCheckedRadioButton = checkedRadioButton;
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return test.size();
    }

    public static class OnlyChoiceQuestionViewHolder extends RecyclerView.ViewHolder {


        // Test test;
        View itemView;
        RadioButton radioButton;
        EditText editText;


        OnlyChoiceQuestionViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            editText = (EditText) itemView.findViewById(R.id.item_edit_text);
            radioButton = (RadioButton) itemView.findViewById(R.id.item_radio_button);

       /* if (test != null) {
            this.test = test;
        }
        else
            this.test = new Test();
        this.context = context;*/
        }

    }


}

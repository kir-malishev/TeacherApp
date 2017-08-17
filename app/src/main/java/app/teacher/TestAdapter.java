package app.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static app.teacher.R.layout.list_view_item;

/**
 * Created by Кирилл on 04.07.2017.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> implements ItemTouchHelperAdapter {


    public interface OnItemClickListener {
        void onItemClick(Challenge challenge, int position);
    }


    Test test;
    Context context;
    OnItemClickListener listener;
    boolean update;



    @Override
    public boolean onItemMove(int firstPos, int secondPos) {
        test.swap(firstPos, secondPos);
        notifyItemMoved(firstPos, secondPos);
        update = true;
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        test.remove(position);
        notifyDataSetChanged();
    }

    TestAdapter(Context context, Test test, OnItemClickListener listener){
        this.context = context;
        this.test = test;
        this.listener = listener;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        TestViewHolder testViewHolder = new TestViewHolder(view);
        return testViewHolder;
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        holder.bind(test.getChallenge(position), position, listener);
        if(test.getChallenge(position).getQuestion().isEmpty())
            holder.header.setText(context.getString(R.string.numberqq) + (position + 1));
        else
            holder.header.setText(test.getChallenge(position).getQuestion());
        switch(test.getChallenge(position).getType()){
            case 0:
                holder.header.setTextColor(context.getResources().getColor(R.color.orange_color));
                holder.subHeader.setText(context.getString(R.string.abouthoice));
                break;
            case 1:
                holder.header.setTextColor(context.getResources().getColor(R.color.white_color));
                holder.subHeader.setText(context.getString(R.string.aboutmultiple));
                break;
            case 2:
                holder.header.setTextColor(context.getResources().getColor(R.color.yellow_color));
                holder.subHeader.setText(context.getString(R.string.aboutinput));
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return test.size();
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {


       // Test test;
        View itemView;
        TextView header;
        TextView subHeader;


        TestViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            header = (TextView) itemView.findViewById(R.id.item_headerText);
            subHeader = (TextView) itemView.findViewById(R.id.item_subHeaderText);

       /* if (test != null) {
            this.test = test;
        }
        else
            this.test = new Test();
        this.context = context;*/
        }

        public void bind(final Challenge challenge, final int position, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    listener.onItemClick(challenge, position);
                }
            });
        }
/*
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
    }*/

        /** Создаёт элемент ListView */
   /* @Override
    public View getView(int i, View someView, ViewGroup arg2) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (someView == null) {
            someView = inflater.inflate(R.layout.list_view_item, arg2, false);
        }
        TextView header = (TextView) someView.findViewById(R.id.item_headerText);
        TextView subHeader = (TextView) someView.findViewById(R.id.item_subHeaderText);
        switch(test.getChallenge(i).getType()){
            case 0:
                header.setTextColor(context.getResources().getColor(R.color.orange_color));
                subHeader.setText(context.getString(R.string.abouthoice));
                break;
            case 1:
                header.setTextColor(context.getResources().getColor(R.color.white_color));
                subHeader.setText(context.getString(R.string.aboutmultiple));
                break;
            case 2:
                header.setTextColor(context.getResources().getColor(R.color.yellow_color));
                subHeader.setText(context.getString(R.string.aboutinput));
                break;
        }
        /*if (test.getChallenge(i) instanceof OnlyChoiceQuestion) {
            header.setTextColor(context.getResources().getColor(R.color.orange_color));
            subHeader.setText(context.getString(R.string.abouthoice));
        } else if (test.getChallenge(i) instanceof MultipleChoiceQuestion) {
            header.setTextColor(context.getResources().getColor(R.color.white_color));
            subHeader.setText(context.getString(R.string.aboutmultiple));
        } else if (test.getChallenge(i) instanceof InputQuestion) {
            header.setTextColor(context.getResources().getColor(R.color.yellow_color));
            subHeader.setText(context.getString(R.string.aboutinput));
        }
        header.setText(context.getString(R.string.numberqq) + (i + 1));
        return someView;
    }*/

    }


}

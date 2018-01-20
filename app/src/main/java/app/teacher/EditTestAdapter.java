package app.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Кирилл on 04.07.2017.
 */
public class EditTestAdapter extends RecyclerView.Adapter<EditTestAdapter.TestViewHolder> implements ItemTouchHelperAdapter {


    public interface OnItemClickListener {
        void onItemClick(Challenge challenge, int position);
    }


    Test test;
    Context context;
    OnItemClickListener listener;
    boolean update;


    EditTestAdapter(Context context, Test test, OnItemClickListener listener) {
        this.context = context;
        this.test = test;
        this.listener = listener;
    }

    void clear(int size){
        notifyItemRangeRemoved(0, size);
    }

    public void addItem(){
        notifyItemInserted(test.size() - 1);
    }

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
            case Challenge.ONLY_CHOICE_QUESTION_TYPE:
                holder.header.setTextColor(context.getResources().getColor(R.color.orange_color));
                holder.subHeader.setText(context.getString(R.string.abouthoice));
                break;
            case Challenge.MULTIPLE_CHOICE_QUESTION_TYPE:
                holder.header.setTextColor(context.getResources().getColor(R.color.white_color));
                holder.subHeader.setText(context.getString(R.string.aboutmultiple));
                break;
            case Challenge.INPUT_QUESTION:
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
        }

        public void bind(final Challenge challenge, final int position, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    listener.onItemClick(challenge, position);
                }
            });
        }
    }


}

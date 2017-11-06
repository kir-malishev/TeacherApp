package app.teacher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Кирилл on 05.11.2017.
 */
public class ReviewTestAdapter extends RecyclerView.Adapter<ReviewTestAdapter.TestViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(TestDescription testDescription, int position);
    }


    ArrayList<TestDescription> descriptions;
    OnItemClickListener listener;


    ReviewTestAdapter(ArrayList<TestDescription> descriptions, OnItemClickListener listener) {
        this.descriptions = descriptions;
        this.listener = listener;
    }

    void clear(int size) {
        notifyItemRangeRemoved(0, size);
    }

    public void addItem() {
        notifyItemInserted(descriptions.size() - 1);
    }

    public void onItemDismiss(int position) {
        descriptions.remove(position);
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
        holder.bind(descriptions.get(position), position, listener);

        TestDescription testDescription = descriptions.get(position);
        holder.header.setText(testDescription.getTestName());
        holder.subHeader.setText(testDescription.getTestID());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
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

        public void bind(final TestDescription testDescription, final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onItemClick(testDescription, position);
                }
            });
        }
    }


}



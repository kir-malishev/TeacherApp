package app.teacher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Кирилл on 22.01.2018.
 */
public class ReviewResultAdapter extends RecyclerView.Adapter<ReviewResultAdapter.ResultViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(ResultDescription resultDescription, int position);
    }


    ArrayList<ResultDescription> descriptions;
    OnItemClickListener listener;


    ReviewResultAdapter(ArrayList<ResultDescription> descriptions, OnItemClickListener listener) {
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
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        ResultViewHolder resultViewHolder = new ResultViewHolder(view);
        return resultViewHolder;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.bind(descriptions.get(position), position, listener);

        ResultDescription resultDescription = descriptions.get(position);
        holder.header.setText(resultDescription.getStudentName());
        holder.subHeader.setText(resultDescription.getStringDate());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {


        // Test test;
        View itemView;
        TextView header;
        TextView subHeader;


        ResultViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            header = (TextView) itemView.findViewById(R.id.item_headerText);
            subHeader = (TextView) itemView.findViewById(R.id.item_subHeaderText);
        }

        public void bind(final ResultDescription resultDescription, final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onItemClick(resultDescription, position);
                }
            });
        }
    }


}



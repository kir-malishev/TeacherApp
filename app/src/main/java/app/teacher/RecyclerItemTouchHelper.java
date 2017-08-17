package app.teacher;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Кирилл on 17.08.2017.
 */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TestAdapter adapter;
    public RecyclerItemTouchHelper(TestAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN , ItemTouchHelper.START | ItemTouchHelper.END);
        this.adapter = adapter;
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(adapter.update) {
            adapter.notifyDataSetChanged();
            adapter.update = false;
        }
    }
}

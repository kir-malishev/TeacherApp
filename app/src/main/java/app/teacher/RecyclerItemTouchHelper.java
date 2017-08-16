package app.teacher;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Collections;

/**
 * Created by Кирилл on 17.08.2017.
 */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TestAdapter adapter;
    public RecyclerItemTouchHelper(TestAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT, 0);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Utils.showToast(adapter.context, "1");
        int firstPos = viewHolder.getAdapterPosition();
        int secondPos = target.getAdapterPosition();
        adapter.test.swap(firstPos, secondPos);
        adapter.notifyItemMoved(firstPos, secondPos);
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Utils.showToast(adapter.context, "2" + direction);
        if(direction == ItemTouchHelper.LEFT){
            int position = viewHolder.getAdapterPosition();
            adapter.test.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        recyclerView.setAdapter(adapter);
    }
}

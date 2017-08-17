package app.teacher;

/**
 * Created by Кирилл on 17.08.2017.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int firstPos, int secondPos);
    void onItemDismiss(int position);
}

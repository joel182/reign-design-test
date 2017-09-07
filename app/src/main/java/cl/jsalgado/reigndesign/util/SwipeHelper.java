package cl.jsalgado.reigndesign.util;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import cl.jsalgado.reigndesign.adapter.ReportAdapter;

/**
 * Created by joels on 05-09-2017.
 *
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback{

    private ReportAdapter adapter;

    public SwipeHelper(ReportAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.deleteItem(viewHolder.getAdapterPosition());
    }

}
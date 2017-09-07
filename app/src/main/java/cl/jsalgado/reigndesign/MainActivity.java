package cl.jsalgado.reigndesign;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ProgressBar;

import cl.jsalgado.reigndesign.adapter.ReportAdapter;
import cl.jsalgado.reigndesign.util.SwipeHelper;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ProgressBar pbProgress;
    SwipeRefreshLayout swipeRefresh;

    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary
        );
        recyclerLoad();
    }

    private void recyclerLoad() {
        recycler = (RecyclerView) findViewById(R.id.rv_report);
        recycler.hasFixedSize();
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(this);
        adapter = new ReportAdapter(this, pbProgress, recycler, swipeRefresh);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SwipeHelper((ReportAdapter) adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recycler);
    }

    @Override
    public void onRefresh() {
        ((ReportAdapter) adapter).hitsLoad(true);
    }

}
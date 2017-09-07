package cl.jsalgado.reigndesign.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cl.jsalgado.reigndesign.R;
import cl.jsalgado.reigndesign.WebViewActivity;
import cl.jsalgado.reigndesign.entity.Hit;
import cl.jsalgado.reigndesign.entity.Report;
import cl.jsalgado.reigndesign.util.Util;

/**
 * Created by joels on 04-09-2017.
 *
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Hit> items;
    private Context context;
    private ProgressBar pbProgress;
    private RecyclerView recycler;
    private SwipeRefreshLayout refreshLayout;
    private View view;
    private int page = 1;
    private Boolean isLoading = false;

    public ReportAdapter(Context context, ProgressBar pbProgress, RecyclerView recycler, SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.pbProgress = pbProgress;
        this.recycler = recycler;
        this.refreshLayout = refreshLayout;
        items = new ArrayList<>();
        if(!Util.isConnected(context)){
            loadData(Util.getData(context));
        }else {
            hitsLoad(false);
        }
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportViewHolder holder, int position) {
        Hit hit = items.get(position);
        String title = (hit.getTitle() != null)? hit.getTitle() : hit.getStory_title();
        if(title != null){
            holder.tvTitle.setText(title);
        }else {
            title = context.getString(R.string.no_title);
            holder.tvTitle.setText(title);
        }
        holder.url = (hit.getUrl() != null)? hit.getUrl() : hit.getStory_url();
        holder.tvAuthor.setText(hit.getAuthor() + " - " + Util.getTime(hit.getCreated_at()));
        holder.cvContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isConnected(context)){
                    if(holder.url != null){
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url", holder.url);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, context.getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }

            }
        });

        if((holder.getAdapterPosition() + 1) == items.size() && !isLoading){
            if(Util.isConnected(context)){
                page++;
                isLoading = true;
                hitsLoad(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvAuthor;
        String url;
        CardView cvContainer;

        ReportViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            cvContainer = (CardView) itemView.findViewById(R.id.cv_container);
        }
    }

    public void hitsLoad(Boolean isRefresh){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Util.URL_DATA + page;
        pbProgress.setVisibility(View.VISIBLE);
        if(isRefresh){
            page = 1;
            items = new ArrayList<>();
            notifyDataSetChanged();
            pbProgress.setVisibility(View.GONE);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbProgress.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                String message = "";
                if (error instanceof NetworkError) {
                    message = context.getString(R.string.network_error_msg);
                } else if (error instanceof ServerError) {
                    message = context.getString(R.string.server_error_msg);
                } else if (error instanceof AuthFailureError) {
                    message = context.getString(R.string.auth_failure_error_msg);
                } else if (error instanceof ParseError) {
                    message = context.getString(R.string.parse_error_msg);
                } else if (error instanceof TimeoutError) {
                    message = context.getString(R.string.time_out_error_msg);
                }
                loadData(Util.getData(context));
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void loadData(String data){
        pbProgress.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
        Report report = new Gson().fromJson(data, Report.class);
        if(report != null){
            if(report.getHits().size() > 0){
                items.addAll(report.getHits());
                notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                report.setHits(items);

                Gson gson = new Gson();
                data = gson.toJson(report);
                Util.saveData(context, data);
            }
        }
        isLoading = false;
    }

    public void deleteItem(int position){
        items.remove(position);
        this.notifyItemRemoved(position);
        if(view != null){
            Snackbar.make(view, context.getString(R.string.delete_item_msg), Snackbar.LENGTH_SHORT).show();
        }
    }

}
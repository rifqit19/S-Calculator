package com.triginandri.qalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triginandri.qalculator.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    List<History> historyList;

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_history,null,false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        History item = historyList.get(position);
        holder.tv_num1.setText(item.getFirst()+" ");
        holder.tv_operator.setText(item.getOperator());
        holder.tv_num2.setText(item.getSecond()+" ");
        holder.tv_result.setText(item.getResult()+" ");
        holder.ll_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), holder.ll_item);
                popup.inflate(R.menu.history_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                historyList.remove(position);
                                notifyDataSetChanged();
                                Context context = view.getContext();
                                SharedPreferences sharedPreferences = context.getSharedPreferences(Config.shared_key, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();

                                String json = gson.toJson(historyList);
                                editor.putString(Config.history_key, json);
                                editor.apply();

                                Toast.makeText(view.getContext(), "Terhapus", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView tv_num1,tv_operator,tv_num2,tv_result;
        LinearLayout ll_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_num1 = itemView.findViewById(R.id.text_num1);
            tv_operator = itemView.findViewById(R.id.text_operator);
            tv_num2 = itemView.findViewById(R.id.text_num2);
            tv_result = itemView.findViewById(R.id.text_result);
            ll_item = itemView.findViewById(R.id.linear_item);





        }
    }
}

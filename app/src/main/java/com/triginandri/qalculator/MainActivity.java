package com.triginandri.qalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.triginandri.qalculator.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputEditText fldNum1, fldNum2;
    AppCompatSpinner spnOperator;
    AppCompatButton btnCalculate;
    RecyclerView rvHistory;
    TextView tvResult;

    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private ImageView header_Arrow_Image;
    private ImageButton btnDelete,btnRefresh;


    List<History> historyList = new ArrayList<>();
    HistoryAdapter adapter;
    String[] operator = { "+", "-",
            "x", ":", };

    String operator_selected;
    double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fldNum1 = findViewById(R.id.fld_num1);
        fldNum2 = findViewById(R.id.fld_num2);
        spnOperator = findViewById(R.id.spinner_operator);
        btnCalculate = findViewById(R.id.btn_calculate);
        rvHistory = findViewById(R.id.rv_history);
        tvResult = findViewById(R.id.tv_result);
        btnRefresh = findViewById(R.id.btn_refresh);

        mBottomSheetLayout = findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        header_Arrow_Image = findViewById(R.id.bottom_sheet_arrow);
        btnDelete = findViewById(R.id.btn_delete);

        spnOperator.setOnItemSelectedListener(this);
        ArrayAdapter operatorAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, operator);
        operatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOperator.setAdapter(operatorAdapter);

        LoadData();
        buildRecyclerView();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               calculate();

            }
        });

        makeBottomSheetHistory();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHistory();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshField();
            }
        });

    }

    public void buildRecyclerView(){
        //        rvHistory.setHasFixedSize(true);
        adapter = new HistoryAdapter(historyList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setAutoMeasureEnabled(false);

//        int height= ; //get height
//        ViewGroup.LayoutParams params_new=rvHistory.getLayoutParams();
//        params_new.height=height;
//        rvHistory.setLayoutParams(params_new);

        rvHistory.setLayoutManager(linearLayoutManager);
//        rv_menu.setNestedScrollingEnabled(false);
        rvHistory.setMotionEventSplittingEnabled(false);
        rvHistory.setAdapter(adapter);

    }

    public void makeBottomSheetHistory(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

//        clBottomsheet.setMaxHeight(height/2);

        header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                header_Arrow_Image.setRotation(slideOffset * 180);
            }
        });
    }

    public void refreshField(){
        fldNum1.setText("");
        fldNum2.setText("");
        spnOperator.setSelection(0);
        tvResult.setText("-");
    }
    public void calculate(){
        if (fldNum1.getText().toString().isEmpty() && fldNum2.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show();
        }else if (fldNum2.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Angka kedua kosong", Toast.LENGTH_SHORT).show();
        }else if (fldNum1.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Angka pertama kosong", Toast.LENGTH_SHORT).show();
        }else{
            switch (spnOperator.getSelectedItemPosition()){
                case 0:
                    operator_selected = "+";
                    result = Double.parseDouble(fldNum1.getText().toString()) + Double.parseDouble(fldNum2.getText().toString());
                    break;
                case 1:
                    operator_selected = "-";
                    result = Double.parseDouble(fldNum1.getText().toString()) - Double.parseDouble(fldNum2.getText().toString());
                    break;
                case 2:
                    operator_selected = "x";
                    result = Double.parseDouble(fldNum1.getText().toString()) * Double.parseDouble(fldNum2.getText().toString());
                    break;
                case 3:
                    operator_selected = ":";
                    result = Double.parseDouble(fldNum1.getText().toString()) / Double.parseDouble(fldNum2.getText().toString());
                    break;
            }

            historyList.add(new History(Integer.parseInt(fldNum1.getText().toString()),Integer.parseInt(fldNum2.getText().toString()), result,operator_selected));
            adapter.notifyDataSetChanged();
//                    adapter.notifyItemInserted(historyList.size());
            tvResult.setText(result + " ");
            SaveHistoryData();
        }
    }
    public void deleteHistory(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Bersihkan Riwayat");
        alertDialog.setMessage("Apakah anda yakin akan menghapus semua riwayat?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hapus Semua",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        historyList.clear();
                        adapter.notifyDataSetChanged();
                        SaveHistoryData();
                        Toast.makeText(MainActivity.this, "Riwayat Terhapus", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.blue));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.error_red));
            }
        });
        alertDialog.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void LoadData(){

        SharedPreferences sharedPreferences = getSharedPreferences(Config.shared_key, MODE_PRIVATE);
        
        Gson gson = new Gson();
        
        String json = sharedPreferences.getString(Config.history_key, null);

        Type type = new TypeToken<ArrayList<History>>() {}.getType();

        historyList = gson.fromJson(json, type);

        if (historyList == null) {
            historyList = new ArrayList<>();
        }

    }

    private void SaveHistoryData(){

        SharedPreferences sharedPreferences = getSharedPreferences(Config.shared_key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(historyList);
        editor.putString(Config.history_key, json);
        editor.apply();
    }
}
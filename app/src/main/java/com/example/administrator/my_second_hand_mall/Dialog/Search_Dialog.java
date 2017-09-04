package com.example.administrator.my_second_hand_mall.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.my_second_hand_mall.R;

/**
 * Created by Administrator on 2017/7/6.
 */

public class Search_Dialog extends Dialog {

    private Context context;
    private TextView txt_clear;
    private TextView txt_search;
    private EditText edit_minprice;
    private EditText edit_maxprice;
    private EditText edit_name;
    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {
        public void dosearch(String name, String maxprice, String minprice);
    }

    public Search_Dialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.search_layout, null);
        setContentView(view);

        txt_clear = (TextView) findViewById(R.id.txt_clear);
        txt_search = (TextView) findViewById(R.id.txt_search);
        edit_minprice = (EditText) findViewById(R.id.edit_minprice);
        edit_maxprice = (EditText) findViewById(R.id.edit_maxprice);
        edit_name = (EditText) findViewById(R.id.edit_name);

        txt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_minprice.setText(null);
                edit_maxprice.setText(null);
                edit_name.setText(null);
            }
        });

        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.dosearch(edit_name.getText().toString(), edit_maxprice.getText().toString(),
                        edit_minprice.getText().toString());
            }
        });
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
            this.clickListenerInterface = clickListenerInterface;
        }
}

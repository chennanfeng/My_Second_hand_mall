package com.example.administrator.my_second_hand_mall.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class Classify_Dialog extends Dialog{

    private List<String> categoryList;
    private List<String> classifyList;
    private Context context;
    private ListView categoryView;
    private ListView classifyView;
    private ClickListenerInterface clickListenerInterface;
    private String categoryClick;
    private String classifyClick;

    public interface ClickListenerInterface {
        public void doclassify(String category, String classify);
    }

    public Classify_Dialog(Context context) {
        super(context);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.classify_layout, null);
        setContentView(view);

        categoryView = (ListView) findViewById(R.id.categoryList);
        classifyView = (ListView) findViewById(R.id.classifyList);

        categoryList = Utils.getStringArray(getContext(), R.array.search);
        categoryView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,categoryList));
        categoryView.setSelector(R.color.colorWhite);

        categoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (categoryList.get(position)){
                    case "电器":
                        categoryClick = "电器";
                        classifyList = Utils.getStringArray(getContext(), R.array.classify1);
                        classifyView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,classifyList));
                        break;
                    case "书籍":
                        categoryClick = "书籍";
                        classifyList = Utils.getStringArray(getContext(), R.array.classify2);
                        classifyView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,classifyList));
                        break;
                    case "运动":
                        categoryClick = "运动";
                        classifyList = Utils.getStringArray(getContext(), R.array.classify3);
                        classifyView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,classifyList));
                        break;
                    case "生活":
                        categoryClick = "生活";
                        classifyList = Utils.getStringArray(getContext(), R.array.classify4);
                        classifyView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,classifyList));
                        break;
                    case "其他":
                        categoryClick = "其他";
                        classifyList = Utils.getStringArray(getContext(), R.array.classify5);
                        classifyView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,classifyList));
                        break;
                }
            }
        });

        classifyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classifyClick = classifyList.get(position);
                clickListenerInterface.doclassify(categoryClick, classifyClick);
            }
        });
    }

    public void setClickListener(ClickListenerInterface clickListenerInterface){
        this.clickListenerInterface = clickListenerInterface;
    }
}

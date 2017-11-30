package com.csbarcelona.choremanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.*;
import android.widget.*;

import java.util.*;

/**
 * Created by Josh on 2017-11-28.
 */

public class TaskCustomAdapter extends ArrayAdapter {
    private Context context;
    private List<Task> taskList;


    public TaskCustomAdapter(Context context, List<Task> taskList){

        super(context, R.layout.task_item_layout, taskList);
        this.context = context;
        this.taskList = taskList;
    }


    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_item_layout, parent, false);

        EditText taskNameTextField = (EditText) rowView.findViewById(R.id.itemName);
        TextView taskDescriptionField = (TextView) rowView.findViewById(R.id.itemDescription);
//        ImageView taskImage = (ImageView) rowView.findViewById(R.id.taskImage);
//        ImageView imgEdit = (ImageView) rowView.findViewById(R.id.imgEdit);

        Task task = taskList.get(position);
        taskNameTextField.setText(task.get_name());
        taskDescriptionField.setText(task.get_description());

        return rowView;
    }

}

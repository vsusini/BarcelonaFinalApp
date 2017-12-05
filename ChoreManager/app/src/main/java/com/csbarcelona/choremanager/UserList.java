package com.csbarcelona.choremanager;

/**
 * Created by sizum on 2017-11-30.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    List<User> users;

    public UserList(Activity context, List<User> users) {
        super(context, R.layout.layout_user_list, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGroup = (TextView) listViewItem.findViewById(R.id.textViewGroup);
        TextView textViewPoints = (TextView)listViewItem.findViewById(R.id.textViewPoints);

        User user = users.get(position);
        textViewName.setText(user.get_name());
        textViewGroup.setText(String.valueOf(user.get_group()));
        textViewPoints.setText(String.valueOf(user.get_totalPoints()));
        return listViewItem;
    }
}


package com.csbarcelona.choremanager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bukola on 12/1/2017.
 */

public class ResourceList extends ArrayAdapter<Resource> {
    private Activity context;
    List<Resource> resources;

    public ResourceList(Activity context, List<Resource> resources){
        super(context, android.R.layout.select_dialog_multichoice, resources);
        this.context = context;
        this.resources = resources;
    }

      //  @Override
    /*public View getView(int position, View convertView, ViewGroup parent) {
            View listViewItem = inflater.inflate(android.R.layout.select_dialog_multichoice, null, true);

            Spinner resourceSpinner = (Spinner)findViewById(R.id.assignedUserSpinner);

            TextView textViewResourceName = (TextView) listViewItem.findViewById(R.id.resource_spinner);

            Resource resource = resources.get(position);
            textViewResourceName.setText(resource.getResourceName());
            return listViewItem;
    }
    */
}

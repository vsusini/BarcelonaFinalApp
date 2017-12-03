package com.csbarcelona.choremanager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bukola on 12/1/2017.
 */

public class ResourceList extends ArrayAdapter<Resource> {
    private Activity context;
    List<Resource> resources;
    Button delResource;

    public ResourceList(Activity context, List<Resource> resources){
        super(context, android.R.layout.select_dialog_multichoice, resources);
        this.context = context;
        this.resources = resources;
    }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
          LayoutInflater inflater = context.getLayoutInflater();
          View listViewItem = inflater.inflate(R.layout.resource_item_layout, null, true);

          TextView resourceViewName = (TextView) listViewItem.findViewById(R.id.resourceName);

          Resource resource = resources.get(position);
          resourceViewName.setText(resource.getResourceName());

          return listViewItem;
      }


}

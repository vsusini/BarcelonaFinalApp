package com.csbarcelona.choremanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Inspired but @Destil: https://stackoverflow.com/questions/5015686/android-spinner-with-multiple-choice
 */

public class MultiSpinner extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }


    /**public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = true;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }
     */

    public void setItems(List <String> items, String allText){
        this.items = items;
        this.defaultText = allText;
        selected = new boolean[items.size()];
        for(int i = 0; i <selected.length; i++){
            selected[i] = true;
        }
        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);

    }

    public void setItems(List <String> items){
        this.items = items;
        selected = new boolean[items.size()];
        for(int i = 0; i <selected.length; i++) {
            selected[i] = false;
        }
    }

    public List<String> getSelectedItems(){
        List<String> selection = new ArrayList<>();
        for(int i = 0; i<items.size(); ++i){
            if(selected[i]){
                selection.add(items.get(i));
            }
        }
        return selection;
    }

    public void setSelected(List<String> selection){
        for(String sel: selection){
            for(int j = 0; j<items.size(); ++j){
                if(items.get(j).equals(sel))
                    selected[j] = true;
            }
        }
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }

    public void onItemsSelected(boolean[] selected){

    }
}
package com.project.lepilulier.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.project.lepilulier.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MedicamentMomentCustomAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public MedicamentMomentCustomAdapter(Activity a, ArrayList<HashMap<String, String>> d){
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(convertView == null)
            vi = inflater.inflate(R.layout.row_medicament_moment, null);

        TextView tvHeure = vi.findViewById(R.id.tvHeure);
        TextView tvMedicament = vi.findViewById(R.id.tvMedicament);
        RadioButton rbPris = vi.findViewById(R.id.rbPris);

        HashMap<String, String> entry = data.get(position);

        tvMedicament.setText(entry.get("medicament") + " " + entry.get("posologie"));
        tvHeure.setText(entry.get("heure"));

        if(entry.get("pris").equals("1")) {
            rbPris.setChecked(true);
        } else {
            rbPris.setChecked(false);
        }

        return vi;
    }
}
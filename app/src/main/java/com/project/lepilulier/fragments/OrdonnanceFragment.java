package com.project.lepilulier.fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.lepilulier.MyDatabase;
import com.project.lepilulier.R;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrdonnanceFragment extends Fragment implements View.OnClickListener {

    private Button bDurée, bMatinHeure, bMidiHeure, bSoirHeure, bAnnuler, bAjout;
    private EditText etMedicament, etMatinDosage, etMidiDosage, etSoirDosage;
    protected CheckBox cbMatin, cbMidi, cbSoir;
    private TextView tvDurée, tvMatinHeure, tvMidiHeure, tvSoirHeure;

    private List<Date> dates;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ordonnance, container, false);

        bDurée = view.findViewById(R.id.bDurée);
        bMatinHeure = view.findViewById(R.id.bMatinTemps);
        bMidiHeure = view.findViewById(R.id.bMidiTemps);
        bSoirHeure = view.findViewById(R.id.bSoirTemps);
        bAnnuler = view.findViewById(R.id.bAnnuler);
        bAjout = view.findViewById(R.id.bAjout);

        bDurée.setOnClickListener(this);
        bMatinHeure.setOnClickListener(this);
        bMidiHeure.setOnClickListener(this);
        bSoirHeure.setOnClickListener(this);
        bAnnuler.setOnClickListener(this);
        bAjout.setOnClickListener(this);

        etMedicament = view.findViewById(R.id.etMedicament);
        etMatinDosage = view.findViewById(R.id.etMatinPosologie);
        etMidiDosage = view.findViewById(R.id.etMidiDosage);
        etSoirDosage = view.findViewById(R.id.etSoirPosologie);

        cbMatin = view.findViewById(R.id.cbMatin);
        cbMidi = view.findViewById(R.id.cbMidi);
        cbSoir = view.findViewById(R.id.cbSoir);

        tvDurée = view.findViewById(R.id.tvDurée);
        tvMatinHeure = view.findViewById(R.id.tvMatinTemps);
        tvMidiHeure = view.findViewById(R.id.tvMidiTemps);
        tvSoirHeure = view.findViewById(R.id.tvSoirTemps);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDurée:
                openDateDialog();
                break;
            case R.id.bMatinTemps:
                openTimeDialog(1);
                break;
            case R.id.bMidiTemps:
                openTimeDialog(2);
                break;
            case R.id.bSoirTemps:
                openTimeDialog(3);
                break;
            case R.id.bAnnuler:
                getActivity().onBackPressed();
                break;
            case R.id.bAjout:
                addOrdonnance();
                break;
        }
    }

    private void openDateDialog() {
        final View view1 = getLayoutInflater().inflate(R.layout.dialog_date_range, null);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        CalendarPickerView calendar = view1.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view1);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dates = calendar.getSelectedDates();

                String dateFormat = "dd MMM";

                if(dates.size() > 1) {
                    //String start = (String) DateFormat.format(dateFormat, dates.get(0));
                    //String end = (String) DateFormat.format(dateFormat, dates.get(dates.size() - 1));
                    tvDurée.setText(dates.size() + " dates selected");
                } else {
                    tvDurée.setText(dates.size() + " date selected");
                }
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Séléctionner la durée");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void openTimeDialog(int which) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                if(which == 1) {
                    tvMatinHeure.setText(selectedHour + ":" + selectedMinute);
                } else if(which == 2) {
                    tvMidiHeure.setText(selectedHour + ":" + selectedMinute);
                } else if(which == 3) {
                    tvSoirHeure.setText(selectedHour + ":" + selectedMinute);
                }
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Choisir heure");
        mTimePicker.show();
    }

    private void addOrdonnance() {
        if(checkEmptyFields()) {
            String medicament = etMedicament.getText().toString();
            String isMatin = cbMatin.isChecked() ? "1" : "0";
            String matinHeure = tvMatinHeure.getText().toString();
            String matinPosologie = etMatinDosage.getText().toString();
            String isMidi = cbMidi.isChecked() ? "1" : "0";
            String midiHeure = tvMidiHeure.getText().toString();
            String midiPosologie = etMidiDosage.getText().toString();
            String isSoir = cbSoir.isChecked() ? "1" : "0";
            String soirHeure = tvSoirHeure.getText().toString();
            String soirPosologie = etSoirDosage.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        MyDatabase myDatabase = new MyDatabase(getActivity());
                        myDatabase.open();

                        for(int i = 0; i < dates.size(); i++) {
                            String dateTemp = sdf.format(dates.get(i));
                            int date = Integer.parseInt(dateTemp);
                            myDatabase.saveOrdonnance(medicament, date, isMatin, matinHeure, matinPosologie, "0",
                                    isMidi, midiHeure, midiPosologie, "0",
                                    isSoir, soirHeure, soirPosologie, "0");
                        }

                        myDatabase.close();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Ordonnance Ajoutée", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            new Thread(runnable).start();
        }
    }
    
    private boolean checkEmptyFields() {
        if(etMedicament.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Veuillez écrire le médicament", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(tvDurée.getText().toString().trim().length() == 0) {
            Toast.makeText(getActivity(), "Veuillez séléctionner la durée", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(cbMatin.isChecked()) {
            if(tvMatinHeure.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "Veuillez séléctionner l'heure du Matin", Toast.LENGTH_SHORT).show();
                return false;
            }

            if(etMatinDosage.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "Veuillez écrire la posologie du Matin", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(cbMidi.isChecked()) {
            if(tvMidiHeure.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "Veuillez séléctionner l'heure du Midi", Toast.LENGTH_SHORT).show();
                return false;
            }

            if(etMidiDosage.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "Veuillez écrire la posologie du Midi", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(cbSoir.isChecked()) {
            if(tvSoirHeure.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "Veuillez séléctionner l'heure du Soir", Toast.LENGTH_SHORT).show();
                return false;
            }

            if(etSoirDosage.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "Veuillez écrire la posologie du Soir", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
}

package com.project.lepilulier.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.lepilulier.MyDatabase;
import com.project.lepilulier.R;
import com.project.lepilulier.adapters.JournalDeBordCustomAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class JournalDeBordFragment extends Fragment {

    private ListView lvJournalDeBord;

    private JournalDeBordCustomAdapter adapter;
    private ArrayList<HashMap<String,String>> entries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journaldebord, container, false);

        lvJournalDeBord = view.findViewById(R.id.lvJournalDeBord);

        entries = new ArrayList<HashMap<String, String>>();

        Date today = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MyDatabase myDatabase = new MyDatabase(getActivity());
                    myDatabase.open();


                    entries = myDatabase.getJournalDeBord(sdf1.format(today));

                    myDatabase.close();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new JournalDeBordCustomAdapter(getActivity(), entries);
                            lvJournalDeBord.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

        return view;
    }

}

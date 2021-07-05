package com.project.lepilulier.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.lepilulier.MyDatabase;
import com.project.lepilulier.R;
import com.project.lepilulier.adapters.JournalDeBordCustomAdapter;
import com.project.lepilulier.adapters.MatinCustomAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MatinFragment extends Fragment {

    private ListView lvMatin;

    private MatinCustomAdapter adapter;
    private ArrayList<HashMap<String,String>> entries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matin, container, false);

        lvMatin = view.findViewById(R.id.lvMatin);

        entries = new ArrayList<HashMap<String, String>>();

        Date today = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MyDatabase myDatabase = new MyDatabase(getActivity());
                    myDatabase.open();

                    entries = myDatabase.getMatin(sdf1.format(today));

                    myDatabase.close();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new MatinCustomAdapter(getActivity(), entries);
                            lvMatin.setAdapter(adapter);
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

package com.project.lepilulier.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.lepilulier.MyDatabase;
import com.project.lepilulier.R;
import com.project.lepilulier.adapters.JournalDeBordCustomAdapter;
import com.project.lepilulier.adapters.MedicamentMomentCustomAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class MedicamentMomentFragment extends Fragment {

    private ListView lvMedicamentMoment;

    private MedicamentMomentCustomAdapter adapter;
    private ArrayList<HashMap<String, String>> entries;
    private ArrayList<String> allDatesEvents;

    private ProgressDialog dialog;

    private View viewTemp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicament_moment, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        lvMedicamentMoment = view.findViewById(R.id.lvMedicamentMoment);

        viewTemp = view;

        getAllCalenderEvents();

        return view;
    }

    private void getAllCalenderEvents() {
        dialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MyDatabase myDatabase = new MyDatabase(getActivity());
                    myDatabase.open();

                    allDatesEvents = myDatabase.getAllDatesEvents();

                    myDatabase.close();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();

                            HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(viewTemp, R.id.calendarView)
                                    .range(startDate, endDate)
                                    .datesNumberOnScreen(5)
                                    .addEvents(new CalendarEventsPredicate() {
                                        @Override
                                        public List<CalendarEvent> events(Calendar date) {
                                            Date tempDate = date.getTime();

                                            for (int i = 0; i < allDatesEvents.size(); i++) {
                                                if (allDatesEvents.get(i).equals(sdf.format(tempDate))) {
                                                    List<CalendarEvent> events = new ArrayList<>();
                                                    events.add(new CalendarEvent(Color.rgb(0, 0, 0), "event"));

                                                    return events;
                                                }
                                            }

                                            return null;
                                        }
                                    })
                                    .build();

                            horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                                @Override
                                public void onDateSelected(Calendar date, int position) {
                                    Date tempDate = date.getTime();

                                    getAllMedicaments(tempDate);
                                    Log.e("----", sdf.format(tempDate));
                                }
                            });

                            getAllMedicaments(new Date());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();
    }

    private void getAllMedicaments(Date date) {
        dialog.show();

        entries = new ArrayList<HashMap<String, String>>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MyDatabase myDatabase = new MyDatabase(getActivity());
                    myDatabase.open();

                    String dateTemp = sdf.format(date);
                    entries = myDatabase.getToday(Integer.parseInt(dateTemp));

                    myDatabase.close();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new MedicamentMomentCustomAdapter(getActivity(), entries);
                            lvMedicamentMoment.setAdapter(adapter);

                            lvMedicamentMoment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HashMap<String, String> entry1 = new HashMap<String, String>();
                                    entry1 = entries.get(position);

                                    String previous_value = entry1.get("pris");
                                    String value = previous_value.equals("1") ? "0" : "1";
                                    takeThisMedicament(entry1.get("mid"), entry1.get("which"), value);
                                }
                            });

                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();
    }

    private void takeThisMedicament(String mid, String which, String value) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MyDatabase myDatabase = new MyDatabase(getActivity());
                    myDatabase.open();

                    myDatabase.takeThisMedicament(mid, which, value);

                    myDatabase.close();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();

                            getAllMedicaments(new Date());
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

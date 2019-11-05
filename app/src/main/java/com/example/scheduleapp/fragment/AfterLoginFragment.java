package com.example.scheduleapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scheduleapp.R;
import com.example.scheduleapp.SViewPage;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.structure.ScheduleObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AfterLoginFragment extends Fragment {
    MaterialCalendarView materialCalendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_after_login, container, false);

        materialCalendarView = rootView.findViewById(R.id.calendar);
        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String strDate = ""+date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDay();

                Intent intent = new Intent(getContext(), SViewPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("date",strDate);
                startActivity(intent);
            }
        });

        HashMap hashMap = new HashMap();
        hashMap.put("doing","initSchedule");

        getSchedules(hashMap);

        return rootView;
    }

    protected class SundayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator(){}

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    protected class SaturdayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator(){}

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }


    private void getSchedules(HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<List<ScheduleObject>> getSchedules = scheduleService.getSchedules(hashMap);

        getSchedules.enqueue(new Callback<List<ScheduleObject>>() {
            @Override
            public void onResponse(Call<List<ScheduleObject>> call, Response<List<ScheduleObject>> response) {
               if(response.isSuccessful())
                    Log.d("CHKCHK",response.body().toString());

               else
                   Log.d("INPUT_SCH_ERR","Input Schedule Error");
            }

            @Override
            public void onFailure(Call<List<ScheduleObject>> call, Throwable t) {

                Log.d("ERRRR",t.getMessage());
            }
        });
    }

}


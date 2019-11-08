package com.example.scheduleapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AfterLoginFragment extends Fragment {
    MaterialCalendarView materialCalendarView;
    SelectDecorator selectDecorator;
    CalendarDay today;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_after_login, container, false);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","initSchedule");

        getSchedules(hashMap);


        today = CalendarDay.today();

        materialCalendarView = rootView.findViewById(R.id.calendar);
        materialCalendarView.setSelectionColor(Color.WHITE);
        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OnDayDecorator());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String strDate = ""+date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDay();
                if(selectDecorator != null)
                  materialCalendarView.removeDecorator(selectDecorator);

                selectDecorator = new SelectDecorator(date);
                materialCalendarView.addDecorator(selectDecorator);

                Intent intent = new Intent(getContext(), SViewPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("date",strDate);
                startActivity(intent);
            }
        });

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

    protected class OnDayDecorator implements DayViewDecorator{
        public OnDayDecorator(){}
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new TextAppearanceSpan(getContext(),R.style.onDayText));
            view.setBackgroundDrawable(getContext().getDrawable(R.drawable.onday_background));
        }
    }

    protected class EventDecorator implements DayViewDecorator {
        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);

            Log.d("CHKCHK","DATES: "+dates.toString());
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
            view.addSpan(new LineBackgroundSpan() {
                @Override
                public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int i, int i1, int i2, int i3, int i4, @NonNull CharSequence charSequence, int i5, int i6, int i7) {
                    canvas.drawText("Title",90,i4,paint);
                }
            });
        }
    }


    protected class SelectDecorator implements DayViewDecorator{
        CalendarDay day;

        public SelectDecorator(CalendarDay day){
            this.day = day;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return !(day.equals(today))&&day.equals(this.day);
        }

        public void decorate(DayViewFacade view){
            view.setBackgroundDrawable(getContext().getDrawable(R.drawable.select_background));
            view.addSpan(new TextAppearanceSpan(getContext(),R.style.onDayText));
        }
    }

    private void setDecorators(List<ScheduleObject> listSchObj){
        List<CalendarDay> dayList = new ArrayList<CalendarDay>();
        Calendar calendar = Calendar.getInstance();
        CalendarDay calendarDay;

        for(int i=0; i<listSchObj.size(); i++){
            String[] strSplits = listSchObj.get(i).getDate().split("-");
            calendar.set(Integer.parseInt(strSplits[0]),Integer.parseInt(strSplits[1])-1,Integer.parseInt(strSplits[2]));
            calendarDay = CalendarDay.from(calendar);

            dayList.add(calendarDay);
        }

        materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#00BCD4"),dayList));
    }

    private void getSchedules(HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<List<ScheduleObject>> getSchedules = scheduleService.getSchedules(hashMap);

        getSchedules.enqueue(new Callback<List<ScheduleObject>>() {
            @Override
            public void onResponse(Call<List<ScheduleObject>> call, Response<List<ScheduleObject>> response) {
               if(response.isSuccessful()) {
                   List<ScheduleObject> listSchObj = response.body();
                   if(listSchObj != null)
                       setDecorators(listSchObj);
               }
               else
                   Log.d("INPUT_SCH_ERR",response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<List<ScheduleObject>> call, Throwable t) {
                Log.d("ERRRR",t.getMessage());
            }
        });
    }

}


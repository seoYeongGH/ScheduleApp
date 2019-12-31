package com.example.scheduleapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scheduleapp.SelectedDayPage;
import com.example.scheduleapp.R;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.AllSchedules;
import com.example.scheduleapp.structure.ScheduleObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static androidx.core.content.ContextCompat.getDrawable;
import static com.example.scheduleapp.structure.Constant.CODE_ISCHANGED;
import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.FOR_USER;
import static com.example.scheduleapp.structure.Constant.FROM_GROUP_SCHEDULE;
import static com.example.scheduleapp.structure.Constant.FROM_USER_SCHEDULE;

public class AfterLoginFragment extends Fragment {
    private MaterialCalendarView materialCalendarView;
    private final CalendarDay today = CalendarDay.today();

    private Drawable eventBackground;

    private List<ScheduleObject> schedules;
    private int groupNum;
    private int currentIndex = 0;
    private int value = 1;
    private boolean haveSchedule;
    private String selectDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_after_login, container, false);

        materialCalendarView = rootView.findViewById(R.id.calendar);

        if(getContext() != null)
            eventBackground = getDrawable(getContext(),R.drawable.back_event);

        selectDate = getStrDate(today);
        initSchedule(groupNum);

        return rootView;
    }

    public AfterLoginFragment(int groupNum){
        this.groupNum = groupNum;
    }

    private void initSchedule(int groupNum){
        boolean isManager = true;
        HashMap<String,Object> hashMap = new HashMap<>();

        if(groupNum == FOR_USER) {
            hashMap.put("doing", "initSchedule");
        }
        else {
            hashMap.put("doing", "getGroupSchedule");
            hashMap.put("groupNum",groupNum);

            isManager = AllGroups.getInstance().isManagerGroup(groupNum);
        }

        setListeners(isManager);
        getSchedules(hashMap);
    }

    private int initIndex(String strDate){
        String strToday = strDate.substring(0,7);

        int i;
        int end = schedules.size();
        if(end == 0)
            return 0;

        for (i = 0; i < end; i++) {
            String compDate = schedules.get(i).getDate();
            if (compDate.contains(strToday) || compDate.compareTo(selectDate) > 0) {
                return i;
            }
        }

        return end-1;
    }

    private void setListeners(final boolean isManager){
       materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectDate = getStrDate(date);

                int idx = getSelectIndex(selectDate);

                Intent intent = new Intent(getContext(), SelectedDayPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("date",selectDate);
                intent.putExtra("haveSchedule",haveSchedule);
                intent.putExtra("scheduleIdx",idx);
                intent.putExtra("groupNum",groupNum);

                if(groupNum == FOR_USER) {
                    startActivityForResult(intent, FROM_USER_SCHEDULE);
                }
                else {
                    intent.putExtra("isManager",isManager);
                    startActivityForResult(intent, FROM_GROUP_SCHEDULE);
                }
            }
        });

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                if(today.getMonth()>date.getMonth())
                    value = -1;
                else
                    value = 1;
            }
        });
    }

    private void setDecorators(){
        CalendarDay calendarDay;
        ArrayList<DayViewDecorator> decorators = new ArrayList<>();

        int size = schedules.size();
        for(int i=0; i<size; i++){
            ScheduleObject schObj = schedules.get(i);
            String[] strSplits = schObj.getDate().split("-");
            calendarDay = CalendarDay.from(Integer.parseInt(strSplits[0]),Integer.parseInt(strSplits[1]),Integer.parseInt(strSplits[2]));

            decorators.add(new EventDecorator(calendarDay,schedules.get(i).getSchedules(),schedules.get(i).getSchedules().size()));
        }

        materialCalendarView.addDecorators(decorators);
        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OnDayDecorator());
    }


    private int getSelectIndex(String strDate){
        if(schedules.size() != 0) {
            int schSize = schedules.size();

            haveSchedule = false;

            ScheduleObject compObject;
            compObject = schedules.get(0);
            if(strDate.compareTo(compObject.getDate())<0) {
                return 0;
            }

            compObject = schedules.get(schSize-1);
            if(strDate.compareTo(compObject.getDate())>0){
                return schSize;
            }

            int i= currentIndex;
            while(i>=0 && i<schSize){
                compObject = schedules.get(i);

                if(strDate.equals(compObject.getDate())){
                    haveSchedule = true;
                    return i;
                }

                haveSchedule = false;

                if(value == 1) {
                    if(strDate.compareTo(compObject.getDate())<0)
                        return i;

                }

                else if(value == -1){
                    if(strDate.compareTo(compObject.getDate())>0)
                        return i+1;
                }

                i += value;
            }
        }

        else {
            return 0;
        }

        return ERR;
    }

    private String getStrDate(CalendarDay day){
        String strMonth;
        String strDay;

        int iDay = day.getMonth();
        if(iDay<10)
            strMonth = "0"+iDay;
        else
            strMonth = String.valueOf(iDay);

        iDay = day.getDay();
        if(iDay<10)
            strDay = "0"+iDay;
        else
            strDay = String.valueOf(iDay);

        return day.getYear()+"-"+strMonth+"-"+strDay;
    }

    private void getSchedules(HashMap<String,Object> hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<List<ScheduleObject>> getSchedules = scheduleService.getSchedules(hashMap);
        getSchedules.enqueue(new Callback<List<ScheduleObject>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<ScheduleObject>> call, Response<List<ScheduleObject>> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        AllSchedules.getInstance().setAllSchedules(response.body());
                        schedules = AllSchedules.getInstance().getAllSchedules();
                        currentIndex = initIndex(selectDate);
                        setDecorators();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<ScheduleObject>> call, Throwable t) {
                Log.d("ERRRR","GET_SCHEDULE_ERR");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == CODE_ISCHANGED) {
            materialCalendarView.removeDecorators();
            setDecorators();
        }

    }


    protected class SundayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        private SundayDecorator(){}

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            calendar.set(day.getYear(),day.getMonth()-1,day.getDay());
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    protected class SaturdayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        private SaturdayDecorator(){}

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            calendar.set(day.getYear(),day.getMonth()-1,day.getDay());
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }
        public void decorate(DayViewFacade view){
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    protected class OnDayDecorator implements DayViewDecorator{
        Context context;
        Drawable background;

        private OnDayDecorator(){
            context = getContext();
            if(context != null)
                background = context.getDrawable(R.drawable.onday_background);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            if(background != null)
                view.setBackgroundDrawable(background);
            view.addSpan(new TextAppearanceSpan(getContext(),R.style.onDayText));
        }
    }

    protected class EventDecorator implements DayViewDecorator {
        Context context;
        private CalendarDay date;
        ArrayList<String> strSchedules;
        int scheduleSize;

        private EventDecorator(CalendarDay date, ArrayList<String> strSchedules, int scheduleSize) {
            context = getContext();
            this.date = date;
            this.strSchedules = strSchedules;
            this.scheduleSize = scheduleSize;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return (day.equals(date));
        }

        @Override
        public void decorate(DayViewFacade view) {
                view.addSpan(new LineBackgroundSpan() {
                    @Override
                    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int i, int i1, int i2, int i3, int i4, @NonNull CharSequence charSequence, int i5, int i6, int i7) {
                        float originSize = paint.getTextSize();

                        paint.setTextSize(38);
                        for(int j=0; j<scheduleSize; j++) {
                            if(j!=4)
                                canvas.drawText(strSchedules.get(j),i+10, i4+38*(j+1), paint);
                            else
                                canvas.drawText(" + more", i+5, i4+38*(j+1), paint);
                        }
                        paint.setTextSize(originSize);
                    }
                });

                view.setBackgroundDrawable(eventBackground);
        }
    }

}


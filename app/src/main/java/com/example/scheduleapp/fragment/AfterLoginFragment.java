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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.scheduleapp.SelectedDayPage;
import com.example.scheduleapp.recyclerView.OnScheduleItemListener;
import com.example.scheduleapp.R;
import com.example.scheduleapp.SInputPage;
import com.example.scheduleapp.recyclerView.ScheduleAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.AllSchedules;
import com.example.scheduleapp.structure.ScheduleObject;
import com.example.scheduleapp.structure.ScheduleViewObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.DOT_COLOR;
import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.FLAG_ADD;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;
import static com.example.scheduleapp.structure.Constant.FOR_USER;
import static com.example.scheduleapp.structure.Constant.FROM_USER_SCHEDULE;
import static com.example.scheduleapp.structure.Constant.SELECT_DAY_COLOR;

public class AfterLoginFragment extends Fragment {
    private MaterialCalendarView materialCalendarView;
    private final CalendarDay today = CalendarDay.today();

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

    private void setListeners(boolean isManager){
       materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectDate = getStrDate(date);

                int idx = setScheduleList(selectDate);

                Log.d("CHKCKH",""+haveSchedule);

                Intent intent = new Intent(getContext(), SelectedDayPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("date",selectDate);
                intent.putExtra("haveSchedule",haveSchedule);
                intent.putExtra("scheduleIdx",idx);
                intent.putExtra("groupNum",groupNum);
                startActivityForResult(intent,FROM_USER_SCHEDULE);
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

    private int setScheduleList(String strDate){
        if(schedules!= null) {
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

    private void setDecorators(){
        Calendar calendar = Calendar.getInstance();
        CalendarDay calendarDay;
        ArrayList<DayViewDecorator> decorators = new ArrayList<>();

        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OnDayDecorator());

        int size = schedules.size();
        for(int i=0; i<size; i++){
            ScheduleObject schObj = schedules.get(i);
            String[] strSplits = schObj.getDate().split("-");
            calendar.set(Integer.parseInt(strSplits[0]),Integer.parseInt(strSplits[1]),Integer.parseInt(strSplits[2]));
            calendarDay = CalendarDay.from(Integer.parseInt(strSplits[0]),Integer.parseInt(strSplits[1]),Integer.parseInt(strSplits[2]));

            decorators.add(new EventDecorator(calendarDay,DOT_COLOR,"+"+schObj.getScheduleSize()));
        }

        materialCalendarView.addDecorators(decorators);
    }

    protected class SundayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        private SundayDecorator(){}

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            //day.copyTo(calendar);
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
            //day.copyTo(calendar);
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
        private String color;
        private CalendarDay date;
        private String text;

        private EventDecorator(CalendarDay date, String color, String text) {
            this.color = color;
            this.date = date;
            this.text = text;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(){
                @Override
                public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNum) {
                   // paint.setColor(Color.parseColor(color));
                    canvas.drawCircle((left+right)/2-15,bottom+16,8,paint);
                }
            });
                view.addSpan(new LineBackgroundSpan() {
                    @Override
                    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int i, int i1, int i2, int i3, int i4, @NonNull CharSequence charSequence, int i5, int i6, int i7) {
                        float originSize = paint.getTextSize();

                       // paint.setColor(Color.parseColor(SELECT_DAY_COLOR));
                        paint.setTextSize(30);
                        canvas.drawText(text, (i+i1)/2+5, i4+30, paint);

                        paint.setTextSize(originSize);
                    }
                });

        }
    }

}


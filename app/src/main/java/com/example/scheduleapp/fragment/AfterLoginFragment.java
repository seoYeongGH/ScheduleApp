package com.example.scheduleapp.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import com.example.scheduleapp.recyclerView.OnScheduleItemListener;
import com.example.scheduleapp.R;
import com.example.scheduleapp.SInputPage;
import com.example.scheduleapp.recyclerView.ScheduleAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
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

import static com.example.scheduleapp.structure.Constant.CODE_MODIFY;
import static com.example.scheduleapp.structure.Constant.DOT_COLOR;
import static com.example.scheduleapp.structure.Constant.FLAG_ADD;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;

public class AfterLoginFragment extends Fragment {
    static int selectDateIndex = -1;
    static int selectObjIndex = -1;

    MaterialCalendarView materialCalendarView;
    CalendarDay today;

    ScheduleAdapter scheduleAdapter;
    SelectDecorator selectDecorator;
    RecyclerView recSchList;

    TextView txtDate;
    Button btnAdd;

    int currentIndex = 0;
    int value = 1;
    String strDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_after_login, container, false);

        materialCalendarView = rootView.findViewById(R.id.calendar);
        txtDate = rootView.findViewById(R.id.txtSelectDay);
        today = CalendarDay.today();

        materialCalendarView.setDateTextAppearance(R.style.calDateText);
        materialCalendarView.setSelectionColor(Color.WHITE);

        strDate = getStrDate(today);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","initSchedule");
        getSchedules(hashMap);

        recSchList = rootView.findViewById(R.id.recSelctSch);
        scheduleAdapter = new ScheduleAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recSchList.setLayoutManager(layoutManager);
        recSchList.setAdapter(scheduleAdapter);

        btnAdd = rootView.findViewById(R.id.btnAdd);
        setListeners();

        return rootView;
    }

    public void refreshData(){
        materialCalendarView.removeDecorators();

        HashMap hashMap = new HashMap();
        hashMap.put("doing","initSchedule");
        getSchedules(hashMap);
    }

    public void setListeners(){
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                strDate = getStrDate(date);

                if(selectDecorator != null)
                    materialCalendarView.removeDecorator(selectDecorator);

                selectDecorator = new SelectDecorator(date);
                materialCalendarView.addDecorator(selectDecorator);

                setSchList(scheduleAdapter,strDate);
                recSchList.setAdapter(scheduleAdapter);

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

        scheduleAdapter.setListener(new OnScheduleItemListener() {
            @Override
            public void onItemClick(ScheduleAdapter.ViewHolder holder, View view, int position) {
                selectObjIndex = position;
                ScheduleViewObject viewObject = scheduleAdapter.getItem(position);

                Intent intent = new Intent(getContext(), SInputPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("flag",FLAG_MODIFY);
                intent.putExtra("date",strDate);
                intent.putExtra("schedule",viewObject.getSchedule());
                intent.putExtra("time",viewObject.getTime());
                startActivityForResult(intent,CODE_MODIFY);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SInputPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("flag",FLAG_ADD);
                intent.putExtra("date",strDate);
                startActivity(intent);
            }
        });

    }

    private int initIndex(){
        String strToday = ""+today.getYear()+"-"+(today.getMonth()+1);

        int i=0;
        int end = AllSchedules.getInstance().getSize();
        for(i=0;i<end;i++){
            String compDate = AllSchedules.getInstance().getSchedule(i).getDate();
            if(compDate.contains(strToday) || (compDate.compareTo(strDate)>0)) {
                return i;
            }
        }
        return i+1;
    }

    private String getStrDate(CalendarDay day){
        String strMonth;
        String strDay;

        int iDay = day.getMonth()+1;
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

    public void setSchList(ScheduleAdapter adapter, String strDate){
        txtDate.setText(strDate);

        if(AllSchedules.getInstance().getAllSchedules()!= null) {
            adapter.clearList();

            ScheduleObject compObject = new ScheduleObject();
            int i=currentIndex;
            int end = AllSchedules.getInstance().getSize();

            while(i>=0 && i<end){
                compObject = AllSchedules.getInstance().getSchedule(i);
                if(compObject.getDate().equals(strDate)){
                    selectDateIndex = i;

                    ArrayList<String> schedules = compObject.getSchedule();
                    for (int j = 0; j < schedules.size(); j++) {
                        String startTime = compObject.getStartTime().get(j);
                        String endTime = compObject.getEndTime().get(j);

                        ScheduleViewObject schObj = new ScheduleViewObject(schedules.get(j), startTime + "~" + endTime);
                        adapter.addItem(schObj);
                    }

                    break;
                }
                i += value;
            }
        }
    }

    private void getSchedules(HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<List<ScheduleObject>> getSchedules = scheduleService.getSchedules(hashMap);

        getSchedules.enqueue(new Callback<List<ScheduleObject>>() {
            @Override
            public void onResponse(Call<List<ScheduleObject>> call, Response<List<ScheduleObject>> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        AllSchedules.getInstance().setAllSchedules(response.body());
                        setDecorators(AllSchedules.getInstance().getAllSchedules());
                        currentIndex = initIndex();
                        setSchList(scheduleAdapter,strDate);
                        recSchList.setAdapter(scheduleAdapter);
                    }
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

    private void setDecorators(List<ScheduleObject> listSchObj){
        List<CalendarDay> dayList = new ArrayList<CalendarDay>();
        Calendar calendar = Calendar.getInstance();
        CalendarDay calendarDay;
        ArrayList<DayViewDecorator> decorators = new ArrayList<DayViewDecorator>();

        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OnDayDecorator());

        for(int i=0; i<listSchObj.size(); i++){
            ScheduleObject schObj = listSchObj.get(i);
            String[] strSplits = schObj.getDate().split("-");
            calendar.set(Integer.parseInt(strSplits[0]),Integer.parseInt(strSplits[1])-1,Integer.parseInt(strSplits[2]));
            calendarDay = CalendarDay.from(calendar);

            decorators.add(new EventDecorator(calendarDay,DOT_COLOR,"+"+schObj.getScheduleSize()));
        }
        materialCalendarView.addDecorators(decorators);

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
        private String color;
        private CalendarDay date;
        private String text;

        public EventDecorator(CalendarDay date, String color, String text) {
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
                    paint.setColor(Color.parseColor(color));
                    canvas.drawCircle((left+right)/2-15,bottom+16,8,paint);
                }
            });
                view.addSpan(new LineBackgroundSpan() {
                    @Override
                    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int i, int i1, int i2, int i3, int i4, @NonNull CharSequence charSequence, int i5, int i6, int i7) {
                        float originSize = paint.getTextSize();
                        paint.setColor(Color.parseColor("#505050"));
                        paint.setTextSize(30);
                        canvas.drawText(text, (i+i1)/2+5, i4+30, paint);
                        paint.setTextSize(originSize);
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

}


package com.example.scheduleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.example.scheduleapp.recyclerView.OnScheduleItemListener;
import com.example.scheduleapp.recyclerView.ScheduleAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllSchedules;
import com.example.scheduleapp.structure.ScheduleObject;
import com.example.scheduleapp.structure.ScheduleViewObject;
import com.example.scheduleapp.structure.USession;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.w3c.dom.Text;

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
import static com.example.scheduleapp.structure.Constant.FLAG_ADD_GROUP;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class GroupSchedulePage extends AppCompatActivity {
    static int selectDateIndex = -1;
    static int selectObjIndex = -1;

    MaterialCalendarView materialCalendarView;
    TextView txtDate;
    Button btnAdd;
    Button btnConnect;
    Button btnDisConnect;

    RecyclerView recSchList;
    ScheduleAdapter scheduleAdapter;
    SelectDecorator selectDecorator;

    List<ScheduleObject> schedules;
    CalendarDay today;
    String strDate;

    int currentIndex = 0;
    int value = 1;
    int groupNum;
    boolean isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_schedule_activity);

        Intent getIntent = getIntent();
        TextView txtGroup = findViewById(R.id.txtGroup);

        materialCalendarView = findViewById(R.id.calendar);
        txtDate = findViewById(R.id.txtSelectDay);
        today = CalendarDay.today();

        materialCalendarView.setDateTextAppearance(R.style.calDateText);
        materialCalendarView.setSelectionColor(Color.WHITE);

        strDate = getStrDate(today);

        schedules = new ArrayList<ScheduleObject>();
        groupNum = getIntent.getIntExtra("groupNum",-1);
        HashMap hashMap = new HashMap();
        hashMap.put("doing","getGpSchedule");
        hashMap.put("groupNum",groupNum);
        getSchedules(hashMap);

        btnConnect = findViewById(R.id.btnConnect);
        btnDisConnect = findViewById(R.id.btnDisconnect);

        ArrayList<Integer> groupNums = USession.getInstance().getConnectGroups();
        int groupSize = groupNums.size();
        isConnect = false;

        for(int i=0; i<groupSize; i++){
            if(groupNums.get(i)==groupNum) {
                isConnect = true;
            }
        }
        setBtnView(isConnect);

        recSchList = findViewById(R.id.recSelctSch);
        scheduleAdapter = new ScheduleAdapter(getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recSchList.setLayoutManager(layoutManager);
        recSchList.setAdapter(scheduleAdapter);

        boolean isManager = getIntent.getBooleanExtra("isManager",false);
        btnAdd = findViewById(R.id.btnAdd);
        if(isManager)
            btnAdd.setVisibility(View.VISIBLE);
        else
            btnAdd.setVisibility(View.INVISIBLE);

        setListeners(isManager);

        txtGroup.setText(getIntent.getStringExtra("groupName")+"그룹의 일정입니다.");
    }

    private void setBtnView(boolean isConnect){
        if(isConnect){
            btnConnect.setVisibility(View.GONE);
            btnDisConnect.setVisibility(View.VISIBLE);
        }
        else{
            btnConnect.setVisibility(View.VISIBLE);
            btnDisConnect.setVisibility(View.GONE);
        }

    }
    public void setListeners(boolean isManager){
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
        if(isManager) {
            scheduleAdapter.setListener(new OnScheduleItemListener() {
                @Override
                public void onItemClick(ScheduleAdapter.ViewHolder holder, View view, int position) {
                    selectObjIndex = position;
                    ScheduleViewObject viewObject = scheduleAdapter.getItem(position);

                    Intent intent = new Intent(getApplicationContext(), SInputPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("flag", FLAG_MODIFY);
                    intent.putExtra("date", strDate);
                    intent.putExtra("schedule", viewObject.getSchedule());
                    intent.putExtra("time", viewObject.getTime());
                    startActivityForResult(intent, CODE_MODIFY);
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SInputPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("flag", FLAG_ADD);
                    intent.putExtra("groupNum", groupNum);
                    intent.putExtra("date", strDate);
                    startActivity(intent);
                }
            });
        }
    }

    public void onBtnConnect(View view){
        HashMap hashMap = new HashMap();
        hashMap.put("doing","connectGroup");
        hashMap.put("groupNum",groupNum);

        doCommunication(hashMap);
    }

    public void onBtnDisconnect(View view){
        HashMap hashMap = new HashMap();
        hashMap.put("doing","disconnectGroup");
        hashMap.put("groupNum",groupNum);

        doCommunication(hashMap);
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

        if(schedules != null) {
            adapter.clearList();

            ScheduleObject compObject = new ScheduleObject();
            int i=currentIndex;
            int end = schedules.size();

            while(i>=0 && i<end){
                compObject = schedules.get(i);
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
    private int initIndex(){
        String strToday = ""+today.getYear()+"-"+(today.getMonth()+1);

        int i=0;
        int end = schedules.size();
        for(i=0;i<end;i++){
            String compDate = schedules.get(i).getDate();
            if(compDate.contains(strToday) || (compDate.compareTo(strDate)>0)) {
                return i;
            }
        }
        return i+1;
    }

    private void getSchedules(HashMap hashMap) {
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<List<ScheduleObject>> getSchedules = scheduleService.getSchedules(hashMap);

        getSchedules.enqueue(new Callback<List<ScheduleObject>>() {
            @Override
            public void onResponse(Call<List<ScheduleObject>> call, Response<List<ScheduleObject>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        schedules = response.body();
                        setDecorators(schedules);
                        currentIndex = initIndex();
                        setSchList(scheduleAdapter, strDate);
                        recSchList.setAdapter(scheduleAdapter);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_SHORT).show();
                    }
                } else
                    Log.d("GET_GPSCH_EXP", response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<List<ScheduleObject>> call, Throwable t) {
                Log.d("ERRRR", t.getMessage());
            }
        });
    }

    private void doCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    if(response.body() == SUCCESS) {
                        isConnect = !isConnect;
                        setBtnView(isConnect);
                        if (isConnect)
                            USession.getInstance().addGroup(groupNum);
                        else
                            USession.getInstance().removeGroup(groupNum);
                    }
                }
                else{
                    Log.d("Login_ERR","Login Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

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
            view.addSpan(new TextAppearanceSpan(getApplicationContext(),R.style.onDayText));
            view.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.onday_background));
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
            view.setBackgroundDrawable(getApplicationContext().getDrawable(R.drawable.select_background));
            view.addSpan(new TextAppearanceSpan(getApplicationContext(),R.style.onDayText));
        }
    }

}

package com.example.mycalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;
import com.example.mycalendar.Schedule.*;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class ScheduleFragment extends Fragment implements ListView.OnItemClickListener,ListView.OnItemLongClickListener  {
    //list to store all the memo
    private  int num =0;
    private List<OneMemo> memolist=new ArrayList<>();
    /**
     *  静态成员变量
     */
    private static Context context;
    //adapter
    MemoAdapter adapter;


    //main ListView
    ListView lv;

    //alarm clock
    int BIG_NUM_FOR_ALARM=100;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        setHasOptionsMenu(true);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("123645320","+++++");

       Toolbar toolbar=(Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Log.i("123645320","+++++");
        /* setHasOptionsMenu(true);*/


        Connector.getDatabase();
        //addDataLitepPal();
        loadHistoryData();

        adapter=new MemoAdapter(context, R.layout.memo_list, memolist);
        lv=(ListView) getView().findViewById(R.id.list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
       lv.setOnItemLongClickListener(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);

    }



    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
        Log.i("123645320","2222222");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                onAdd();
                break;
            default:
        }
        Log.i("123645320","1111111");
        return super.onOptionsItemSelected(item);
    }

    private void loadHistoryData() {
        List<Memo> memoes= DataSupport.findAll(Memo.class);

        if(memoes.size()==0) {
            initializeLitePal();
            memoes = DataSupport.findAll(Memo.class);
        }

        for(Memo record:memoes) {
            Log.d("MainActivity", "current num: " + record.getNum());
            Log.d("MainActivity", "id: " + record.getId());
            Log.d("MainActivity", "getAlarm: " + record.getAlarm());
            int tag = record.getTag();
            String textDate = record.getTextDate();
            String textTime = record.getTextTime();
            boolean alarm = record.getAlarm().length() > 1 ? true : false;
            String mainText = record.getMainText();
            OneMemo temp = new OneMemo(tag, textDate, textTime, alarm, mainText);
            memolist.add(temp);
        }

    }

    //test
    public void testAdd(View v) {
        /*
        Memo record=new Memo();
        record.setNum(1);
        record.setTag(1);
        record.setTextDate("1212");
        record.setTextTime("23:00");
        record.setAlarm("123");
        record.setMainText("hahaha");
        record.save();
        */
    }

   // @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it=new Intent(context,Edit.class);

        Memo record=getMemoWithNum(position);

        //add information into intent
        transportInformationToEdit(it, record);

        startActivityForResult(it,position);
    }


   @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        int n=memolist.size();

        //if this memo has an alarm clock
        //cancel it
        if(memolist.get(position).getAlarm()) {
            cancelAlarm(position);
        }
        memolist.remove(position);
        adapter.notifyDataSetChanged();

        String whereArgs = String.valueOf(position); //why not position ?
        DataSupport.deleteAll(Memo.class, "num = ?", whereArgs);

        for(int i=position+1; i<n; i++) {
            ContentValues temp = new ContentValues();
            temp.put("num", i-1);
            String where = String.valueOf(i);
            DataSupport.updateAll(Memo.class, temp, "num = ?", where);
        }

        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        if(resultCode==RESULT_OK) {
            updateLitePalAndList(requestCode, it);
        }
    }



    //update the database and memolist acccording to the "num" memo that Edit.class return
    private void updateLitePalAndList(int requestCode, Intent it) {

        int num=requestCode;
        int tag=it.getIntExtra("tag",0);

        Calendar c=Calendar.getInstance();
        String current_date=getCurrentDate(c);
        String current_time=getCurrentTime(c);

        String alarm=it.getStringExtra("alarm");
        String mainText=it.getStringExtra("mainText");

        boolean gotAlarm = alarm.length() > 1 ? true : false;
        OneMemo new_memo = new OneMemo(tag, current_date, current_time, gotAlarm, mainText);

        if((requestCode+1)>memolist.size()) {
            // add a new memo record into database
            addRecordToLitePal(num, tag, current_date, current_time, alarm, mainText);

            // add a new OneMemo object into memolist and show
            memolist.add(new_memo);
        }
        else {
            //if the previous has got an alarm clock
            //cancel it first
            if(memolist.get(num).getAlarm()) {
                cancelAlarm(num);
            }

            //update the previous "num" memo
            ContentValues temp = new ContentValues();
            temp.put("tag", tag);
            temp.put("textDate", current_date);
            temp.put("textTime", current_time);
            temp.put("alarm", alarm);
            temp.put("mainText", mainText);
            String where = String.valueOf(num);
            DataSupport.updateAll(Memo.class, temp, "num = ?", where);

            memolist.set(num, new_memo);
        }
        //if user has set up an alarm
        if(gotAlarm) {
            loadAlarm(alarm, requestCode, 0);
        }

        adapter.notifyDataSetChanged();
    }

    //when there's no memo in the app
    private void initializeLitePal() {
        Calendar c=Calendar.getInstance();
        String textDate=getCurrentDate(c);
        String textTime=getCurrentTime(c);

        //insert two records into the database
        addRecordToLitePal(0,0,textDate,textTime,"","点击添加事件" );
        addRecordToLitePal(1,1,textDate,textTime,"","长按删除事件");
    }

    //get current date in XX/XX format
    private String getCurrentDate(Calendar c){
        return c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
    }

    //get current time in XX:XX format
    private String getCurrentTime(Calendar c){
        String current_time="";
        if(c.get(Calendar.HOUR_OF_DAY)<10) current_time=current_time+"0"+c.get(Calendar.HOUR_OF_DAY);
        else current_time=current_time+c.get(Calendar.HOUR_OF_DAY);

        current_time=current_time+":";

        if(c.get(Calendar.MINUTE)<10) current_time=current_time+"0"+c.get(Calendar.MINUTE);
        else current_time=current_time+c.get(Calendar.MINUTE);

        return current_time;
    }

    private void addRecordToLitePal(int num, int tag, String textDate, String textTime, String alarm, String mainText) {
        Memo record=new Memo();
        record.setNum(num);
        record.setTag(tag);
        record.setTextDate(textDate);
        record.setTextTime(textTime);
        record.setAlarm(alarm);

        record.setMainText(mainText);
        record.save();
    }

    private void transportInformationToEdit(Intent it, Memo record) {
        it.putExtra("num",record.getNum());
        it.putExtra("tag",record.getTag());
        it.putExtra("textDate",record.getTextDate());
        it.putExtra("textTime",record.getTextTime());
        it.putExtra("alarm",record.getAlarm());
        it.putExtra("mainText",record.getMainText());
    }

    //press the add button
    public void onAdd() {
        Intent it=new Intent(context,Edit.class);

        int position = memolist.size();

        Calendar c=Calendar.getInstance();
        String current_date=getCurrentDate(c);
        String current_time=getCurrentTime(c);

        it.putExtra("num",position);
        it.putExtra("tag",0);
        it.putExtra("textDate",current_date);
        it.putExtra("textTime",current_time);
        it.putExtra("alarm","");
        it.putExtra("mainText","");

        startActivityForResult(it,position);
    }

    private Memo getMemoWithNum(int num) {
        String whereArgs = String.valueOf(num);
        Memo record= DataSupport.where("num = ?", whereArgs).findFirst(Memo.class);
        return record;
    }

    //***********************************load or cancel alarm************************************************************************************
    //*****************BUG  SOLVED*************************
    //still have a bug as I know:
    //after deleting a memo, the "num" changes, then the cancelAlarm may have some trouble (it do not cancel actually)
    //establishing a hash table may solve this problem
    //SOLVED through adding id
    //******************************************

    //set an alarm clock according to the "alarm"
    private void loadAlarm(String alarm, int num, int days) {
        int alarm_hour=0;
        int alarm_minute=0;
        int alarm_year=0;
        int alarm_month=0;
        int alarm_day=0;

        int i=0, k=0;
        while(i<alarm.length()&&alarm.charAt(i)!='/') i++;
        alarm_year=Integer.parseInt(alarm.substring(k,i));
        k=i+1;i++;
        while(i<alarm.length()&&alarm.charAt(i)!='/') i++;
        alarm_month=Integer.parseInt(alarm.substring(k,i));
        k=i+1;i++;
        while(i<alarm.length()&&alarm.charAt(i)!=' ') i++;
        alarm_day=Integer.parseInt(alarm.substring(k,i));
        k=i+1;i++;
        while(i<alarm.length()&&alarm.charAt(i)!=':') i++;
        alarm_hour=Integer.parseInt(alarm.substring(k,i));
        k=i+1;i++;
        alarm_minute=Integer.parseInt(alarm.substring(k));

        Memo record=getMemoWithNum(num);

        // When the alarm goes off, we want to broadcast an Intent to our
        // BroadcastReceiver. Here we make an Intent with an explicit class
        // name to have our own receiver (which has been published in
        // AndroidManifest.xml) instantiated and called, and then create an
        // IntentSender to have the intent executed as a broadcast.
        Intent intent = new Intent(context, OneShotAlarm.class);
        intent.putExtra("alarmId",record.getId()+BIG_NUM_FOR_ALARM);
        PendingIntent sender = PendingIntent.getBroadcast(
                context, record.getId()+BIG_NUM_FOR_ALARM, intent, 0);

        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 5);

        Calendar alarm_time = Calendar.getInstance();
        alarm_time.set(alarm_year,alarm_month-1,alarm_day,alarm_hour,alarm_minute);

        int interval = 1000 * 60 * 60 * 24 *days;

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        //if(interval==0)
        am.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), sender);
    }

    //cancel the alarm
    private void cancelAlarm(int num) {
        Memo record=getMemoWithNum(num);

        Intent intent = new Intent(context,
                OneShotAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(
               context, record.getId()+BIG_NUM_FOR_ALARM, intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.cancel(sender);
    }

    //********************************************************************************************************************************
}

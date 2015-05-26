package momomo00.calendartest;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    private Thread mThread;
    private ContactCalendar mContactCalendar;
    private Button mButton;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mThread = null;
        mContactCalendar = null;
        mHandler = new MyHandler(this);

        mButton = (Button)findViewById(R.id.get_calendar_information);
        mButton.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.get_calendar_information:
                    getCalendarInfo();
                    break;

                default:
                    break;
            }
        }
    };

    private static final String TAG = "TEST";
    private static final String ACCOUNT_NAME = "song.of.saya00@gmail.com";
    private static final String ACCOUNT_TYPE = "com.google";
    private static final String CALENDAR_ACCESS_LEVEL = "700";
    private static final String CALENDAR_ID = "1";
    private static final long DTSTART = System.currentTimeMillis();
    private static final long DTEND = DTSTART + 1000 * 60 * 60 * 48;

    private void getCalendarInfo() {
//        mContactCalendar = new ContactCalendar();
//        mContactCalendar.setContactFinishCalendarListener(mListener);
//
//        mThread = new Thread(mContactCalendar);
//        mThread.start();

        ContentResolver cr = getContentResolver();

        String[] projection = {
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
        };
        String selection = "(" +
                "(" + CalendarContract.Events.ACCOUNT_NAME + " = ?) AND " +
                "(" + CalendarContract.Events.ACCOUNT_TYPE + " = ?) AND " +
                "(" + CalendarContract.Events.DTSTART + " >= ?) AND" +
                "(" + CalendarContract.Events.DTEND + " <= ?) AND" +
                "(" + CalendarContract.Events.CALENDAR_ID + " = ?)" +
                ")";
        String[] selectionArgs = {ACCOUNT_NAME, ACCOUNT_TYPE, Long.toString(DTSTART), Long.toString(DTEND), CALENDAR_ID};
        Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI, projection, selection, selectionArgs, null);

        for(boolean hasNext = cursor.moveToFirst(); hasNext; hasNext = cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String title = cursor.getString(1);
            Long startTime = cursor.getLong(2);
            Long endTime = cursor.getLong(3);

            SimpleDateFormat format = new SimpleDateFormat("MM.dd HH:mm", Locale.JAPAN);

            Log.d(TAG, id + ":" + title);
            Log.d(TAG, format.format(startTime) + " - " + format.format(endTime));
            Log.d(TAG, "--------------------");
        }
        cursor.close();
    }

    private ContactCalendar.ContactCompletionOfCalendarListener mListener = new ContactCalendar.ContactCompletionOfCalendarListener() {
        @Override
        public void ContactCompletionOfCalendar() {
            mHandler.sendEmptyMessage(0);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyHandler extends Handler {
        private WeakReference<MainActivity> mReference;

        public MyHandler(MainActivity mainActivity) {
            mReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mReference.get();

            if(mainActivity == null) {
                return;
            }
            mainActivity.getHandlerMessage(msg);
        }
    }

    public void getHandlerMessage(Message msg) {
        Toast.makeText(this, "onClick()", Toast.LENGTH_SHORT).show();
    }
}

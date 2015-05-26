package momomo00.calendartest;

import android.os.Handler;

/**
 *
 * Created by songo_000 on 2015/05/26.
 */
public class ContactCalendar implements Runnable {
    private ContactCompletionOfCalendarListener mListener;

    public ContactCalendar() {
        mListener = null;
    }

    @Override
    public void run() {
        ContactCompletionOfCalendar();
    }


    interface ContactCompletionOfCalendarListener {
        void ContactCompletionOfCalendar();
    }

    public void setContactFinishCalendarListener(ContactCompletionOfCalendarListener listener) {
        mListener = listener;
    }

    private void ContactCompletionOfCalendar() {
        if(mListener == null) {
            return;
        }
        mListener.ContactCompletionOfCalendar();
    }
}

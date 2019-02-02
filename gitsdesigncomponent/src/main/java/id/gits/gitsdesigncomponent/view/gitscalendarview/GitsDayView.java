package id.gits.gitsdesigncomponent.view.gitscalendarview;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author radhikayusuf.
 */

public class GitsDayView extends android.support.v7.widget.AppCompatTextView {

    private Date date;
    private List<GitsDayDecorator> decorators;

    public GitsDayView(Context context) {
        this(context, null, 0);
    }

    public GitsDayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GitsDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 3 || !this.isInEditMode()) {

        }
    }

    public void bind(Date date, List<GitsDayDecorator> decorators, CalendarModel model) {
        this.date = date;
        this.decorators = decorators;
        this.setBackgroundColor(Color.TRANSPARENT);
        SimpleDateFormat df = new SimpleDateFormat("d", Locale.ENGLISH);
        int day = Integer.parseInt(df.format(date));
        this.setText(String.valueOf(day));
        setFocusable(true);
        setClickable(true);
    }

    public void decorate() {
        if (null != this.decorators) {
            Iterator i$ = this.decorators.iterator();

            while (i$.hasNext()) {
                GitsDayDecorator decorator = (GitsDayDecorator) i$.next();
                decorator.decorate(this);
            }
        }

    }

    public Date getDate() {
        return this.date;
    }
}

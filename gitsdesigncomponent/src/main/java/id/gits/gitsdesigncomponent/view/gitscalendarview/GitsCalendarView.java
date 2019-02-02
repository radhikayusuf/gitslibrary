package id.gits.gitsdesigncomponent.view.gitscalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import id.gits.gitsdesigncomponent.R;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Calendar.DAY_OF_MONTH;

/**
 * @author radhikayusuf.
 */

public class GitsCalendarView extends LinearLayout {

    private static final String DAY_OF_WEEK = "dayOfWeek";
    private static final String DAY_OF_MONTH_TEXT = "dayOfMonthText";
    private static final String DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer";
    public static int MHR = 0;
    public static int MY_CALENDAR = 1;
    private Context mContext;
    private View view;
    private View loadingView;
    private ImageView previousMonthButton;
    private ImageView nextMonthButton;
    private GitsCalendarListener calendarListener;
    private Calendar currentCalendar = null;
    private Locale locale;
    private Date lastSelectedDay;
    private Typeface customTypeface;
    private int firstDayOfWeek;
    private List<GitsDayDecorator> decorators;
    private int disabledDayBackgroundColor;
    private int disabledDayTextColor;
    private int calendarBackgroundColor;
    private int selectedDayBackground;
    private int weekLayoutBackgroundColor;
    private int calendarTitleBackgroundColor;
    private int selectedDayTextColor;
    private int calendarTitleTextColor;
    private int dayOfWeekTextColor;
    private int dayOfMonthTextColor;
    private int currentDayOfMonth;
    private int currentMonthIndex;
    private boolean isOverflowDateVisible;
    private OnClickListener onDayOfMonthClickListener;
    private List<CalendarModel> mData = new ArrayList<>();
    private int mType = -1;
    private int firstDatePosition = 0;
    private GitsCalendarUserActionListener mListener;


    public GitsCalendarView(Context mContext) {
        this(mContext, null);
    }

    public GitsCalendarView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.firstDayOfWeek = 1;
        this.decorators = null;
        this.currentMonthIndex = 0;
        this.isOverflowDateVisible = true;
        this.onDayOfMonthClickListener = view -> {
            ViewGroup dayOfMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfMonthContainer.getTag();
            tagId = tagId.substring("dayOfMonthContainer".length(), tagId.length());
            TextView dayOfMonthText = view.findViewWithTag("dayOfMonthText" + tagId);
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(GitsCalendarView.this.getFirstDayOfWeek());
            calendar.setTime(GitsCalendarView.this.currentCalendar.getTime());
            calendar.set(DAY_OF_MONTH, Integer.valueOf(dayOfMonthText.getText().toString()).intValue());
            GitsCalendarView.this.markDayAsSelectedDay(calendar.getTime());
            GitsCalendarView.this.markDayAsCurrentDay(GitsCalendarView.this.currentCalendar);
            if (GitsCalendarView.this.calendarListener != null) {
                GitsCalendarView.this.calendarListener.onDateSelected(calendar.getTime());
            }

        };
        this.mContext = mContext;
        if (Build.VERSION.SDK_INT < 3 || !this.isInEditMode()) {
            this.getAttributes(attrs);
        }
    }

    @SuppressLint("WrongConstant")
    public static boolean isSameMonthStatic(Calendar c1, Calendar c2) {
        return (c1 != null && c2 != null) && (c1.get(0) == c2.get(0) && c1.get(1) == c2.get(1) && c1.get(2) == c2.get(2));
    }

    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    @SuppressLint("WrongConstant")
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The dates must not be null");
        }
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray typedArray = this.mContext.obtainStyledAttributes(attrs, R.styleable.GitsCalendarView, 0, 0);
        this.calendarBackgroundColor = typedArray.getColor(R.styleable.GitsCalendarView_calendarBackgroundColor, this.getResources().getColor(R.color.white));
        this.calendarTitleBackgroundColor = typedArray.getColor(R.styleable.GitsCalendarView_titleLayoutBackgroundColor, this.getResources().getColor(R.color.white));
        this.calendarTitleTextColor = typedArray.getColor(R.styleable.GitsCalendarView_calendarTitleTextColor, this.getResources().getColor(R.color.colorPrimary));
        this.weekLayoutBackgroundColor = typedArray.getColor(R.styleable.GitsCalendarView_weekLayoutBackgroundColor, this.getResources().getColor(R.color.white));
        this.dayOfWeekTextColor = typedArray.getColor(R.styleable.GitsCalendarView_dayOfWeekTextColor, this.getResources().getColor(R.color.black));
        this.dayOfMonthTextColor = typedArray.getColor(R.styleable.GitsCalendarView_dayOfMonthTextColor, this.getResources().getColor(R.color.black));
        this.disabledDayBackgroundColor = typedArray.getColor(R.styleable.GitsCalendarView_disabledDayBackgroundColor, this.getResources().getColor(R.color.day_disabled_background_color));
        this.disabledDayTextColor = typedArray.getColor(R.styleable.GitsCalendarView_disabledDayTextColor, this.getResources().getColor(R.color.day_disabled_text_color));
        this.selectedDayBackground = typedArray.getColor(R.styleable.GitsCalendarView_selectedDayBackgroundColor, this.getResources().getColor(R.color.selected_day_background));
        this.selectedDayTextColor = typedArray.getColor(R.styleable.GitsCalendarView_selectedDayTextColor, this.getResources().getColor(R.color.white));
        this.currentDayOfMonth = typedArray.getColor(R.styleable.GitsCalendarView_currentDayOfMonthColor, this.getResources().getColor(R.color.colorPrimary));
        typedArray.recycle();
    }

    public void initializeCalendar(GitsCalendarUserActionListener mListener) {
        @SuppressLint("WrongConstant")
        LayoutInflater inflate = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        this.mListener = mListener;
        this.view = inflate.inflate(R.layout.custom_calendar_layout, this, true);
        this.loadingView = view.findViewById(R.id.loadingView);
        this.previousMonthButton = this.view.findViewById(R.id.leftButton);
        this.nextMonthButton = this.view.findViewById(R.id.rightButton);
        this.previousMonthButton.setOnClickListener(v -> {
            loadingView.setVisibility(View.VISIBLE);
            Calendar c = GitsCalendarView.this.currentCalendar;
            c.add(Calendar.MONTH, -1);
            mListener.onClickPreviousMonth(DateHelper.getMonthInt(c.getTime().getTime()), DateHelper.getYearInt(c.getTime().getTime()));
        });
        this.nextMonthButton.setOnClickListener(v -> {
            loadingView.setVisibility(View.VISIBLE);
            Calendar c = GitsCalendarView.this.currentCalendar;
            c.add(Calendar.MONTH, 1);
            mListener.onClickNextMonth(DateHelper.getMonthInt(c.getTime().getTime()), DateHelper.getYearInt(c.getTime().getTime()));
        });
        this.setFirstDayOfWeek(1);
    }

    public void clickNextMonth() {
        GitsCalendarView.this.currentMonthIndex++;
        GitsCalendarView.this.currentCalendar = Calendar.getInstance(Locale.getDefault());
        GitsCalendarView.this.currentCalendar.add(2, GitsCalendarView.this.currentMonthIndex);
        if (GitsCalendarView.this.calendarListener != null) {
            GitsCalendarView.this.calendarListener.onMonthChanged(GitsCalendarView.this.currentCalendar.getTime());
        }

        this.refreshCalendar(GitsCalendarView.this.currentCalendar);
        loadingView.setVisibility(View.GONE);
    }

    public void clickPreviousMonth() {
        GitsCalendarView.this.currentMonthIndex--;
        GitsCalendarView.this.currentCalendar = Calendar.getInstance(Locale.getDefault());
        GitsCalendarView.this.currentCalendar.add(2, GitsCalendarView.this.currentMonthIndex);
        if (GitsCalendarView.this.calendarListener != null) {
            GitsCalendarView.this.calendarListener.onMonthChanged(GitsCalendarView.this.currentCalendar.getTime());
        }

        this.refreshCalendar(GitsCalendarView.this.currentCalendar);
        loadingView.setVisibility(View.GONE);
    }

    public void setData(List<CalendarModel> mData, boolean init) {
        this.mData.clear();
        this.mData.addAll(mData);
        if (init) {
            this.mType = MHR;
            if (GitsCalendarView.this.currentCalendar == null) {
                Locale locale = this.mContext.getResources().getConfiguration().locale;
                GitsCalendarView.this.currentCalendar = Calendar.getInstance(locale);
            }
        }


        this.refreshCalendar(GitsCalendarView.this.currentCalendar);
    }

    public List<CalendarModel> getData() {
        return mData;
    }

    public void setData(List<CalendarModel> mData, int type, int year, int month) {
        this.mType = type;
        this.mData.clear();
        this.mData.addAll(mData);


        if (GitsCalendarView.this.currentCalendar == null) {
            Locale locale = this.mContext.getResources().getConfiguration().locale;
            GitsCalendarView.this.currentCalendar = Calendar.getInstance(locale);
            GitsCalendarView.this.currentCalendar.set(year, month, 1);

        }

        this.refreshCalendar(GitsCalendarView.this.currentCalendar);
    }

    @SuppressLint("WrongConstant")
    private void initializeTitleLayout() {
        View titleLayout = this.view.findViewById(R.id.titleLayout);
        titleLayout.setBackgroundColor(this.calendarTitleBackgroundColor);
        @SuppressLint("WrongConstant") String dateText = (new DateFormatSymbols(this.locale)).getMonths()[this.currentCalendar.get(2)].toString();
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        TextView dateTitle = this.view.findViewById(R.id.dateTitle);
        dateTitle.setTextColor(this.calendarTitleTextColor);
        dateTitle.setText(dateText + " " + this.currentCalendar.get(1));
        dateTitle.setTextColor(this.calendarTitleTextColor);
        if (null != this.getCustomTypeface()) {
            dateTitle.setTypeface(this.getCustomTypeface(), 1);
        }
        dateTitle.setOnClickListener(v -> {
            if (mListener != null) {
//                mListener.onClickMainDate();
            }
        });

    }

    @SuppressLint({"DefaultLocale"})
    private void initializeWeekLayout() {
        View titleLayout = this.view.findViewById(R.id.weekLayout);
        titleLayout.setBackgroundColor(this.weekLayoutBackgroundColor);
        String[] weekDaysArray = (new DateFormatSymbols(this.locale)).getShortWeekdays();

        for (int i = 1; i < weekDaysArray.length; ++i) {
            String dayOfTheWeekString = weekDaysArray[i];
            dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase();
            TextView dayOfWeek = this.view.findViewWithTag("dayOfWeek" + this.getWeekIndex(i, this.currentCalendar));
            dayOfWeek.setText(dayOfTheWeekString);
//            dayOfWeek.setTextColor(this.dayOfWeekTextColor);
            if (null != this.getCustomTypeface()) {
                dayOfWeek.setTypeface(this.getCustomTypeface());
            }
        }

    }

    @SuppressLint("WrongConstant")
    private void setDaysInCalendar() {
        Calendar calendar = Calendar.getInstance(this.locale);
        calendar.setTime(this.currentCalendar.getTime());
        calendar.set(5, 1);
        calendar.setFirstDayOfWeek(this.getFirstDayOfWeek());
        @SuppressLint("WrongConstant") int firstDayOfMonth = calendar.get(7);
        int dayOfMonthIndex = this.getWeekIndex(firstDayOfMonth, calendar);
        @SuppressLint("WrongConstant") int actualMaximum = calendar.getActualMaximum(5);
        Calendar startCalendar = (Calendar) calendar.clone();
        startCalendar.add(5, -(dayOfMonthIndex - 1));
        int monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1);

        GitsDayView mGitsDayView;
        for (int i = 1; i < 43; ++i) {
            ViewGroup dayOfMonthContainer = this.view.findViewWithTag("dayOfMonthContainer" + i);
            RelativeLayout relativeViewGroup = this.view.findViewWithTag("viewgroupinfo" + i);
            FrameLayout frameLayout = this.view.findViewWithTag("frame" + i);
            ImageView imageLayout = this.view.findViewWithTag("imageMyCalendar" + i);
            mGitsDayView = this.view.findViewWithTag("dayOfMonthText" + i);
            TextView tvProgress = this.view.findViewWithTag("progressTextTag" + i);
            ImageView ivDot = this.view.findViewWithTag("imageDotTag" + i);
            TextView tvApprovedTextTag = this.view.findViewWithTag("approvedTextTag" + i);


            ivDot.setVisibility(View.GONE);
            ivDot.setImageResource(R.drawable.white_circle);

            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) tvProgress.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            layoutParams.bottomMargin = 8;
            tvProgress.setLayoutParams(layoutParams);

            layoutParams =
                    (RelativeLayout.LayoutParams) tvApprovedTextTag.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            tvApprovedTextTag.setLayoutParams(layoutParams);

            if (mGitsDayView != null) {
                GitsDayView finalMGitsDayView = mGitsDayView;
                finalMGitsDayView.setOnClickListener(v -> {
                    Toast.makeText(mContext, finalMGitsDayView.getText(), Toast.LENGTH_SHORT).show();
                });
                mGitsDayView.bind(startCalendar.getTime(), this.getDecorators(), null);
                mGitsDayView.setVisibility(0);
                if (null != this.getCustomTypeface()) {
                    mGitsDayView.setTypeface(this.getCustomTypeface());
                }

                if (this.isSameMonth(calendar, startCalendar)) {
                    if (!mData.isEmpty() && firstDatePosition < mData.size()) {
                        String a = DateHelper.getCustomDate(mData.get(firstDatePosition).getDate(), "dd/MM/yyyy");
                        String b = DateHelper.getCustomDate(startCalendar.getTimeInMillis(), "dd/MM/yyyy");

                        if (DateHelper.getCustomDate(mData.get(firstDatePosition).getDate(), "dd/MM/yyyy").equalsIgnoreCase(DateHelper.getCustomDate(startCalendar.getTimeInMillis(), "dd/MM/yyyy"))) {
                            tvProgress.setText(mData.get(firstDatePosition).getExtraText().length() > 4 ? mData.get(firstDatePosition).getExtraText().substring(0, 4) : mData.get(firstDatePosition).getExtraText());
                            tvProgress.setVisibility(!mData.get(firstDatePosition).getExtraText().isEmpty() ? View.VISIBLE : View.INVISIBLE);
                            tvApprovedTextTag.setVisibility(View.INVISIBLE);

                            if (mData.get(firstDatePosition).getBackgroundExtra() != null) {
                                try {
                                    Drawable res = ContextCompat.getDrawable(getContext(), mData.get(firstDatePosition).getBackgroundExtra());
                                    tvProgress.setBackground(res);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            if (mData.get(firstDatePosition).getDotDrawable() != null) {
                                try {
                                    ivDot.setVisibility(View.VISIBLE);
                                    ivDot.setImageResource(mData.get(firstDatePosition).getDotDrawable());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            mListener.onCreateDateView(dayOfMonthContainer, i, startCalendar.getTimeInMillis(), mData.get(firstDatePosition));
                            firstDatePosition++;
                        } else {
                            tvProgress.setVisibility(View.INVISIBLE);
                            mListener.onCreateDateView(dayOfMonthContainer, i, startCalendar.getTimeInMillis(), null);
                        }
                    } else {
                        tvProgress.setVisibility(View.INVISIBLE);
                        mListener.onCreateDateView(dayOfMonthContainer, i, startCalendar.getTimeInMillis(), null);
                    }
                    dayOfMonthContainer.setOnClickListener(null);
                    if (isWeekEnd(i)) {
                        mGitsDayView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                    } else {
                        mGitsDayView.setTextColor(this.dayOfWeekTextColor);
                    }

                    this.markDayAsCurrentDay(startCalendar);
                } else {
                    mGitsDayView.setVisibility(View.INVISIBLE);
                    tvProgress.setVisibility(View.INVISIBLE);
                    tvApprovedTextTag.setVisibility(View.INVISIBLE);
                    mGitsDayView.setBackgroundColor(this.disabledDayBackgroundColor);
                    mGitsDayView.setTextColor(this.disabledDayTextColor);
                    if (!this.isOverflowDateVisible()) {
                        mGitsDayView.setVisibility(8);
                    } else if (i >= 36 && (float) monthEndIndex / 7.0F >= 1.0F) {
                        mGitsDayView.setVisibility(8);
                    }
                }

                mGitsDayView.decorate();
                startCalendar.add(5, 1);
                ++dayOfMonthIndex;
            }
        }

        ViewGroup weekRow = this.view.findViewWithTag("weekRow6");
        mGitsDayView = this.view.findViewWithTag("dayOfMonthText36");
        if (mGitsDayView.getVisibility() != 0) {
            weekRow.setVisibility(8);
        } else {
            weekRow.setVisibility(0);
        }

    }

    @SuppressLint("WrongConstant")
    public boolean isSameMonth(Calendar c1, Calendar c2) {
        return (c1 != null && c2 != null) && (c1.get(0) == c2.get(0) && c1.get(1) == c2.get(1) && c1.get(2) == c2.get(2));
    }

    private void clearDayOfTheMonthStyle(Date currentDate) {
        if (currentDate != null) {
            Calendar calendar = this.getTodaysCalendar();
            calendar.setFirstDayOfWeek(this.getFirstDayOfWeek());
            calendar.setTime(currentDate);
            GitsDayView GitsDayView = this.getDayOfMonthText(calendar);
            GitsDayView.setBackgroundColor(this.calendarBackgroundColor);

            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(currentDate);
            GitsDayView.setTextColor(getResources().getColor((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) ? R.color.colorRed : R.color.black));
        }

    }

    private GitsDayView getDayOfMonthText(Calendar currentCalendar) {
        return (GitsDayView) this.getView("dayOfMonthText", currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = this.getMonthOffset(currentCalendar);
        @SuppressLint("WrongConstant") int currentDay = currentCalendar.get(5);
        int index = currentDay + monthOffset;
        return index;
    }

    @SuppressLint("WrongConstant")
    private int getMonthOffset(Calendar currentCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(this.getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(5, 1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        @SuppressLint("WrongConstant") int dayPosition = calendar.get(7);
        return firstDayWeekPosition == 1 ? dayPosition - 1 : (dayPosition == 1 ? 6 : dayPosition - 2);
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();
        return firstDayWeekPosition == 1 ? weekIndex : (weekIndex == 1 ? 7 : weekIndex - 1);
    }

    private View getView(String key, Calendar currentCalendar) {
        int index = this.getDayIndexByDate(currentCalendar);
        View childView = this.view.findViewWithTag(key + index);
        return childView;
    }

    private Calendar getTodaysCalendar() {
        Calendar currentCalendar = Calendar.getInstance(this.mContext.getResources().getConfiguration().locale);
        currentCalendar.setFirstDayOfWeek(this.getFirstDayOfWeek());
        return currentCalendar;
    }

    @SuppressLint({"DefaultLocale"})
    public void refreshCalendar(Calendar currentCalendar) {
        firstDatePosition = 0;
        this.currentCalendar = currentCalendar;
        this.currentCalendar.setFirstDayOfWeek(this.getFirstDayOfWeek());
        this.locale = this.mContext.getResources().getConfiguration().locale;
        this.initializeTitleLayout();
        this.initializeWeekLayout();
        this.setDaysInCalendar();
    }

    public int getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public void markDayAsCurrentDay(Calendar calendar) {
        GitsDayView dayOfMonth = this.getDayOfMonthText(calendar);
        if (calendar != null && isToday(calendar) && isSameMonthStatic(calendar, Calendar.getInstance())) {
            dayOfMonth.setTextColor(this.currentDayOfMonth);
            dayOfMonth.setBackgroundResource(R.drawable.ic_current_day);
            dayOfMonth.setTypeface(Typeface.DEFAULT_BOLD);
            dayOfMonth.setTextSize(18);
        } else {
            dayOfMonth.setTypeface(Typeface.DEFAULT);
            dayOfMonth.setTextSize(16);
        }

    }

    public void markDayAsSelectedDay(Date currentDate) {
        Calendar currentCalendar = this.getTodaysCalendar();
        currentCalendar.setFirstDayOfWeek(this.getFirstDayOfWeek());
        currentCalendar.setTime(currentDate);
        this.clearDayOfTheMonthStyle(this.lastSelectedDay);
        this.storeLastValues(currentDate);
        GitsDayView view = this.getDayOfMonthText(currentCalendar);
        if (mType == MHR) {
            view.setBackgroundResource(R.drawable.border_red);
            view.setTextColor(this.selectedDayTextColor);
        }
    }

    private void storeLastValues(Date currentDate) {
        this.lastSelectedDay = currentDate;
    }

    public void setCalendarListener(GitsCalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public List<GitsDayDecorator> getDecorators() {
        return this.decorators;
    }

    public void setDecorators(List<GitsDayDecorator> decorators) {
        this.decorators = decorators;
    }

    public boolean isOverflowDateVisible() {
        return this.isOverflowDateVisible;
    }

    public void setShowOverflowDate(boolean isOverFlowEnabled) {
        this.isOverflowDateVisible = isOverFlowEnabled;
    }

    public Typeface getCustomTypeface() {
        return this.customTypeface;
    }

    public void setCustomTypeface(Typeface customTypeface) {
        this.customTypeface = customTypeface;
    }

    public Calendar getCurrentCalendar() {
        return this.currentCalendar;
    }

    public boolean isWeekEnd(int i) {
        return (i == 1) || (i == 8) || (i == 15) || (i == 22) || (i == 29) || (i == 36);
    }

    public void changeCalendarState(long timeStamp, int rangemonth, int year) {
        this.currentMonthIndex = rangemonth;
        GitsCalendarView.this.currentCalendar.add(2, GitsCalendarView.this.currentMonthIndex);
        this.currentCalendar.setTime(new Date(timeStamp));
        initializeTitleLayout();
    }


}

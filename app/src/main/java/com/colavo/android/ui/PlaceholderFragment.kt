package com.colavo.android.ui


import android.os.Bundle
import com.colavo.android.R
import com.colavo.android.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_01.*
import com.alamkanak.weekview.WeekViewEvent
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import android.graphics.RectF
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.alamkanak.weekview.DateTimeInterpreter
import java.text.SimpleDateFormat
import java.util.*
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.toolbar.*
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.ui.salons.SalonListActivity
import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.support.design.widget.FloatingActionButton
import android.text.method.Touch.onTouchEvent
import android.view.*
import android.widget.TextView
import com.colavo.android.R.id.fab
import kotlinx.android.synthetic.main.popup_content.*
import kotlinx.android.synthetic.main.popup_content.view.*
import android.view.MotionEvent
import android.view.View.OnTouchListener
import com.colavo.android.R.id.container
import com.colavo.android.R.layout.fragment_01


/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment : BaseFragment()
        , WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private val TYPE_DAY_VIEW = 1
    private val TYPE_THREE_DAY_VIEW = 2
    private val TYPE_WEEK_VIEW = 3
    private var mWeekViewType = TYPE_THREE_DAY_VIEW
    private var mWeekView: WeekView? = weekView

//    private lateinit var popup: MaryPopup

    override fun getLayout() = R.layout.fragment_01



    companion object {
        fun newInstance() = PlaceholderFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDateTimeInterpreter(true)
        setHasOptionsMenu(true)
      //  (activity as AppCompatActivity).supportActionBar?.title = salon.name
        val fabButton =
                (activity as AppCompatActivity).findViewById(R.id.fab) as FloatingActionButton?
        fabButton?.setOnClickListener {
            view ->  Toast.makeText(context, "Clicked FAB" , Toast.LENGTH_LONG).show()
/*            val dialogFrag : CreateFabFragment = CreateFabFragment()
            dialogFrag.setParentFab(fab)
            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag())*/
        }


    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
        toolBar.setTitle (salon.name)
        toolBar.inflateMenu(R.menu.salon_main)


        weekView.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                // Interpret MotionEvent data
                // Handle touch here

                return false
            }


        })


   /*     toolbar_title.setText (salon.name)
        val myTypeface : Typeface = Typeface.createFromAsset(activity.assets, "Poppins-SemiBold.ttf")
        toolbar_title.setTypeface(myTypeface)*/
        // Get a reference for the week view in the layout.
       // mWeekView = R.id.weekView as WeekView


        // Show a toast message about the touched event.
        weekView.setOnEventClickListener(this)

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        weekView.setMonthChangeListener(this)

        // Set long press listener for events.
        weekView.eventLongPressListener = this

        // Set long press listener for empty view
        weekView.emptyViewLongPressListener = this

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(true)


/*
        popup = MaryPopup.with(this.activity)
                .cancellable(true)
                .draggable(true)
                .blackOverlayColor(Color.parseColor("#DD444444"))
                .backgroundColor(Color.parseColor("#EFF4F5"));
*/
    }



    override fun onMonthChange(newYear: Int, newMonth: Int): List<WeekViewEvent> {
        // Populate the week view with some events.
        val events = ArrayList<WeekViewEvent>()

        var startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 1)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        var endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        var event = WeekViewEvent(1, "STRÅLA\n",getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 1)
        startTime.set(Calendar.MINUTE, 30)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.set(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(2, "Tony Stark\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor02)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 4)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.set(Calendar.DATE, 2)
        endTime = startTime.clone() as Calendar
        endTime.set(Calendar.HOUR_OF_DAY, 6)
        endTime.set(Calendar.MINUTE, 0)
        event = WeekViewEvent(3, "John Mayer\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor03)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 5)
        startTime.set(Calendar.MINUTE, 30)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(4, "Frank Ocean\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor03)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 5)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(5, "Kanye\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor04)
        events.add(event)


        startTime = Calendar.getInstance()
        startTime.set(Calendar.DAY_OF_MONTH, 1)
        startTime.set(Calendar.HOUR_OF_DAY, 3)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = WeekViewEvent(6, "Depp\n",getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH))
        startTime.set(Calendar.HOUR_OF_DAY, 15)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        event = WeekViewEvent(7, "Antonio\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)


        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 2)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 2)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(8, "Dr.Strange\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor04)
        events.add(event)


        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 1)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 3)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(9, "Soonsiki\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 3)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 3)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(10, "가람\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor03)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 3)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(11, "리아\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 4)
        startTime.set(Calendar.MINUTE, 30)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 2)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(12, "Hannah\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)


        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 3)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 4)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(13, "Vinter\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor04)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 1)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 5)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 1)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(14, "아라\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor04)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 2)
        startTime.set(Calendar.MINUTE, 30)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 5)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(15, "마루\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 5)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 3)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(16, "BRUDSLÖJA\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor04)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 0)
        startTime.set(Calendar.MINUTE, 45)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.add(Calendar.MINUTE, 0)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(16, "로하\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor02)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 3)
        startTime.set(Calendar.MINUTE, 45)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 0)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 1)
        endTime.add(Calendar.MINUTE, 15)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(17, "지우\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor02)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 8)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 0)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 1)
        endTime.add(Calendar.MINUTE, 45)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(18, "LOHALS\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor04)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 10)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 0)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(19, "재인\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 8)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 0)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.add(Calendar.MINUTE, 45)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(20, "Vince\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 7)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 2)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime.add(Calendar.MINUTE, 45)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(21, "도토리어린이\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

  /*      //AllDay event
        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 0)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 23)
        event = WeekViewEvent(7, getEventTitle(startTime), null, startTime, endTime, true)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.DAY_OF_MONTH, 8)
        startTime.set(Calendar.HOUR_OF_DAY, 2)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.set(Calendar.DAY_OF_MONTH, 10)
        endTime.set(Calendar.HOUR_OF_DAY, 23)
        event = WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        // All day event until 00:00 next day
        startTime = Calendar.getInstance()
        startTime.set(Calendar.DAY_OF_MONTH, 10)
        startTime.set(Calendar.HOUR_OF_DAY, 0)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.SECOND, 0)
        startTime.set(Calendar.MILLISECOND, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.set(Calendar.DAY_OF_MONTH, 11)
        event = WeekViewEvent(8, getEventTitle(startTime), null, startTime, endTime, true)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)*/

        return events
    }
    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private fun setupDateTimeInterpreter(shortDate: Boolean) {
        mWeekView?.dateTimeInterpreter = object : DateTimeInterpreter { //TODO mWeekView -> weekView
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat("\nM/d", Locale.getDefault())

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = weekday[0].toString()
                //return weekday.toUpperCase() + format.format(date.time)
                return weekday + format.format(date.time)
            }

            override fun interpretTime(hour: Int): String {
                if (hour == 12 ) {
                    return if (hour > 11) (hour - 12).toString() + " P"
                    else if (hour == 0) "12 AM" else hour.toString() + " A"
                }
                else
                    return ""
            }
        }
    }

    protected fun getEventTitle(time: Calendar): String {
 //       return String.format("Event of %02d:%02d \n%s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH))
/*        return String.format("%d:%02d " +
                            "%s/%d"
                            , time.get(Calendar.HOUR_OF_DAY)
                            , time.get(Calendar.MINUTE)
                            , time.get(Calendar.MONTH) + 1
                            , time.get(Calendar.DAY_OF_MONTH))*/
        return "Cut, Perm, Coloring"
    }

    override fun onEventClick(event: WeekViewEvent, eventRect: RectF) {
        Toast.makeText(context, "Clicked ${event.name}" , Toast.LENGTH_LONG).show()

        val mView = LinearLayout(this.context)

        // Gets the layout params that will allow you to resize the layout
/*

        val relativeParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        relativeParams.setMargins(eventRect.left.toInt(), eventRect.top.toInt(), 0, 0)
        relativeParams.width = eventRect.width().toInt()
        relativeParams.height = eventRect.height().toInt()
        mView.setBackgroundColor(Color.rgb(255,0,0))
        mView.run {
            setLayoutParams(relativeParams)
            setVisibility(View.VISIBLE)
        }
*/

        //Show Tooltip Window
        SwipeDismissDialog.Builder(this.context)
                .setLayoutResId(R.layout.popup_content)
                .setFlingVelocity(0.06f)
                .build()
                .show()

        updateEventPoptip(event)


        /* SimpleTooltip
       SimpleTooltip.Builder(context)
                .anchorView(weekView)
                .text("Text do Tooltip")
                .gravity(Gravity.END)
                .animated(true)
                .transparentOverlay(false)
                .build()
                .show()*/
/* MaryPopup
        popup.content(R.layout.popup_content)
                .cancellable(true)
                .from(mView)
                .show()
*/

    }

/*  MaryPopup
    fun onBackPressed() {
        if (popup.isOpened) {
            popup.close(true)
        } else {
            //super.onBackPressed()
        }
    }*/

    override fun onEventLongPress(event: WeekViewEvent, eventRect: RectF) {
        Toast.makeText(context, "Long pressed event: ${event.name}" , Toast.LENGTH_LONG).show()
        //    Toast.makeText(this, "Long pressed event: " + event.name, Toast.LENGTH_SHORT).show()
    }

    override fun onEmptyViewLongPress(time: Calendar) {
        Toast.makeText(context, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show()
    }

    fun getWeekView(): WeekView? {
        return mWeekView
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.salon_main, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        setupDateTimeInterpreter(id == R.id.action_week_view)
        when (id) {
            R.id.action_today -> {
                weekView.goToToday()
                return true
            }
            R.id.action_create_event ->{
                val dialogFrag = CreateFabFragment.newInstance()
                dialogFrag.setParentFab(fab)
                dialogFrag.show((activity as AppCompatActivity).getSupportFragmentManager(), dialogFrag.getTag())
                return true
            }
            R.id.action_day_view -> {
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_DAY_VIEW
                    weekView.setNumberOfVisibleDays(1)

                    // Lets change some dimensions to best fit the view.
                    weekView.setColumnGap(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt())
                    weekView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                    weekView.setEventTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                }
                return true
            }
            R.id.action_three_day_view -> {
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_THREE_DAY_VIEW
                    weekView.setNumberOfVisibleDays(3)

                    // Lets change some dimensions to best fit the view.
                    weekView.setColumnGap(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt())
                    weekView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                    weekView.setEventTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt())
                }
                return true
            }
            R.id.action_week_view -> {
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_WEEK_VIEW
                    weekView.setNumberOfVisibleDays(7)

                    // Lets change some dimensions to best fit the view.
                    weekView.setColumnGap(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt())
                    weekView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt())
                    weekView.setEventTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt())
                }
                return true
            }

            R.id.action_sign_out -> {

                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }


    fun updateEventPoptip(event: WeekViewEvent){
            Toast.makeText(context, "Clicked Poptip ${event.name}" , Toast.LENGTH_LONG).show()
        pop_customer_name?.setText("${event.name}")

    }




}

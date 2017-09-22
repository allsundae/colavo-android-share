package com.colavo.android.ui

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.colavo.android.R
import com.colavo.android.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_01.*
import com.alamkanak.weekview.WeekViewEvent
import com.alamkanak.weekview.MonthLoader
import com.alamkanak.weekview.WeekView
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.alamkanak.weekview.DateTimeInterpreter
import kotlinx.android.synthetic.main.event_item.*
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Color.parseColor
import android.graphics.Color.parseColor
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.colavo.android.ui.event.eventView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.view.Gravity
import android.R.attr.gravity
import android.support.annotation.VisibleForTesting
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip


/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment : BaseFragment() , WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private val TYPE_DAY_VIEW = 1
    private val TYPE_THREE_DAY_VIEW = 2
    private val TYPE_WEEK_VIEW = 3
    private val mWeekViewType = TYPE_DAY_VIEW
    private var mWeekView: WeekView? = null

//    private lateinit var popup: MaryPopup

    override fun getLayout() = R.layout.fragment_01

    companion object {
        fun newInstance() = PlaceholderFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDateTimeInterpreter(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Get a reference for the week view in the layout.
//        mWeekView = findViewById(R.id.weekView) as WeekView

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
        startTime.set(Calendar.HOUR_OF_DAY, 3)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        var endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 1)
        endTime.set(Calendar.MONTH, newMonth - 1)
        var event = WeekViewEvent(1, getEventTitle(startTime),"\nLocation", startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 3)
        startTime.set(Calendar.MINUTE, 0)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(1, getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 4)
        startTime.set(Calendar.MINUTE, 20)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.set(Calendar.HOUR_OF_DAY, 5)
        endTime.set(Calendar.MINUTE, 0)
        event = WeekViewEvent(10, getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context,R.color.eventColor02)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 5)
        startTime.set(Calendar.MINUTE, 30)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 2)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(2, getEventTitle(startTime), startTime, endTime)
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
        event = WeekViewEvent(3, getEventTitle(startTime), startTime, endTime)
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
        event = WeekViewEvent(5, getEventTitle(startTime), startTime, endTime)
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
        event = WeekViewEvent(5, getEventTitle(startTime), startTime, endTime)
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
        mWeekView?.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                var weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat(" M/d", Locale.getDefault())

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
                    return if (hour > 11) (hour - 12).toString() + " PM"
                    else if (hour == 0) "12 AM" else hour.toString() + " AM"
                }
                else
                    return ""
            }
        }
    }

    protected fun getEventTitle(time: Calendar): String {
 //       return String.format("Event of %02d:%02d \n%s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH))
        return String.format("Event of %02d:%02d " +
                            "%s/%d"
                            , time.get(Calendar.HOUR_OF_DAY)
                            , time.get(Calendar.MINUTE)
                            , time.get(Calendar.MONTH) + 1
                            , time.get(Calendar.DAY_OF_MONTH))
    }

    override fun onEventClick(event: WeekViewEvent, eventRect: RectF) {
        Toast.makeText(context, "Clicked ${event.name}" , Toast.LENGTH_LONG).show()

        val mView = LinearLayout(this.context)

        // Gets the layout params that will allow you to resize the layout

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

        //Show Tooltip Window

        SimpleTooltip.Builder(context)
                .anchorView(weekView)
                .text("Text do Tooltip")
                .gravity(Gravity.END)
                .animated(true)
                .transparentOverlay(false)
                .build()
                .show()
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

}

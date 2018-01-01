package com.colavo.android.ui


import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.os.Bundle
import com.colavo.android.R
import com.colavo.android.base.BaseFragment
import com.colavo.android.weekview.WeekViewEvent
import com.colavo.android.weekview.MonthLoader
import com.colavo.android.weekview.WeekView
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.widget.Toast
import com.colavo.android.weekview.DateTimeInterpreter
import java.text.SimpleDateFormat
import java.util.*
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.poptip.SwipeDismissDialog
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.animation.*
import android.view.animation.Interpolator
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_01.*
import kotlinx.android.synthetic.main.popup_event_detail.view.*
import com.colavo.android.poptip.OnCancelListener
import com.colavo.android.poptip.OnSwipeDismissListener
import com.colavo.android.poptip.SwipeDismissDirection
import com.colavo.android.utils.Logger
import kotlinx.android.synthetic.main.popup_event_detail.*
import java.util.concurrent.TimeUnit


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
        //val dialog = LayoutInflater.from(this.context!!).inflate(R.layout.popup_event_detail, null)
//        val fabButton = fab_calendar as FloatingActionButton
     /*   val fabButton =
                (activity as AppCompatActivity).findViewById(R.id.fab) as FloatingActionButton?
     */


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_CONVERSATION) as SalonModel
        toolBar.title = ""//salon.name
      //  toolBar.setTitleTextAppearance(this.context!!,ToolbarTitleText)
        toolBar.inflateMenu(R.menu.salon_main)
        spinner_designer.prompt = salon.name

        val designer_list = ArrayList<String>()
        designer_list.add(salon.name)
        designer_list.add("Name")

        val designerAdapter = ArrayAdapter<String>(this.context!!, R.layout.spinner_designer, R.id.designer_name, designer_list)
        designerAdapter.setDropDownViewResource(R.layout.spinner_popup)
        spinner_designer.adapter = designerAdapter//setText(salon.name)

        // Show a toast message about the touched event.
        weekView.setOnEventClickListener(this)
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        weekView.monthChangeListener = this

        // Set long press listener for events.
        weekView.eventLongPressListener = this
        // Set long press listener for empty view
        weekView.emptyViewLongPressListener = this
        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
   //     setupDateTimeInterpreter(true)

/*        fab_calendar.setOnClickListener {
            //view ->  Toast.makeText(context, "Clicked FAB" , Toast.LENGTH_LONG).show()
            Toast.makeText(context, "Clicked FAB" , Toast.LENGTH_LONG).show()
*//*            val dialogFrag : CreateFabFragment = CreateFabFragment()
            dialogFrag.setParentFab(fab)
            dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag())*//*
        }*/
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 1)
        startTime.set(Calendar.MINUTE, 30)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.set(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR, 1)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(2, "Tony Stark\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor02)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor03)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor03)
        events.add(event)

        startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, 5)
        startTime.set(Calendar.MINUTE, 15)
        startTime.set(Calendar.MONTH, newMonth - 1)
        startTime.set(Calendar.YEAR, newYear)
        startTime.add(Calendar.DATE, 1)
        endTime = startTime.clone() as Calendar
        endTime.add(Calendar.HOUR_OF_DAY, 3)
        endTime.set(Calendar.MONTH, newMonth - 1)
        event = WeekViewEvent(5, "Kanye\n", getEventTitle(startTime), startTime, endTime)
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor04)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
        events.add(event)

/*
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor04)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor03)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor04)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor04)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor04)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor02)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor02)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor04)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
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
        event.color = ContextCompat.getColor(this.context!!,R.color.eventColor01)
        events.add(event)
*/
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
       return String.format("%d:%02d " +
                            "%s/%d"
                            , time.get(Calendar.HOUR_OF_DAY)
                            , time.get(Calendar.MINUTE)
                            , time.get(Calendar.MONTH) + 1
                            , time.get(Calendar.DAY_OF_MONTH))
//        return "Cut, Perm, Coloring"
    }

    protected fun getEventTime(time: Calendar): String {
 //       return String.format("Event of %02d:%02d \n%s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH))
/*        return String.format("%s/%d, " + "%d:%02d"
                            , time.get(Calendar.MONTH) + 1
                            , time.get(Calendar.DAY_OF_MONTH)
                            , time.get(Calendar.HOUR_OF_DAY)
                            , time.get(Calendar.MINUTE)
                            )*/
        val nowMsec = time.timeInMillis
        val date = Date(nowMsec)
        val sdf = SimpleDateFormat("EEE, MMM d, h:mm a", Locale.getDefault())
        return sdf.format(date)
    }

    protected fun getEventDurationTime(startTime: Calendar, endTime:Calendar): String {
        val durMillis = endTime.timeInMillis - startTime.timeInMillis
        val sdf = SimpleDateFormat("h:mm", Locale.getDefault())
        return sdf.format(Date(durMillis*1000))
        /*        return String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(durMillis),
                        TimeUnit.MILLISECONDS.toMinutes(durMillis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(durMillis)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(durMillis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durMillis)))*/
    }

    override fun onEventClick(event: WeekViewEvent, eventRect: RectF, translateRect: Rect) {
  //      Toast.makeText(context, "Clicked ${event.name}" , Toast.LENGTH_LONG).show()
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
        val window : Window  = activity!!.window
        window.setBackgroundDrawableResource(R.drawable.color_gradient)

/*        val animation : ValueAnimator = ValueAnimator.ofArgb(event.color, Color.BLACK)
        animation.addUpdateListener(ValueAnimator.AnimatorUpdateListener() {
            fun onAnimationUpdate (animaton: ValueAnimator){
                val value: Int = animation.getAnimatedValue() as Int
                event.setColor(value)
            }
         })
        animation.setInterpolator(LinearInterpolator())
        animation.setDuration(1000)
        animation.start()*/

   //     calendar_fragment.setBackgroundResource(R.drawable.ic_rounded)
        scaleAnim(calendar_fragment, 1.0f, 0.97f, false)
        val dialog = LayoutInflater.from(this.context!!).inflate(R.layout.popup_event_detail, null)


        val displaymetrics : DisplayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val width : Int = displaymetrics.widthPixels
        val height : Int = displaymetrics.heightPixels
        var boolX : Int = 1
        var boolY : Int = 1
/*
        val afterWidth : Int = ( width -  dialog.width )/2
        val afterHeight : Int = ( height -  dialog.height )/2

        if (translateRect.top < afterHeight) boolY = -1
        if (translateRect.left < afterHeight) boolX = -1

        val percentageX : Float = ((translateRect.left*100) / afterWidth * boolX).toFloat()
        val percentageY : Float = ((translateRect.top*100) / afterHeight * boolY).toFloat()*/


       //val anim_in = AnimationUtils.loadAnimation(this.context!!, R.anim.mydialog_translate)
 //       val translateAnimation   = TranslateAnimation(percentageX, 0f, percentageY,0f)

        val x1 = translateRect.left
        val y1 = translateRect.top

        val w1 = eventRect.width()
        val h1 = eventRect.height()
        dialog.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val w2 = dialog.measuredWidth//dialog.measuredWidth
        val h2 = dialog.measuredHeight//height

        val centeredX : Int = ( width -  w2 )/2
        val centeredY : Int = ( height -  h2 )/2

        val normalizedW = w1/w2
        val normalizedH = h1/h2

        val x2 = centeredX //afterWidth
        val y2 = centeredY // afterHeight

        Logger.log("onEventClick: width: ${width}, height : ${height} \t w1: ${w1}, h1 : ${h1}")
        Logger.log("onEventClick: x1: ${x1}, y1 : ${y1} \t w1: ${w1}, h1 : ${h1}")
        Logger.log("onEventClick: x2: ${x2}, y2 : ${y2} \t w2: ${w2}, h2 : ${h2}")

/*
       val translateAnimation   = TranslateAnimation(Animation.ABSOLUTE, x1.toFloat(), Animation.ABSOLUTE, centeredX.toFloat(),
                Animation.ABSOLUTE, y1.toFloat() ,Animation.ABSOLUTE, centeredY.toFloat())
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        val scaleAnimation = ScaleAnimation(w1,w2.toFloat(),h1,h2.toFloat(),0.5f,0.5f)

*/
// 1
        val positionAnimatorX = ValueAnimator.ofFloat(x1.toFloat(), centeredX.toFloat())
        val positionAnimatorY = ValueAnimator.ofFloat(y1.toFloat(), centeredY.toFloat())

        positionAnimatorX.addUpdateListener {
            val value = it.animatedValue as Float
            dialog.translationX = value
        }
        positionAnimatorY.addUpdateListener {
            val value = it.animatedValue as Float
            dialog.translationY = value
        }
// 2
        val scaleAnimatorW = ValueAnimator.ofFloat(normalizedW, 1.0.toFloat())
        val scaleAnimatorH= ValueAnimator.ofFloat(normalizedH, 1.0.toFloat())

        scaleAnimatorW.addUpdateListener {
            val value = it.animatedValue as Float
            dialog.scaleX = value
        }
        scaleAnimatorH.addUpdateListener {
            val value = it.animatedValue as Float
            dialog.scaleY = value
        }

// 3
        val alphaAnimator = ObjectAnimator.ofFloat(dialog, "alpha", 0f, 1f)

// 4
        val animatorSet = AnimatorSet()
//        animatorSet.play(positionAnimatorX).with(positionAnimatorY).with(alphaAnimator).with(scaleAnimatorW).with(scaleAnimatorH)
        animatorSet.playTogether(positionAnimatorX, positionAnimatorY
        ,alphaAnimator, scaleAnimatorW, scaleAnimatorH)
        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.duration = 1600
        animatorSet.start()
        //animatorSet.fillBefore
        //animatorSet.setFillAfter(true)
        //dialog.startAnimation()

/*        dialog.animate()
          .x((centeredX).toFloat())
          .y((centeredY).toFloat())*/

           //     .scaleX( w2 / w1)
           //     .scaleY(w2 / w1)
          //      .translationX(centeredX - x1 - w1 * (1 - (w2 / w1)) / 2)
          //      .translationY(centeredY - y1 - h1 * (1 - (h2 / h1)) / 2)
       // anim_in.duration = 300
        //dialog.startAnimation(anim_in)*/

        fillDialogData(event, dialog)
        //Show Tooltip Window
       SwipeDismissDialog.Builder(this.context!!)
               // .setLayoutResId(R.layout.popup_event_detail)
                .setView(dialog)
                .setFlingVelocity(0.02f)
                .setOnCancelListener(object : OnCancelListener {
                    override fun onCancel(view: View) {
   //                     Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
                        scaleAnim(calendar_fragment, 0.97f, 1f, true)
                        calendar_fragment.setBackgroundColor(resources.getColor(R.color.backgroundWhite))
                        calendar_fragment.setBackgroundResource(R.drawable.ic_nonerounded)
                    }
                })
                .setOnSwipeDismissListener(object : OnSwipeDismissListener {
                    override fun onSwipeDismiss(view: View, direction: SwipeDismissDirection) {
    //                    Toast.makeText(context, "Swipe dismissed to: " + direction, Toast.LENGTH_SHORT).show()
                        scaleAnim(calendar_fragment, 0.95f, 1f, true)
                        calendar_fragment.setBackgroundColor(resources.getColor(R.color.backgroundWhite))
                        calendar_fragment.setBackgroundResource(R.drawable.ic_nonerounded)
                    }
                })
                .build()
                .show()

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
        popup.content(R.layout.popup_event_detail)
                .cancellable(true)
                .from(mView)
                .show()
*/
    }

    private fun fillDialogData(event: WeekViewEvent, dialog: View) {
        val colorFilter = PorterDuffColorFilter(event.color, PorterDuff.Mode.MULTIPLY)
        val colorFilter2 = PorterDuffColorFilter(event.color, PorterDuff.Mode.SCREEN)

        dialog.pop_customer_name.text = event.name
        dialog.pop_customer_status.text = "Past"
        dialog.pop_time.text = getEventTime(event.startTime) + " (${getEventDurationTime(event.startTime, event.endTime)})"
        dialog.pop_menu.text = event.location
        dialog.pop_memo.text = event.color.toString()
        dialog.pop_modifiedDate.text = getEventTime(event.startTime)

        dialog.background.colorFilter = colorFilter
        dialog.pop_customer_image.colorFilter = colorFilter
        dialog.pop_ic_time.colorFilter = colorFilter2
        dialog.pop_ic_memo.colorFilter = colorFilter2
        dialog.pop_ic_menu.colorFilter = colorFilter2
    }

    fun scaleAnim (objectTo :View, fromScale:Float, toScale:Float, zoomIn:Boolean){
       val scaleAnim  = ScaleAnimation(
                fromScale, toScale,
                fromScale, toScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF , 1.0f)

        scaleAnim.repeatCount = 0
        if (zoomIn) {
            scaleAnim.duration = 500
            scaleAnim.interpolator = OvershootInterpolator()
        }
        else {
            //scaleAnim.startOffset = 300
            scaleAnim.duration = 600
            scaleAnim.interpolator = OvershootInterpolator()
        }

        scaleAnim.fillAfter = true
        scaleAnim.fillBefore = true
        scaleAnim.isFillEnabled = true

        objectTo.startAnimation(scaleAnim)
    }

    class CameraAnim : Animation() {
        var cx: Float = 0.toFloat()
        var cy:Float = 0.toFloat()

        override fun initialize (width: Int, height: Int, parentWidth: Int, parentHeight: Int ){
            super.initialize(width, height, parentWidth, parentHeight)
            cx = (width/2).toFloat()
            cy = (height/2).toFloat()
            duration = 3000
            this.interpolator = OvershootInterpolator()
        }

        override fun applyTransformation (interpolatedTime: Float, t: Transformation)
        {
            val cam = Camera()
            val iTime = 2 * interpolatedTime
            cam.rotate(iTime, 0f, 0f)
            val matrix = t.matrix
            cam.getMatrix(matrix)
            matrix.preTranslate(-cx, -cy)
            matrix.postTranslate(cx, cy)
        }
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
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.salon_main, menu)
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
 //               val dialogFrag = CreateFabFragment.newInstance()
//                dialogFrag.setParentFab(fab_calendar)
//                dialogFrag.show((activity as AppCompatActivity).getSupportFragmentManager(), dialogFrag.getTag())
                return true
            }
            R.id.action_day_view -> {
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_DAY_VIEW
                    weekView.numberOfVisibleDays = 1

                    // Lets change some dimensions to best fit the view.
                    weekView.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()
                    weekView.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                    weekView.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                }
                return true
            }
            R.id.action_three_day_view -> {
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_THREE_DAY_VIEW
                    weekView.numberOfVisibleDays = 3

                    // Lets change some dimensions to best fit the view.
                    weekView.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()
                    weekView.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                    weekView.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                }
                return true
            }
            R.id.action_week_view -> {
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_WEEK_VIEW
                    weekView.numberOfVisibleDays = 7

                    // Lets change some dimensions to best fit the view.
                    weekView.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
                    weekView.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt()
                    weekView.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt()
                }
                return true
            }

            R.id.action_sign_out -> {

                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }






}

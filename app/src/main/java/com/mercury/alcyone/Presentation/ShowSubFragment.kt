package com.mercury.alcyone.Presentation


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.mercury.alcyone.Data.DataSources.ApiResult
import com.mercury.alcyone.Data.MySharedPreferencesManager
import com.mercury.alcyone.Presentation.TabLayout.AdapterTabLayout
import com.mercury.alcyone.Presentation.ViewModels.SecondSubGroupFragmentViewModel
import com.example.alcyone.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Locale.ENGLISH
import javax.inject.Inject


@AndroidEntryPoint
class ShowSubFragment: Fragment() {

    @Inject
    lateinit var sharedPreferencesManager: MySharedPreferencesManager


    private lateinit var alcyoneGroup: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var tvError: TextView
    private lateinit var selectData: String
    private lateinit var selectDataWeek: String
    private val viewModel: SecondSubGroupFragmentViewModel by activityViewModels()
    private lateinit var SingleRowCalendar: SingleRowCalendar
    private lateinit var btnLeft: ImageButton
    private lateinit var btnRight: ImageButton
    private lateinit var tvDate: TextView
    private lateinit var tvDay: TextView


    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_show_sub, container, false)

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]
        SingleRowCalendar = view.findViewById(R.id.main_single_row_calendar)

        alcyoneGroup = view.findViewById(R.id.alcyoneGroup)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager2 = view.findViewById(R.id.viewPager2)
        tvError = view.findViewById(R.id.tvError)
        btnLeft = view.findViewById(R.id.btn_left)
        btnRight = view.findViewById(R.id.btn_right)
        tvDate = view.findViewById(R.id.tvDate)
        tvDay = view.findViewById(R.id.tvDay)

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        btnRight.setOnClickListener {
            SingleRowCalendar.setDates(getDatesOfNextMonth())
        }
        btnLeft.setOnClickListener {
            SingleRowCalendar.setDates(getDatesOfPreviousMonth())
        }

        val adapter = AdapterTabLayout(this)
        viewPager2.adapter = adapter
        val tabName = arrayOf(getString(R.string.first_group), getString(R.string.second_group))
        TabLayoutMediator(tabLayout, viewPager2) {tab, position ->
            tab.text = tabName[position]
        }.attach()
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
                val selectedTabPosition = tab.position
                sharedPreferencesManager.saveIntData("TabPos", selectedTabPosition)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        val savedTabPosition = sharedPreferencesManager.getIntData("TabPos", 0)
        tabLayout.getTabAt(savedTabPosition)?.select()



        alcyoneGroup.text = getString(R.string.app_name) + ": " + getGroup()

        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                val cal = Calendar.getInstance()
                cal.time = date
                return if (isSelected)
                when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.MONDAY -> R.layout.calendar_item_selected
                    Calendar.WEDNESDAY -> R.layout.calendar_item_selected
                    Calendar.FRIDAY -> R.layout.calendar_item_selected
                    else -> R.layout.calendar_item_selected
                } else
                    when(cal[Calendar.DAY_OF_WEEK]) {
                        Calendar.MONDAY -> R.layout.calendar_item
                        Calendar.WEDNESDAY -> R.layout.calendar_item
                        Calendar.FRIDAY -> R.layout.calendar_item
                        else -> R.layout.calendar_item
                    }
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                holder.itemView.findViewById<TextView>(R.id.tv_date_calendar_item).text = DateUtils.getDayNumber(date)
                holder.itemView.findViewById<TextView>(R.id.tv_day_calendar_item).text = DateUtils.getDay3LettersName(date)
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                tvDate.text = ", ${DateUtils.getDayNumber(date)} ${DateUtils.getMonthName(date)}"
                tvDay.text = DateUtils.getDayName(date)
                selectData = getDayName(date).toUpperCase(Locale.ENGLISH)
                selectDataWeek = DateUtils.getNumberOfWeek(date)
                viewModel.filterData(selectData, selectDataWeek)
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                val cal = Calendar.getInstance()
                cal.time = date
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.SUNDAY -> true
                    else -> true
                }
            }
        }

        val singleRowCalendar = SingleRowCalendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
            select(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1)
        }
        observeData()
        return view


    }

    fun observeData() {
        lifecycleScope.launch {
            viewModel.filteredDataFlow
                .collect { data ->
                when(data) {
                    is ApiResult.Error -> {
                        Log.e("ApiResult", "Message${data.message}")
                    }
                    is ApiResult.Loading -> {
                        Log.e("ApiResult", "Loading")
                        tvError.text = getString(R.string.loading)
                    }
                    is ApiResult.Success -> {
                        val list = data.data
                        list.forEach{
                            Log.e("ApiResult", it.toString())
                        }
                        tvError.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun getDayName(date: Date): String =
        SimpleDateFormat("EEEE", ENGLISH).format(date)

    private fun getGroup(): String {
        val pos = sharedPreferencesManager.getPosition()
        val string = when(pos) {
            0 -> getString(R.string.test)
            1 -> getString(R.string.group3842)
            2 -> getString(R.string.group3832)
            else -> {
            }
        }
        return string.toString()
    }


    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++
        if (currentMonth == 12) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 //
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth--
        if (currentMonth == -1) {
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }


    private fun getDates(list: MutableList<Date>): List<Date> {
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }
}
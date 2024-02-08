package com.mercury.alcyone.Presentation


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val adapter = AdapterTabLayout(this)
        viewPager2.adapter = adapter
        val tabName = arrayOf(getString(R.string.first_group), getString(R.string.second_group))
        TabLayoutMediator(tabLayout, viewPager2) {tab, position ->
            tab.text = tabName[position]
        }.attach()
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
                val selectedTabPosition = tab?.position ?: 0
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
                return if (isSelected) {
                    R.layout.calendar_item_selected
                } else {
                    R.layout.calendar_item
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
            futureDaysCount = 10
            pastDaysCount = 3
            includeCurrentDate = true
            init()
            select(3)
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
            else -> {
            }
        }
        return string.toString()
    }
}
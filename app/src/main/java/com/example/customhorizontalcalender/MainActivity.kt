package com.example.customhorizontalcalender

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.customhorizontalcalender.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // all variable
    private val lastMonthInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    private val dates = mutableListOf<Date>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lastMonthInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar()

        binding.imgPrev.setOnClickListener {
            if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setUpCalendar()
                else  setUpCalendar(cal)
            }
        }

        binding.imgNext.setOnClickListener {
            if (cal.before(lastMonthInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setUpCalendar(cal)
            }
        }

    }

    private fun setUpCalendar(changeMonth: Calendar? = null) {

        // set current month
        binding.txtMonth.text= sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }

        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }

        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        // add all dates...

        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // set adapter

        val calenderAdapter= CalenderAdapter(this,dates,currentDate,changeMonth,object :CalenderAdapter.OnDateClickListener{
            override fun onClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
                selectedMonth = clickCalendar[Calendar.MONTH]
                selectedYear = clickCalendar[Calendar.YEAR]
                Log.d("xxx", "selectedDay: $selectedDay/${selectedMonth+1}/$selectedYear")
                Snackbar.make(binding.rootLayout,"$selectedDay/${selectedMonth+1}/$selectedYear",Snackbar.LENGTH_SHORT).show()

            }

        })

        binding.rvCalender.apply {
            setHasFixedSize(true)
            adapter= calenderAdapter
        }


        when {
            currentPosition > 2 -> binding.rvCalender.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> binding.rvCalender.scrollToPosition(currentPosition)
            else -> binding.rvCalender.scrollToPosition(currentPosition)
        }

    }
}
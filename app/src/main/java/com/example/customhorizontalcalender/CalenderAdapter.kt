package com.example.customhorizontalcalender

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.customhorizontalcalender.databinding.RowCalenderBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalenderAdapter(
    private val context: Context,
    private val dateList: MutableList<Date>,
    private val currentDate: Calendar,
    private val changeMonth: Calendar?,
    private val listener: OnDateClickListener
) : RecyclerView.Adapter<CalenderAdapter.CalenderViewHolder>() {

    private var index = -1
    private var selectCurrentDate = true
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]

    private val selectedDay =
        when {
            changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            else -> currentDay
        }
    private val selectedMonth =
        when {
            changeMonth != null -> changeMonth[Calendar.MONTH]
            else -> currentMonth
        }
    private val selectedYear =
        when {
            changeMonth != null -> changeMonth[Calendar.YEAR]
            else -> currentYear
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CalenderViewHolder {
        return CalenderViewHolder(RowCalenderBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }

    override fun getItemCount(): Int {
        return if (dateList.isNullOrEmpty()) 0 else dateList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CalenderViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.time = dateList[position]

        val displayMonth = cal[Calendar.MONTH]
        val displayYear= cal[Calendar.YEAR]
        val displayDay = cal[Calendar.DAY_OF_MONTH]

        try {
            val dayInWeek = sdf.parse(cal.time.toString())
            sdf.applyPattern("EEE")
            holder.binding.txtDay.text = sdf.format(dayInWeek).toString()
        } catch (ex: ParseException) {
            Log.v("Exception", ex.localizedMessage!!)
        }

        holder.binding.txtDate.text = cal[Calendar.DAY_OF_MONTH].toString()

        if(displayYear>= currentYear){

            if(displayMonth >= currentMonth || displayYear > currentYear){

                if(displayDay >= currentDay || displayMonth > currentMonth || displayYear > currentYear){

                    holder.binding.layoutRoot.setOnClickListener {
                        index= position
                        selectCurrentDate = false
                        listener.onClick(position)
                        notifyDataSetChanged()
                    }

                    if (index == position) setItemSelect(holder)
                    else {
                        if (displayDay == selectedDay
                            && displayMonth == selectedMonth
                            && displayYear == selectedYear
                            && selectCurrentDate)
                            setItemSelect(holder)
                        else
                            setItemDefault(holder)
                    }

                } else setItemDisable(holder)

            }else setItemDisable(holder)

        }else setItemDisable(holder)


    }

    class CalenderViewHolder(val binding: RowCalenderBinding) : RecyclerView.ViewHolder(binding.root)

    private fun setItemDisable(holder: CalenderViewHolder){
        holder.binding.apply {
            txtDay.setTextColor(ContextCompat.getColor(context,R.color.txt_disable_color))
            txtDate.background= ContextCompat.getDrawable(context,R.drawable.bg_calender_white)
            txtDate.setTextColor(ContextCompat.getColor(context,R.color.txt_disable_color))
            layoutRoot.isEnabled= false
        }
    }

    private fun setItemDefault(holder: CalenderViewHolder){
        holder.binding.apply {
            txtDay.setTextColor(ContextCompat.getColor(context,R.color.black))
            txtDate.background= ContextCompat.getDrawable(context,R.drawable.bg_calender_white)
            txtDate.setTextColor(ContextCompat.getColor(context,R.color.black))
            layoutRoot.isEnabled= true
        }
    }

    private fun setItemSelect(holder: CalenderViewHolder){
        holder.binding.apply {
            txtDay.setTextColor(ContextCompat.getColor(context,R.color.black))
            txtDate.background= ContextCompat.getDrawable(context,R.drawable.bg_calender)
            txtDate.setTextColor(ContextCompat.getColor(context,R.color.white))
            layoutRoot.isEnabled= true
        }
    }

    interface OnDateClickListener {
        fun onClick(position: Int)
    }

}
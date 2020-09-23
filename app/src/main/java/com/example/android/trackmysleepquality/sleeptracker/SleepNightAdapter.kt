package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.SleepNightItemBinding


private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}

class OnSleepClickListener(private val clickListener: (Long) -> Unit) {
    fun onClick(nightId: Long) = clickListener(nightId)
}

class SleepNightAdapter(private val onSleepClickListener: OnSleepClickListener) : ListAdapter<SleepNight, SleepNightAdapter.SleepNightHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightHolder {
        return SleepNightHolder.createInstance(parent, onSleepClickListener)

    }

    override fun onBindViewHolder(holder: SleepNightHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class SleepNightHolder private constructor(private val binding: SleepNightItemBinding, private val onSleepClickListener: OnSleepClickListener) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var sleepNight: SleepNight

        companion object {
            fun createInstance(parent: ViewGroup, onSleepClickListener: OnSleepClickListener): SleepNightHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = SleepNightItemBinding.inflate(inflater, parent, false)

                return SleepNightHolder(binding, onSleepClickListener)
            }
        }

        init {
            binding.root.setOnClickListener {
                onSleepClickListener.onClick(sleepNight.nightId)
            }
        }

        fun bind(item: SleepNight) {
            val res = itemView.context.resources        //itemView == binding.root
            sleepNight = item
            binding.sleepLength.text = convertDurationToFormatted(sleepNight.startTimeMilli, sleepNight.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(sleepNight.sleepQuality, res)
            binding.qualityImage.setImageResource(when (sleepNight.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_launcher_sleep_tracker_foreground
            })
        }

    }
}

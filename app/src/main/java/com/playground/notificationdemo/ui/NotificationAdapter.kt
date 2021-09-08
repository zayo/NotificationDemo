package com.playground.notificationdemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playground.notificationdemo.R
import com.playground.notificationdemo.db.NotificationItem
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(
    private val clickListener: (NotificationItem) -> Unit
) : ListAdapter<NotificationItem, NotificationAdapter.Holder>(diff) {

    private val clickPositionListener : (Int) -> Unit = { position -> clickListener(getItem(position))}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        LayoutInflater.from(parent.context).run {
            Holder(inflate(R.layout.notification_item, parent, false), clickPositionListener)
        }

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    class Holder(private val view: View, clickPositionListener: (Int) -> Unit) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                clickPositionListener(adapterPosition)
            }
        }

        private val time by lazy { view.findViewById<TextView>(R.id.time)}
        private val title by lazy { view.findViewById<TextView>(R.id.title)}
        private val content by lazy { view.findViewById<TextView>(R.id.content)}

        fun bind(item: NotificationItem) {
            time.text = format.format(item.date)
            title.text = item.title
            content.text = item.content
        }
    }

    companion object {

        val format = SimpleDateFormat("dd MMM\nHH:mm", Locale.ENGLISH)
        val diff = object : DiffUtil.ItemCallback<NotificationItem>() {
            override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean =
                oldItem == newItem
        }
    }
}
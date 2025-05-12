package com.example.mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.remote.dtos.Notification.NotificationResponse

class NotificationAdapter(
    private val notifications: List<NotificationResponse>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)

        val typeColor = when (notification.type.uppercase()) {
            "ACCOUNTANT" -> R.color.accountant_color
            "DEPARTMENT" -> R.color.department_color
            "EMPLOYEE" -> R.color.employee_color
            "ALL" -> R.color.all_color
            else -> R.color.default_color
        }

        holder.setTypeColor(typeColor)

        if (position == itemCount - 1) {
            holder.setTimelineVisibility(false)
        } else {
            holder.setTimelineVisibility(true)
        }
    }

    override fun getItemCount() = notifications.size

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeText: TextView = itemView.findViewById(R.id.notificationType)
        private val messageText: TextView = itemView.findViewById(R.id.notificationMessage)
        private val dateText: TextView = itemView.findViewById(R.id.notificationDate)
        private val timelineIndicator: View = itemView.findViewById(R.id.timeline_indicator)

        fun bind(notification: NotificationResponse) {
            typeText.text = notification.type
            messageText.text = notification.message
            dateText.text = notification.createdAt
        }

        fun setTypeColor(color: Int) {
            val resolvedColor = itemView.context.getColor(color)
            typeText.setTextColor(resolvedColor)
        }

        fun setTimelineVisibility(visible: Boolean) {
            timelineIndicator.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }
}
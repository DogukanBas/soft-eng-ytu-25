package com.example.mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.models.ApprovalHistoryItem
import java.time.format.DateTimeFormatter

class ApprovalHistoryAdapter(
    private val historyItems: List<ApprovalHistoryItem>
) : RecyclerView.Adapter<ApprovalHistoryAdapter.ApprovalHistoryViewHolder>() {

    class ApprovalHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusTextView: TextView = itemView.findViewById(R.id.history_status)
        val dateTextView: TextView = itemView.findViewById(R.id.history_date)
        val actorTextView: TextView = itemView.findViewById(R.id.history_actor)
        val descriptionTextView: TextView = itemView.findViewById(R.id.history_description)
        val descriptionContainer: LinearLayout = itemView.findViewById(R.id.description_container)
        val timelineIndicator: View = itemView.findViewById(R.id.timeline_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApprovalHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_approval_history, parent, false)
        return ApprovalHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApprovalHistoryViewHolder, position: Int) {
        val historyItem = historyItems[position]
        
        // Set status text and color based on status
        holder.statusTextView.text = historyItem.status
        setStatusTextColor(holder.statusTextView, historyItem.status)
        
        // Format date as yyyy-MM-dd
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        holder.dateTextView.text = historyItem.date.format(formatter)
        
        // Set actor info
        holder.actorTextView.text = "${historyItem.actorId} (${historyItem.actorRole})"
        
        // Set description if available, hide container if not
        if (historyItem.description.isNullOrBlank()) {
            holder.descriptionContainer.visibility = View.GONE
        } else {
            holder.descriptionContainer.visibility = View.VISIBLE
            holder.descriptionTextView.text = historyItem.description
        }
        
        // Show timeline connector for all except last item
        holder.timelineIndicator.visibility = 
            if (position == historyItems.size - 1) View.GONE else View.VISIBLE
    }

    override fun getItemCount() = historyItems.size

    private fun setStatusTextColor(textView: TextView, status: String) {
        val context = textView.context
        val color = when {
            status.contains("APPROVED") || status.contains("SENT_TO_ACCOUNTANT") -> 
                android.R.color.holo_green_dark
            status.contains("REJECTED") || status.contains("CANCELED") -> 
                android.R.color.holo_red_dark
            else -> android.R.color.black
        }
        textView.setTextColor(context.resources.getColor(color, context.theme))
    }
}
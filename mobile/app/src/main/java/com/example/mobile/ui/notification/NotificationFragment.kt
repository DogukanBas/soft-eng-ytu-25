package com.example.mobile.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.adapters.NotificationAdapter
import com.example.mobile.remote.dtos.Notification.NotificationResponse
import com.example.mobile.ui.BaseFragment

class NotificationFragment (
    private val notifications : List<NotificationResponse>
) : BaseFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.notificationsRecyclerView)
        adapter = NotificationAdapter(notifications)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@NotificationFragment.adapter
        }
    }
}
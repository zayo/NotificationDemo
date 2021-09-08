package com.playground.notificationdemo.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.playground.notificationdemo.R
import com.playground.notificationdemo.db.NotificationDao
import com.playground.notificationdemo.db.NotificationItem
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val data: LiveData<List<NotificationItem>> by inject()

    private val dao: NotificationDao by inject()

    private var content: View? = null
    private var empty: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val adapter = NotificationAdapter { notification ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", notification.id)
            startActivity(intent)
        }
        recycler.adapter = adapter
        content = recycler
        empty = findViewById<View>(R.id.permission).apply {
            setOnClickListener {
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").apply {
                    Toast.makeText(this@MainActivity,
                        "Click to $title and give permission. Then navigate back.", Toast.LENGTH_LONG).show()
                    startActivity(this)
                }
            }
        }

        data.observe(this) {
            adapter.submitList(it)
            invalidateOptionsMenu()
        }

        findViewById<Button>(R.id.generate).setOnClickListener {
            createCustomNotification()
        }
    }

    override fun onResume() {
        super.onResume()
        val has = Settings.Secure.getString(contentResolver, "enabled_notification_listeners").contains(packageName)
        empty?.isVisible = !has
        content?.isVisible = has
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean =
        true.also { menuInflater.inflate(R.menu.menu_main, menu) }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.delete_all).isVisible = !data.value.isNullOrEmpty()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            lifecycle.coroutineScope.launch {
                dao.deleteAll()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createCustomNotification() {
        // Get the layouts to use in the custom notification
        val notificationLayout = RemoteViews(packageName, R.layout.notification_small)
        val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_large)

        val channelId = "channel_lorem"
        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(500)
            }
        } else {
            null
        }

        // Apply the layouts to the notification
        val customNotification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)
            .build()

        getSystemService<NotificationManager>()!!.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(requireNotNull(notificationChannel))
            }
            notify(12341, customNotification)
        }
    }
}
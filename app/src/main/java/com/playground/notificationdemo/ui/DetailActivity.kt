package com.playground.notificationdemo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.playground.notificationdemo.R
import com.playground.notificationdemo.db.NotificationDao
import com.playground.notificationdemo.db.NotificationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity() {

    private val dao: NotificationDao by inject()

    private val notification: NotificationItem by lazy {
        requireNotNull(dao.get(intent.getLongExtra("id", -1L)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(findViewById(R.id.toolbar))

        if (!intent.hasExtra("id")) {
            Toast.makeText(this, "Notification not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycle.coroutineScope.launch {
            findViewById<TextView>(R.id.content_text).text = withContext(Dispatchers.Default) { notification.format }
        }

        title = "Detail"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean =
        true.also { menuInflater.inflate(R.menu.menu_detail, menu) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            lifecycle.coroutineScope.launch {
                withContext(Dispatchers.Default) {
                    dao.delete(notification)
                }
                finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private val NotificationItem.format
        inline get() = "$date\n\n$title\n\n$content\n\n$raw"
}
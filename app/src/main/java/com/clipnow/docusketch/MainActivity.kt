package com.clipnow.docusketch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.clipnow.feature.drying_area_view.RoomView
import com.clipnow.feature.drying_area_view.mock.RoomRepo
import com.clipnow.feature.drying_area_view.renderer.data.RoomDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val roomMapper = RoomDataMapper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showRoomData()
    }

    private fun showRoomData() = lifecycleScope.launch(Dispatchers.Default){
        val result = RoomRepo.getRooms()
        val room = result.last()
        val roomData = roomMapper.mapRoom(room)

        findViewById<RoomView>(R.id.room_view)?.setRoomData(roomData)
    }
}
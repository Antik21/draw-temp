package com.clipnow.docusketch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.clipnow.feature.sketch_view.renderer.view.SketchView
import com.clipnow.feature.sketch_view.mock.RoomRepo
import com.clipnow.feature.sketch_view.renderer.compose.SketchComposeView
import com.clipnow.feature.sketch_view.renderer.data.RoomDataMapper
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

    private fun showRoomData() = lifecycleScope.launch(Dispatchers.Default) {
        val result = RoomRepo.getRooms()
        val room = result.first()
        val roomData = roomMapper.mapRoom(room)

        launch(Dispatchers.Main) {
            findViewById<SketchView>(R.id.room_view)?.setRoomData(roomData)
        }
        /*launch(Dispatchers.Main) {
            findViewById<ComposeView>(R.id.room_view)?.setContent {
                SketchComposeView(roomData = roomData)
            }
        }*/
    }
}
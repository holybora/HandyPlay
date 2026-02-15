package com.sls.handbook.recording

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sls.handbook.ui.HandyPlayApp
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.time.Instant

@AndroidEntryPoint
class RecordingActivity : ComponentActivity() {

    private val controller = RecordingController()

    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            controller.recordBackPress()
            // Temporarily disable to let the actual back press propagate
            isEnabled = false
            onBackPressedDispatcher.onBackPressed()
            isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        onBackPressedDispatcher.addCallback(this, backPressCallback)

        setContent {
            RecordingOverlay(
                controller = controller,
                onStopRecording = ::saveAndFinish,
            ) {
                HandyPlayApp()
            }
        }
    }

    private fun saveAndFinish() {
        val metrics = resources.displayMetrics
        val session = RecordingSession(
            metadata = RecordingMetadata(
                appPackage = packageName,
                deviceModel = Build.MODEL,
                screenWidth = metrics.widthPixels,
                screenHeight = metrics.heightPixels,
                density = metrics.density,
                recordedAt = Instant.now().toString(),
            ),
            events = controller.getEvents(),
        )

        val json = session.toJson().toString(2)
        val outputFile = File(filesDir, "e2e_recording.json")
        outputFile.writeText(json)

        Log.i(LOG_TAG, "RECORDING_COMPLETE:${outputFile.absolutePath}")
        Log.i(LOG_TAG, "Events recorded: ${session.events.size}")

        finish()
    }

    companion object {
        private const val LOG_TAG = "E2E_RECORDING"
    }
}

package com.sls.handbook.recording

import org.json.JSONArray
import org.json.JSONObject

data class RecordedEvent(
    val type: String,
    val x: Float? = null,
    val y: Float? = null,
    val startX: Float? = null,
    val startY: Float? = null,
    val endX: Float? = null,
    val endY: Float? = null,
    val durationMs: Long? = null,
    val text: String? = null,
    val timestampMs: Long,
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("type", type)
        x?.let { put("x", it.toDouble()) }
        y?.let { put("y", it.toDouble()) }
        startX?.let { put("startX", it.toDouble()) }
        startY?.let { put("startY", it.toDouble()) }
        endX?.let { put("endX", it.toDouble()) }
        endY?.let { put("endY", it.toDouble()) }
        durationMs?.let { put("durationMs", it) }
        text?.let { put("text", it) }
        put("timestampMs", timestampMs)
    }

    companion object {
        const val TYPE_TAP = "TAP"
        const val TYPE_SWIPE = "SWIPE"
        const val TYPE_BACK_PRESS = "BACK_PRESS"
        const val TYPE_TEXT_INPUT = "TEXT_INPUT"

        fun tap(x: Float, y: Float, timestampMs: Long) = RecordedEvent(
            type = TYPE_TAP,
            x = x,
            y = y,
            timestampMs = timestampMs,
        )

        fun swipe(
            startX: Float,
            startY: Float,
            endX: Float,
            endY: Float,
            durationMs: Long,
            timestampMs: Long,
        ) = RecordedEvent(
            type = TYPE_SWIPE,
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            durationMs = durationMs,
            timestampMs = timestampMs,
        )

        fun backPress(timestampMs: Long) = RecordedEvent(
            type = TYPE_BACK_PRESS,
            timestampMs = timestampMs,
        )

        fun textInput(text: String, timestampMs: Long) = RecordedEvent(
            type = TYPE_TEXT_INPUT,
            text = text,
            timestampMs = timestampMs,
        )
    }
}

data class RecordingMetadata(
    val appPackage: String,
    val deviceModel: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val density: Float,
    val recordedAt: String,
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("appPackage", appPackage)
        put("deviceModel", deviceModel)
        put("screenWidth", screenWidth)
        put("screenHeight", screenHeight)
        put("density", density.toDouble())
        put("recordedAt", recordedAt)
    }
}

data class RecordingSession(
    val metadata: RecordingMetadata,
    val events: List<RecordedEvent>,
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("metadata", metadata.toJson())
        put("events", JSONArray().apply {
            events.forEach { put(it.toJson()) }
        })
    }
}

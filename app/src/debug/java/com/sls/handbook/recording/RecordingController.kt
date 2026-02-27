package com.sls.handbook.recording

class RecordingController {

    private val events = mutableListOf<RecordedEvent>()
    private val startTimeMs = System.currentTimeMillis()

    @Synchronized
    fun addEvent(event: RecordedEvent) {
        events.add(event)
    }

    fun recordTap(x: Float, y: Float) {
        addEvent(RecordedEvent.tap(x, y, elapsedMs()))
    }

    fun recordSwipe(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        durationMs: Long,
    ) {
        addEvent(RecordedEvent.swipe(startX, startY, endX, endY, durationMs, elapsedMs()))
    }

    fun recordBackPress() {
        addEvent(RecordedEvent.backPress(elapsedMs()))
    }

    fun recordTextInput(text: String) {
        addEvent(RecordedEvent.textInput(text, elapsedMs()))
    }

    @Synchronized
    fun getEvents(): List<RecordedEvent> = events.toList()

    private fun elapsedMs(): Long = System.currentTimeMillis() - startTimeMs
}

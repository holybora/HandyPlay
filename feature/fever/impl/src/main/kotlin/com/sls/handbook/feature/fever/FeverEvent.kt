package com.sls.handbook.feature.fever

sealed interface FeverEvent {
    data object Refresh : FeverEvent
}

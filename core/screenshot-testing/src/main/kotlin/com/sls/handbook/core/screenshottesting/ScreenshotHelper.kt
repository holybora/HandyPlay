package com.sls.handbook.core.screenshottesting

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage

val DefaultRoborazziOptions = RoborazziOptions(
    compareOptions = RoborazziOptions.CompareOptions(
        changeThreshold = 0.01f,
    ),
    recordOptions = RoborazziOptions.RecordOptions(
        resizeScale = 0.5,
    ),
)

fun ComposeContentTestRule.captureScreenshot(
    screenshotName: String,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    content: @Composable () -> Unit,
) {
    setContent(content)
    onRoot().captureRoboImage(
        filePath = "src/test/screenshots/$screenshotName.png",
        roborazziOptions = roborazziOptions,
    )
}

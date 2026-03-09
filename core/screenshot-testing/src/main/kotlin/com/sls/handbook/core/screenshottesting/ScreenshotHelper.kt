package com.sls.handbook.core.screenshottesting

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage

fun defaultRoborazziOptions() = RoborazziOptions(
    compareOptions = RoborazziOptions.CompareOptions(
        changeThreshold = 0.001f,
    ),
    recordOptions = RoborazziOptions.RecordOptions(
        resizeScale = 0.5,
    ),
)

fun ComposeContentTestRule.captureScreenshot(
    screenshotName: String,
    roborazziOptions: RoborazziOptions = defaultRoborazziOptions(),
    content: @Composable () -> Unit,
) {
    setContent(content)
    onRoot().captureRoboImage(
        filePath = "src/test/screenshots/$screenshotName.png",
        roborazziOptions = roborazziOptions,
    )
}

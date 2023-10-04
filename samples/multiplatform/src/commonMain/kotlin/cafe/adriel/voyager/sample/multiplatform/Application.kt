package cafe.adriel.voyager.sample.multiplatform

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
public fun SampleApplication() {
    Navigator(
        screen = BasicNavigationScreen(index = 0),
        // TODO Set to null again
        onBackPressed = {
            true
        }
    )
}

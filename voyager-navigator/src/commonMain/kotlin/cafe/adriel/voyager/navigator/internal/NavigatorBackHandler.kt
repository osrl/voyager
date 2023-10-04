package cafe.adriel.voyager.navigator.internal

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.OnBackPressed

@Composable
internal expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)

/*
    Current behavior:
        * onBackPressed = null
            * :check: stack size = 0: default platform behavior
            * :x: stack size > 0: default platform behavior
              Pop backstack instead
              "closes app" is instead the default platform behavior,
               BackHandler is not being handled properly
        * onBackPressed = { false }
            * :x: stack size = 0: closes app
              Back press being supplied means that dev will take care of that.
              According to docs "return `false` won't pop the current screen" is actually not true
            * :check: stack size > 0: does nothing
        * onBackPressed = { true }
            * :check: stack size = 0: closes app
            * :check: stack size > 0: pop

    New behavior:
        * onBackPressed = null
            * :check: stack size = 0: default platform behavior
            * :check: stack size > 0: pop
        * onBackPressed = { false }
            * :x: stack size = 0: closes app
              Back press being supplied means that dev will take care of that.
              According to docs "return `false` won't pop the current screen" is actually not true
            * :check: stack size > 0: does nothing
        * onBackPressed = { true }
            * :check: stack size = 0: closes app
            * :check: stack size > 0: pop
*/
@Composable
internal fun NavigatorBackHandler(
    navigator: Navigator,
    onBackPressed: OnBackPressed
) {
    if (onBackPressed == null) {
        BackHandler(
            enabled = navigator.size > 1,
            onBack = navigator::popRecursively
        )
        return
    }

    BackHandler(
        enabled = navigator.canPop || navigator.parent?.canPop ?: false,
        onBack = {
            if (onBackPressed(navigator.lastItem))
                navigator.popRecursively()
        }
    )
}

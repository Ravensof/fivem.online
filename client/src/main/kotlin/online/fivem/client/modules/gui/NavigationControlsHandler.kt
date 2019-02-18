package online.fivem.client.modules.gui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import online.fivem.client.modules.basics.ControlHandlerModule
import online.fivem.common.extensions.repeatJob
import online.fivem.common.gtav.NativeControls
import kotlin.coroutines.CoroutineContext

class NavigationControlsHandler(
	override val coroutineContext: CoroutineContext,
	controlHandlerModule: ControlHandlerModule,
	private val listener: Listener

) : ControlHandlerModule.Listener, CoroutineScope {

	private var repeatTask: Job? = null

	init {
		controlHandlerModule.addListener(this)
	}

	override val registeredKeys: List<NativeControls.Keys> = listOf(
		NativeControls.Keys.CELLPHONE_UP,
		NativeControls.Keys.CELLPHONE_DOWN,
		NativeControls.Keys.CELLPHONE_LEFT,
		NativeControls.Keys.CELLPHONE_RIGHT,

		NativeControls.Keys.CELLPHONE_SELECT, //LEFT MOUSE | ENTER
		NativeControls.Keys.CELLPHONE_CANCEL,//RIGHT MOUSE| BACKSPACE

		NativeControls.Keys.CELLPHONE_OPTION, // DEL
		NativeControls.Keys.CELLPHONE_EXTRA_OPTION, // MIDDLE MOUSE

		NativeControls.Keys.CELLPHONE_SCROLL_FORWARD, //MOUSE SCROLL DOWN
		NativeControls.Keys.CELLPHONE_SCROLL_BACKWARD, //MOUSE SCROLL UP

		NativeControls.Keys.CELLPHONE_UP, //onKeyJustPressed) { keyUp = setInterval(KEY_INTERVAL_CLICK) { onKeyUp() } }
		NativeControls.Keys.CELLPHONE_UP, //onKeyJustReleased) { clearInterval(keyUp) }

		NativeControls.Keys.CELLPHONE_DOWN, //onKeyJustPressed) { keyDown = setInterval(KEY_INTERVAL_CLICK) { onKeyDown() } }
		NativeControls.Keys.CELLPHONE_DOWN, //onKeyJustReleased) { clearInterval(keyDown) }

		NativeControls.Keys.CELLPHONE_LEFT, //onKeyJustPressed) { keyLeft = setInterval(KEY_INTERVAL_CLICK) { onKeyLeft() } }
		NativeControls.Keys.CELLPHONE_LEFT, //onKeyJustReleased) { clearInterval(keyLeft) }

		NativeControls.Keys.CELLPHONE_RIGHT, //onKeyJustPressed) { keyRight = setInterval(KEY_INTERVAL_CLICK) { onKeyRight() } }
		NativeControls.Keys.CELLPHONE_RIGHT, //onKeyJustReleased) { clearInterval(keyRight) }

		NativeControls.Keys.CELLPHONE_CANCEL,
		NativeControls.Keys.FRONTEND_PAUSE_ALTERNATE
	)

	override fun onJustPressed(control: NativeControls.Keys): Boolean {
		when (control) {
			NativeControls.Keys.CELLPHONE_UP -> repeat(listener::onKeyUp)
			NativeControls.Keys.CELLPHONE_DOWN -> repeat(listener::onKeyDown)
			NativeControls.Keys.CELLPHONE_LEFT -> repeat(listener::onKeyLeft)
			NativeControls.Keys.CELLPHONE_RIGHT -> repeat(listener::onKeyRight)

			else -> return super.onJustPressed(control)
		}

		return true
	}

	override fun onJustReleased(control: NativeControls.Keys): Boolean {
		when (control) {
			NativeControls.Keys.CELLPHONE_UP -> stopRepeat()
			NativeControls.Keys.CELLPHONE_DOWN -> stopRepeat()
			NativeControls.Keys.CELLPHONE_LEFT -> stopRepeat()
			NativeControls.Keys.CELLPHONE_RIGHT -> stopRepeat()

			else -> return super.onJustPressed(control)
		}

		return true
	}

	override fun onShortPressed(control: NativeControls.Keys): Boolean {
		when (control) {
			NativeControls.Keys.FRONTEND_PAUSE_ALTERNATE -> listener.onKeyExit()//??

//			NativeControls.Keys.CELLPHONE_UP -> listener.onKeyUp()
//			NativeControls.Keys.CELLPHONE_DOWN -> listener.onKeyDown()
//			NativeControls.Keys.CELLPHONE_LEFT -> listener.onKeyLeft()
//			NativeControls.Keys.CELLPHONE_RIGHT -> listener.onKeyRight()
			NativeControls.Keys.CELLPHONE_SELECT -> listener.onKeySelect()
			NativeControls.Keys.CELLPHONE_CANCEL -> listener.onKeyCancel()
			NativeControls.Keys.CELLPHONE_OPTION -> listener.onKeyOption()
			NativeControls.Keys.CELLPHONE_EXTRA_OPTION -> listener.onKeyExtraOption()
			NativeControls.Keys.CELLPHONE_SCROLL_FORWARD -> listener.onScrollDown()
			NativeControls.Keys.CELLPHONE_SCROLL_BACKWARD -> listener.onScrollUp()
			else ->
				return super.onShortPressed(control)
		}

		return true
	}

	override fun onLongPressed(control: NativeControls.Keys): Boolean {
		if (control == NativeControls.Keys.CELLPHONE_CANCEL) {
			listener.onKeyExit()
			return true
		}

		return super.onLongPressed(control)
	}

	override fun onFocus() {
		listener.onFocus()
	}

	override fun onFocusLost() {
		listener.onFocusLost()
	}

	private fun repeat(function: () -> Unit) {
		repeatTask?.cancel()
		repeatTask = repeatJob(KEY_INTERVAL_CLICK) { function() }
	}

	private fun stopRepeat() {
		repeatTask?.cancel()
	}

	interface Listener {
		fun onKeyUp() {}
		fun onKeyDown() {}
		fun onKeyLeft() {}
		fun onKeyRight() {}
		fun onKeySelect() {}
		fun onKeyCancel() {}
		fun onKeyOption() {}
		fun onKeyExtraOption() {}
		fun onScrollDown() {}
		fun onScrollUp() {}
		fun onKeyExit()

		fun onFocus()
		fun onFocusLost()
	}

	companion object {
		private const val KEY_INTERVAL_CLICK = 250L
	}
}
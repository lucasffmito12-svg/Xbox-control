package com.eeisso.gamepad

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class GamepadAccessibilityService : AccessibilityService() {
 override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
 override fun onInterrupt() {}
 fun tap(x:Float,y:Float) { val p=Path().apply { moveTo(x,y) }; val g=GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(p,0,70)).build(); dispatchGesture(g,null,null) }
 companion object { var instance:GamepadAccessibilityService?=null }
 override fun onServiceConnected() { super.onServiceConnected(); instance=this }
 override fun onUnbind(intent:android.content.Intent?):Boolean { instance=null; return super.onUnbind(intent) }
}

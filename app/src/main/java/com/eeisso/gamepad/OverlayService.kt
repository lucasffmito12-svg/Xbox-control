package com.eeisso.gamepad

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class OverlayService: Service() {
 private lateinit var wm:WindowManager; private val views= mutableListOf<android.view.View>()
 override fun onCreate(){ super.onCreate(); wm=getSystemService(WINDOW_SERVICE) as WindowManager
  val panel=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(8,8,8,8); setBackgroundColor(0x990d260d.toInt()) }
  val title=TextView(this).apply { text="XBOX • E É ISSO"; setTextColor(0xff7CFC00.toInt()); textSize=12f; gravity=Gravity.CENTER }
  panel.addView(title)
  val row=LinearLayout(this)
  listOf("Y","X","B","A").forEach { label -> row.addView(Button(this).apply { text=label; setOnClickListener { GamepadAccessibilityService.instance?.tap(0.82f*resources.displayMetrics.widthPixels,0.72f*resources.displayMetrics.heightPixels) } },LinearLayout.LayoutParams(0,52,1f)) }
  panel.addView(row)
  val move=Button(this).apply { text="▲   ◀   ●   ▶   ▼"; setOnClickListener { GamepadAccessibilityService.instance?.tap(0.18f*resources.displayMetrics.widthPixels,0.75f*resources.displayMetrics.heightPixels) } }; panel.addView(move)
  val type=if(Build.VERSION.SDK_INT>=26) LayoutParams.TYPE_APPLICATION_OVERLAY else LayoutParams.TYPE_PHONE
  val lp=LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,type,LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT).apply { gravity=Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL; y=45 }
  wm.addView(panel,lp); views.add(panel)
 }
 override fun onDestroy(){ views.forEach { try{wm.removeView(it)}catch(_:Exception){} }; views.clear(); super.onDestroy() }
 override fun onBind(intent:Intent?):IBinder?=null
}

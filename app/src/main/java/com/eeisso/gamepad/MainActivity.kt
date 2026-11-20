package com.eeisso.gamepad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
  val root=LinearLayout(this).apply { orientation=1; gravity=Gravity.CENTER; setPadding(28,40,28,40); setBackgroundColor(0xff151515.toInt()) }
  root.addView(TextView(this).apply { text="🎮  E é isso"; textSize=30f; setTextColor(0xff7CFC00.toInt()); gravity=Gravity.CENTER })
  root.addView(TextView(this).apply { text="Controle virtual flutuante"; textSize=16f; setTextColor(-1); gravity=Gravity.CENTER; setPadding(0,18,0,28) })
  root.addView(btn("PERMITIR SOBREPOSIÇÃO") { if(!Settings.canDrawOverlays(this)) startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) })
  root.addView(btn("ATIVAR ACESSIBILIDADE") { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) })
  root.addView(btn("SOBREPOR CONTROLE") { if(Settings.canDrawOverlays(this)) startService(Intent(this,OverlayService::class.java)) })
  root.addView(btn("ESCONDER CONTROLE") { stopService(Intent(this,OverlayService::class.java)) })
  setContentView(root)
 }
 private fun btn(label:String, action:()->Unit)=Button(this).apply { text=label; setOnClickListener { action() } }
}

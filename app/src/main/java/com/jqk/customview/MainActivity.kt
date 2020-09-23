package com.jqk.customview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jqk.customview.auxiliaryline.AuxiliaryLineActivity
import com.jqk.customview.drap.DrapActivity
import com.jqk.customview.watch.WatchActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.file.Watchable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auxiliaryLine.setOnClickListener {
            jumpActivity(AuxiliaryLineActivity().javaClass)
        }

        watch.setOnClickListener {
            jumpActivity(WatchActivity().javaClass)
        }

        drap.setOnClickListener {
            jumpActivity(DrapActivity().javaClass)
        }
    }

    fun jumpActivity(clazz: Class<*>) {
        var intent = Intent()
        intent.setClass(this, clazz)
        startActivity(intent)
    }
}
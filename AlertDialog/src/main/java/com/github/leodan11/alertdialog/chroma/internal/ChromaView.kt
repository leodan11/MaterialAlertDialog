package com.github.leodan11.alertdialog.chroma.internal

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.chroma.ColorMode

internal class ChromaView : RelativeLayout {

    companion object {
        const val DEFAULT_COLOR = Color.GRAY
        val DefaultModel = ColorMode.RGB
    }

    @ColorInt
    var currentColor: Int private set

    val colorMode: ColorMode

    constructor(context: Context) : this(DEFAULT_COLOR, DefaultModel, context)

    constructor(
        @ColorInt initialColor: Int,
        colorMode: ColorMode,
        context: Context
    ) : super(context) {
        this.currentColor = initialColor
        this.colorMode = colorMode
        init()
    }

    private fun init() {
        inflate(context, R.layout.m_chroma_view, this)
        clipToPadding = false

        val colorView = findViewById<View>(R.id.color_view)
        colorView.setBackgroundColor(currentColor)

        val channelViews = colorMode.channels.map { ChannelView(it, currentColor, context) }

        val seekbarChangeListener: () -> Unit = {
            currentColor = colorMode.evaluateColor(channelViews.map { it.channel })
            colorView.setBackgroundColor(currentColor)
        }

        val channelContainer: ViewGroup = findViewById(R.id.channel_container)
        channelViews.forEach {
            channelContainer.addView(it)

            val layoutParams = it.layoutParams as LinearLayout.LayoutParams
            layoutParams.topMargin =
                resources.getDimensionPixelSize(R.dimen.channel_view_margin_top)
            layoutParams.bottomMargin =
                resources.getDimensionPixelSize(R.dimen.channel_view_margin_bottom)

            it.registerListener(seekbarChangeListener)
        }
    }

    internal interface ButtonBarListener {
        fun onPositiveButtonClick(color: Int)
        fun onNegativeButtonClick()
    }

    internal fun enableButtonBar(listener: ButtonBarListener?) {
        with(findViewById<View>(R.id.button_bar)) {
            val positiveButton = findViewById<Button>(R.id.positive_button)
            val negativeButton = findViewById<Button>(R.id.negative_button)

            if (listener != null) {
                visibility = VISIBLE
                positiveButton.setOnClickListener { listener.onPositiveButtonClick(currentColor) }
                negativeButton.setOnClickListener { listener.onNegativeButtonClick() }
            } else {
                visibility = GONE
                positiveButton.setOnClickListener(null)
                negativeButton.setOnClickListener(null)
            }
        }
    }

}

package com.github.leodan11.alertdialog.chroma

import android.graphics.Color
import com.github.leodan11.alertdialog.R

enum class ColorMode {

    ARGB {
        override val channels: List<Channel> = listOf(
            Channel(R.string.channel_alpha, 0, 255, Color::alpha),
            Channel(R.string.channel_red, 0, 255, Color::red),
            Channel(R.string.channel_green, 0, 255, Color::green),
            Channel(R.string.channel_blue, 0, 255, Color::blue)
        )

        override fun evaluateColor(channels: List<Channel>): Int = Color.argb(
            channels[0].progress, channels[1].progress, channels[2].progress, channels[3].progress
        )
    },

    RGB {
        override val channels: List<Channel> = ARGB.channels.drop(1)

        override fun evaluateColor(channels: List<Channel>): Int = Color.rgb(
            channels[0].progress, channels[1].progress, channels[2].progress
        )
    },

    HSV {
        override val channels: List<Channel> = listOf(
            Channel(R.string.channel_hue, 0, 360, ::hue),
            Channel(R.string.channel_saturation, 0, 100, ::saturation),
            Channel(R.string.channel_value, 0, 100, ::value)
        )

        override fun evaluateColor(channels: List<Channel>): Int = Color.HSVToColor(
            floatArrayOf(
                (channels[0].progress).toFloat(),
                (channels[1].progress / 100.0).toFloat(),
                (channels[2].progress / 100.0).toFloat()
            )
        )
    };

    internal abstract val channels: List<Channel>

    internal abstract fun evaluateColor(channels: List<Channel>): Int

    internal data class Channel(
        val nameResourceId: Int,
        val min: Int, val max: Int,
        val extractor: (color: Int) -> Int,
        var progress: Int = 0
    )

    companion object {
        @JvmStatic
        fun fromName(name: String) = entries.find { it.name == name } ?: RGB
    }

}
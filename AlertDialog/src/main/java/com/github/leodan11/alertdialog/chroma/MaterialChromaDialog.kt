package com.github.leodan11.alertdialog.chroma

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.DialogFragment
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.chroma.internal.ChromaView
import kotlin.properties.Delegates

class MaterialChromaDialog : DialogFragment() {
    companion object {
        private const val ARG_INITIAL_COLOR = "arg_initial_color"
        private const val ARG_COLOR_MODE_NAME = "arg_color_mode_name"

        @JvmStatic
        private fun newInstance(
            @ColorInt initialColor: Int,
            colorMode: ColorMode
        ): MaterialChromaDialog {
            val fragment = MaterialChromaDialog()
            fragment.arguments = makeArgs(initialColor, colorMode)
            return fragment
        }

        @JvmStatic
        private fun makeArgs(@ColorInt initialColor: Int, colorMode: ColorMode): Bundle {
            val args = Bundle()
            args.putInt(ARG_INITIAL_COLOR, initialColor)
            args.putString(ARG_COLOR_MODE_NAME, colorMode.name)
            return args
        }
    }

    class Builder {
        @ColorInt
        private var initialColor: Int = ChromaView.DEFAULT_COLOR
        private var colorMode: ColorMode = ChromaView.DefaultModel
        private var listener: ColorSelectListener? = null

        fun initialColor(@ColorInt initialColor: Int): Builder {
            this.initialColor = initialColor
            return this
        }

        fun colorMode(colorMode: ColorMode): Builder {
            this.colorMode = colorMode
            return this
        }

        fun onColorSelected(listener: ColorSelectListener): Builder {
            this.listener = listener
            return this
        }

        fun create(): MaterialChromaDialog {
            val fragment = newInstance(initialColor, colorMode)
            fragment.listener = listener
            return fragment
        }
    }

    private var listener: ColorSelectListener? = null
    private var chromaView: ChromaView by Delegates.notNull()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        chromaView = if (savedInstanceState == null) {
            ChromaView(
                arguments?.getInt(ARG_INITIAL_COLOR) ?: ChromaView.DEFAULT_COLOR,
                ColorMode.fromName(
                    arguments?.getString(ARG_COLOR_MODE_NAME) ?: ChromaView.DefaultModel.name
                ), requireContext()
            )
        } else {
            ChromaView(
                savedInstanceState.getInt(ARG_INITIAL_COLOR, ChromaView.DEFAULT_COLOR),
                ColorMode.fromName(
                    savedInstanceState.getString(ARG_COLOR_MODE_NAME)
                        ?: ChromaView.DefaultModel.name
                ), requireContext()
            )
        }

        chromaView.enableButtonBar(object : ChromaView.ButtonBarListener {
            override fun onNegativeButtonClick() = dismiss()
            override fun onPositiveButtonClick(color: Int) {
                listener?.onColorSelected(color)
                dismiss()
            }
        })

        return AlertDialog.Builder(context).setView(chromaView).create().apply {
            setOnShowListener {
                val width: Int
                val height: Int
                if (orientation(context) == ORIENTATION_LANDSCAPE) {
                    height = resources.getDimensionPixelSize(R.dimen.chroma_dialog_height)
                    width = 80 percentOf screenDimensions(context).widthPixels
                } else {
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    width = resources.getDimensionPixelSize(R.dimen.chroma_dialog_width)
                }
                window?.setLayout(width, height)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(makeArgs(chromaView.currentColor, chromaView.colorMode))
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener = null
    }

}
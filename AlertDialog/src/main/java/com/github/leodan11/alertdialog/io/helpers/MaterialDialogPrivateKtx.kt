package com.github.leodan11.alertdialog.io.helpers

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT_LANDSCAPE
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET_LANDSCAPE
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.Config.MAX_CHART_SEQUENCE_LENGTH
import com.github.leodan11.alertdialog.io.content.Config.MAX_CHART_SEQUENCE_LENGTH_TABLET
import com.github.leodan11.alertdialog.io.models.BoxCornerRadiusTextField
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.DetailsAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.IconInputDialog
import com.github.leodan11.alertdialog.io.models.IconTintAlert
import com.github.leodan11.alertdialog.io.models.InputCodeExtra
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.github.leodan11.customview.core.ReadMoreOption
import com.github.leodan11.k_extensions.color.colorOnSurface
import com.github.leodan11.k_extensions.color.colorPrimary
import com.github.leodan11.k_extensions.color.colorSurface
import com.github.leodan11.otptextview.OtpTextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale
import java.util.concurrent.TimeUnit

@RestrictTo(RestrictTo.Scope.LIBRARY)
fun Alert.State.toAlertDialog(mContext: Context, view: ImageView, defaultIcon: IconAlert) {
    when (this) {
        Alert.State.DELETE -> view.apply {
            setImageResource(R.drawable.ic_baseline_delete)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.ERROR -> view.apply {
            setImageResource(R.drawable.ic_baseline_error)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.HELP -> view.apply {
            setImageResource(R.drawable.ic_baseline_help)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.INFORMATION -> view.apply {
            setImageResource(R.drawable.ic_baseline_information)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.SUCCESS -> view.apply {
            setImageResource(R.drawable.ic_baseline_success)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.WARNING -> view.apply {
            setImageResource(R.drawable.ic_baseline_warning)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.WITHOUT_INTERNET -> view.apply {
            setImageResource(R.drawable.ic_baseline_cloud)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.WITHOUT_INTERNET_MOBILE -> view.apply {
            setImageResource(R.drawable.ic_baseline_mobile_alert)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        Alert.State.WITHOUT_INTERNET_WIFI -> view.apply {
            setImageResource(R.drawable.ic_baseline_wifi_alert)
            imageTintList = ColorStateList.valueOf(mContext.colorSurface())
        }

        else -> defaultIcon.let {
            it.drawable?.let { drawable -> view.setImageDrawable(drawable) }
            it.drawableResId?.let { id -> view.setImageResource(id) }
        }
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun ButtonCountDownTimer.toButtonView(view: MaterialButton): CountDownTimer {
    val buttonText = view.text
    val countTimer = object : CountDownTimer(millis, countInterval) {

        override fun onTick(millisUntilFinished: Long) {
            view.apply {
                isEnabled = false
                alpha = 0.5f
                text = String.format(
                    Locale.getDefault(),
                    this@toButtonView.format,
                    buttonText,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                )
            }
        }

        override fun onFinish() {
            view.apply {
                isEnabled = true
                alpha = 1f
                text = buttonText
            }
        }

    }
    return countTimer
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun ButtonCountDownTimer.toButtonView(view: TextView): CountDownTimer {
    val buttonText = view.text
    val countTimer = object : CountDownTimer(millis, countInterval) {

        override fun onTick(millisUntilFinished: Long) {
            view.apply {
                isEnabled = false
                alpha = 0.5f
                text = String.format(
                    Locale.getDefault(),
                    this@toButtonView.format,
                    buttonText,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                )
            }
        }

        override fun onFinish() {
            view.apply {
                isEnabled = true
                alpha = 1f
                text = buttonText
            }
        }

    }
    return countTimer
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun ButtonAlertDialog?.toButtonView(
    context: Context,
    view: MaterialButton,
    mTintColor: ColorStateList
) {
    view.isVisible = this != null
    this?.let {
        view.text = it.title
        view.setTextColor(mTintColor)
        if (it.iconAlert.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) {
            view.icon = ContextCompat.getDrawable(context, it.iconAlert.icon)
            view.iconTint = mTintColor
            view.toGravityButton(it.iconAlert.gravity)
        }
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun ButtonAlertDialog?.toButtonView(
    dialog: DialogAlertInterface,
    view: TextView,
    mTintColor: ColorStateList,
    which: DialogAlertInterface.UI
) {
    view.isVisible = this != null
    this?.let {
        view.text = it.title
        view.setTextColor(mTintColor)
        view.setOnClickListener { onClickListener?.onClick(dialog, which) }
    }
}


fun Context.isTablet(): Boolean {
    return (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun Context.toMessageAndDetailsViews(
    message: MessageAlert<*>?,
    messageView: TextView,
    details: DetailsAlert<*>?,
    detailsView: TextView,
    maxLength: Int,
    messageDetails: (message: CharSequence, details: CharSequence) -> Unit,
    onlyDetails: (details: CharSequence) -> Unit
) {
    messageView.isVisible = message != null
    message?.let {
        messageView.setTextColor(this.colorOnSurface())
        messageView.toAlignmentTextView(it.textAlignment)
    }
    details?.let {
        detailsView.setTextColor(this.colorOnSurface())
    }
    if (message != null && details != null) {
        if (message.getText().length > maxLength) {
            messageDetails.invoke(message.getText(), details.getText())
        } else {
            messageView.text = message.getText()
            onlyDetails.invoke(details.getText())
        }
        detailsView.isVisible = true
    } else if (message != null) {
        if (message.getText().length > maxLength) {
            messageDetails.invoke(
                this.getString(R.string.label_text_details_are_specified_below),
                message.getText()
            )
            detailsView.isVisible = true
        } else {
            messageView.text = message.getText()
            detailsView.isVisible = false
        }
    } else if (details != null) {
        onlyDetails.invoke(details.getText())
        detailsView.isVisible = true
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun Context.toInputSampleView(
    layout: TextInputLayout,
    hintText: String,
    helper: String?,
    errorText: String?,
    boxCorner: BoxCornerRadiusTextField?,
    counterMax: Int?,
    startIcon: IconInputDialog?,
    endIcon: IconInputDialog?
) {
    layout.apply {
        setColorList(this@toInputSampleView.colorPrimary())
        hint = hintText
        helper?.let { helperText = it }
        isHelperTextEnabled = !helper.isNullOrEmpty()
        errorText?.let { error = it }
        counterMax?.let { counterMaxLength = it }
        isCounterEnabled = counterMax != null
        isErrorEnabled = !errorText.isNullOrEmpty()
        endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        boxCorner?.let { boxCorner ->
            setBoxCornerRadii(
                boxCorner.topStart,
                boxCorner.topEnd,
                boxCorner.bottomStart,
                boxCorner.bottomEnd
            )
        }
        startIcon?.let {
            layout.apply {
                startIconDrawable = ContextCompat.getDrawable(this@toInputSampleView, it.icon)
                startIconContentDescription = it.contentDescription
                    ?: this@toInputSampleView.getString(it.contentDescriptionRes!!)
                setStartIconOnClickListener(it.listener)
            }
        }
        endIcon?.let {
            layout.apply {
                endIconMode = TextInputLayout.END_ICON_CUSTOM
                endIconDrawable = ContextCompat.getDrawable(this@toInputSampleView, it.icon)
                endIconContentDescription = it.contentDescription
                    ?: this@toInputSampleView.getString(it.contentDescriptionRes!!)
                setEndIconOnClickListener(it.listener)
            }
        }
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun DetailsAlert<*>?.toDetailsView(context: Context, view: TextView, maxLength: Int) {
    view.isVisible = this != null
    this?.let {
        view.setTextColor(context.colorOnSurface())
        val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(context)
            .textLength(maxLength)
            .textLengthType(ReadMoreOption.TYPE_CHARACTER)
            .moreLabelColor(context.colorPrimary())
            .lessLabelColor(context.colorPrimary())
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()
        readMoreOption.addReadMoreTo(view, it.getText())
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun DetailsAlert<*>?.toDetailsView(
    context: Context,
    view: NestedScrollView,
    textView: TextView, maxLength: Int
) {
    view.isVisible = this != null
    this?.let {
        textView.setTextColor(context.colorOnSurface())
        val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(context)
            .textLength(maxLength)
            .textLengthType(ReadMoreOption.TYPE_CHARACTER)
            .moreLabelColor(context.colorPrimary())
            .onClickMoreListener {
                val isTablet = context.isTablet()
                val maxCharacters =
                    if (isTablet) MAX_CHART_SEQUENCE_LENGTH_TABLET else MAX_CHART_SEQUENCE_LENGTH
                val orientation = context.resources.configuration.orientation
                view.layoutParams.height =
                    if (this.getText().length >= maxCharacters) {
                        if (isTablet) {
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET_LANDSCAPE else DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET
                        } else {
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) DEFAULT_LAYOUT_PARAMS_HEIGHT_LANDSCAPE else DEFAULT_LAYOUT_PARAMS_HEIGHT
                        }
                    } else ViewGroup.LayoutParams.WRAP_CONTENT
            }
            .lessLabelColor(context.colorPrimary())
            .onClickLessListener {
                view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()
        readMoreOption.addReadMoreTo(textView, it.getText())
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun InputCodeExtra?.toInputEditText(context: Context, layout: TextInputLayout, editText: EditText) {
    layout.isVisible = this != null
    this?.let {
        layout.hint = textHide
        textHelper?.let { textHelper ->
            layout.helperText = textHelper
            layout.isHelperTextEnabled = true
        }
        textHelperRes?.let { textHelperRes ->
            layout.helperText = context.getString(textHelperRes)
            layout.isHelperTextEnabled = true
        }
        textDefaultValue?.let { textDefaultValue ->
            editText.setText(textDefaultValue)
        }
        editText.isEnabled = enabled
        editText.requestFocus()
        editText.inputType = this.inputType
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun IconAlert?.toImageView(view: ImageView, tint: IconTintAlert? = null) {
    view.isVisible = this != null
    this?.let {
        it.drawableResId?.let { id -> view.setImageResource(id) }
        it.drawable?.let { drawable -> view.setImageDrawable(drawable) }
        tint?.toTintColor(view)
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun IconTintAlert.toTintColor(view: ImageView) {
    this.tintColorInt?.let { color ->
        view.setColorFilter(color)
    }
    this.tintColorRes?.let { color ->
        view.setColorFilter(ContextCompat.getColor(view.context, color))
    }
    this.tintColorStateList?.let { color ->
        view.imageTintList = color
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun OtpTextView.isValidOtp(): Boolean {
    if (otp.isNullOrEmpty()) {
        showError()
        return false
    } else if (otp != null) {
        val result = otp!!.length == 6
        if (result) showSuccess() else showError()
        return result
    }
    return false
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun TextInputLayout.setColorList(color: Int) {
    this.boxStrokeColor = color
    this.hintTextColor = ColorStateList.valueOf(color)
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun MessageAlert<*>?.toMessageView(view: TextView) {
    view.isVisible = this != null
    this?.let {
        view.text = it.getText()
        view.setTextColor(view.context.colorOnSurface())
        view.toAlignmentTextView(it.textAlignment)
    }
}


@RestrictTo(RestrictTo.Scope.LIBRARY)
fun TitleAlert?.toTitleView(view: TextView) {
    view.isVisible = this != null
    this?.let {
        view.text = it.title
        view.setTextColor(view.context.colorOnSurface())
        view.toAlignmentTextView(it.textAlignment)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && it.textAlignment == Alert.TextAlignment.JUSTIFY) {
            view.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
    }
}


internal fun MaterialButton.toGravityButton(gravity: Alert.IconGravity) {
    when (gravity) {
        Alert.IconGravity.END -> this.iconGravity = MaterialButton.ICON_GRAVITY_END
        Alert.IconGravity.TEXT_END -> this.iconGravity =
            MaterialButton.ICON_GRAVITY_TEXT_END

        Alert.IconGravity.TEXT_START -> this.iconGravity =
            MaterialButton.ICON_GRAVITY_TEXT_START

        Alert.IconGravity.TEXT_TOP -> this.iconGravity =
            MaterialButton.ICON_GRAVITY_TEXT_TOP

        Alert.IconGravity.TOP -> this.iconGravity = MaterialButton.ICON_GRAVITY_TOP
        else -> this.iconGravity = MaterialButton.ICON_GRAVITY_START
    }
}


internal fun TextView.toAlignmentTextView(alignment: Alert.TextAlignment) {
    when (alignment) {
        Alert.TextAlignment.CENTER -> this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        Alert.TextAlignment.END -> this.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        Alert.TextAlignment.INHERIT -> this.textAlignment = View.TEXT_ALIGNMENT_INHERIT
        else -> this.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }
}
package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.CountDownTimer
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AlignmentSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.github.leodan11.alertdialog.MaterialAlertDialogEvents
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogBinding
import com.github.leodan11.alertdialog.dist.base.AlertBuilder
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_CHART_SEQUENCE_LENGTH
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_CHART_SEQUENCE_LENGTH_DETAILS
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT_LANDSCAPE
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET_LANDSCAPE
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_RADIUS
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.Config.MAX_CHART_SEQUENCE_LENGTH
import com.github.leodan11.alertdialog.io.content.Config.MAX_CHART_SEQUENCE_LENGTH_TABLET
import com.github.leodan11.alertdialog.io.helpers.DisplayUtil
import com.github.leodan11.alertdialog.io.helpers.isTablet
import com.github.leodan11.alertdialog.io.helpers.toAlertDialog
import com.github.leodan11.alertdialog.io.helpers.toButtonView
import com.github.leodan11.alertdialog.io.helpers.toMessageAndDetailsViews
import com.github.leodan11.alertdialog.io.helpers.toTitleView
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.ButtonCountDownTimer
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert
import com.github.leodan11.alertdialog.io.models.DetailsAlert
import com.github.leodan11.alertdialog.io.models.IconAlert
import com.github.leodan11.alertdialog.io.models.MessageAlert
import com.github.leodan11.alertdialog.io.models.SpanLength
import com.github.leodan11.alertdialog.io.models.TitleAlert
import com.github.leodan11.customview.core.ReadMoreOption
import com.github.leodan11.k_extensions.color.colorError
import com.github.leodan11.k_extensions.color.colorPrimary
import com.github.leodan11.k_extensions.color.colorSecondary
import com.github.leodan11.k_extensions.context.createBitmap
import com.google.android.material.button.MaterialButton

abstract class EventsComponentBase(
    protected open var mContext: Context,
    protected open var icon: IconAlert,
    protected open var type: Alert.State,
    protected open var backgroundColorSpanInt: Int?,
    protected open var backgroundColorSpanResource: Int?,
    protected open var countDownTimer: ButtonCountDownTimer?,
    protected open var messageSpanLengthMax: Int,
    protected open var detailsSpanLengthMax: SpanLength,
    protected open var title: TitleAlert?,
    protected open var message: MessageAlert<*>?,
    protected open var details: DetailsAlert<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : DialogAlertInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var binding: MAlertDialogBinding
    private var mCountDownTimer: CountDownTimer? = null
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: DialogAlertInterface.OnDismissListener? = null
    protected open var mOnCancelListener: DialogAlertInterface.OnCancelListener? = null
    protected open var mOnShowListener: DialogAlertInterface.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        binding = MAlertDialogBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        try {
            with(binding) {
                // Set Icon
                type.toAlertDialog(mContext, imageViewIconAlertDialog, icon)
                // Set Icon BackgroundTint
                val triangleIv = ImageView(mContext)
                triangleIv.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    DisplayUtil.dp2px(mContext, 10f)
                )
                triangleIv.setImageBitmap(
                    createTriangle(
                        (DisplayUtil.getScreenSize(mContext).x * 0.7).toFloat(),
                        DisplayUtil.dp2px(mContext, 10f).toFloat()
                    )
                )
                topLayout.addView(triangleIv)
                val radius = DisplayUtil.dp2px(mContext, DEFAULT_RADIUS)
                val outerRadii = floatArrayOf(
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat(),
                    radius.toFloat(),
                    0f,
                    0f,
                    0f,
                    0f
                )
                val roundRectShape = RoundRectShape(outerRadii, null, null)
                val shapeDrawable = ShapeDrawable(roundRectShape)
                shapeDrawable.paint.style = Paint.Style.FILL
                shapeDrawable.paint.color = getColor()
                binding.llTop.background = shapeDrawable
                // Set Background Tint
                val mBackgroundTint: ColorStateList = ColorStateList.valueOf(getColor())
                // Set Title
                title.toTitleView(textViewTitleAlertDialog)
                // Set Message and Details
                mContext.toMessageAndDetailsViews(
                    message,
                    textViewMessageAlertDialog,
                    details,
                    textViewDetailsAlertDialog,
                    messageSpanLengthMax,
                    { message, details ->
                        setMessageDetailsIfExists(
                            message,
                            textViewMessageAlertDialog,
                            details,
                            textViewDetailsAlertDialog,
                            nestedScrollViewContainerDetails
                        )
                    },
                    { details ->
                        setDetailsIfExists(
                            details,
                            textViewDetailsAlertDialog,
                            nestedScrollViewContainerDetails
                        )
                    })
                // Set Positive Button
                eventsActions.buttonActionPositiveAlertDialog.apply {
                    setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_semi_bold))
                    mPositiveButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mPositiveButton?.onClickListener?.onClick(
                            this@EventsComponentBase,
                            DialogAlertInterface.UI.BUTTON_POSITIVE
                        )
                    }
                }
                // Set Neutral Button
                eventsActions.buttonActionNeutralAlertDialog.apply {
                    setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_semi_bold))
                    mNeutralButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mNeutralButton?.onClickListener?.onClick(
                            this@EventsComponentBase,
                            DialogAlertInterface.UI.BUTTON_NEUTRAL
                        )
                    }
                }
                // Set Negative Button
                eventsActions.buttonActionNegativeAlertDialog.apply {
                    setTypeface(ResourcesCompat.getFont(mContext, R.font.montserrat_semi_bold))
                    mNegativeButton.toButtonView(mContext, this, mBackgroundTint)
                    setOnClickListener {
                        mNegativeButton?.onClickListener?.onClick(
                            this@EventsComponentBase,
                            DialogAlertInterface.UI.BUTTON_NEGATIVE
                        )
                    }
                }
                // Set CountDownTimer to button
                countDownTimer?.let {
                    val button = getButton(it.button)
                    mCountDownTimer = it.toButtonView(button)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    /**
     * Cancel this dialog, removing it from the screen.
     * This method can be invoked safely from any thread.
     * Note that you should not override this method to do cleanup when the dialog is canceled.
     *
     */
    override fun cancel() {
        if (mDialog != null) {
            mCountDownTimer?.cancel()
            mDialog?.cancel()
        } else throwNullDialog()
    }

    /**
     * Create triangle
     *
     * @param width Defines the width of the bitmap
     * @param height Defines the height of the bitmap
     * @return [Bitmap] or null
     */
    private fun createTriangle(width: Float, height: Float): Bitmap? {
        return if (width <= 0 || height <= 0) null else mContext.createBitmap(
            width,
            height,
            getColor()
        )
    }

    /**
     * Dismiss this dialog, removing it from the screen.
     * This method can be invoked safely from any thread.
     * Note that you should not override this method to do cleanup when the dialog is dismissed.
     *
     */
    override fun dismiss() {
        if (mDialog != null) {
            mCountDownTimer?.cancel()
            mDialog?.dismiss()
        } else throwNullDialog()
    }

    /**
     * Get the button with the specified type.
     *
     * @param which The type of button.
     *
     * @return [MaterialButton]
     *
     * @throws IllegalArgumentException
     *
     */
    @Throws(IllegalArgumentException::class)
    open fun getButton(which: DialogAlertInterface.UI): MaterialButton {
        return when (which) {
            DialogAlertInterface.UI.BUTTON_POSITIVE -> binding.eventsActions.buttonActionPositiveAlertDialog
            DialogAlertInterface.UI.BUTTON_NEGATIVE -> binding.eventsActions.buttonActionNegativeAlertDialog
            DialogAlertInterface.UI.BUTTON_NEUTRAL -> binding.eventsActions.buttonActionNeutralAlertDialog
        }
    }

    /**
     * Start the dialog and display it on screen.
     * The window is placed in the application layer and opaque.
     * Note that you should not override this method to do initialization when the dialog is shown.
     *
     */
    open fun show() {
        if (mDialog != null) {
            mDialog?.show()
            mCountDownTimer?.start()
        } else throwNullDialog()
    }

    /**
     * Set the interface for callback events when the dialog is canceled.
     *
     * @param onCancelListener
     */
    open fun setOnCancelListener(onCancelListener: DialogAlertInterface.OnCancelListener) {
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener { cancelCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: DialogAlertInterface.OnDismissListener) {
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnDismissListener { dismissCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: DialogAlertInterface.OnShowListener) {
        this.mOnShowListener = onShowListener
        mDialog?.setOnShowListener { showCallback() }
    }

    private fun cancelCallback() {
        mOnCancelListener?.onCancel(this)
    }

    private fun dismissCallback() {
        mOnDismissListener?.onDismiss(this)
    }

    private fun getColor(): Int {
        return when (type) {
            Alert.State.DELETE -> getColorCallback(R.color.delete)
            Alert.State.ERROR -> mContext.colorError()
            Alert.State.HELP -> mContext.colorSecondary()
            Alert.State.INFORMATION -> mContext.colorPrimary()
            Alert.State.SUCCESS -> getColorCallback(R.color.success)
            Alert.State.WARNING -> getColorCallback(R.color.warning)
            Alert.State.WITHOUT_INTERNET, Alert.State.WITHOUT_INTERNET_MOBILE, Alert.State.WITHOUT_INTERNET_WIFI -> getColorCallback(
                R.color.caution
            )

            else -> {
                if (backgroundColorSpanInt != null) backgroundColorSpanInt!!
                else if (backgroundColorSpanResource != null) getColorCallback(
                    backgroundColorSpanResource!!
                ) else mContext.colorSecondary()
            }
        }
    }

    private fun getColorCallback(@ColorRes color: Int): Int {
        return mContext.getColor(color)
    }

    private fun setDetailsIfExists(
        detailsText: CharSequence,
        mDetailsView: TextView,
        mDetailsViewContainer: NestedScrollView
    ) {
        val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(mContext.applicationContext)
            .textLength(detailsSpanLengthMax.maxLength)
            .textLengthType(detailsSpanLengthMax.type)
            .moreLabelColor(getColor())
            .lessLabelColor(getColor())
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()
        readMoreOption.addReadMoreTo(mDetailsView, detailsText)
        mDetailsViewContainer.apply {
            isVisible = true
            readMoreOption.addMoreClickListener {
                val isTablet = mContext.isTablet()
                val maxCharacters =
                    if (isTablet) MAX_CHART_SEQUENCE_LENGTH_TABLET else MAX_CHART_SEQUENCE_LENGTH
                val orientation = mContext.resources.configuration.orientation
                layoutParams.height =
                    if (detailsText.length >= maxCharacters) {
                        if (isTablet) {
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET_LANDSCAPE else DEFAULT_LAYOUT_PARAMS_HEIGHT_TABLET
                        } else {
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) DEFAULT_LAYOUT_PARAMS_HEIGHT_LANDSCAPE else DEFAULT_LAYOUT_PARAMS_HEIGHT
                        }
                    } else ViewGroup.LayoutParams.WRAP_CONTENT
            }
            readMoreOption.addLessClickListener {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    private fun setMessageDetailsIfExists(
        messageText: CharSequence,
        mMessageView: TextView,
        detailsText: CharSequence,
        mDetailsView: TextView,
        mDetailsViewContainer: NestedScrollView
    ) {
        if (messageText.length > messageSpanLengthMax) {
            val spannableString = SpannableStringBuilder()

            val spanMessage = SpannableStringBuilder()
            spanMessage.append(messageText)
            spanMessage.setSpan(StyleSpan(Typeface.BOLD), 0, spanMessage.length, 0)
            spanMessage.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                spanMessage.length,
                0
            )

            spannableString.append(spanMessage)
            spannableString.append("\n\n")

            val spanDetails = SpannableStringBuilder()
            spanDetails.append("------------------- ")
            spanDetails.append(mContext.getString(R.string.label_text_additional_details))
            spanDetails.append(" -------------------")
            spanDetails.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                spanDetails.length,
                0
            )

            spannableString.append(spanDetails)
            spannableString.append("\n\n")

            spannableString.append(detailsText)

            mMessageView.text = mContext.getString(R.string.label_text_details_are_specified_below)
            setDetailsIfExists(spannableString, mDetailsView, mDetailsViewContainer)

        } else {
            mMessageView.text = messageText
            setDetailsIfExists(detailsText, mDetailsView, mDetailsViewContainer)
        }
    }

    private fun showCallback() {
        mOnShowListener?.onShow(this)
    }

    private fun throwNullDialog() {
        throw NullPointerException("Called method on null Dialog. Create dialog using `Builder` before calling on Dialog")
    }

    /**
     * Creates a builder for an alert dialog that uses the default alert dialog theme.
     * The default alert dialog theme is defined by [android.R.attr.alertDialogTheme] within the parent context's theme.
     * @param context – the parent context
     */
    abstract class Builder<D : EventsComponentBase>(protected val context: Context) :
        AlertBuilder() {

        protected open var icon: IconAlert = IconAlert(context.applicationInfo.icon)
        protected open var backgroundColorSpanInt: Int? = null
        protected open var backgroundColorSpan: Int? = null
        protected open var countDownTimer: ButtonCountDownTimer? = null
        protected open var messageSpanLengthMax: Int = DEFAULT_CHART_SEQUENCE_LENGTH
        protected open var detailsSpanLengthMax: SpanLength =
            SpanLength(DEFAULT_CHART_SEQUENCE_LENGTH_DETAILS, ReadMoreOption.TYPE_CHARACTER)
        protected open var type: Alert.State = Alert.State.CUSTOM
        protected open var title: TitleAlert? = null
        protected open var message: MessageAlert<*>? = null
        protected open var details: DetailsAlert<*>? = null
        protected open var isCancelable: Boolean = true
        protected open var gravity: Int? = null
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var neutralButton: ButtonAlertDialog? = null
        protected open var negativeButton: ButtonAlertDialog? = null

        /**
         * Set the [Drawable] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIcon(icon: Drawable): Builder<D> {
            this.icon = IconAlert(icon)
            return this
        }


        /**
         * Set the [DrawableRes] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlert(icon)
            return this
        }

        /**
         * Set material dialog type. Use the following types
         * [Alert.State.CUSTOM], [Alert.State.ERROR], [Alert.State.HELP],
         * [Alert.State.INFORMATION], [Alert.State.SUCCESS], [Alert.State.WARNING],
         * [Alert.State.WITHOUT_INTERNET].
         *
         * @param dialogType By default, it is used [Alert.State.CUSTOM].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setType(dialogType: Alert.State): Builder<D> {
            this.type = dialogType
            return this
        }

        /**
         * Set background [ColorInt].
         *
         * @param color Color int. E.g. [Color.GREEN]
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setBackgroundColorSpan(@ColorInt color: Int): Builder<D> {
            this.backgroundColorSpanInt = color
            return this
        }

        /**
         * Set background color, Return a color-int from red, green, blue components.
         * These component values should be [0..255],
         * so if they are out of range, the returned color is undefined.
         *
         * @param red to extract the red component
         * @param green to extract the green component
         * @param blue to extract the blue component
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setBackgroundColorSpan(
            @IntRange(from = 0, to = 255) red: Int,
            @IntRange(from = 0, to = 255) green: Int,
            @IntRange(from = 0, to = 255) blue: Int,
        ): Builder<D> {
            this.backgroundColorSpanInt = Color.rgb(red, green, blue)
            return this
        }

        /**
         * Set background [ColorRes].
         *
         * @param color Color resource.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setBackgroundColorSpanRes(@ColorRes color: Int): Builder<D> {
            this.backgroundColorSpan = color
            return this
        }

        /**
         * Set count down timer. Default interval `1000`
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setCountDownTimer(button: DialogAlertInterface.UI, millis: Long): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis)
            return this
        }

        /**
         * Set count down timer. Default interval `1000`
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param format [String] format of the count-down timer.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setCountDownTimer(
            button: DialogAlertInterface.UI,
            millis: Long,
            format: String
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, format = format)
            return this
        }


        /**
         * Set count down timer.
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param countInterval [Long] time in milliseconds.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(
            button: DialogAlertInterface.UI,
            millis: Long,
            countInterval: Long
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, countInterval)
            return this
        }


        /**
         * Set count down timer.
         *
         * @param button [DialogAlertInterface.UI] `AlertDialog.UI.BUTTON_POSITIVE`, `AlertDialog.UI.BUTTON_NEGATIVE` or `AlertDialog.UI.BUTTON_NEUTRAL`
         * @param millis [Long] time in milliseconds.
         * @param countInterval [Long] time in milliseconds.
         * @param format the format of the countdown timer. Default `%s (%d)`
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCountDownTimer(
            button: DialogAlertInterface.UI,
            millis: Long,
            countInterval: Long,
            format: String
        ): Builder<D> {
            this.countDownTimer = ButtonCountDownTimer(button, millis, countInterval, format)
            return this
        }

        /**
         * Set the title displayed in the dialog.
         *
         * @param title The title to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, Alert.TextAlignment.CENTER)
        }


        /**
         * Set the title displayed in the dialog.
         *
         * @param title The title to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, Alert.TextAlignment.CENTER)
        }


        /**
         * Set the title displayed in the dialog. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(title: String, alignment: Alert.TextAlignment): Builder<D> {
            this.title = TitleAlert(title, alignment)
            return this
        }


        /**
         * Set the title displayed in the dialog. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setTitle(@StringRes title: Int, alignment: Alert.TextAlignment): Builder<D> {
            this.title = TitleAlert(context.getString(title), alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(message: String): Builder<D> {
            return setMessage(message, Alert.TextAlignment.CENTER)
        }


        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, Alert.TextAlignment.CENTER)
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(message: String, alignment: Alert.TextAlignment): Builder<D> {
            this.message = MessageAlert.text(message, alignment)
            return this
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(@StringRes message: Int, alignment: Alert.TextAlignment): Builder<D> {
            this.message = MessageAlert.text(context.getString(message), alignment)
            return this
        }


        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setMessage(message: Spanned): Builder<D> {
            return setMessage(message, Alert.TextAlignment.CENTER)
        }


        /**
         * Sets the message to display. With text alignment.
         *
         * @see [Alert.TextAlignment]
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: Alert.TextAlignment): Builder<D> {
            this.message = MessageAlert.spanned(text = message, alignment = alignment)
            return this
        }

        /**
         * Set the message span length max. Default [DEFAULT_CHART_SEQUENCE_LENGTH].
         *
         * @param messageSpanLengthMax The message span length max.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessageSpanLengthMax(messageSpanLengthMax: Int): Builder<D> {
            this.messageSpanLengthMax = messageSpanLengthMax
            return this
        }

        /**
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetails(detail: String): Builder<D> {
            this.details = DetailsAlert.text(text = detail)
            return this
        }

        /**
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetails(@StringRes detail: Int): Builder<D> {
            this.details = DetailsAlert.text(text = context.getString(detail))
            return this
        }

        /**
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetails(detail: Spanned): Builder<D> {
            this.details = DetailsAlert.spanned(text = detail)
            return this
        }

        /**
         * Set the details span length max. Default [DEFAULT_CHART_SEQUENCE_LENGTH_DETAILS].
         *  - Then the option to [ReadMoreOption] is added.
         *
         * @param maxCharacters
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetailsSpanLengthMaxForCharacter(maxCharacters: Int): Builder<D> {
            this.detailsSpanLengthMax = SpanLength(maxCharacters, ReadMoreOption.TYPE_CHARACTER)
            return this
        }


        /**
         * Set the details span length max.
         *  - Then the option to [ReadMoreOption] is added.
         *
         * @param maxLines
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetailsSpanLengthMaxForLine(maxLines: Int): Builder<D> {
            this.detailsSpanLengthMax = SpanLength(maxLines, ReadMoreOption.TYPE_LINE)
            return this
        }


        /**
         * Sets whether the dialog is cancelable or not.
         *
         * @param isCancelable is [Boolean] value. Default is true.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setCancelable(isCancelable: Boolean): Builder<D> {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * Set gravity of the dialog
         *
         * @param gravity [Int] value
         * @see [android.view.Gravity]
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setGravity(gravity: Int): Builder<D> {
            this.gravity = gravity
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_cancel].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(onClickListener: DialogAlertInterface.OnClickListener): Builder<D> {
            return setNegativeButton(
                R.string.label_text_cancel,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNegativeButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_cancel].
         *
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNegativeButton(R.string.label_text_cancel, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNegativeButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(text, icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text        The text to display in negative button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNegativeButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.negativeButton = initNegativeButton(context.getString(text), icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_decline].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(onClickListener: DialogAlertInterface.OnClickListener): Builder<D> {
            return setNeutralButton(
                R.string.label_text_decline,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            text: String,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNeutralButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_decline].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNeutralButton(R.string.label_text_decline, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.neutralButton = initNeutralButton(text, icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            @StringRes text: Int,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setNeutralButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param text        The text to display in neutral button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setNeutralButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.neutralButton = initNeutralButton(context.getString(text), icon, onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_accept].
         *
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(onClickListener: DialogAlertInterface.OnClickListener): Builder<D> {
            return setPositiveButton(
                R.string.label_text_accept,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setPositiveButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * - Default button text is [R.string.label_text_accept].
         *
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setPositiveButton(R.string.label_text_accept, icon, onClickListener)
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            text: String,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.positiveButton = initPositiveButton(text, icon, onClickListener)
            return this
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            return setPositiveButton(
                text,
                ButtonIconAlert(MATERIAL_ALERT_DIALOG_UI_NOT_ICON),
                onClickListener
            )
        }


        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text        The text to display in positive button.
         * @param icon        The [ButtonIconAlert] to be set as an icon for the button.
         * @param onClickListener    The [DialogAlertInterface.OnClickListener] to use.
         *
         * @return [Builder] object to allow for chaining of calls to set methods
         *
         */
        fun setPositiveButton(
            @StringRes text: Int,
            icon: ButtonIconAlert,
            onClickListener: DialogAlertInterface.OnClickListener
        ): Builder<D> {
            this.positiveButton = initPositiveButton(context.getString(text), icon, onClickListener)
            return this
        }


        /**
         * Creates an [MaterialAlertDialogEvents] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = MaterialAlertDialogEvents.Builder(context)
         *     ...
         *     .create()
         * dialog.show()
         *
         * ```
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}
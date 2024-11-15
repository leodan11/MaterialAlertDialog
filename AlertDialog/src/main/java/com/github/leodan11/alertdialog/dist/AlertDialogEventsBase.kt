package com.github.leodan11.alertdialog.dist

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.text.Editable
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.github.leodan11.alertdialog.MaterialAlertDialogEvents
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.databinding.MAlertDialogBinding
import com.github.leodan11.alertdialog.io.content.AlertDialog
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_CHART_SEQUENCE_LENGTH
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_DETAILS_SCROLL_HEIGHT_SPAN
import com.github.leodan11.alertdialog.io.content.Config.DEFAULT_RADIUS
import com.github.leodan11.alertdialog.io.content.Config.MATERIAL_ALERT_DIALOG_UI_NOT_ICON
import com.github.leodan11.alertdialog.io.content.MaterialDialogInterface
import com.github.leodan11.alertdialog.io.helpers.DisplayUtil
import com.github.leodan11.alertdialog.io.models.ButtonAlertDialog
import com.github.leodan11.alertdialog.io.models.DetailsAlertDialog
import com.github.leodan11.alertdialog.io.models.IconAlertDialog
import com.github.leodan11.alertdialog.io.models.MessageAlertDialog
import com.github.leodan11.alertdialog.io.models.TitleAlertDialog
import com.github.leodan11.k_extensions.color.colorError
import com.github.leodan11.k_extensions.color.colorOnSurface
import com.github.leodan11.k_extensions.color.colorPrimary
import com.github.leodan11.k_extensions.color.colorSecondary
import com.github.leodan11.k_extensions.color.colorSurface
import com.github.leodan11.k_extensions.context.createBitmap
import com.github.leodan11.k_extensions.view.onTextViewTextSize
import com.leodan.readmoreoption.ReadMoreOption

abstract class AlertDialogEventsBase(
    protected open var mContext: Context,
    protected open var icon: IconAlertDialog,
    protected open var type: AlertDialog.State,
    protected open var backgroundColorSpanInt: Int?,
    protected open var backgroundColorSpanResource: Int?,
    protected open var messageSpanLengthMax: Int,
    protected open var detailsScrollHeightSpan: Int,
    protected open var title: TitleAlertDialog?,
    protected open var message: MessageAlertDialog<*>?,
    protected open var details: DetailsAlertDialog<*>?,
    protected open var mCancelable: Boolean,
    protected open var mPositiveButton: ButtonAlertDialog?,
    protected open var mNeutralButton: ButtonAlertDialog?,
    protected open var mNegativeButton: ButtonAlertDialog?,
) : MaterialDialogInterface {

    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: MaterialDialogInterface.OnDismissListener? = null
    protected open var mOnCancelListener: MaterialDialogInterface.OnCancelListener? = null
    protected open var mOnShowListener: MaterialDialogInterface.OnShowListener? = null

    @SuppressLint("WrongConstant")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun createView(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null,
    ): View {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        val binding: MAlertDialogBinding =
            MAlertDialogBinding.inflate(layoutInflater, container, false)
        // Initialize Views
        val mIconView = binding.imageViewIconAlertDialog
        val mTitleView = binding.textViewTitleAlertDialog
        val mMessageView = binding.textViewMessageAlertDialog
        val mDetailsViewContainer = binding.nestedScrollViewContainerDetails
        val mDetailsView = binding.textViewDetailsAlertDialog
        val mPositiveButtonView = binding.buttonActionPositiveAlertDialog
        val mNeutralButtonView = binding.buttonActionNeutralAlertDialog
        val mNegativeButtonView = binding.buttonActionNegativeAlertDialog

        // Set Icon
        when (type) {
            AlertDialog.State.DELETE -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_delete)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.ERROR -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_error)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.HELP -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_help)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.INFORMATION -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_information)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.SUCCESS -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_success)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.WARNING -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_warning)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.WITHOUT_INTERNET -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_cloud)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.WITHOUT_INTERNET_MOBILE -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_mobile_alert)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            AlertDialog.State.WITHOUT_INTERNET_WIFI -> mIconView.apply {
                setImageResource(R.drawable.ic_baseline_wifi_alert)
                imageTintList = ColorStateList.valueOf(mContext.colorSurface())
            }

            else -> mIconView.setImageResource(icon.mDrawableResId)
        }
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
        binding.topLayout.addView(triangleIv)
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
        shapeDrawable.paint.setColor(getColor())
        binding.llTop.background = shapeDrawable
        // Set Title
        mTitleView.isVisible = title != null
        title?.let {
            mTitleView.text = it.title
            mTitleView.textAlignment = it.textAlignment.alignment
        }
        //Set Message and Details
        mMessageView.isVisible = message != null
        if (message != null && details != null) {
            val messageText = message?.getText() ?: ""
            val detailsText = details?.getText() ?: ""
            if (messageText.length > messageSpanLengthMax) {
                message?.let { mMessageView.textAlignment = it.textAlignment.alignment }
                setMessageDetailsIfExists(
                    messageText,
                    mMessageView,
                    detailsText,
                    mDetailsView,
                    mDetailsViewContainer
                )
            } else {
                message?.let {
                    mMessageView.text = messageText
                    mMessageView.textAlignment = it.textAlignment.alignment
                }
                setDetailsIfExists(detailsText, mDetailsView, mDetailsViewContainer)
            }
        } else if (message != null) {
            val messageText = message?.getText() ?: ""
            if (messageText.length > messageSpanLengthMax) {
                message?.let { mMessageView.textAlignment = it.textAlignment.alignment }
                setMessageDetailsIfExists(
                    mContext.getString(R.string.label_text_details_are_specified_below),
                    mMessageView,
                    messageText,
                    mDetailsView,
                    mDetailsViewContainer
                )
            } else {
                message?.let {
                    mMessageView.text = messageText
                    mMessageView.textAlignment = it.textAlignment.alignment
                }
            }
        } else if (details != null) {
            setDetailsIfExists(details?.getText() ?: "", mDetailsView, mDetailsViewContainer)
        }
        // Set Positive Button
        mPositiveButtonView.isVisible = mPositiveButton != null
        mPositiveButton?.let {
            mPositiveButtonView.text = it.title
            if (it.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mPositiveButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, it.icon)
            mPositiveButtonView.setOnClickListener {
                mPositiveButton?.onClickListener?.onClick(this, AlertDialog.UI.BUTTON_POSITIVE)
            }
        }
        // Set Neutral Button
        mNeutralButtonView.isVisible = mNeutralButton != null
        mNeutralButton?.let {
            mNeutralButtonView.text = it.title
            if (mNeutralButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mNeutralButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, it.icon)
            mNeutralButtonView.setOnClickListener {
                mNeutralButton?.onClickListener?.onClick(this, AlertDialog.UI.BUTTON_NEUTRAL)
            }
        }
        // Set Negative Button
        mNegativeButtonView.isVisible = mNegativeButton != null
        mNegativeButton?.let {
            mNegativeButtonView.text = it.title
            if (mNegativeButton?.icon != MATERIAL_ALERT_DIALOG_UI_NOT_ICON) mNegativeButtonView.icon =
                ContextCompat.getDrawable(mContext.applicationContext, it.icon)
            mNegativeButtonView.setOnClickListener {
                mNegativeButton?.onClickListener?.onClick(this, AlertDialog.UI.BUTTON_NEGATIVE)
            }
        }
        // Apply Styles
        try {
            // Set Title Text Color
            mTitleView.setTextColor(mContext.colorOnSurface())
            // Set Message Text Color
            mMessageView.setTextColor(mContext.colorOnSurface())
            // Set Details Text Color
            mDetailsView.setTextColor(mContext.colorOnSurface())
            // Set Background Tint
            val mBackgroundTint: ColorStateList = ColorStateList.valueOf(getColor())
            // Set Positive Button Icon Tint
            val mPositiveButtonTint: ColorStateList = mBackgroundTint
            mPositiveButtonView.setTextColor(mPositiveButtonTint)
            mPositiveButtonView.iconTint = mPositiveButtonTint
            // Set Neutral Button Icon & Text Tint
            val mNeutralButtonTint: ColorStateList = mBackgroundTint
            mNeutralButtonView.setTextColor(mNeutralButtonTint)
            mNeutralButtonView.iconTint = mNeutralButtonTint
            // Set Negative Button Icon & Text Tint
            val mNegativeButtonTint: ColorStateList = mBackgroundTint
            mNegativeButtonView.setTextColor(mNegativeButtonTint)
            mNegativeButtonView.iconTint = mNegativeButtonTint
            // Set Button Ripple Color
            mPositiveButtonView.rippleColor = mBackgroundTint.withAlpha(75)
            mNeutralButtonView.rippleColor = mBackgroundTint.withAlpha(75)
            mNegativeButtonView.rippleColor = mBackgroundTint.withAlpha(75)
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
        if (mDialog != null) mDialog?.cancel()
        else throwNullDialog()
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
        if (mDialog != null) mDialog?.dismiss()
        else throwNullDialog()
    }

    /**
     * Start the dialog and display it on screen.
     * The window is placed in the application layer and opaque.
     * Note that you should not override this method to do initialization when the dialog is shown.
     *
     */
    fun show() {
        if (mDialog != null) mDialog?.show()
        else throwNullDialog()
    }


    /**
     * Set the interface for callback events when the dialog is canceled.
     *
     * @param onCancelListener
     */
    open fun setOnCancelListener(onCancelListener: MaterialDialogInterface.OnCancelListener) {
        this.mOnCancelListener = onCancelListener
        mDialog?.setOnCancelListener { cancelCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is dismissed.
     *
     * @param onDismissListener
     */
    open fun setOnDismissListener(onDismissListener: MaterialDialogInterface.OnDismissListener) {
        this.mOnDismissListener = onDismissListener
        mDialog?.setOnDismissListener { dismissCallback() }
    }

    /**
     * Set the interface for callback events when the dialog is shown.
     *
     * @param onShowListener
     */
    open fun setOnShowListener(onShowListener: MaterialDialogInterface.OnShowListener) {
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
            AlertDialog.State.DELETE -> getColorCallback(R.color.delete)
            AlertDialog.State.ERROR -> mContext.colorError()
            AlertDialog.State.HELP -> mContext.colorSecondary()
            AlertDialog.State.INFORMATION -> mContext.colorPrimary()
            AlertDialog.State.SUCCESS -> getColorCallback(R.color.success)
            AlertDialog.State.WARNING -> getColorCallback(R.color.warning)
            AlertDialog.State.WITHOUT_INTERNET, AlertDialog.State.WITHOUT_INTERNET_MOBILE, AlertDialog.State.WITHOUT_INTERNET_WIFI -> {
                getColorCallback(R.color.caution)
            }

            else -> {
                if (backgroundColorSpanInt != null) backgroundColorSpanInt!!
                else if (backgroundColorSpanResource != null) getColorCallback(
                    backgroundColorSpanResource!!
                ) else mContext.colorSecondary()
            }
        }
    }

    private fun getColorCallback(@ColorRes color: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContext.getColor(color)
        } else {
            ContextCompat.getColor(mContext, color)
        }
    }

    private fun setDetailsIfExists(
        detailsText: CharSequence,
        mDetailsView: TextView,
        mDetailsViewContainer: NestedScrollView
    ) {
        val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(mContext.applicationContext)
            .textLength(6)
            .textLengthType(ReadMoreOption.TYPE_LINE)
            .moreLabelColor(getColor())
            .lessLabelColor(getColor())
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()
        readMoreOption.addReadMoreTo(mDetailsView, detailsText)
        mDetailsView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val bounds = mDetailsView.onTextViewTextSize(it.toString())
                    mDetailsViewContainer.apply {
                        layoutParams.height =
                            if (bounds.width() > 6000) detailsScrollHeightSpan else ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }

        })
        mDetailsViewContainer.visibility = View.VISIBLE
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
    abstract class Builder<D : AlertDialogEventsBase>(protected val context: Context) {

        protected open var icon: IconAlertDialog = IconAlertDialog(context.applicationInfo.icon)
        protected open var backgroundColorSpanInt: Int? = null
        protected open var backgroundColorSpan: Int? = null
        protected open var detailsScrollHeightSpan: Int = DEFAULT_DETAILS_SCROLL_HEIGHT_SPAN
        protected open var messageSpanLengthMax: Int = DEFAULT_CHART_SEQUENCE_LENGTH
        protected open var type: AlertDialog.State = AlertDialog.State.CUSTOM
        protected open var title: TitleAlertDialog? = null
        protected open var message: MessageAlertDialog<*>? = null
        protected open var details: DetailsAlertDialog<*>? = null
        protected open var isCancelable: Boolean = true
        protected open var positiveButton: ButtonAlertDialog? = null
        protected open var neutralButton: ButtonAlertDialog? = null
        protected open var negativeButton: ButtonAlertDialog? = null

        /**
         * Set the [DrawableRes] to be used in the title.
         *
         * @param icon Drawable to use as the icon.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setIcon(@DrawableRes icon: Int): Builder<D> {
            this.icon = IconAlertDialog(mDrawableResId = icon)
            return this
        }

        /**
         * Set material dialog type. Use the following types
         * [AlertDialog.State.CUSTOM], [AlertDialog.State.ERROR], [AlertDialog.State.HELP],
         * [AlertDialog.State.INFORMATION], [AlertDialog.State.SUCCESS], [AlertDialog.State.WARNING],
         * [AlertDialog.State.WITHOUT_INTERNET].
         *
         * @param dialogType By default, it is used [AlertDialog.State.CUSTOM].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setType(dialogType: AlertDialog.State): Builder<D> {
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
         * Set the maximum scroll size. Default 400.
         *
         * @param heightSpan height.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetailsScrollHeightSpan(heightSpan: Int): Builder<D> {
            this.detailsScrollHeightSpan = heightSpan
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogEvents].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogEvents].
         *
         * @param title The title to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(@StringRes title: Int): Builder<D> {
            return setTitle(title, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogEvents]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(title: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.title = TitleAlertDialog(title = title, textAlignment = alignment)
            return this
        }

        /**
         * Set the title displayed in the [MaterialAlertDialogEvents]. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param title The title to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTitle(
            @StringRes title: Int,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.title =
                TitleAlertDialog(title = context.getString(title), textAlignment = alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(@StringRes message: Int): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: String, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.text(text = message, alignment = alignment)
            return this
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(
            @StringRes message: Int,
            alignment: AlertDialog.TextAlignment,
        ): Builder<D> {
            this.message =
                MessageAlertDialog.text(text = context.getString(message), alignment = alignment)
            return this
        }

        /**
         * Sets the message to display.
         *
         * @param message The message to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned): Builder<D> {
            return setMessage(message, AlertDialog.TextAlignment.CENTER)
        }

        /**
         * Sets the message to display. With text alignment: [AlertDialog.TextAlignment.START], [AlertDialog.TextAlignment.CENTER], [AlertDialog.TextAlignment.END].
         *
         * @param message The message to display in the dialog.
         * @param alignment The message alignment. Default [AlertDialog.TextAlignment.CENTER].
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setMessage(message: Spanned, alignment: AlertDialog.TextAlignment): Builder<D> {
            this.message = MessageAlertDialog.spanned(text = message, alignment = alignment)
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
            this.details = DetailsAlertDialog.text(text = detail)
            return this
        }

        /**
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetails(@StringRes detail: Int): Builder<D> {
            this.details = DetailsAlertDialog.text(text = context.getString(detail))
            return this
        }

        /**
         * Set the message details to display.
         *
         * @param detail The details to display in the dialog.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setDetails(detail: Spanned): Builder<D> {
            this.details = DetailsAlertDialog.spanned(text = detail)
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
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setPositiveButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setPositiveButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            buttonText: String? = null,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_accept)
                else buttonText
            positiveButton =
                ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param buttonText        The text to display in positive button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setPositiveButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            positiveButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNeutralButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNeutralButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            buttonText: String? = null,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_decline)
                else buttonText
            neutralButton =
                ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the neutral button of the dialog is pressed.
         *
         * @param buttonText        The text to display in neutral button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNeutralButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            neutralButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
            )
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            buttonText: String? = null,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNegativeButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            @StringRes buttonText: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            return setNegativeButton(buttonText, MATERIAL_ALERT_DIALOG_UI_NOT_ICON, onClickListener)
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            buttonText: String? = null,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            val valueText =
                if (buttonText.isNullOrEmpty()) context.getString(R.string.label_text_cancel)
                else buttonText
            negativeButton =
                ButtonAlertDialog(title = valueText, icon = icon, onClickListener = onClickListener)
            return this
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param buttonText        The text to display in negative button.
         * @param onClickListener    The [MaterialDialogInterface.OnClickListener] to use.
         * @param icon        The [DrawableRes] to be set as an icon for the button.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setNegativeButton(
            @StringRes buttonText: Int,
            @DrawableRes icon: Int,
            onClickListener: MaterialDialogInterface.OnClickListener,
        ): Builder<D> {
            negativeButton = ButtonAlertDialog(
                title = context.getString(buttonText),
                icon = icon,
                onClickListener = onClickListener
            )
            return this
        }


        /**
         * Creates an [MaterialAlertDialogEvents] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * @return [D] object to allow for chaining of calls to set methods
         */
        abstract fun create(): D

    }

}
package com.github.leodan11.alertdialog.dist

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.annotation.RestrictTo
import com.airbnb.lottie.LottieAnimationView
import com.github.leodan11.alertdialog.LottieAlertDialog
import com.github.leodan11.alertdialog.databinding.MAlertDialogLottieBinding
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface

abstract class LottieComponentBase protected constructor(
    protected open var mContext: Context,
    protected open var mAnimationAsset: String?,
    protected open var mAnimationRaw: Int?,
    protected open var mAnimationRepeatCount: Int?,
    protected open var mAnimationSpeed: Float?,
    protected open var mAnimationUrl: String?,
    protected open var mCancelable: Boolean,
    protected open var mLayoutHeight: Int? = null,
    protected open var mTimeout: Long? = null,
) : DialogAlertInterface {

    open val isShowing: Boolean get() = mDialog?.isShowing ?: false
    private lateinit var animationView: LottieAnimationView
    private lateinit var binding: MAlertDialogLottieBinding
    protected open var mDialog: Dialog? = null
    protected open var mOnDismissListener: DialogAlertInterface.OnDismissListener? = null
    protected open var mOnCancelListener: DialogAlertInterface.OnCancelListener? = null
    protected open var mOnShowListener: DialogAlertInterface.OnShowListener? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    protected open fun onCreateViewDialogContent(
        layoutInflater: LayoutInflater,
        container: ViewGroup? = null
    ): View {
        binding = MAlertDialogLottieBinding.inflate(layoutInflater, container, false)
        animationView = binding.lottieAnimationView
        try {
            with(binding) {
                lottieAnimationView.apply {
                    mAnimationAsset?.let { setAnimation(it) }
                    mAnimationRaw?.let { setAnimation(it) }
                    mAnimationRepeatCount?.let { setRepeatCount(it) }
                    mAnimationSpeed?.let { speed = it }
                    mAnimationUrl?.let { setAnimationFromUrl(it) }
                    mLayoutHeight?.let { layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, it) }
                }
                mTimeout?.let { setTimeout(it) }
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
            animationView.cancelAnimation()
            mDialog?.cancel()
        } else throwNullDialog()
    }

    /**
     * Dismiss this dialog, removing it from the screen.
     * This method can be invoked safely from any thread.
     * Note that you should not override this method to do cleanup when the dialog is dismissed.
     *
     */
    override fun dismiss() {
        if (mDialog != null) {
            animationView.cancelAnimation()
            mDialog?.dismiss()
        } else throwNullDialog()
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

    /**
     * Sets a timeout for the dialog to automatically dismiss after a specified duration.
     *
     * @param milliseconds The duration in milliseconds before the dialog is automatically dismissed.
     *
     */
    open fun setTimeout(milliseconds: Long) {
        Handler(mContext.mainLooper).postDelayed({
            if (mDialog?.isShowing == true) mDialog?.dismiss()
        }, milliseconds)
    }


    private fun cancelCallback() {
        mOnCancelListener?.onCancel(this)
    }

    private fun dismissCallback() {
        mOnDismissListener?.onDismiss(this)
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
    abstract class Builder<D : LottieComponentBase>(protected val context: Context) {

        protected open var isCancelable: Boolean = true
        protected open var lottieAnimationAsset: String? = null
        protected open var lottieAnimationRaw: Int? = null
        protected open var lottieAnimationRepeatCount: Int? = null
        protected open var lottieAnimationSpeed: Float? = null
        protected open var lottieAnimationUrl: String? = null
        protected open var lottieLayoutHeight: Int? = null
        protected open var onTimeout: Long? = null
        protected open var gravity: Int? = null

        /**
         * Sets the animation asset to be used in the dialog.
         *
         * @param asset is [String] value. Default is null.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setAnimation(asset: String): Builder<D> {
            lottieAnimationAsset = asset
            return this
        }

        /**
         * Sets the animation raw to be used in the dialog.
         *
         * @param raw is [Int] value. Default is null.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setAnimation(@RawRes raw: Int): Builder<D> {
            lottieAnimationRaw = raw
            return this
        }

        /**
         * Sets the animation url to be used in the dialog.
         *
         * @param url is [String] value. Default is null.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setAnimationFromUrl(url: String): Builder<D> {
            lottieAnimationUrl = url
            return this
        }

        /**
         * Sets the animation repeat count to be used in the dialog.
         *
         * @param repeatCount is [Int] value. Default is null.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setAnimationRepeatCount(repeatCount: Int): Builder<D> {
            lottieAnimationRepeatCount = repeatCount
            return this
        }

        /**
         * Sets the animation speed to be used in the dialog.
         *
         * @param speed is [Float] value. Default is null.
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setAnimationSpeed(speed: Float): Builder<D> {
            lottieAnimationSpeed = speed
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
         * Sets the layout params for the dialog.
         *
         * @param layoutParamsHeight the layout params to set
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setLayoutParams(layoutParamsHeight: Int): Builder<D> {
            lottieLayoutHeight = layoutParamsHeight
            return this
        }


        /**
         * Sets the timeout for the dialog.
         *
         * @param milliseconds the timeout in milliseconds
         * @return [Builder] object to allow for chaining of calls to set methods
         */
        fun setTimeout(milliseconds: Long): Builder<D> {
            onTimeout = milliseconds
            return this
        }


        /**
         * Creates an [LottieAlertDialog] with the arguments supplied to this builder.
         * Calling this method does not display the dialog.
         * If no additional processing is needed, [show] may be called instead to both create and display the dialog.
         *
         * ```kotlin
         *
         * val dialog = LottieAlertDialog.Builder(context)
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
package com.github.leodan11.alertdialog.api.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import com.github.leodan11.alertdialog.R
import com.github.leodan11.alertdialog.internal.base.BaseBindingDialogFragment


/**
 * DialogFragment implementation that displays a full-screen dialog
 * using ViewDataBinding as its content view.
 *
 * This class extends [BaseBindingDialogFragment] and follows the
 * standard Fragment view lifecycle for binding management.
 *
 * The dialog is styled to occupy the full screen using a custom theme
 * and window configuration.
 *
 * ## Lifecycle behavior
 * - [onCreate] is used to configure dialog style
 * - [onCreateView] inflates and initializes the binding
 * - [onStart] ensures full-screen window layout is applied
 * - Binding is cleared in [onDestroyView] (handled by base class)
 *
 * ## Responsibilities
 * - Manage full-screen dialog presentation
 * - Initialize ViewDataBinding during view creation
 * - Provide lifecycle hooks for subclass customization
 *
 * ## Usage notes
 * - Use [onCreated] for initialization before view creation
 * - Use [onViewSetup] for UI initialization
 * - Use [onDialogStarted] when the dialog is fully visible
 *
 * @param VB The type of ViewDataBinding used for the dialog layout.
 *
 * @since 1.10.10
 */
abstract class FullScreenDialogFragment<VB : ViewDataBinding> : BaseBindingDialogFragment<VB>() {


    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_MaterialDialog_FullScreen)
        onCreated()
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onCreateBinding(inflater = inflater, container = container)
        binding.lifecycleOwner = viewLifecycleOwner
        onViewSetup()
        return binding.root
    }

    final override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        onDialogStarted()
    }

    /**
     * Called during [onCreate] before the view is created.
     *
     * This is intended for early initialization such as configuration
     * of dialog behavior, dependencies, or arguments processing.
     *
     * No view or binding is available at this stage.
     * @since 2.0.0
     */
    @MainThread
    protected open fun onCreated() = Unit

    /**
     * Called during [onStart] after the dialog is visible and
     * its window has been configured to full-screen size.
     *
     * This is the final lifecycle stage where UI adjustments
     * related to window or visibility should be applied.
     * @since 2.0.0
     */
    @MainThread
    protected open fun onDialogStarted() = Unit
}
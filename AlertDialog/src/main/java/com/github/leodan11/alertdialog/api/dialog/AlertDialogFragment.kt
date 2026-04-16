package com.github.leodan11.alertdialog.api.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import com.github.leodan11.alertdialog.internal.base.BaseBindingDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * DialogFragment implementation that displays a Material AlertDialog
 * using ViewDataBinding as its content view.
 *
 * This class extends [BaseBindingDialogFragment] and delegates
 * binding creation to the subclass implementation.
 *
 * The view is attached manually to a [MaterialAlertDialogBuilder].
 *
 * ## Lifecycle behavior
 * - Binding is created during [onCreateDialog]
 * - [onCreateView] is not part of the main UI inflation flow
 * - The view is attached directly to the dialog instance
 *
 * ## Usage
 * Use [onCreateDialogSetup] to configure the dialog before creation.
 *
 * @param VB The type of ViewDataBinding used for the dialog layout.
 *
 * @since 1.10.10
 */
abstract class AlertDialogFragment<VB : ViewDataBinding> : BaseBindingDialogFragment<VB>() {

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        onCreateBinding(inflater = layoutInflater, container = null)
        builder.setView(binding.root)
        onCreateDialogSetup(builder)
        return builder.create()
    }

    /**
     * Allows customization of the [MaterialAlertDialogBuilder] before the dialog is created.
     *
     * This method is invoked inside [onCreateDialog] before calling `create()`.
     *
     * ## Important
     * - Only configure the builder here.
     * - Do not interact with the Dialog instance yet.
     *
     * @param builder Dialog builder instance.
     * @since 2.0.0
     */
    @MainThread
    protected open fun onCreateDialogSetup(builder: MaterialAlertDialogBuilder): Unit = Unit

}
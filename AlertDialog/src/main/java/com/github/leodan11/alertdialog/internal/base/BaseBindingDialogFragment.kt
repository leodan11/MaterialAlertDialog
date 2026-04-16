package com.github.leodan11.alertdialog.internal.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Base class for [DialogFragment] implementations that use ViewDataBinding.
 *
 * This class provides a reusable foundation for dialog fragments with:
 * - Automatic ViewDataBinding lifecycle management
 * - Lifecycle-safe access to the view binding
 * - Coroutine utilities bound to the view lifecycle
 * - Clean extension hooks for subclasses
 *
 * This class is intended to be extended by concrete dialog implementations
 * such as alert dialogs or fullscreen dialogs.
 *
 * ## Lifecycle behavior
 * - Binding is created in [onCreateView]
 * - Binding is cleared in [onDestroyView]
 * - All view-related logic must be handled within the view lifecycle
 *
 * ## Example
 * ```kotlin
 * class MyDialog : BaseBindingDialogFragment<MyBinding>() {
 *
 *     override val layoutId: Int = R.layout.my_dialog
 *
 *     override fun onViewSetup() {
 *         binding.title.text = "Hello"
 *     }
 *
 *     override fun onViewDestroyed() {
 *         // cleanup
 *     }
 * }
 * ```
 *
 * @param VB The type of [ViewDataBinding] associated with this dialog.
 * @property layoutId Layout resource used to inflate the binding.
 * @since 2.0.0
 */
abstract class BaseBindingDialogFragment<VB : ViewDataBinding> : DialogFragment() {

    /**
     * Backing property for the view binding.
     *
     * This should only be accessed internally. It is cleared in [onDestroyView]
     * to prevent memory leaks.
     */
    private var _binding: VB? = null

    /**
     * View binding valid only between [onCreateView] and [onDestroyView].
     *
     * Accessing this outside that window will throw an exception.
     */
    protected val binding: VB
        get() = _binding ?: error("Binding is only valid between onCreateView and onDestroyView")

    /**
     * Layout resource used to inflate the binding.
     */
    @get:LayoutRes
    abstract val layoutId: Int

    final override fun onDestroyView() {
        onViewDestroyed()
        _binding = null
        super.onDestroyView()
    }

    /**
     * Launches a coroutine tied to the view lifecycle.
     *
     * The block is executed when the lifecycle is at least in the given [state]
     * and is automatically cancelled when it falls below that state.
     *
     * This is useful for collecting Flows or observing UI state safely.
     *
     * ## Example
     * ```kotlin
     * launchOnLifecycle {
     *     viewModel.state.collect { render(it) }
     * }
     * ```
     *
     * @param state Minimum lifecycle state. Defaults to [Lifecycle.State.STARTED].
     * @param block Suspend lambda executed in lifecycle-aware scope.
     * @since 2.0.0
     */
    protected inline fun launchOnLifecycle(
        state: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline block: suspend CoroutineScope.() -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state) {
                block()
            }
        }
    }

    /**
     * Called when the view has been created and the binding is ready.
     *
     * This is the main entry point for initializing UI components.
     * @since 2.0.0
     */
    @MainThread
    protected open fun onViewSetup() = Unit

    /**
     * Called when the view is about to be destroyed.
     *
     * Use this method to clean up resources tied to the view lifecycle.
     * @since 2.0.0
     */
    @MainThread
    protected open fun onViewDestroyed() = Unit

    /**
     * Inflates and initializes the ViewDataBinding instance for this dialog.
     *
     * This method is responsible for creating the binding using the provided [LayoutInflater]
     * and optional [ViewGroup] container, based on the [layoutId] defined by the subclass.
     *
     * The resulting binding is stored internally and will be cleared automatically in [onDestroyView].
     *
     * This method is intended to be used only by the base class during the view lifecycle
     * and should not be called directly by subclasses.
     *
     * ## Lifecycle
     * - Called internally during dialog view creation
     * - Binding is valid between [onCreateView] and [onDestroyView]
     *
     * @param inflater The LayoutInflater used to inflate the binding.
     * @param container Optional parent view group for the inflated layout.
     *
     * @see ViewDataBinding
     * @see LayoutInflater
     * @since 2.0.0
     */
    @MainThread
    protected open fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
    }

}

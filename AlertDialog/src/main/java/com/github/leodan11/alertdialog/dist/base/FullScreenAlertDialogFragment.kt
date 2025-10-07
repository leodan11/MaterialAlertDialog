package com.github.leodan11.alertdialog.dist.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.leodan11.alertdialog.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Base [DialogFragment] class for creating full-screen dialogs with Data Binding support.
 *
 * This abstract class simplifies implementing full-screen dialog fragments by managing
 * the Data Binding lifecycle, applying a full-screen style, and providing lifecycle hooks
 * for subclasses to initialize binding and perform additional setup.
 *
 * @param ViewBinding The specific type of [ViewDataBinding] associated with the layout.
 *
 * @property layoutId The resource ID of the layout to inflate and bind.
 *
 * @since 1.10.10
 */
abstract class FullScreenAlertDialogFragment<ViewBinding : ViewDataBinding> : DialogFragment() {

    private var _binding: ViewBinding? = null

    /**
     * The binding instance associated with this fragment's view.
     *
     * This property is only valid between [onCreateView] and [onDestroyView].
     *
     * @throws IllegalStateException if accessed outside of the valid lifecycle window.
     */
    val binding get() = _binding!!

    @get:LayoutRes
    abstract val layoutId: Int

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_MaterialDialog_FullScreen)
        executeCreate()
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initializeBinding()
        binding.lifecycleOwner = viewLifecycleOwner
        executeOnCreate()
        return binding.root
    }

    final override fun onDestroyView() {
        super.onDestroyView()
        executeOnDestroyView()
        _binding = null
    }

    final override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        executeOnStart()
    }

    /**
     * Collects data from a Kotlin [Flow] scoped to the [viewLifecycleOwner]'s lifecycle.
     *
     * This utility method launches a coroutine in the lifecycle scope of the fragment's view,
     * and repeats the collection when the lifecycle is at least at the specified [state].
     *
     * @param state The minimum [Lifecycle.State] at which collection starts. Defaults to [Lifecycle.State.STARTED].
     * @param block The suspend lambda containing the code to collect from the flow.
     *
     * @see Lifecycle
     * @see Lifecycle.State
     * @see androidx.lifecycle.LifecycleOwner.lifecycleScope
     * @see androidx.lifecycle.LifecycleOwner.repeatOnLifecycle
     *
     * @since 1.10.10
     */
    protected fun collectDataFlow(
        state: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state) {
                block()
            }
        }
    }

    /**
     * Called after [DialogFragment.onCreate] is executed.
     *
     * Override this method to perform initialization logic during fragment creation.
     *
     * Default implementation is a no-op.
     *
     * @since 1.14.10
     */
    protected open fun executeCreate(): Unit = Unit

    /**
     * Called during [onDestroyView], before the binding is cleared.
     *
     * Override this method to execute any cleanup logic tied to the view's lifecycle.
     *
     * Default implementation is a no-op.
     *
     * @see [onDestroyView]
     * @see [ViewDataBinding]
     *
     * @since 1.14.10
     */
    protected open fun executeOnDestroyView(): Unit = Unit

    /**
     * Called during [onCreateView] after the binding is initialized and the lifecycle owner is set.
     *
     * Override this method to perform additional view setup or initialization.
     *
     * Default implementation is a no-op.
     *
     * @since 1.14.10
     */
    protected open fun executeOnCreate(): Unit = Unit

    /**
     * Hook method called immediately after [DialogFragment.onStart].
     *
     * This function is invoked when the dialog is visible and ready to be interacted with,
     * allowing subclasses to perform additional setup such as UI adjustments or window configurations.
     * By default, this method performs no operation.
     *
     * @since 1.14.10
     */
    protected open fun executeOnStart(): Unit = Unit

    /**
     * Initializes data inside the [binding] when inflating the XML layout.
     *
     * Override this method to set binding variables such as view models or handlers.
     *
     * Default implementation is a no-op.
     *
     * @see [ViewDataBinding]
     *
     * @since 1.14.10
     */
    protected open fun initializeBinding(): Unit = Unit

}

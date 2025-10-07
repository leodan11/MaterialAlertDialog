package com.github.leodan11.alertdialog.dist.base

import android.app.Dialog
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Base [DialogFragment] class that handles view binding using Android Data Binding.
 *
 * This abstract class simplifies the usage of Data Binding in dialog fragments by managing
 * the binding lifecycle automatically and providing hooks for subclasses to customize behavior
 * during view creation and destruction.
 *
 * @param ViewBinding The type of [ViewDataBinding] generated for the layout.
 *
 * @property layoutId The layout resource ID to be inflated and bound.
 *
 * @since 1.10.10
 */
abstract class AlertDialogFragment<ViewBinding : ViewDataBinding> : DialogFragment() {

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

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
        _binding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        builder.setView(binding.root)
        return builder.create()
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializeBinding()
        binding.lifecycleOwner = viewLifecycleOwner
        executeOnCreateView()
        return binding.root
    }

    final override fun onDestroyView() {
        super.onDestroyView()
        executeOnDestroyView()
        _binding = null
    }

    /**
     * Collects data from a Kotlin [Flow] scoped to the [viewLifecycleOwner]'s lifecycle.
     *
     * This utility function launches a coroutine in the lifecycle scope of the fragment's view,
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
     * Called during [onDestroyView], before the binding is cleared.
     *
     * Override this method to execute any cleanup logic tied to the view's lifecycle.
     *
     * Default implementation is a no-op.
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
    protected open fun executeOnCreateView(): Unit = Unit

    /**
     * Initializes data inside the [binding] when inflating the XML layout.
     *
     * Override this method to set binding variables such as view models or handlers.
     *
     * Default implementation is a no-op.
     *
     * @since 1.14.10
     */
    protected open fun initializeBinding(): Unit = Unit
}

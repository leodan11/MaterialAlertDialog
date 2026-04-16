package com.github.leodan11.alertdialog.api.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Base [BottomSheetDialogFragment] class that manages view binding using Android Data Binding.
 *
 * This abstract class simplifies the usage of Data Binding in bottom sheet dialog fragments by
 * handling the binding lifecycle and providing convenient extension points for subclasses to
 * initialize bindings and handle lifecycle events.
 *
 * @param VB The specific type of [ViewDataBinding] generated for the layout.
 *
 * @property layoutId The resource ID of the layout to inflate and bind.
 *
 * @since 1.10.10
 */
abstract class BottomSheetDialogFragment<VB : ViewDataBinding> : BottomSheetDialogFragment() {

    /**
     * The view binding for the fragment. This property is only valid between `onCreateView`
     * and `onDestroyView`. It provides access to the views in the layout file.
     */
    private var _binding: VB? = null

    /**
     * The view binding for this fragment.
     *
     * This is a **non-null** getter that returns the inflated binding object. The binding is only valid
     * between `onCreateView` and `onDestroyView`. If accessed outside this range, it will throw an exception.
     */
    protected open val binding
        get() = _binding
            ?: error("ViewBinding is only valid between onCreateView and onDestroyView")

    @get:LayoutRes
    abstract val layoutId: Int

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        onViewSetup()
        return binding.root
    }

    /**
     * Cleans up resources when the fragment's view is destroyed.
     *
     * This method is called during the fragment's lifecycle when the view is destroyed.
     * It nullifies the binding and executes any additional cleanup defined by the subclass.
     *
     * **Important:** This method should NOT be overridden. Instead, use `executeOnDestroyView()` to implement
     * any custom logic or resource cleanup specific to the subclass.
     *
     * This method is provided by the base class to ensure that resources are properly cleaned up
     * and that the binding is set to null when the fragment's view is destroyed.
     */
    final override fun onDestroyView() {
        onViewDestroyed()
        _binding = null
        super.onDestroyView()
    }

    /**
     * This utility method launches a coroutine in the lifecycle scope of the fragment's view,
     * and repeats the collection when the lifecycle is at least at the specified [state].
     *
     * @param state The minimum [Lifecycle.State] at which collection starts. Defaults to [Lifecycle.State.STARTED].
     * @param block The suspend lambda containing the code to collect from the flow.
     *
     * @see Lifecycle
     * @see Lifecycle.State
     * @see lifecycleScope
     * @see repeatOnLifecycle
     *
     * @since 1.14.12
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
}
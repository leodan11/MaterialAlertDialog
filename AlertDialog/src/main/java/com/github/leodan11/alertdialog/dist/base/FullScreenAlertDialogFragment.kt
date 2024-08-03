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
import kotlinx.coroutines.launch

abstract class FullScreenAlertDialogFragment<ViewBinding : ViewDataBinding> : DialogFragment() {

    private var _binding: ViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_MaterialDialog_FullScreen)
        onCreated()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        setInitDataInViewBinding()
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }


    /**
     * Scope to get flow type data
     *
     * @param state [Lifecycle.State] default [Lifecycle.State.STARTED]
     * @param block Code block containing one or more launch
     *
     */
    fun collectDataFlow(
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
     * It is executed after calling function overrides: [onCreated] in class [DialogFragment].
     */
    open fun onCreated(): Unit = Unit

    /**
     * Initialize data inside data binding when inflating XML
     *
     */
    open fun setInitDataInViewBinding(): Unit = Unit

}

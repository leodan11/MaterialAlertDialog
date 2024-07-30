package com.github.leodan11.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.leodan11.alertdialog.dist.base.MaterialAlertDialogFragment
import com.github.leodan11.dialog.databinding.FragmentBlankBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : MaterialAlertDialogFragment<FragmentBlankBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_blank

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            button.setOnClickListener {
                if (TextUtils.isEmpty(textInputEditText.text.toString().trim())) {
                    textInputLayout.isErrorEnabled = true
                    textInputLayout.error =
                        getString(com.github.leodan11.alertdialog.R.string.text_value_error)
                } else {
                    textInputLayout.isErrorEnabled = false
                    Toast.makeText(
                        requireContext(),
                        textInputEditText.text.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            buttonActionNegative.setOnClickListener { dialog?.dismiss() }

            buttonActionPositive.setOnClickListener {
                if (!TextUtils.isEmpty(textInputEditText.text.toString().trim())) {
                    dialog?.dismiss()
                }
            }

        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            BlankFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}
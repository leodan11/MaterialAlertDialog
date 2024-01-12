package com.github.leodan11.dialog

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.leodan11.alertdialog.AboutMaterialDialog
import com.github.leodan11.alertdialog.IOSAlertDialog
import com.github.leodan11.alertdialog.IOSProgressDialog
import com.github.leodan11.alertdialog.MaterialAlertDialogCentered
import com.github.leodan11.alertdialog.MaterialAlertDialogEvents
import com.github.leodan11.alertdialog.MaterialAlertDialogInput
import com.github.leodan11.alertdialog.MaterialAlertDialogProgress
import com.github.leodan11.alertdialog.MaterialAlertDialogProgressSmall
import com.github.leodan11.alertdialog.MaterialAlertDialogSignIn
import com.github.leodan11.alertdialog.MaterialAlertDialogVerificationCode
import com.github.leodan11.alertdialog.ProgressMaterialDialog
import com.github.leodan11.dialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            buttonActionAbout.setOnClickListener {
                AboutMaterialDialog.Builder(this@MainActivity)
                    .setApplicationIcon(R.mipmap.ic_launcher_round)
                    .setApplicationName(R.string.app_name)
                    .setApplicationVersion("Lorem Ipsum")
                    .setApplicationLegalese("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setPositiveButton(null) { dialog, _ -> dialog?.dismiss() }
                    .create()
                    .show()
            }

            buttonActionAlert.setOnClickListener {
                MaterialAlertDialogCentered.Builder(this@MainActivity)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setPositiveButton(
                        null,
                        R.drawable.ic_baseline_light_mode
                    ) { dialog, _ -> dialog?.dismiss() }
                    .create()
                    .show()
            }

            buttonActionAlertIOS.setOnClickListener {
                IOSAlertDialog.Builder(this@MainActivity)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setPositiveButton(null) { dialog, _ -> dialog?.dismiss() }
                    .setNegativeButton(null) { dialog, _ -> dialog?.dismiss() }
                    .create()
                    .show()
            }

            buttonActionAlertPlus.setOnClickListener {
                MaterialAlertDialogEvents.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setBackgroundColorSpanRGB(Color.rgb(123, 200, 150))
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setDetails("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                    .setCancelable(false)
                    .setPositiveButton(null) { dialog, _ -> dialog?.dismiss() }
                    .setNeutralButton(null) { dialog, _ -> dialog?.dismiss() }
                    .setNegativeButton(
                        null,
                        R.drawable.ic_baseline_close
                    ) { dialog, _ -> dialog?.dismiss() }
                    .create()
                    .show()
            }

            buttonActionCode.setOnClickListener {
                MaterialAlertDialogVerificationCode.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setPositiveButton(null) { dialog, _, _, _, _ -> dialog.dismiss() }
                    .setNegativeButton(
                        null,
                        R.drawable.ic_baseline_close
                    ) { dialog, _ -> dialog?.dismiss() }
                    .create()
                    .show()
            }

            buttonActionLogin.setOnClickListener {
                MaterialAlertDialogSignIn.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setTitle("Lorem Ipsum")
                    .setCancelable(false)
                    .setPositiveButton(null) { dialog, _, _ -> dialog?.dismiss() }
                    .setNegativeButton(
                        null,
                        R.drawable.ic_baseline_close
                    ) { dialog, _ -> dialog?.dismiss() }
                    .create()
                    .show()
            }

            buttonActionProgress.setOnClickListener {
                MaterialAlertDialogProgress.Builder(this@MainActivity)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .create()
                    .show()
            }

            buttonActionProgressIOS.setOnClickListener {
                IOSProgressDialog.Builder(this@MainActivity)
                    .setMessage("What is Lorem Ipsum?")
                    .create()
                    .show()
            }

            buttonActionProgressSmall.setOnClickListener {
                MaterialAlertDialogProgressSmall.Builder(this@MainActivity)
                    .setMessage(null)
                    .create()
                    .show()
            }

            buttonActionCircularProgress.setOnClickListener {
                ProgressMaterialDialog.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setIndeterminable(true)
                    .setCancelable(false)
                    .setNegativeButton(
                        null,
                        R.drawable.ic_baseline_close
                    ) { dialog, _ -> dialog?.dismiss() }
                    .create()
                    .show()
            }

            buttonActionInput.setOnClickListener {
                MaterialAlertDialogInput.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setPositiveButton(null, R.drawable.ic_baseline_close) { dialog, contentValue ->
                        Toast.makeText(this@MainActivity, contentValue, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }

        }

    }
}
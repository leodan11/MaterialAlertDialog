package com.github.leodan11.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.leodan11.alertdialog.AboutAlertDialog
import com.github.leodan11.alertdialog.IOSAlertDialog
import com.github.leodan11.alertdialog.IOSProgressDialog
import com.github.leodan11.alertdialog.LottieAlertDialog
import com.github.leodan11.alertdialog.ProgressVectorAlertDialog
import com.github.leodan11.alertdialog.MaterialAlertDialogCentered
import com.github.leodan11.alertdialog.MaterialAlertDialogEvents
import com.github.leodan11.alertdialog.MaterialAlertDialogInput
import com.github.leodan11.alertdialog.MaterialAlertDialogVerificationCode
import com.github.leodan11.alertdialog.MaterialAlertDialogVector
import com.github.leodan11.alertdialog.ProgressAlertDialog
import com.github.leodan11.alertdialog.SettingsAlertDialog
import com.github.leodan11.alertdialog.chroma.ColorMode
import com.github.leodan11.alertdialog.chroma.ColorSelectListener
import com.github.leodan11.alertdialog.chroma.MaterialChromaDialog
import com.github.leodan11.alertdialog.io.content.Alert
import com.github.leodan11.alertdialog.io.content.DialogAlertInterface
import com.github.leodan11.alertdialog.io.content.materialAlertDialogSignIn
import com.github.leodan11.alertdialog.io.models.ButtonIconAlert
import com.github.leodan11.dialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val launchSettings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Toast.makeText(this, "Settings activity closed", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            buttonActionAbout.setOnClickListener {
                AboutAlertDialog.Builder(this@MainActivity)
                    .setApplicationIcon(R.mipmap.ic_launcher_round)
                    .setApplicationName(R.string.app_name)
                    .setCountDownTimer(DialogAlertInterface.UI.BUTTON_POSITIVE, 2000)
                    .setApplicationVersion("Lorem Ipsum")
                    .setApplicationLegalese("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setPositiveButton { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            buttonActionAlert.setOnClickListener {
                MaterialAlertDialogCentered.Builder(this@MainActivity)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setCountDownTimer(DialogAlertInterface.UI.BUTTON_POSITIVE, 4000)
                    .setPositiveButton(
                        ButtonIconAlert(
                            R.drawable.ic_baseline_light_mode,
                            Alert.IconGravity.END
                        )
                    ) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            buttonActionAlertIOS.setOnClickListener {
                IOSAlertDialog.Builder(this@MainActivity)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setGravity(Gravity.TOP)
                    .setCountDownTimer(DialogAlertInterface.UI.BUTTON_POSITIVE, 6000)
                    .setPositiveButton { dialog, _ -> dialog.dismiss() }
                    .setNegativeButton { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            buttonActionAlertPlus.setOnClickListener {
                MaterialAlertDialogEvents.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setBackgroundColorSpan(60, 100, 200)
                    .setCountDownTimer(DialogAlertInterface.UI.BUTTON_POSITIVE, 8000)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setDetails("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Elettra sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                    .setCancelable(false)
                    .setPositiveButton { dialog, _ -> dialog.dismiss() }
                    .setNeutralButton { dialog, _ -> dialog.dismiss() }
                    .setNegativeButton(ButtonIconAlert(R.drawable.ic_baseline_close)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            buttonActionCode.setOnClickListener {
                MaterialAlertDialogVerificationCode.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setTitle("Lorem Ipsum")
                    .setInputCodeLength(2)
                    .setMessage("What is Lorem Ipsum?")
                    .setCancelable(false)
                    .setMessageOTP("Enter code below")
                    .setCountDownTimer(DialogAlertInterface.UI.BUTTON_POSITIVE, 2000)
                    .setPositiveButton { dialog, code, reason, firstInput, secondInput ->
                        Toast.makeText(
                            this@MainActivity,
                            "Code: $code, Reason: $reason, First: $firstInput, Second: $secondInput",
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton(ButtonIconAlert(R.drawable.ic_baseline_close)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            buttonActionLogin.setOnClickListener {
                materialAlertDialogSignIn {
                    setIcon(R.drawable.ic_baseline_light_mode)
                    setTitle("Lorem Ipsum")
                    setBoxCornerRadius(30f)
                    setCountDownTimer(DialogAlertInterface.UI.BUTTON_POSITIVE, 4000)
                    setCancelable(false)
                    setPositiveButton { dialog, _, _ -> dialog.dismiss() }
                    setNegativeButton(ButtonIconAlert(R.drawable.ic_baseline_close)) { dialog, _ -> dialog.dismiss() }
                }.show()
            }

            buttonActionProgress.setOnClickListener {
                ProgressVectorAlertDialog.Builder(this@MainActivity)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setTimeout(5000)
                    .create()
                    .show()
            }

            buttonActionProgressIOS.setOnClickListener {
                IOSProgressDialog.Builder(this@MainActivity)
                    .setMessage("What is Lorem Ipsum?")
                    .create()
                    .show()
            }

            buttonActionProgresLottie.setOnClickListener {
                LottieAlertDialog.Builder(this@MainActivity)
                    .setAnimation(com.github.leodan11.alertdialog.R.raw.animation_welcome)
                    .setCancelable(false)
                    .setLayoutParams(200)
                    .setTimeout(10000)
                    .create()
                    .show()
            }

            buttonActionProgressSmall.setOnClickListener {
                MaterialAlertDialogVector.Builder(this@MainActivity)
                    .setMessage(com.github.leodan11.alertdialog.R.string.label_text_charging_please)
                    .create()
                    .show()
            }

            buttonActionCircularProgress.setOnClickListener {
                ProgressAlertDialog.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setIndeterminable(true)
                    .setCancelable(false)
                    .setNegativeButton(ButtonIconAlert(R.drawable.ic_baseline_close)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            buttonActionInput.setOnClickListener {
                MaterialAlertDialogInput.Builder(this@MainActivity)
                    .setIcon(R.drawable.ic_baseline_light_mode)
                    .setTitle("Lorem Ipsum")
                    .setMessage("What is Lorem Ipsum?")
                    .setCountDownTimer(DialogAlertInterface.UI.BUTTON_POSITIVE, 6000)
                    .setCancelable(false)
                    .setPositiveButton(ButtonIconAlert(R.drawable.ic_baseline_close)) { dialog, contentValue ->
                        Toast.makeText(this@MainActivity, contentValue, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }

            buttonActionFragment.setOnClickListener {
                val dialog: BlankFragment = BlankFragment.newInstance()
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, dialog.tag)
            }

            buttonActionSettings.setOnClickListener {
                SettingsAlertDialog.Builder(this@MainActivity)
                    .setLaunch(launchSettings)
                    .setCancelable(false)
                    .setPositiveButton { dialog, _ -> dialog.dismiss() }
                    .setNegativeButton { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            buttonActionChroma.setOnClickListener {
                MaterialChromaDialog.Builder()
                    .initialColor(Color.BLUE)
                    .colorMode(ColorMode.HSV)
                    .onColorSelected(object : ColorSelectListener {
                        override fun onColorSelected(color: Int) {
                            buttonActionChroma.setBackgroundColor(color)
                        }
                    })
                    .create()
                    .show(supportFragmentManager, "ChromaDialog")
            }

        }

    }
}
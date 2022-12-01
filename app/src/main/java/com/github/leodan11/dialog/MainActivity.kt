package com.github.leodan11.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.leodan11.alertdialog.*
import com.github.leodan11.alertdialog.dist.helpers.AlertDialog
import com.github.leodan11.dialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonActionAlert.setOnClickListener {
            MaterialAlertDialog.Builder(this@MainActivity)
                .setType(AlertDialog.DIALOG_STYLE_INFORMATION)
                .setTitle("Lorem Ipsum")
                .setMessage("What is Lorem Ipsum?")
                .setDetails("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                .setCancelable(false)
                .setPositiveButton(null, R.drawable.ic_baseline_light_mode) { dialog, _ -> dialog?.dismiss() }
                .create().show()
        }

        binding.buttonActionAlertPlus.setOnClickListener {
            MaterialAlertDialog.Builder(this@MainActivity)
                .setIcon(R.drawable.ic_baseline_light_mode)
                .setTitle("Lorem Ipsum")
                .setMessage("What is Lorem Ipsum?")
                .setDetails("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                .setCancelable(false)
                .setPositiveButton(null) { dialog, _ -> dialog?.dismiss() }
                .setNeutralButton(null) { dialog, _ -> dialog?.dismiss() }
                .setNegativeButton(null, R.drawable.ic_baseline_close) { dialog, _ -> dialog?.dismiss() }
                .create().show()
        }

        binding.buttonActionCode.setOnClickListener {
            MaterialCodeAlertDialog.Builder(this@MainActivity)
                .setIcon(R.drawable.ic_baseline_light_mode)
                .setTitle("Lorem Ipsum")
                .setMessage("What is Lorem Ipsum?")
                .setCancelable(false)
                .setPositiveButton(null) { dialog, _, _, _, _ -> dialog.dismiss() }
                .setNegativeButton(null, R.drawable.ic_baseline_close) { dialog, _ -> dialog?.dismiss() }
                .create().show()
        }

        binding.buttonActionLogin.setOnClickListener {
            MaterialLoginAlertDialog.Builder(this@MainActivity)
                .setIcon(R.drawable.ic_baseline_light_mode)
                .setTitle("Lorem Ipsum")
                .setCancelable(false)
                .setPositiveButton(null) { dialog, _, _ -> dialog?.dismiss() }
                .setNegativeButton(null, R.drawable.ic_baseline_close) { dialog, _ -> dialog?.dismiss() }
                .create().show()
        }

        binding.buttonActionProgress.setOnClickListener {
            MaterialProgressAlertDialog.Builder(this@MainActivity)
                .setTitle("Lorem Ipsum")
                .setMessage("What is Lorem Ipsum?")
                .create().show()
        }

        binding.buttonActionCircularProgress.setOnClickListener {
            MaterialCircularProgressAlertDialog.Builder(this@MainActivity)
                .setIcon(R.drawable.ic_baseline_light_mode)
                .setTitle("Lorem Ipsum")
                .setMessage("What is Lorem Ipsum?")
                .setIndeterminable(true)
                .setCancelable(false)
                .setNegativeButton(null, R.drawable.ic_baseline_close) { dialog, _ -> dialog?.dismiss() }
                .create().show()
        }

    }
}
package com.github.leodan11.alertdialog.io.content

import android.content.Context
import android.content.res.Configuration
import androidx.fragment.app.Fragment
import com.github.leodan11.alertdialog.AboutAlertDialog
import com.github.leodan11.alertdialog.IOSAlertDialog
import com.github.leodan11.alertdialog.IOSProgressDialog
import com.github.leodan11.alertdialog.LottieAlertDialog
import com.github.leodan11.alertdialog.MaterialAlertDialogCentered
import com.github.leodan11.alertdialog.MaterialAlertDialogEvents
import com.github.leodan11.alertdialog.MaterialAlertDialogInput
import com.github.leodan11.alertdialog.MaterialAlertDialogAnimatedDrawable
import com.github.leodan11.alertdialog.MaterialAlertDialogSignIn
import com.github.leodan11.alertdialog.MaterialAlertDialogVerificationCode
import com.github.leodan11.alertdialog.MaterialDialogAnimatedDrawable
import com.github.leodan11.alertdialog.ProgressAlertDialog
import com.github.leodan11.alertdialog.SettingsAlertDialog
import com.github.leodan11.alertdialog.chroma.MaterialChromaDialog

/**
 * Creates an [AboutAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = aboutMaterialDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertAboutDialog)
 *
 */
inline fun Context.aboutMaterialDialog(init: AboutAlertDialog.Builder.() -> Unit): AboutAlertDialog {
    val dialog = AboutAlertDialog.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [IOSAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = iOSAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/IOSAlertDialog)
 *
 */
inline fun Context.iOSAlertDialog(init: IOSAlertDialog.Builder.() -> Unit): IOSAlertDialog {
    val dialog = IOSAlertDialog.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [IOSProgressDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = iOSProgressDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/IOSProgressDialog)
 *
 */
inline fun Context.iOSProgressDialog(init: IOSProgressDialog.Builder.() -> Unit): IOSProgressDialog {
    val dialog = IOSProgressDialog.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * This function will check if the device is a tablet
 *
 * @receiver [Context] of the application
 *
 * @return `true` if the device is a tablet
 */
fun Context.isTablet(): Boolean {
    return (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
}


/**
 * Creates an [LottieAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = lottieAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/LottieAlertDialog)
 *
 */
inline fun Context.lottieAlertDialog(init: LottieAlertDialog.Builder.() -> Unit): LottieAlertDialog {
    val dialog = LottieAlertDialog.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [MaterialAlertDialogCentered] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogCentered {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogCentered)
 *
 */
inline fun Context.materialAlertDialogCentered(init: MaterialAlertDialogCentered.Builder.() -> Unit): MaterialAlertDialogCentered {
    val dialog = MaterialAlertDialogCentered.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [MaterialAlertDialogEvents] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogEvents {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogEvents)
 *
 */
inline fun Context.materialAlertDialogEvents(init: MaterialAlertDialogEvents.Builder.() -> Unit): MaterialAlertDialogEvents {
    val dialog = MaterialAlertDialogEvents.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [MaterialAlertDialogInput] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogInput {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogInput)
 *
 */
inline fun Context.materialAlertDialogInput(init: MaterialAlertDialogInput.Builder.() -> Unit): MaterialAlertDialogInput {
    val dialog = MaterialAlertDialogInput.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [MaterialAlertDialogAnimatedDrawable] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogAnimatedDrawable {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogAnimatedDrawable)
 *
 */
inline fun Context.materialAlertDialogAnimatedDrawable(init: MaterialAlertDialogAnimatedDrawable.Builder.() -> Unit): MaterialAlertDialogAnimatedDrawable {
    val dialog = MaterialAlertDialogAnimatedDrawable.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [MaterialAlertDialogSignIn] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogSignIn {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogSignIn)
 *
 */
inline fun Context.materialAlertDialogSignIn(init: MaterialAlertDialogSignIn.Builder.() -> Unit): MaterialAlertDialogSignIn {
    val dialog = MaterialAlertDialogSignIn.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [MaterialAlertDialogVerificationCode] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogVerificationCode {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogVerificationCode)
 *
 */
inline fun Context.materialAlertDialogVerificationCode(init: MaterialAlertDialogVerificationCode.Builder.() -> Unit): MaterialAlertDialogVerificationCode {
    val dialog = MaterialAlertDialogVerificationCode.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [MaterialDialogAnimatedDrawable] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialDialogAnimatedDrawable {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialDialogAnimatedDrawable)
 *
 */
inline fun Context.materialDialogAnimatedDrawable(init: MaterialDialogAnimatedDrawable.Builder.() -> Unit): MaterialDialogAnimatedDrawable {
    val dialog = MaterialDialogAnimatedDrawable.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [ProgressAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = progressAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/ProgressAlertDialog)
 *
 */
inline fun Context.progressAlertDialog(init: ProgressAlertDialog.Builder.() -> Unit): ProgressAlertDialog {
    val dialog = ProgressAlertDialog.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [SettingsAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = settingsAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/SettingsAlertDialog)
 *
 */
inline fun Context.settingsAlertDialog(init: SettingsAlertDialog.Builder.() -> Unit): SettingsAlertDialog {
    val dialog = SettingsAlertDialog.Builder(this)
    dialog.init()
    return dialog.create()
}


/**
 * Creates an [AboutAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = aboutMaterialDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertAboutDialog)
 *
 */
inline fun Fragment.aboutMaterialDialog(init: AboutAlertDialog.Builder.() -> Unit): AboutAlertDialog {
    return this.requireActivity().aboutMaterialDialog(init)
}


/**
 * Creates an [IOSAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = iOSAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/IOSAlertDialog)
 *
 */
inline fun Fragment.iOSAlertDialog(init: IOSAlertDialog.Builder.() -> Unit): IOSAlertDialog {
    return this.requireActivity().iOSAlertDialog(init)
}


/**
 * Creates an [IOSProgressDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = iOSProgressDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/IOSProgressDialog)
 *
 */
inline fun Fragment.iOSProgressDialog(init: IOSProgressDialog.Builder.() -> Unit): IOSProgressDialog {
    return this.requireActivity().iOSProgressDialog(init)
}


/**
 * This function will check if the device is a tablet
 *
 * @receiver [Fragment] or class that extends `Fragment`
 *
 * @return `true` if the device is a tablet
 */
fun Fragment.isTablet(): Boolean {
    return this.requireActivity().isTablet()
}


/**
 * Creates an [LottieAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = lottieAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/LottieAlertDialog)
 *
 */
inline fun Fragment.lottieAlertDialog(init: LottieAlertDialog.Builder.() -> Unit): LottieAlertDialog {
    return this.requireActivity().lottieAlertDialog(init)
}


/**
 * Creates an [MaterialAlertDialogCentered] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogCentered {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogCentered)
 *
 */
inline fun Fragment.materialAlertDialogCentered(init: MaterialAlertDialogCentered.Builder.() -> Unit): MaterialAlertDialogCentered {
    return this.requireActivity().materialAlertDialogCentered(init)
}


/**
 * Creates an [MaterialAlertDialogEvents] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogEvents {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogEvents)
 *
 */
inline fun Fragment.materialAlertDialogEvents(init: MaterialAlertDialogEvents.Builder.() -> Unit): MaterialAlertDialogEvents {
    return this.requireActivity().materialAlertDialogEvents(init)
}


/**
 * Creates an [MaterialAlertDialogInput] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogInput {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogInput)
 *
 */
inline fun Fragment.materialAlertDialogInput(init: MaterialAlertDialogInput.Builder.() -> Unit): MaterialAlertDialogInput {
    return this.requireActivity().materialAlertDialogInput(init)
}


/**
 * Creates an [MaterialAlertDialogAnimatedDrawable] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogAnimatedDrawable {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogAnimatedDrawable)
 *
 */
inline fun Fragment.materialAlertDialogAnimatedDrawable(init: MaterialAlertDialogAnimatedDrawable.Builder.() -> Unit): MaterialAlertDialogAnimatedDrawable {
    return this.requireActivity().materialAlertDialogAnimatedDrawable(init)
}


/**
 * Creates an [MaterialAlertDialogSignIn] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogSignIn {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogSignIn)
 *
 */
inline fun Fragment.materialAlertDialogSignIn(init: MaterialAlertDialogSignIn.Builder.() -> Unit): MaterialAlertDialogSignIn {
    return this.requireActivity().materialAlertDialogSignIn(init)
}


/**
 * Creates an [MaterialAlertDialogVerificationCode] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialAlertDialogVerificationCode {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialAlertDialogVerificationCode)
 *
 */
inline fun Fragment.materialAlertDialogVerificationCode(init: MaterialAlertDialogVerificationCode.Builder.() -> Unit): MaterialAlertDialogVerificationCode {
    return this.requireActivity().materialAlertDialogVerificationCode(init)
}


/**
 * Creates an [MaterialDialogAnimatedDrawable] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialDialogAnimatedDrawable {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialDialogAnimatedDrawable)
 *
 */
inline fun Fragment.materialDialogAnimatedDrawable(init: MaterialDialogAnimatedDrawable.Builder.() -> Unit): MaterialDialogAnimatedDrawable {
    return this.requireActivity().materialDialogAnimatedDrawable(init)
}


/**
 * Creates an [ProgressAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = progressAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/ProgressAlertDialog)
 *
 */
inline fun Fragment.progressAlertDialog(init: ProgressAlertDialog.Builder.() -> Unit): ProgressAlertDialog {
    return this.requireActivity().progressAlertDialog(init)
}


/**
 * Creates an [SettingsAlertDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = settingsAlertDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/SettingsAlertDialog)
 *
 */
inline fun Fragment.settingsAlertDialog(init: SettingsAlertDialog.Builder.() -> Unit): SettingsAlertDialog {
    return this.requireActivity().settingsAlertDialog(init)
}


/**
 * Creates an [MaterialChromaDialog] with the arguments supplied to this builder.
 *
 * ```kotlin
 *
 *  val dialog = materialChromaDialog {
 *      ...
 *  }
 *  dialog.show()
 *
 * ```
 *
 * See [wiki](https://github.com/leodan11/MaterialAlertDialog/wiki/MaterialChromaDialog)
 *
 */
inline fun materialChromaDialog(init: MaterialChromaDialog.Builder.() -> Unit): MaterialChromaDialog {
    val dialog = MaterialChromaDialog.Builder()
    dialog.init()
    return dialog.create()
}

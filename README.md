# MaterialAlertDialog

[![](https://jitpack.io/v/leodan11/MaterialAlertDialog.svg)](https://jitpack.io/#leodan11/MaterialAlertDialog)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)

A fluent and extensible dialog API for Kotlin and Android.

# Installation

<details>
  <summary>Gradle</summary>

- Step 1. Add the JitPack repository to your build file

  Add it in your root build.gradle at the end of repositories:

  ```gradle
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
      }
  }
  ```

- Step 2. Add the dependency

  ```gradle
  dependencies {
    implementation 'com.github.leodan11:MaterialAlertDialog:{latest version}'
  }
  ```

</details>

<details>
    <summary>Kotlin</summary>

- Step 1. Add the JitPack repository to your build file.

  Add it in your root build.gradle at the end of repositories:

  ```kotlin
  repositories {
      ...
      maven(url = "https://jitpack.io")
  }
  ```

- Step 2. Add the dependency

    ```kotlin
    dependencies {
      implementation("com.github.leodan11:MaterialAlertDialog:${latest version}")
    }
    ```

</details>

<details>
    <summary>Moven</summary>

- Step 1. Add the JitPack repository

  ```xml
  <repositories>
    <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
    </repository>
  </repositories>
  ```

- Step 2. Add the dependency

  ```xml
  <dependency>
    <groupId>com.github.leodan11</groupId>
      <artifactId>MaterialAlertDialog</artifactId>
      <version>latest version</version>
   </dependency>
  ```

</details>

# Usage

## Material AlertDialog

Alert dialog, to display messages of different types.

- Init

```kotlin
MaterialAlertDialog.Builder(this)
```

> You have different [types](https://github.com/leodan11/MaterialAlertDialog/tree/master/images):

- AlertDialog.DIALOG_STYLE_CUSTOM
- AlertDialog.DIALOG_STYLE_ERROR
- AlertDialog.DIALOG_STYLE_HELP
- AlertDialog.DIALOG_STYLE_INFORMATION
- AlertDialog.DIALOG_STYLE_SUCCESS
- AlertDialog.DIALOG_STYLE_WARNING

> Simple definition:

```kotlin
MaterialAlertDialog.Builder(this)
    .setType(AlertDialog.DIALOG_STYLE_SUCCESS)
    .setTitle("Example")
    .setMessage("Example Message")
    .setDetails("Example Details")
    .setPositiveButton(null, object : AlertDialogInterface.OnClickListener {
        override fun onClick(dialog: AlertDialogInterface, which: Int) {
            dialog.dismiss()
        }
    })
    .create()
    .show()
```

## Material Circular Progress AlertDialog

Create a dialog, with circular progress.

- Init

```kotlin
MaterialCircularProgressAlertDialog.Builder(this)
```

> Simple definition:

```kotlin
MaterialCircularProgressAlertDialog.Builder(this)
    .setTitle("Example")
    .setMessage("Example Message")
    .setNegativeButton(null, object : AlertDialogInterface.OnClickListener {
        override fun onClick(dialog: AlertDialogInterface, which: Int) {
            dialog.dismiss()
        }
    })
    .create()
    .show()
```

## Material Code AlertDialog

Create a dialog, with for access by means of a 6-digit code.

- Init

```kotlin
MaterialCodeAlertDialog.Builder(this)
```

> Simple definition:

```kotlin
MaterialCodeAlertDialog.Builder(this)
    .setTitle("Example")
    .setMessage("Example Message")
    .setPositiveButton(null, object : AlertDialogInterface.OnChildClickListenerInput {
        override fun onClick(
            dialog: AlertDialogInterface,
            code: String,
            reason: String,
            numberDecimal: Double?,
            valuePercentage: Double?
        ) {
            dialog.dismiss()
        }
    })
    .setNegativeButton(null, object : AlertDialogInterface.OnClickListener {
        override fun onClick(dialog: AlertDialogInterface, which: Int) {
            dialog.dismiss()
        }
    })
    .create()
    .show()
```

## Material Login AlertDialog

Create a dialog, for login.

- Init

```kotlin
MaterialLoginAlertDialog.Builder(this)
```

> Simple definition:

```kotlin
MaterialLoginAlertDialog.Builder(this)
    .setTitle("Example")
    .setPositiveButton(null, object : AlertDialogInterface.OnClickInvokedCallback {
        override fun onClick(dialog: AlertDialogInterface, username: String, password: String) {
            dialog.dismiss()
        }
    })
    .create()
    .show()
```

## Material Progress AlertDialog

Create a dialog, to wait for a progress through an animated drawable.

- Init

```kotlin
MaterialProgressAlertDialog.Builder(this)
```

> Simple definition:

```kotlin
MaterialProgressAlertDialog.Builder(this)
    .setIcon(R.drawable.ic_animated)
    .setTitle("Example")
    .setMessage("Example Message")
    .setNegativeButton(null, object : AlertDialogInterface.OnClickListener {
        override fun onClick(dialog: AlertDialogInterface, which: Int) {
            dialog.dismiss()
        }
    })
    .create().show()
```

## Material Small Progress AlertDialog

Create a dialog, to wait for a progress through an animated drawable. Only message visible

- Init

```kotlin
MaterialProgressSmallDialog.Builder(this)
```

> Simple definition:

```kotlin
val dialog = MaterialProgressSmallDialog.Builder(this)
    .setIcon(R.drawable.ic_animated)
    .setMessage("Example Message")
    .create()

dialog.show()
dialog.dismiss()
```
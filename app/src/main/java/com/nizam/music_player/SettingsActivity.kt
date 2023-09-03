package com.nizam.music_player

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nizam.music_player.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sortBy()

        appearance()

        @Suppress("DEPRECATION")
        binding.versionInfo.text = "App Version : ${applicationContext.packageManager.getPackageInfo(packageName,0).versionName}"
    }

    @SuppressLint("ResourceType")
    private fun appearance() {

        binding.appearance.setOnClickListener {
            val sharedPreferencesAmplifted = SharedPreferencesAmplifted(this@SettingsActivity)
            val light = RadioButton(this@SettingsActivity)
            val dark = RadioButton(this@SettingsActivity)
            val system = RadioButton(this@SettingsActivity)


            light.setText(R.string.light_theme)
            light.id = 1
            dark.setText(R.string.dark_theme)
            dark.id = 2
            system.setText(R.string.system_theme)
            system.id = 3

            when (sharedPreferencesAmplifted.getUiMode()) {
                "light" -> light.isChecked = true
                "dark" -> dark.isChecked = true
                "system" -> system.isChecked = true
            }

            val radioGroup = RadioGroup(this@SettingsActivity)
            //byNameAsc.isChecked = true

            radioGroup.addView(light)
            radioGroup.addView(dark)
            radioGroup.addView(system)

            val linearLayout = LinearLayout(this@SettingsActivity)
            linearLayout.addView(radioGroup)
            linearLayout.setPadding(40,10,40,10)

            MaterialAlertDialogBuilder(this)
                .setTitle("Theme")
                .setView(linearLayout)
                .setMessage("Select Theme Mode.")
                .setPositiveButton("Apply") { _, _ ->
                    println(radioGroup.checkedRadioButtonId.toString())
                    when (radioGroup.checkedRadioButtonId) {
                        1 -> {
                            sharedPreferencesAmplifted.setUiMode("light")
                            setTheme()
                        }
                        2 -> {
                            sharedPreferencesAmplifted.setUiMode("dark")
                            setTheme()
                        }
                        3 -> {
                            sharedPreferencesAmplifted.setUiMode("system")
                            setTheme()
                        }

                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()
        }
    }

    private fun setTheme() {
        when (SharedPreferencesAmplifted(this).getUiMode()) {
            "light" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            "dark" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            "system" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun sortBy() {
        val sharedPreferencesAmplifted = SharedPreferencesAmplifted(this@SettingsActivity)

        binding.sortBy.setOnClickListener {

            val byNameAsc = RadioButton(this@SettingsActivity)
            val byNameDesc = RadioButton(this@SettingsActivity)
            val byDateAddedDesc = RadioButton(this@SettingsActivity)
            val byDateAddedAsc = RadioButton(this@SettingsActivity)

            byNameAsc.setText(R.string.name_asc)
            byNameAsc.id = 1
            byNameDesc.setText(R.string.name_desc)
            byNameDesc.id = 2
            byDateAddedDesc.setText(R.string.date_desc)
            byDateAddedDesc.id = 3
            byDateAddedAsc.setText(R.string.date_asc)
            byDateAddedAsc.id = 4

            when(sharedPreferencesAmplifted.getSortBy()) {
                1 -> byNameAsc.isChecked=true
                2 -> byNameDesc.isChecked=true
                3 -> byDateAddedDesc.isChecked=true
                4 -> byDateAddedAsc.isChecked=true
            }

            val radioGroup = RadioGroup(this@SettingsActivity)
            radioGroup.addView(byNameAsc)
            radioGroup.addView(byNameDesc)
            radioGroup.addView(byDateAddedDesc)
            radioGroup.addView(byDateAddedAsc)

            val linearLayout = LinearLayout(this@SettingsActivity)
            linearLayout.addView(radioGroup)
            linearLayout.setPadding(40,10,40,10)

            MaterialAlertDialogBuilder(this)
                .setTitle("Sort By")
                .setView(linearLayout)
                .setMessage("Select order of songs displayed.")
                .setPositiveButton("Apply") { _, _ ->
                    println(radioGroup.checkedRadioButtonId.toString())
                    when (radioGroup.checkedRadioButtonId) {
                        1 -> sharedPreferencesAmplifted.setSortBy(1)
                        2 -> sharedPreferencesAmplifted.setSortBy(2)
                        3 -> sharedPreferencesAmplifted.setSortBy(3)
                        4 -> sharedPreferencesAmplifted.setSortBy(4)
                    }
                }
                .setNegativeButton("Cancel"){_,_->}
                .show()


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
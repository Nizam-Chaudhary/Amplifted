package com.nizam.music_player

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nizam.music_player.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sortBy()
    }

    @SuppressLint("ResourceType")
    private fun sortBy() {
        val sharedPreferencesAmplifted = SharedPreferencesAmplifted(this@SettingsActivity)

        binding.sortBy.setOnClickListener {

            val byNameAsc = RadioButton(this@SettingsActivity)
            val byNameDesc = RadioButton(this@SettingsActivity)
            val byDateModifiedDesc = RadioButton(this@SettingsActivity)
            val byDateModifiedAsc = RadioButton(this@SettingsActivity)

            byNameAsc.setText(R.string.name_asc)
            byNameAsc.id = 1
            byNameDesc.setText(R.string.name_desc)
            byNameDesc.id = 2
            byDateModifiedDesc.setText(R.string.date_desc)
            byDateModifiedDesc.id = 3
            byDateModifiedAsc.setText(R.string.date_asc)
            byDateModifiedAsc.id = 4

            when(sharedPreferencesAmplifted.getSortBy()) {
                1 -> byNameAsc.isChecked=true
                2 -> byNameDesc.isChecked=true
                3 -> byDateModifiedDesc.isChecked=true
                4 -> byDateModifiedAsc.isChecked=true
            }

            val radioGroup = RadioGroup(this@SettingsActivity)
            radioGroup.setPadding(10)
            //byNameAsc.isChecked = true

            radioGroup.addView(byNameAsc)
            radioGroup.addView(byNameDesc)
            radioGroup.addView(byDateModifiedDesc)
            radioGroup.addView(byDateModifiedAsc)

            val linearLayout = LinearLayout(this@SettingsActivity)
            linearLayout.addView(radioGroup)
            linearLayout.setPadding(20)

            MaterialAlertDialogBuilder(this)
                .setTitle("Sort By")
                .setView(linearLayout)
                .setPositiveButton("Apply") { _, _ ->
                    println(radioGroup.checkedRadioButtonId.toString())
                    when (radioGroup.checkedRadioButtonId) {
                        1 -> sharedPreferencesAmplifted.setSortBy(1)
                        2 -> sharedPreferencesAmplifted.setSortBy(2)
                        3 -> sharedPreferencesAmplifted.setSortBy(3)
                        4 -> sharedPreferencesAmplifted.setSortBy(4)
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->

                }
                .show()


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
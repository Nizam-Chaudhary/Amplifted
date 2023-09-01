package com.nizam.music_player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "About"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.aboutTv.text = setAbout()

        openGithub()
    }

    private fun setAbout(): String {
        return "Developed By Nizam Chaudhary" +
                "\n\n" +
                "View Source Code :"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun openGithub() {
        binding.githubLink.setOnClickListener {
            val openGithub = Intent(Intent.ACTION_VIEW)
            openGithub.data = Uri.parse("https://github.com/iMxNizam/Amplifted/")
            startActivity(openGithub)
        }
    }
}
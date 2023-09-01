package com.nizam.music_player

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nizam.music_player.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {

    lateinit var binding: ActivityFeedbackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Feedback"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val subject = binding.feedbackTopic.text.toString()
        val feedbackMessage = "${binding.feedbackMessage.text.toString()}  \n Email ID: ${binding.emailID.text.toString()}"

        sendFeedback(subject, feedbackMessage)
    }

    private fun sendFeedback(subject: String, feedbackMessage: String) {
        binding.sendFeedback.setOnClickListener {
            Toast.makeText(this@FeedbackActivity,feedbackMessage,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
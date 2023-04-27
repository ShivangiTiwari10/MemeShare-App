package com.example.memeshare

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.example.memeshare.databinding.ActivityMainBinding

import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url: String = "https://meme-api.com/gimme"
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMemedata()

        binding.buttonNext.setOnClickListener {
            getMemedata()
        }
        binding.buttonShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hey cheackout this cool meme igot from raddit $url")
            val chooser = Intent.createChooser(intent, "share this meme using..")
            startActivity(chooser)
        }
    }


    fun getMemedata() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("progressing")
        progressDialog.show()

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.e("Response", "getMeme data$response")

                val responseObject = JSONObject(response)

                responseObject.get("postLink")
                binding.memeTitle.text = responseObject.getString("title")
                binding.memeAuther.text = responseObject.getString("author")

                Glide.with(this).load(responseObject.get("url")).into(binding.memeImage)
                progressDialog.dismiss()


            }, { error ->
                progressDialog.dismiss()

                Toast.makeText(this@MainActivity, error.localizedMessage, Toast.LENGTH_LONG).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }


}
package com.deba.githubapp.ui.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.deba.githubapp.BuildConfig
import com.deba.githubapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofit: Retrofit
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRetrofit()
        setupLoginBtnClick()
    }

    private fun setupRetrofit() {
        val builder = Retrofit.Builder()
            .baseUrl("https://github.com/")
            .addConverterFactory(GsonConverterFactory.create())
        retrofit = builder.build()
        apiClient = retrofit.create(ApiClient::class.java)
    }

    private fun setupLoginBtnClick() {
        binding.btnLogin.setOnClickListener {
            initiateLoginFlow()
        }
    }

    private fun initiateLoginFlow() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "https://github.com/login/oauth/authorize" +
                        "?client_id=" + BuildConfig.CLIENT_ID +
                        "&scope=repo" +
                        "&redirect_uri=" + BuildConfig.CALLBACK
            )
        )
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        val uri = intent.data
        uri?.let {
            if (it.toString().startsWith(BuildConfig.CALLBACK)) {
                val code = it.getQueryParameter("code")
                if (code != null) {
                    getAccessToken(code)
                } else if (it.getQueryParameter("error") != null){
                    showToast(message = "Error = ${it.getQueryParameter("error")}")
                }
            }
        }
    }

    private fun getAccessToken(code: String) {
        val accessTokenCall = apiClient.getAccessToken(
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET,
            code = code
        )
        accessTokenCall.enqueue(object: Callback<AccessToken>{
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful && response.body() != null) {
                    showToast(message = "AccessCode = ${response.body()?.accessToken}")
                } else {
                    showToast(message = "Request not successful")
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                showToast(message = "Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this@MainActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
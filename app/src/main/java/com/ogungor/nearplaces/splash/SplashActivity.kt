package com.ogungor.nearplaces.splash

import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ogungor.nearplaces.R
import com.ogungor.nearplaces.base.BaseActivity
import com.ogungor.nearplaces.util.RunTimePermissionHelper
import com.ogungor.nearplaces.util.extention.startLocationPermissionActivity
import com.ogungor.nearplaces.util.extention.startMainActivity

class SplashActivity : BaseActivity(), SplashActivityContract.View {

    private val MS_HANDLER_DELAY_TIME = 4000L
    private lateinit var splashActivityPresenter: SplashActivityContract.Presenter
    private lateinit var imageViewSplashGif: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        val runTimePermissionListener = RunTimePermissionHelper(this)
        splashActivityPresenter = SplashActivityPresenter(runTimePermissionListener).apply {
            setView(this@SplashActivity)
            create()
        }
    }
    override fun getLayout(): Int = R.layout.activity_splash

    override fun onDestroy() {
        super.onDestroy()
        splashActivityPresenter.destroy()
    }

    override fun initUi() {
        imageViewSplashGif = findViewById(R.id.image_view_splash_gif)
    }

    override fun startGifAnim() {
        Glide.with(this).load(R.drawable.loc).into(imageViewSplashGif)
    }

    override fun startHandler() {
        Handler().postDelayed({
            splashActivityPresenter.handlerFinished()
        }, MS_HANDLER_DELAY_TIME)
    }

    override fun intentToLocationPermissionActivity() {
        this.startLocationPermissionActivity()
    }

    override fun intentToMainActivity() {
        this.startMainActivity()
    }

    override fun finishCurrentActivity() {
        this.finish()
    }
}
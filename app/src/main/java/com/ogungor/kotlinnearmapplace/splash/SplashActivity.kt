package com.ogungor.kotlinnearmapplace.splash

import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ogungor.kotlinnearmapplace.R
import com.ogungor.kotlinnearmapplace.base.BaseActivity
import com.ogungor.kotlinnearmapplace.util.RunTimePermissionHelper
import com.ogungor.kotlinnearmapplace.util.extention.startLocationPermissionActivity
import com.ogungor.kotlinnearmapplace.util.extention.startMapsActivity

class SplashActivity : BaseActivity(), SplashActivityContract.View {

    private val MS_HANDLER_DELAY_TIME=4000L

    private lateinit var splashActivityPresenter: SplashActivityContract.Presenter

    private lateinit var  imageViewSplashGif: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val runTimePermissionListener=RunTimePermissionHelper(this)

        splashActivityPresenter = SplashActivityPresenter(runTimePermissionListener).apply {
            setView(this@SplashActivity)
            create()
        }


    }

    override fun getLayout(): Int =R.layout.activity_splash

    override fun onDestroy() {
        super.onDestroy()
        splashActivityPresenter.destroy()
    }

    override fun initUi() {
        imageViewSplashGif=findViewById(R.id.image_view_splash_gif)
    }

    override fun startGifAnim() {
        Glide.with(this).load(R.drawable.loc).into(imageViewSplashGif)
    }

    override fun startHandler() {

        Handler().postDelayed({
            splashActivityPresenter.handlerFinished()


        },MS_HANDLER_DELAY_TIME)
    }

    override fun intentToMapsActivity() {
        this.startMapsActivity()
    }

    override fun intentToLocationPermissionActivity() {
        this.startLocationPermissionActivity()
    }

    override fun finishCurrentActivity() {
        this.finish()
    }

}
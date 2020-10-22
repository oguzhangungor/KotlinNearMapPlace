package com.ogungor.kotlinnearmapplace.main

interface MainActivityContract {

    interface Presenter {

        fun setView()

        fun create ()

        fun destroy()


    }

    interface View {

        fun initUi()

        fun stopLocation()


    }


}
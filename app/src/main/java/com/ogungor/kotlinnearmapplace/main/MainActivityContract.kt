package com.ogungor.kotlinnearmapplace.main

interface MainActivityContract {

    interface Presenter {

        fun setView(view : View)

        fun create ()

        fun destroy()


    }

    interface View {

        fun initUi()

        fun stopLocation()


    }


}
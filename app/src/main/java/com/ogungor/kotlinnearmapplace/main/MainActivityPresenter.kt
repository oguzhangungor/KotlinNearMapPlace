package com.ogungor.kotlinnearmapplace.main



class MainActivityPresenter : MainActivityContract.Presenter {
    private var view: MainActivityContract.View?=null
    override fun setView(view : MainActivityContract.View) {
        this.view=view
    }

    override fun create() {
        view?.run {
            initUi()
        }
    }

    override fun destroy() {
        view=null
    }



}
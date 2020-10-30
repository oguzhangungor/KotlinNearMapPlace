package com.ogungor.nearplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ogungor.nearplaces.Common.Common
import com.ogungor.nearplaces.Common.Common.currentResult
import com.ogungor.nearplaces.Model.PlaceDetail
import com.ogungor.nearplaces.Remote.IGoogleAPIService
import kotlinx.android.synthetic.main.activity_view_place.*

class ViewPlaceActivity : AppCompatActivity() {

    internal lateinit var mService: IGoogleAPIService
    var mPlace : PlaceDetail?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)


        //init service
        mService=Common.googleAPIService


        //set empyt for all text
        place_name_text.text=""
        place_address_text.text=""
        place_open_hour.text=""

        button_view_direction.setOnClickListener {

        }


        if(currentResult!!.photos!=null && currentResult!!.photos!!.size>0)
        {
            //image.setImageBitmap(Common.currentResult.photos!![0]

        }

        if (currentResult!!.name!=null)
        {
            place_name_text.text= currentResult!!.name
            place_address_text.text= currentResult!!.reference

        }




    }
}
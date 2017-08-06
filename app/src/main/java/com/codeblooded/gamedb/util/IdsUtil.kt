package com.codeblooded.gamedb.util

import android.util.Log
import com.codeblooded.gamedb.IdCallBackListener
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import org.json.JSONArray
import org.json.JSONObject
import cz.msebera.android.httpclient.Header


/**
 * Created by tejas on 8/6/17.
 */
class IdsUtil internal  constructor( string : String, callBackListener: IdCallBackListener) {

    var listener : IdCallBackListener = callBackListener

    init {

        val requestParams: RequestParams = RequestParams()

        val idsString: StringBuilder = StringBuilder()


        RestClient.addHeaders()
        RestClient.get(string, requestParams, object : JsonHttpResponseHandler() {
            override fun onStart() {
                super.onStart()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
                for (i in 0..(response!!.length() - 1)) {
                    val string :String = response.getJSONObject(i).get("id").toString()
                    if(i==0) idsString.append(string)
                    else idsString.append(","+string)
                }
                Log.e("Ids",idsString.toString())
                listener.method(idsString.toString())
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                super.onFailure(statusCode, headers, responseString, throwable)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONArray?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
            }

        }

        )

    }


}


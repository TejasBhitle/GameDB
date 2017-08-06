package com.codeblooded.gamedb.util

import android.util.Log
import com.codeblooded.gamedb.getAPIKey
import com.codeblooded.gamedb.getBaseUrl
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.loopj.android.http.AsyncHttpClient


/**
 * Created by tejas on 8/5/17.
 */
class RestClient {

    companion object {

        private val BASE_URL = getBaseUrl()
        private var client: AsyncHttpClient? = AsyncHttpClient()

        fun addHeaders(){
            client?.addHeader("user-key", getAPIKey())
            client?.addHeader("Accept", "application/json")
        }

        fun get(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
            Log.d("RestClient GET", url)
            client?.get(getAbsoluteUrl(url), params, responseHandler)
        }

        fun post(url: String, params: RequestParams, responseHandler: AsyncHttpResponseHandler) {
            Log.d("RestClient POST", url)
            client?.post(getAbsoluteUrl(url), params, responseHandler)
        }

        private fun getAbsoluteUrl(relativeUrl: String): String {
            return BASE_URL + relativeUrl
        }

   }


}

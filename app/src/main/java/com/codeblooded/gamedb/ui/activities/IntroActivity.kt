package com.codeblooded.gamedb

import android.os.Bundle

import com.heinrichreimersoftware.materialintro.slide.SimpleSlide


class IntroActivity : com.heinrichreimersoftware.materialintro.app.IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        /* Enable/disable skip button */
        setButtonBackVisible(true)
        setButtonBackFunction(BUTTON_BACK_FUNCTION_SKIP)

        /* Enable/disable finish button */
        setFinishEnabled(true)
        setButtonNextVisible(true)
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH)


        addSlide(SimpleSlide.Builder()
                .title(R.string.app_name)
                .description(R.string.welcome)
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build())

        addSlide(SimpleSlide.Builder()
                .description(R.string.intro1)
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build())

        addSlide(SimpleSlide.Builder()
                .description("You can launch this intro again from the About page")
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build())

    }

}
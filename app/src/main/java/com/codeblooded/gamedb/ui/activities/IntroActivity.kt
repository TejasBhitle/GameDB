package com.codeblooded.gamedb

import android.content.Intent
import android.os.Bundle
import com.codeblooded.gamedb.ui.activities.SignupActivity

import com.heinrichreimersoftware.materialintro.slide.SimpleSlide


class IntroActivity : com.heinrichreimersoftware.materialintro.app.IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        /* Enable/disable skip button */
        isButtonBackVisible = true
        buttonBackFunction = BUTTON_BACK_FUNCTION_SKIP

        /* Enable/disable finish button */
        isFinishEnabled = true
        isButtonNextVisible = true
        buttonNextFunction = BUTTON_NEXT_FUNCTION_NEXT_FINISH


        addSlide(SimpleSlide.Builder()
                .title(R.string.app_name)
                .description(R.string.welcome)
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build())

        addSlide(SimpleSlide.Builder()
                .description(R.string.intro1)
                .image(R.drawable.ic_videogame_asset_black_24dp)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build())

        addSlide(SimpleSlide.Builder()
                .description(R.string.sign_in_description)
                .image(R.drawable.ic_favorite_white_24dp)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .buttonCtaLabel(R.string.sign_in)
                .buttonCtaClickListener {
                    startActivity(Intent(this@IntroActivity, SignupActivity::class.java))
                }
                .build())

    }

}
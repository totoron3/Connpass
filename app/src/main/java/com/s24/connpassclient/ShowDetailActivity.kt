package com.s24.connpassclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ShowDetailActivity : AppCompatActivity()  {
    companion object {
        @JvmStatic
        fun newIntent(context: Context, event: Event): Intent {
            return Intent(context, ShowDetailActivity::class.java).apply {
                putExtra("event", event)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        val event = intent.getParcelableExtra<Event>("event") ?: return

        if (savedInstanceState == null) {
            val fragment = ShowDetailFragment.newInstance(event)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,fragment, ShowDetailFragment.TAG)
                .commit()

        }

    }
}
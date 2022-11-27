package com.s24.connpassclient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailSearchActivity : AppCompatActivity(), DetailSearchFragment.Callback  {

    companion object {
        private const val EXTRA_QUERY = "query"

        fun newIntent(context: Context, query: Query): Intent {
            val intent = Intent(context, DetailSearchActivity::class.java)
            intent.putExtra(EXTRA_QUERY, query)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_search)

        if (savedInstanceState == null) {
            val query: Query = intent.getParcelableExtra(EXTRA_QUERY) ?: Query()
            val fragment = DetailSearchFragment.newInstance(query)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, DetailSearchFragment.TAG)
                .commit()
        }
    }

    override fun searching(query: Query) {

        val result = Intent().apply {
            putExtra("query", query)
        }
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}
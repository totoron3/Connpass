package com.s24.connpassclient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.s24.connpassclient.databinding.SearchBinding

class SearchFragment : Fragment(R.layout.search), LifecycleObserver, EventListAdapter.Callback{
    companion object {
        val TAG = SearchFragment::class.java.simpleName
        private const val BUNDLE_KEY_TIMELINE_ORDINAL = "timeline_type_ordinal"
        private val RESULT_KEY = "${TAG}_TOOT_EDIT_"

        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private var binding: SearchBinding? = null

    private var eventListSnapshot = ArrayList<Event>()
    private lateinit var adapter: EventListAdapter
    private lateinit var layoutManager: LinearLayoutManager


    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            requireContext()
        )
    }


    private val loadNextScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(
            recyclerView: RecyclerView,
            dx: Int,
            dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)

            val isLoadingSnapshot = viewModel.isLoading.value ?: return
            if (isLoadingSnapshot || !viewModel.hasNext) {
                return
            }

            val visibleItemCount = recyclerView.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if ((totalItemCount - visibleItemCount) <= firstVisibleItemPosition) {
                viewModel.loadNext()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventListSnapshot = viewModel.eventList.value ?: ArrayList<Event>().also {
            viewModel.eventList.value = it
        }

        adapter = EventListAdapter(
            layoutInflater,
            eventListSnapshot,
            this
        )

        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        val bindingData: SearchBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return

        bindingData.lifecycleOwner = viewLifecycleOwner
        bindingData.searchBinding = viewModel

        bindingData.recyclerview.also {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addOnScrollListener(loadNextScrollListener)
        }

        bindingData.swipeRefreshLayout.setOnRefreshListener {
            eventListSnapshot.clear()
            viewModel.setSimpleSearch()
            viewModel.loadNext()
        }

        bindingData.button.setOnClickListener {
            eventListSnapshot.clear()
            viewModel.setSimpleSearch()
            viewModel.loadNext()
            hideKeyboard()
        }

        bindingData.search.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    viewModel.setSimpleSearch()
                    eventListSnapshot.clear()
                    viewModel.loadNext()
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }


        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding?.swipeRefreshLayout?.isRefreshing = it
        })

        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            eventListSnapshot = it
            adapter.notifyDataSetChanged()
        })

        viewLifecycleOwner.lifecycle.addObserver(viewModel)
        lifecycle.addObserver(searchObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.detail_search -> {
                launchDetailSearchActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun launchDetailSearchActivity() {
        val intent = DetailSearchActivity.newIntent(requireContext(), viewModel.query)
        searchObserver.start(intent)
    }

    private val searchObserver  by lazy {
        object : DefaultLifecycleObserver {
            private lateinit var startForResult: ActivityResultLauncher<Intent>

            override fun onCreate(owner: LifecycleOwner) {
                val registry = requireActivity().activityResultRegistry
                startForResult =registry.register(
                    RESULT_KEY,
                    owner,
                    ActivityResultContracts.StartActivityForResult()
                ){
                    if (it.resultCode == Activity.RESULT_OK) {
                        viewModel.apply{
                            eventListSnapshot.clear()
                            viewModel.loadNext()
                        }
                    }
                }
            }

            fun start (intent: Intent){
                startForResult.launch(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.unbind()
    }

    override fun openDetail(event: Event) {
        val intent = ShowDetailActivity.newIntent(requireContext(), event)
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val manager = requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
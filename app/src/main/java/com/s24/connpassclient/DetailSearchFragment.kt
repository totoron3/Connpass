package com.s24.connpassclient

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.s24.connpassclient.databinding.EventDetailSearchBinding

class DetailSearchFragment : Fragment(R.layout.event_detail_search) {
    companion object {
        val TAG = DetailSearchFragment::class.java.simpleName
        private const val BUNDLE_QUERY = "query"

        fun newInstance(query: Query): DetailSearchFragment {
            val args = Bundle()
            args.putParcelable(BUNDLE_QUERY, query)
            return DetailSearchFragment().apply {
                arguments = args
            }
        }
    }

    interface Callback {
        fun searching(query: Query)
    }

    private var binding: EventDetailSearchBinding? = null
    private var callback: Callback? = null

    private val viewModel: DetailSearchViewModel by viewModels {
        DetailSearchViewModelFactory(
            requireContext()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setHasOptionsMenu(true)
        callback = context as? Callback
        if (callback == null) {
            throw ClassCastException("$context must implement Callback")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val query: Query = requireArguments().getParcelable(BUNDLE_QUERY) ?: Query()
        viewModel.keyword.value = query.keyword
        viewModel.date.value = query.date
        viewModel.condition.value = query.searchCondition
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bindingData: EventDetailSearchBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return
        viewModel.condition.postValue(R.id.radioButton2)
        bindingData.lifecycleOwner = viewLifecycleOwner
        bindingData.viewModel = viewModel
        viewLifecycleOwner.lifecycle.addObserver(viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_post -> {
                if (viewModel.date.value?.length == 8 || viewModel.date.value.isNullOrBlank()) {
                    val query = Query(
                        keyword = viewModel.keyword.value,
                        date = viewModel.date.value,
                        searchCondition = viewModel.condition.value,
                        prefecture = Prefecture.convertString(viewModel.isChecked.value)
                    )
                    callback?.searching(query)
                } else {
                    binding?.edtDate?.error = getString(R.string.error_date)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}
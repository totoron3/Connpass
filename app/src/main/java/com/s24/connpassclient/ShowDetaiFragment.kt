package com.s24.connpassclient

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.s24.connpassclient.databinding.EventDetailBinding

class ShowDetailFragment : Fragment(R.layout.event_detail)  {
    companion object {
        val TAG = ShowDetailFragment::class.java.simpleName

        fun newInstance(event: Event): ShowDetailFragment {
            val bundle = Bundle().apply {
                putParcelable("event", event)
            }
            return  ShowDetailFragment().apply {
                arguments = bundle
            }
        }
    }
    private var event: Event? = null
    private var binding: EventDetailBinding? = null
    private val viewModel: ShowDetailViewModel by viewModels {
        ShowDetailViewModelFactory(
            event,
            requireContext()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().also {
            event = it.getParcelable("event")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bindingData: EventDetailBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return

        if (event == null) {
            return
        }

        viewModel.event.observe(viewLifecycleOwner,  {
            bindingData.event = it
        })

        viewLifecycleOwner.lifecycle.addObserver(viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.unbind()
    }
}
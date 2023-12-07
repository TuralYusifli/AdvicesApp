package com.example.advicesapp.presentation.history

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.advicesapp.common.BaseFragment
import com.example.advicesapp.R
import com.example.advicesapp.databinding.FragmentSearchHistoryBinding
import com.example.advicesapp.databinding.ItemSavedAdviceBinding
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.presentation.GenericAdapter
import com.example.advicesapp.presentation.SavedAdvicesViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchHistoryFragment : BaseFragment<FragmentSearchHistoryBinding>(
    FragmentSearchHistoryBinding::inflate
) {
    private lateinit var viewModel: SearchHistoryViewModel

    private lateinit var adapter: GenericAdapter<Advice, ItemSavedAdviceBinding, SavedAdvicesViewHolder>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SearchHistoryViewModel::class.java]
        setupRecyclerView()
        subscribeToObservers()
    }

    private fun setupRecyclerView() {
        adapter = GenericAdapter(
            bindingInflater = { inflater, parent, attachToParent ->
                ItemSavedAdviceBinding.inflate(inflater, parent, attachToParent)
            },
            viewHolderFactory = { binding ->
                SavedAdvicesViewHolder(binding)
            },
            itemCallback = object : DiffUtil.ItemCallback<Advice>() {
                override fun areItemsTheSame(oldItem: Advice, newItem: Advice): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Advice, newItem: Advice): Boolean {
                    return oldItem == newItem
                }
            },
            bindViewHolder = { holder, advice ->
                holder.binding.deleteImageView.setImageResource(R.drawable.ic_delete)
                holder.binding.adviceTextView.text = advice.advice
                holder.binding.queryTextView.text = advice.query
            },
            clickListeners = listOf(
                R.id.deleteImageView to { advice ->
                    viewModel.deleteFromSearchAdviceHistory(advice)
                },
            )
        )
        (binding.serchHistoryRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations =
            false
        binding.serchHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.serchHistoryRecyclerView.adapter = adapter
    }

    private fun subscribeToObservers() {
        viewModel.adviceSearchHistory.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}
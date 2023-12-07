package com.example.advicesapp.presentation.saved

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.advicesapp.common.BaseFragment
import com.example.advicesapp.R
import com.example.advicesapp.databinding.FragmentSavedBinding
import com.example.advicesapp.databinding.ItemSavedAdviceBinding
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.presentation.GenericAdapter
import com.example.advicesapp.presentation.SavedAdvicesViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : BaseFragment<FragmentSavedBinding>(FragmentSavedBinding::inflate) {
    private lateinit var viewModel: SavedViewModel

    private lateinit var adapter: GenericAdapter<Advice, ItemSavedAdviceBinding, SavedAdvicesViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SavedViewModel::class.java]

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
                    viewModel.deleteFromSavedAdvices(advice)
                },
            )
        )
        (binding.savedRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations =
            false
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.savedRecyclerView.adapter = adapter
    }

    private fun subscribeToObservers() {
        viewModel.savedAdvices.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}

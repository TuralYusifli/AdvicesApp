package com.example.advicesapp.presentation.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.advicesapp.common.BaseFragment
import com.example.advicesapp.common.Constants.Companion.SAVED
import com.example.advicesapp.R
import com.example.advicesapp.common.State
import com.example.advicesapp.databinding.FragmentSearchBinding
import com.example.advicesapp.databinding.ItemAdviceBinding
import com.example.advicesapp.domain.model.Advice
import com.example.advicesapp.presentation.GenericAdapter
import com.example.advicesapp.presentation.SearchAdviceViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {
    private lateinit var viewModel: SearchViewModel

    private lateinit var adapter: GenericAdapter<Advice, ItemAdviceBinding, SearchAdviceViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setupRecyclerView()
        searchTextListener()
        subscribeToObservers()

    }

    private fun setupRecyclerView() {
        adapter = GenericAdapter(
            bindingInflater = { inflater, parent, attachToParent ->
                ItemAdviceBinding.inflate(inflater, parent, attachToParent)
            },
            viewHolderFactory = { binding ->
                SearchAdviceViewHolder(binding)
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
                with(holder.binding) {
                    queryTextView.text = advice.query
                    adviceTextView.text = advice.advice
                    isSavedImageView.setImageResource(
                        if (advice.isSaved)
                            R.drawable.ic_saved
                        else
                            R.drawable.ic_unsaved
                    )
                }
            },
            clickListeners = listOf(
                R.id.isSavedImageView to { advice ->
                    if (advice.isSaved)
                        viewModel.deleteFromSavedAdvices(advice)
                    else
                        viewModel.insertToSavedAdvices(advice)
                },
            )
        )

        (binding.searchRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations =
            false
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.adapter = adapter
    }

    private fun subscribeToObservers() {
        viewModel.mergedLiveData.observe(viewLifecycleOwner) { list ->
            isLoading(false)
            adapter.submitList(list)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.adviceResponseFlow.collectLatest { state ->
                when (state) {
                    is State.Success -> {
                        Log.i("SearchFragment", "to cached")
                    }

                    is State.Error -> {
                        isLoading(false, state.errorMessage)
                    }

                    is State.Loading -> {
                        isLoading(true)
                    }
                }
            }
        }
    }

    private fun searchTextListener() {
        binding.searchAdviceEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                requireActivity().hideKeyboard(view)
                if (binding.searchAdviceEditText.text.isNotEmpty()) {
                    isLoading(true)
                    viewModel.getAdvice(binding.searchAdviceEditText.text.toString())
                } else {
                    binding.errorTv.setVisibility(false)
                    Toast.makeText(requireContext(), "Enter search advice", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            true
        }
    }

    private fun Activity.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun View.setVisibility(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun isLoading(load: Boolean, error: String? = null) {
        if (load) {
            adapter.submitList(listOf())
            binding.progressBar.setVisibility(true)
            binding.errorTv.setVisibility(false)
        } else {
            binding.progressBar.setVisibility(false)
            binding.errorTv.text = error
            binding.errorTv.setVisibility(true)
        }

    }
}
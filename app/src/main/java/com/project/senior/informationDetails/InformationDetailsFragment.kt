package com.project.senior.informationDetails

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentInformationDetailsBinding
import com.project.senior.informationDetails.recyclerview.DetailsAdapter
import com.project.senior.seniorInformation.SeniorInformationViewModel
import com.project.senior.seniorInformation.recyclerview.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class InformationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentInformationDetailsBinding
    private val viewModel: InformationDetailsViewModel by viewModels()
    private lateinit var detailsAdapter: DetailsAdapter
    private var categoryTitle: String? = null
    private var categoryId: Int? = null
    private var userId: Int? = null
    private var isListEmpty = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding =
            FragmentInformationDetailsBinding.inflate(inflater, container, false)
        val categoryDetailsArgs by navArgs<InformationDetailsFragmentArgs>()
        categoryTitle = categoryDetailsArgs.categoryTitle
        categoryId = categoryDetailsArgs.categoryId
        userId = categoryDetailsArgs.userId
        binding.tvCategoryTitleInformationDetails.text = categoryTitle

        val layoutManger = LinearLayoutManager(context)
        binding.rvInformationDetails.layoutManager = layoutManger
        detailsAdapter = DetailsAdapter()
        binding.rvInformationDetails.adapter = detailsAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.getCategoryDetails(categoryId!!)
        }

        viewModel.getCategoryDetailsResponseState.observe(viewLifecycleOwner, Observer { state->

            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        viewModel.detailsList.observe(viewLifecycleOwner, Observer { list ->
            if(list?.size == 0) {
                isListEmpty = true
                binding.rvInformationDetails.visibility = View.GONE
                binding.progressBarInformationDetails.visibility = View.GONE
                binding.groupEmptyDetails.visibility = View.VISIBLE
            }
            else {
                isListEmpty = false
                detailsAdapter.submitList(list)
            }
        })

        clickListener()
    }

    private fun clickListener() {
        binding.imgBackInformationDetails.setOnClickListener {
            backToCategoriesFragment()
        }

        detailsAdapter.onDeleteClick = {categoryDetailsData ->

            showDeleteDialog(categoryDetailsData.id)

        }
    }

    private fun backToCategoriesFragment() {
        findNavController().popBackStack()
    }

    private fun successState() {
        if(isListEmpty) {
            binding.rvInformationDetails.visibility = View.GONE
            binding.progressBarInformationDetails.visibility = View.GONE
            binding.groupEmptyDetails.visibility = View.VISIBLE
        }
        else{
            binding.rvInformationDetails.visibility = View.VISIBLE
            binding.progressBarInformationDetails.visibility = View.GONE
            binding.groupEmptyDetails.visibility = View.GONE
        }
    }

    private fun loadingState() {
        binding.rvInformationDetails.visibility = View.GONE
        binding.groupEmptyDetails.visibility = View.GONE
        binding.progressBarInformationDetails.visibility = View.VISIBLE
    }

    private fun errorState() {
        lifecycleScope.launchWhenCreated {
            viewModel.getCategoryDetails(categoryId!!)
        }
    }

    private fun showDeleteDialog(categoryDetailsId: Int) {
        fun deleteErrorState() {
            lifecycleScope.launchWhenCreated {
                viewModel.deleteInformationCategory(categoryDetailsId)
            }

        }
        val builder = AlertDialog.Builder(requireContext())
        val title = SpannableString(getString(R.string.delete))
        title.setSpan(ForegroundColorSpan(Color.RED), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setTitle(title)
        builder.setMessage(getString(R.string.category_alert_dialog))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            dialog.dismiss()
            lifecycleScope.launchWhenCreated {
                viewModel.deleteInformationCategory(categoryDetailsId)
            }
            viewModel.deleteCategoryDetailsResponseState.observe(viewLifecycleOwner, Observer { state->

                when(state){
                    is Resource.Success -> getCategoryDetailsAndSuccessState()
                    is Resource.Loading -> loadingState()
                    is Resource.Error -> deleteErrorState()
                    else -> deleteErrorState()
                }
            })
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()


    }

    private fun getCategoryDetailsAndSuccessState() {
        runBlocking {
            viewModel.getCategoryDetails(categoryId!!)
        }
        successState()
    }
}
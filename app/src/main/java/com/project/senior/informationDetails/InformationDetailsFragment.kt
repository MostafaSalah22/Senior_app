package com.project.senior.informationDetails

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import com.project.senior.seniorInformation.SeniorInformationFragmentDirections
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

        detailsAdapter.onEditClick = {categoryDetailsData ->
            titleAndDescriptionDialog(requireContext(), categoryDetailsData.id, 1)
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
                viewModel.deleteCategoryDetails(categoryDetailsId)
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
                viewModel.deleteCategoryDetails(categoryDetailsId)
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

    private fun titleAndDescriptionDialog(context: Context, requiredId: Int, funId: Int) {
        val dialogBinding = layoutInflater.inflate(R.layout.edit_category_details_dialog,null)

        val myDialog = Dialog(context)
        myDialog.setContentView(dialogBinding)

        myDialog.setCancelable(true)

        // Make the dialog width (match_parent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(myDialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT

        myDialog.show()
        myDialog.window?.attributes = lp
        myDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val title = dialogBinding.findViewById<EditText>(R.id.et_title_edit_category_details)
        val description = dialogBinding.findViewById<EditText>(R.id.et_description_edit_category_details)

        val saveBtn = dialogBinding.findViewById<Button>(R.id.btn_edit_category_details)
        saveBtn.setOnClickListener {
            if(title.text.toString().trim() == "" && description.text.toString().trim() == "") {
                title.error = "Enter any data"
                title.requestFocus()
                description.error = "Enter any data"
                description.requestFocus()
            }
            else {
                if(funId == 1) {
                    lifecycleScope.launchWhenCreated {
                        viewModel.editCategoryDetails(
                            requiredId,
                            title.text.toString().trim(),
                            description.text.toString().trim()
                        )
                    }
                    viewModel.editCategoryDetailsResponseState.observe(
                        viewLifecycleOwner,
                        Observer { state ->
                            when (state) {
                                is Resource.Success -> editSuccessState()
                                is Resource.Loading -> dismissDialogAndLoadingState(myDialog)
                                is Resource.Error -> editErrorState(
                                    requiredId,
                                    title.text.toString().trim(),
                                    description.text.toString().trim()
                                )
                                else -> editErrorState(requiredId, title.text.toString().trim(), description.text.toString().trim())
                            }
                        })
                }
                else{
                    /*lifecycleScope.launchWhenCreated {
                        viewModel.addNewCategory(
                            requiredId,
                            title.text.toString().trim()
                        )
                    }
                    viewModel.addNewCategoryResponseState.observe(
                        viewLifecycleOwner,
                        Observer { state ->
                            when (state) {
                                is Resource.Success -> getCategoriesAndSuccessState()
                                is Resource.Loading -> dismissDialogAndLoadingState(myDialog)
                                is Resource.Error -> addErrorState(
                                    requiredId,
                                    title.text.toString().trim()
                                )
                                else -> addErrorState(requiredId, title.text.toString().trim())
                            }
                        })*/
                }
            }
        }

        val backEditDetails = dialogBinding.findViewById<ImageView>(R.id.img_back_edit_category_details)
        backEditDetails.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun editSuccessState() {
        Log.i("testt1", "editSuccessState: ")
        runBlocking {
            viewModel.getCategoryDetails(categoryId!!)
        }
        findNavController().navigate(InformationDetailsFragmentDirections.actionInformationDetailsFragmentSelf(categoryId!!, userId!!, categoryTitle!!))
        //successState()
    }

    private fun dismissDialogAndLoadingState(myDialog: Dialog){
        Log.i("testt1", "dismissDialogAndLoadingState: ")
        myDialog.dismiss()
        loadingState()
    }

    private fun editErrorState(categoryDetailsId: Int, title:String, description: String) {
        Log.i("testt1", "editErrorState: ")
        lifecycleScope.launchWhenCreated {
            viewModel.editCategoryDetails(categoryDetailsId, title, description)
        }
    }
}
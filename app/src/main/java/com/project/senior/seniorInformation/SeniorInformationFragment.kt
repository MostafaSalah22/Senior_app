package com.project.senior.seniorInformation

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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
import com.project.senior.databinding.FragmentSeniorInformationBinding
import com.project.senior.seniorInformation.recyclerview.CategoriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SeniorInformationFragment : Fragment() {

    private lateinit var binding: FragmentSeniorInformationBinding
    private val viewModel: SeniorInformationViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var userId: Int? = null
    private var isListEmpty = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSeniorInformationBinding.inflate(inflater,container,false)
        val informationFragmentArgs by navArgs<SeniorInformationFragmentArgs>()
        userId = informationFragmentArgs.userId
        val layoutManger = LinearLayoutManager(context)
        binding.rvInformation.layoutManager = layoutManger
        categoriesAdapter = CategoriesAdapter()
        binding.rvInformation.adapter = categoriesAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.getInformationCategories(userId!!)
        }

        viewModel.getInformationCategoriesResponseState.observe(viewLifecycleOwner, Observer { state->

            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }

        })

        viewModel.categoriesList.observe(viewLifecycleOwner, Observer {list ->
        if(list?.size == 0) {
            isListEmpty = true
            binding.rvInformation.visibility = View.GONE
            binding.progressBarInformation.visibility = View.GONE
            binding.groupEmptyCategories.visibility = View.VISIBLE
        }
        else {
            isListEmpty = false
            categoriesAdapter.submitList(list)
        }

        })
        clickListener()
    }

    private fun successState() {
        if (isListEmpty){
            binding.rvInformation.visibility = View.GONE
            binding.progressBarInformation.visibility = View.GONE
            binding.groupEmptyCategories.visibility = View.VISIBLE
        }
        else {
            binding.rvInformation.visibility = View.VISIBLE
            binding.progressBarInformation.visibility = View.GONE
            binding.groupEmptyCategories.visibility = View.GONE
        }
    }

    private fun loadingState() {
        binding.rvInformation.visibility = View.GONE
        binding.groupEmptyCategories.visibility = View.GONE
        binding.progressBarInformation.visibility = View.VISIBLE
    }

    private fun errorState() {
        lifecycleScope.launchWhenCreated {
            viewModel.getInformationCategories(userId!!)
        }
    }

    private fun clickListener() {
        binding.imgBackSeniorInformation.setOnClickListener {
            findNavController().popBackStack()
        }

        categoriesAdapter.onDeleteClick = {categoryData ->
           showDeleteDialog(categoryData.id)
        }

        categoriesAdapter.onEditClick = {categoryData ->
            titleDialog(requireContext(), categoryData.id, 1)
        }

        categoriesAdapter.onItemClick = {categoryData ->
            findNavController().navigate(SeniorInformationFragmentDirections.actionSeniorInformationFragmentToInformationDetailsFragment(categoryData.id, categoryData.user_id, categoryData.title))
        }

        binding.fabAddInformation.setOnClickListener {
            titleDialog(requireContext(), userId!!, 2)
        }
    }

    private fun showDeleteDialog(categoryId: Int) {
        fun deleteErrorState() {
            lifecycleScope.launchWhenCreated {
                viewModel.deleteInformationCategory(categoryId)
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
                viewModel.deleteInformationCategory(categoryId)
            }
            viewModel.deleteInformationCategoryResponseState.observe(viewLifecycleOwner, Observer { state->

                when(state){
                    is Resource.Success -> getCategoriesAndSuccessState()
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

    private fun getCategoriesAndSuccessState() {
        runBlocking {
            viewModel.getInformationCategories(userId!!)
        }
        successState()
    }

    private fun titleDialog(context: Context, requiredId: Int, funId: Int) {
        val dialogBinding = layoutInflater.inflate(R.layout.edit_category_title_dialog,null)

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

        val title = dialogBinding.findViewById<EditText>(R.id.et_title_edit_category)

        val saveBtn = dialogBinding.findViewById<Button>(R.id.btn_edit_category_title)
        saveBtn.setOnClickListener {
            if(title.text.toString().trim() == "") {
                title.error = getString(R.string.enter_title)
                title.requestFocus()
            }
            else {
                if(funId == 1) {
                    lifecycleScope.launchWhenCreated {
                        viewModel.editInformationCategoryTitle(
                            requiredId,
                            title.text.toString().trim()
                        )
                    }
                    viewModel.editInformationCategoryTitleResponseState.observe(
                        viewLifecycleOwner,
                        Observer { state ->
                            when (state) {
                                is Resource.Success -> editSuccessState()
                                is Resource.Loading -> dismissDialogAndLoadingState(myDialog)
                                is Resource.Error -> editErrorState(
                                    requiredId,
                                    title.text.toString().trim()
                                )
                                else -> editErrorState(requiredId, title.text.toString().trim())
                            }
                        })
                }
                else{
                    lifecycleScope.launchWhenCreated {
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
                        })
                }
            }
        }

        val backEditTitle = dialogBinding.findViewById<ImageView>(R.id.img_back_edit_category)
        backEditTitle.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun editSuccessState() {
        runBlocking {
            viewModel.getInformationCategories(userId!!)
        }
        findNavController().navigate(SeniorInformationFragmentDirections.actionSeniorInformationFragmentSelf(userId!!))
        //successState()
    }

    private fun dismissDialogAndLoadingState(myDialog: Dialog){
        myDialog.dismiss()
        loadingState()
    }

    private fun editErrorState(categoryId: Int, title:String) {
        lifecycleScope.launchWhenCreated {
            viewModel.editInformationCategoryTitle(categoryId, title)
        }
    }

    private fun addErrorState(categoryId: Int, title:String) {
        lifecycleScope.launchWhenCreated {
            viewModel.addNewCategory(categoryId, title)
        }
    }
}
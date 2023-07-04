package com.project.senior.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.FirstActivity
import com.project.senior.R
import com.project.senior.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentProfileBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.getProfileData()
        }

        getProfileData()

        clickListener()
    }

    private fun getProfileData() {
        viewModel.profileUser.observe(viewLifecycleOwner, Observer { profileUser ->

            binding.tvNameProfile.text = profileUser.data.name
            binding.tvNameInformation.text = profileUser.data.name
            binding.imgProfile.load(profileUser.data.image){
                transformations(CircleCropTransformation())
                placeholder(R.drawable.loading_img)
            }
            binding.tvPhoneInformation.text = profileUser.data.phone
            binding.tvEmailInformation.text = profileUser.data.email

        })
    }

    private fun clickListener() {

        binding.imgEditPasswordProfile.setOnClickListener {
            showPasswordDialog(requireContext())
        }

        binding.imgEditProfile.setOnClickListener {
            navigateToEditProfileFragment()
        }

        binding.tvLogoutProfile.setOnClickListener {
            showLogoutDialog()
        }

    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val title = SpannableString(getString(R.string.logout))
        title.setSpan(ForegroundColorSpan(Color.RED), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setTitle(title)
        builder.setMessage(getString(R.string.logout_alert_dialog))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            dialog.dismiss()
            lifecycleScope.launchWhenCreated {
                viewModel.logout()
                backToFirstFragment()
            }
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun backToFirstFragment() {
        val intent = Intent(requireContext(), FirstActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToEditProfileFragment() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
    }

    private fun showPasswordDialog(context: Context) {
        val dialogBinding = layoutInflater.inflate(R.layout.password_dialog,null)

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

        val oldPassword = dialogBinding.findViewById<EditText>(R.id.et_current_password)
        val newPassword = dialogBinding.findViewById<EditText>(R.id.et_new_password)
        val confirmPassword = dialogBinding.findViewById<EditText>(R.id.et_confirm_password)
        val tvErrorPassword = dialogBinding.findViewById<TextView>(R.id.tv_error_password)

        val updatePasswordBtn = dialogBinding.findViewById<Button>(R.id.btn_update_password)
        updatePasswordBtn.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModel.changeProfilePassword(
                    oldPassword.text.toString().trim(),
                    newPassword.text.toString().trim(),
                    confirmPassword.text.toString().trim()
                )
                viewModel.changePasswordResponseState.observe(viewLifecycleOwner, Observer { state ->
                    Log.i("LoginViewModel", "showPasswordDialog: $state")
                    when (state) {
                        is Resource.Success -> changeSuccessState(myDialog)
                        is Resource.Loading -> changeLoadingState(myDialog)
                        is Resource.Error -> changeErrorState(myDialog, tvErrorPassword)
                        else -> changeErrorState(myDialog, tvErrorPassword)
                    }
                })
            }
        }

        val dismissDialog = dialogBinding.findViewById<ImageView>(R.id.img_back_editPassword)
        dismissDialog.setOnClickListener {
            myDialog.dismiss()
        }
    }

    private fun changeSuccessState(myDialog: Dialog) {
        myDialog.dismiss()
        binding.groupProfile.visibility = View.VISIBLE
        binding.progressBarProfile.visibility = View.GONE
        Snackbar.make(requireView(),"Password Changed.",Snackbar.LENGTH_LONG).show()
    }

    private fun changeLoadingState(myDialog: Dialog) {
        myDialog.dismiss()
        binding.groupProfile.visibility = View.GONE
        binding.progressBarProfile.visibility = View.VISIBLE
    }

    private fun changeErrorState(myDialog: Dialog, tvErrorPassword:TextView){
        binding.groupProfile.visibility = View.VISIBLE
        binding.progressBarProfile.visibility = View.GONE
        myDialog.show()
        viewModel.changePasswordResponse.observe(viewLifecycleOwner, Observer { changeResponse ->
            tvErrorPassword.text = changeResponse.message
        })
    }
}
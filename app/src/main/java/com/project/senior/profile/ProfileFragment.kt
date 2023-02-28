package com.project.senior.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var body: MultipartBody.Part
    private val REQUEST_CODE_PICK_IMAGE = 101

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

        viewModel.responseState.observe(viewLifecycleOwner, Observer {state ->
            when(state){
                is Resource.Success -> successState()
                is Resource.Loading -> loadingState()
                is Resource.Error -> errorState()
                else -> errorState()
            }
        })

       clickListener()
    }

    private fun successState() {
        viewModel.profileUser.observe(viewLifecycleOwner, Observer { profileUser ->

            binding.tvNameProfile.text = profileUser.data.name
            binding.tvNameInformation.text = profileUser.data.name
            binding.imgProfile.load(profileUser.data.image){
                transformations(CircleCropTransformation())
                placeholder(R.drawable.loading_img)
            }
            //binding.tvDateInformation.text = profileUser.data.birthdate
            binding.tvPhoneInformation.text = profileUser.data.phone
            binding.tvEmailInformation.text = profileUser.data.email

        })
        binding.groupProfile.visibility = View.VISIBLE
        binding.progressBarProfile.visibility = View.GONE
    }

    private fun loadingState() {
        binding.groupProfile.visibility = View.GONE
        binding.progressBarProfile.visibility = View.VISIBLE
    }

    private fun errorState() {
        Log.i("ProfileViewModel", "ERROR")
    }

    private fun clickListener() {
        binding.imgBackProfile.setOnClickListener {
            backToChatFragment()
        }

        binding.btnEditProfile.setOnClickListener {
            navigateToEditProfileFragment()
        }

        binding.imgEditPasswordProfile.setOnClickListener {
            showPasswordDialog(requireContext())
        }

        binding.imgSeniorsProfile.setOnClickListener {
            navigateToSeniorsFragment()
        }

        binding.imgProfile.setOnClickListener {
            verifyStoragePermissions(requireActivity())
            viewModel.changeImageResponseState.observe(viewLifecycleOwner, Observer {state ->
                when(state){
                    is Resource.Success -> imageSuccessState()
                    is Resource.Loading -> loadingState()
                    is Resource.Error -> imageErrorState()
                    else -> Snackbar.make(requireView(),"Something Error! Please, Try Again.",Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun imageSuccessState() {
        runBlocking {
            viewModel.getProfileData()
        }
        successState()
    }

    private fun imageErrorState() {
        binding.progressBarProfile.visibility = View.GONE
        binding.groupProfile.visibility = View.VISIBLE
        viewModel.changeImageResponse.observe(viewLifecycleOwner, Observer { userResponse ->
            if(userResponse.status == "E03" || userResponse.status == "E00") Snackbar.make(requireView(),userResponse.message,Snackbar.LENGTH_LONG).show()
            else {
                lifecycleScope.launchWhenCreated {
                    viewModel.changeProfileImage(body)
                }
            }
        })
    }

    private fun backToChatFragment() {
        findNavController().popBackStack()
    }

    private fun navigateToEditProfileFragment() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
    }

    private fun navigateToSeniorsFragment() {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSeniorsFragment())
    }

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        else pickImageFromGallery()
    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data ?: return
            val filePath = getRealPathFromURI(imageUri)
            Log.i("ProfileViewModel", "onActivityResult: $filePath")
            val file = File(filePath!!)
            lifecycleScope.launchWhenCreated {
                uploadImage(file)
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            filePath
        } else {
            uri.path
        }
    }

    private suspend fun uploadImage(file: File) {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        viewModel.changeProfileImage(body)
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
                //myDialog.dismiss()
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
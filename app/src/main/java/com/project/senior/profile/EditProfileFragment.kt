package com.project.senior.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.project.domain.repo.Resource
import com.project.senior.R
import com.project.senior.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var body: MultipartBody.Part
    private val REQUEST_CODE_PICK_IMAGE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentEditProfileBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfileData()

        clickListener()
    }

    private fun getProfileData() {
        viewModel.profileUser.observe(viewLifecycleOwner, Observer { profileUser ->

            binding.tvNameEditProfile.text = profileUser.data.name
            binding.imgEditProfile.load(profileUser.data.image){
                transformations(CircleCropTransformation())
                placeholder(R.drawable.loading_img)
            }
            binding.etNameEditProfile.setText(profileUser.data.name)
            binding.etUsernameEditProfile.setText(profileUser.data.username)
            binding.etPhoneEditProfile.setText(profileUser.data.phone)
            binding.etEmailEditProfile.setText(profileUser.data.email)

        })
    }

    private fun clickListener() {
        binding.imgBackEditProfile.setOnClickListener {
            backToProfileFragment()
        }

        binding.btnSaveEditProfile.setOnClickListener {
            backToProfileFragment()
        }

        binding.btnCancelEditProfile.setOnClickListener {
            backToProfileFragment()
        }

        binding.imgEditProfile.setOnClickListener {
            verifyStoragePermissions(requireActivity())
            viewModel.changeImageResponseState.observe(viewLifecycleOwner, Observer {state ->
                when(state){
                    is Resource.Success -> imageSuccessState()
                    is Resource.Loading -> loadingState()
                    is Resource.Error -> imageErrorState()
                    else -> Snackbar.make(requireView(),"Something Error! Please, Try Again.",
                        Snackbar.LENGTH_LONG).show()
                }
            })
        }

        binding.btnSaveEditProfile.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                viewModel.updateProfileData(binding.etNameEditProfile.text.toString().trim(),
                                            binding.etUsernameEditProfile.text.toString().trim(),
                                            binding.etPhoneEditProfile.text.toString().trim(),
                                            binding.etEmailEditProfile.text.toString().trim())
            }
            viewModel.updateProfileDataResponseState.observe(viewLifecycleOwner, Observer {state ->
                when(state){
                    is Resource.Success -> updateSuccessState()
                    is Resource.Loading -> loadingState()
                    is Resource.Error -> Snackbar.make(requireView(),"Something Error! Please, Try Again.",
                        Snackbar.LENGTH_LONG).show()
                    else -> Snackbar.make(requireView(),"Something Error! Please, Try Again.",
                        Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }


    private fun backToProfileFragment() {
        findNavController().popBackStack()
    }

    private fun imageSuccessState() {
        runBlocking {
            viewModel.getProfileDataFromRemoteAndUpdateDataStore()
            viewModel.getProfileData()
        }
        getProfileData()
        binding.groupEditProfile.visibility = View.VISIBLE
        binding.progressBarEditProfile.visibility = View.GONE
    }

    private fun updateSuccessState() {
        runBlocking {
            viewModel.getProfileDataFromRemoteAndUpdateDataStore()
        }
        backToProfileFragment()
    }

    private fun loadingState() {
        binding.groupEditProfile.visibility = View.GONE
        binding.progressBarEditProfile.visibility = View.VISIBLE
    }

    private fun imageErrorState() {
        binding.progressBarEditProfile.visibility = View.GONE
        binding.groupEditProfile.visibility = View.VISIBLE
        viewModel.changeImageResponse.observe(viewLifecycleOwner, Observer { userResponse ->
            if(userResponse.status == "E03" || userResponse.status == "E00") Snackbar.make(requireView(),userResponse.message,Snackbar.LENGTH_LONG).show()
            else {
                lifecycleScope.launchWhenCreated {
                    viewModel.changeProfileImage(body)
                }
            }
        })
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

}
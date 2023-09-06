package com.example.go2life.view.navigation.reels

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.go2life.R
import com.example.go2life.adapter.reelsAdapter.ReelsFragmentViewPager
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentReelsBinding
import com.example.go2life.databinding.LayoutBottomReelCommentBinding
import com.example.go2life.model.homeData.getReels.Body
import com.example.go2life.model.reels.likeUnlikeReels.likeUnlikeReelsPramModel
import com.example.go2life.network.Repository
import com.example.go2life.utils.Status
import com.example.go2life.utils.gone
import com.example.go2life.utils.visible
import com.example.go2life.view.navigation.MainActivity
import com.example.go2life.viewmodels.ReelViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class ReelsFragment : BaseFragment() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var reelsFragmentViewPager: ReelsFragmentViewPager
    lateinit var binding: FragmentReelsBinding
    lateinit var bindingBottom: LayoutBottomReelCommentBinding
    var postId: Int = 0
    private val viewModel by viewModels<ReelViewModel> {
        ViewModelFactory(
            application = Application(), repository = Repository()
        )
    }
    private val CAMERA_PERMISSION_REQUEST = 123

    private var mList: ArrayList<Body> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReelsBinding.inflate(layoutInflater)
        viewModel.onGetReels()
        observer()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Initialize the permission launcher
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    navigateToCustomReelsCameraFragment()
                } else {
                    showPermissionDeniedDialog()
                }
            }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observer() {
        viewModel.resultGetReels.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    mList.clear()
                    mList.addAll(result.data!!.body)
                    if (mList.isEmpty()) {
                        binding.clShimmer.visible()
                    } else
                        reelsFragmentViewPager = ReelsFragmentViewPager(
                            requireContext(),
                            mList,
                            lifecycleScope,
                            callbacks
                        )
                    binding.vpReels.adapter = reelsFragmentViewPager
                    reelsFragmentViewPager.notifyDataSetChanged()
                    binding.clShimmer.gone()
                }

                Status.LOADING -> { /* Handle loading case */

                }

                Status.ERROR -> { /* Handle error case */
                }
            }
        }
        viewModel.resultLikeUnlikeReels.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {

                }

                Status.LOADING -> { /* Handle loading case */

                }

                Status.ERROR -> { /* Handle error case */
                }
            }
        }
    }

    private val callbacks = object : ReelsFragmentViewPager.ReelsFragmentViewPagerCallback {
        override fun onItemClick(id: Int, type: Int) {
            viewLifecycleOwner.lifecycleScope.launch {
                if (type == 1) {
                    postId = id
                    viewModel.onLikeUnlikeReels(
                        likeUnlikeReelsPramModel(
                            "0",
                            postId.toString(),
                            "1"
                        )
                    )
                } else if (type == 0)
                    viewModel.onLikeUnlikeReels(
                        likeUnlikeReelsPramModel(
                            "0",
                            postId.toString(),
                            "0"
                        )
                    )

            }
        }

        override fun onCommentClick(id: Int, type: Int) {
            val bundle = Bundle()
            bundle.putInt("reelId", id)
            val dialog = ReelsCommentDialog()
            dialog.arguments = bundle
            bindingBottom = LayoutBottomReelCommentBinding.inflate(layoutInflater)
            dialog.show(childFragmentManager, "")
        }

        override fun onCameraClick(id: Int) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                navigateToCustomReelsCameraFragment()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST
                )
            }
        }

    }

    private fun showPermissionDeniedDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle(getString(R.string.camera_permission_denied))
            setMessage(getString(R.string.camera_permission_denied_message))
            setPositiveButton(getString(R.string.go_to_settings)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                openAppSettings()
            }
            setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            setCancelable(false)
        }

        alertDialogBuilder.create().show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).visibleIcon()
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToCustomReelsCameraFragment()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    private fun navigateToCustomReelsCameraFragment() {
        findNavController().navigate(R.id.action_reelsFragment_to_customReelsCameraFragment)
    }


}
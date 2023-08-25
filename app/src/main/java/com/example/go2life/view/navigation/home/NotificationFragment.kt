package com.example.go2life.view.navigation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.go2life.adapter.homeAdapter.GetNotificationAdapter
import com.example.go2life.base.BaseFragment
import com.example.go2life.base.MyApplication
import com.example.go2life.databinding.LayoutGetNotificationBinding
import com.example.go2life.model.homeData.getNotification.Body
import com.example.go2life.utils.Status.ERROR
import com.example.go2life.utils.Status.LOADING
import com.example.go2life.utils.Status.SUCCESS
import com.example.go2life.viewmodels.HomeViewModel
import com.example.go2life.viewmodels.ViewModelFactory

class NotificationFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: LayoutGetNotificationBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(application, repository)
    }
    private val mList: ArrayList<Body> = ArrayList()
    var  isloaded =true
    private lateinit var getNotificationAdapter: GetNotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutGetNotificationBinding.inflate(inflater)
        viewModel.onGetNotification()
        observer()
        binding.onClick = this
        return binding.root
    }

    private fun observer() {
        viewModel.resultGetNotification.observe(viewLifecycleOwner) { data ->
            when (data.status) {
                SUCCESS -> {
                    MyApplication.hideLoader()
                    mList.clear()
                    mList.addAll(data.data?.body.orEmpty())
                    sortNotificationList()
                    initNotificationAdapter()
                }

                LOADING -> {
                    if (isloaded)
                    MyApplication.showLoader(requireContext())
                    isloaded=false
                }

                ERROR -> {
                    // Handle error
                }
            }
        }
        viewModel.resultNotificationRead.observe(viewLifecycleOwner) { data ->
            when (data.status) {
                SUCCESS -> {
                    MyApplication.hideLoader()
                    viewModel.onGetNotification()
                }

                LOADING -> {

                }

                ERROR -> {
                    // Handle error
                }
            }
        }
    }

    private fun initNotificationAdapter() {
        getNotificationAdapter =
            GetNotificationAdapter(requireContext(), mList, getNotificationAdapterCallback)
        binding.rvNotification.adapter = getNotificationAdapter
    }

    private val getNotificationAdapterCallback =
        object : GetNotificationAdapter.GetNotificationAdapterCallbacks {
            override fun onItemClick(body: Int) {
                viewModel.onNotificationRead(body.toString())
            }

        }

    private fun sortNotificationList() {
        mList.sortByDescending { it.created_at }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivArrow ->{
                findNavController().popBackStack()
            }
        }
    }
}

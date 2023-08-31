package com.example.go2life.view.navigation.reels

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.example.go2life.viewmodels.ReelViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class ReelsFragment : BaseFragment() {
    private lateinit var reelsFragmentViewPager: ReelsFragmentViewPager
    lateinit var binding: FragmentReelsBinding
    lateinit var bindingBottom: LayoutBottomReelCommentBinding
    var postId: Int = 0
    private val viewModel by viewModels<ReelViewModel> {
        ViewModelFactory(
            application = Application(), repository = Repository()
        )
    }
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
    }

}
package com.example.go2life.view.navigation.home

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.example.go2life.R
import com.example.go2life.databinding.LayoutBottomCommentsBinding
import com.example.go2life.network.Repository
import com.example.go2life.viewmodels.AuthViewModel
import com.example.go2life.viewmodels.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomCommentDialog : BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var binding: LayoutBottomCommentsBinding
    val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory(
            application = Application(),
            repository = Repository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomCommentsBinding.inflate(layoutInflater)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding = LayoutBottomCommentsBinding.inflate(inflater, container, false)
        binding.onClick = this
        return binding.root

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = true
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_COLLAPSED

        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}
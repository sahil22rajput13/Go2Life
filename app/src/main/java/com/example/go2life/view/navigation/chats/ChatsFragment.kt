package com.example.go2life.view.navigation.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.go2life.adapter.ChatsFragmentRecyclerView
import com.example.go2life.base.BaseFragment
import com.example.go2life.databinding.FragmentChatsBinding

class ChatsFragment:BaseFragment(){
    var chatsFragmentRecyclerView : ChatsFragmentRecyclerView? = null
    lateinit var binding: FragmentChatsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        adapterSetup()
        return binding.root

    }

    private fun adapterSetup() {
        chatsFragmentRecyclerView = ChatsFragmentRecyclerView(requireContext())
        binding.vpReels.adapter = chatsFragmentRecyclerView

    }
}
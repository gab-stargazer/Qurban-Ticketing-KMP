package org.lelestacia.qurban_ticketing.domain.viewmodel.member.add

import androidx.lifecycle.ViewModel
import org.lelestacia.qurban_ticketing.domain.repository.MemberRepository

class MemberAddViewModel(
    private val repository: MemberRepository
) : ViewModel()
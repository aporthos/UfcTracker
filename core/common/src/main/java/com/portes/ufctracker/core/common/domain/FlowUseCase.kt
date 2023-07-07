package com.portes.ufctracker.core.common.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import com.portes.ufctracker.core.common.models.Result

abstract class FlowUseCase<in P, R>(private val dispatcher: CoroutineDispatcher) {
    operator fun invoke(params: P): Flow<Result<R>> = execute(params).flowOn(dispatcher)

    protected abstract fun execute(params: P): Flow<Result<R>>
}
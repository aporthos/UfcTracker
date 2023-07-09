package com.portes.ufctracker.core.data.repositories

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.model.entities.FightBetsEntity
import com.portes.ufctracker.core.model.entities.toModel
import com.portes.ufctracker.core.model.models.FighterBetRequestModel
import com.portes.ufctracker.core.model.models.FighterModel
import com.portes.ufctracker.core.model.models.GamblerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FightBetsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FightBetsRepository {
    companion object {
        private const val COLLECTION_FIGHT_BETS = "fightbets"
        private const val COLLECTION_GAMBLER = "gambler"
        val nameFake = "amadeus"
    }

    override fun getFightBetsList(eventId: Int): Flow<List<GamblerModel>> = flow {
        val countriesRef = firestore.collection(COLLECTION_FIGHT_BETS)
        val ukDocRef = countriesRef.document("$eventId")
        val snapshot = firestore.collectionGroup(COLLECTION_GAMBLER)
            .orderBy(FieldPath.documentId())
            .startAt(ukDocRef.path)
            .endAt(ukDocRef.path + "\uf8ff")
            .get().await()
        val result = snapshot.toObjects<FightBetsEntity>().map {
            it.toModel()
        }.groupBy { it.name }.map {
            GamblerModel(it.key, it.value.map { it })
        }
        emit(result)
    }

    override fun getMyFightBets(eventId: Int): Flow<List<Int?>> = flow {
        val countriesRef = firestore.collection(COLLECTION_FIGHT_BETS)
        val ukDocRef = countriesRef.document("$eventId")
        val snapshot = firestore.collectionGroup(COLLECTION_GAMBLER)
            .orderBy(FieldPath.documentId())
            .startAt(ukDocRef.path)
            .endAt(ukDocRef.path + "\uf8ff")
            .get().await()
        val result = snapshot.toObjects<FightBetsEntity>().map {
            it.toModel()
        }.filter { it.name == nameFake }.map { it.fighter?.fighterId }
        emit(result)
    }

    override fun addOrRemoveFightBetsList(
        eventId: Int,
        addFighterBets: List<FighterBetRequestModel>,
        removeFighterBets: List<FighterBetRequestModel>
    ): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading)
            val batch = firestore.batch()
            addFighterBets.forEach {
                val fighterBet = Fighter(name = nameFake, fightId = it.fightId, it.fighter)
                val document =
                    firestore.document("/fightbets/$eventId/fights/${it.fightId}/gambler/$nameFake")
                batch.set(document, fighterBet)
            }

            removeFighterBets.forEach {
                val documentDelete =
                    firestore.document("/fightbets/$eventId/fights/${it.fightId}/gambler/$nameFake")
                batch.delete(documentDelete)
            }
            batch.commit()
            emit(Result.Success(true))
        } catch (e: Exception) {
            Timber.e(e)
            emit(Result.Error(e.localizedMessage))
        }
    }
}

data class Fighter(
    val name: String,
    val fightId: Int,
    val fighter: FighterModel
)

interface FightBetsRepository {
    fun getFightBetsList(eventId: Int): Flow<List<GamblerModel>>
    fun getMyFightBets(eventId: Int): Flow<List<Int?>>
    fun addOrRemoveFightBetsList(
        eventId: Int,
        addFighterBets: List<FighterBetRequestModel>,
        removeFighterBets: List<FighterBetRequestModel>
    ): Flow<Result<Boolean>>
}

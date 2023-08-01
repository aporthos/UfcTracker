package com.portes.ufctracker.core.data.repositories

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.common.toDate
import com.portes.ufctracker.core.common.todayOrAfter
import com.portes.ufctracker.core.model.entities.EventRequest
import com.portes.ufctracker.core.model.entities.FightBetsEntity
import com.portes.ufctracker.core.model.entities.toModel
import com.portes.ufctracker.core.model.models.FighterBetRequestModel
import com.portes.ufctracker.core.model.models.FighterRequest
import com.portes.ufctracker.core.model.models.GamblerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FightBetsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,

) : FightBetsRepository {
    companion object {
        private const val COLLECTION_FIGHT_BETS = "fightbets"
        private const val COLLECTION_GAMBLER = "gambler"
    }

    override fun getFightBetsList(eventId: Int): Flow<List<GamblerModel>> = flow {
        val fightbets = firestore.collection(COLLECTION_FIGHT_BETS)
        val event = fightbets.document("$eventId")
        val snapshot = firestore.collectionGroup(COLLECTION_GAMBLER)
            .orderBy(FieldPath.documentId())
            .startAt(event.path)
            .endAt(event.path + "\uf8ff")
            .get().await()
        val result = snapshot.toObjects<FightBetsEntity>().map {
            it.toModel()
        }.groupBy { it.name }.map {
            GamblerModel(it.key, it.value.map { it })
        }
        emit(result)
    }

    override fun getMyFightBets(eventId: Int, nickname: String): Flow<List<Int?>> = flow {
        val fightbets = firestore.collection(COLLECTION_FIGHT_BETS)
        val event = fightbets.document("$eventId")
        val snapshot = firestore.collectionGroup(COLLECTION_GAMBLER)
            .orderBy(FieldPath.documentId())
            .startAt(event.path)
            .endAt(event.path + "\uf8ff")
            .get().await()
        val result = snapshot.toObjects<FightBetsEntity>().map {
            it.toModel()
        }.filter { it.name == nickname }.map { it.fighter?.fighterId }
        emit(result)
    }

    override fun getFightBetsByEvent(): Flow<List<Int>> = flow {
        val fightBets = firestore.collection(COLLECTION_FIGHT_BETS).get().await()
        val event = fightBets.toObjects<EventRequest>().map {
            it.eventId
        }
        emit(event)
    }

    override fun getFightLast(): Flow<EventRequest?> = flow {
        val fightBets = firestore.collection(COLLECTION_FIGHT_BETS).get().await()
        val event = fightBets.toObjects<EventRequest>().find {
            it.day.toDate().todayOrAfter()
        }
        emit(event)
    }

    override fun addOrRemoveFightBetsList(
        eventRequest: EventRequest,
        nickname: String,
        addFighterBets: List<FighterBetRequestModel>,
        removeFighterBets: List<FighterBetRequestModel>
    ): Flow<Result<Boolean>> = flow {
        try {
            emit(Result.Loading)
            val batch = firestore.batch()
            addFighterBets.forEach {
                val event =
                    firestore.document("/$COLLECTION_FIGHT_BETS/${eventRequest.eventId}")
                batch.set(event, eventRequest)
                val fighterBet = FighterRequest(name = nickname, fightId = it.fightId, it.fighter)
                val document =
                    firestore.document("/$COLLECTION_FIGHT_BETS/${eventRequest.eventId}/fights/${it.fightId}/gambler/$nickname")
                batch.set(document, fighterBet)
            }

            removeFighterBets.forEach {
                val documentDelete =
                    firestore.document("/$COLLECTION_FIGHT_BETS/${eventRequest.eventId}/fights/${it.fightId}/gambler/$nickname")
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

interface FightBetsRepository {
    fun getFightBetsList(eventId: Int): Flow<List<GamblerModel>>
    fun getMyFightBets(eventId: Int, nickname: String): Flow<List<Int?>>
    fun getFightBetsByEvent(): Flow<List<Int>>
    fun getFightLast(): Flow<EventRequest?>
    fun addOrRemoveFightBetsList(
        eventRequest: EventRequest,
        nickname: String,
        addFighterBets: List<FighterBetRequestModel>,
        removeFighterBets: List<FighterBetRequestModel>
    ): Flow<Result<Boolean>>
}

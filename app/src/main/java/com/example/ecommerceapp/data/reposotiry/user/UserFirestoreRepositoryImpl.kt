package com.example.ecommerceapp.data.reposotiry.user

import com.example.ecommerceapp.data.model.Resource
import com.example.ecommerceapp.data.model.user.UserDetailsModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserFirestoreRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserFirestoreRepository {

    override suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>> =
        callbackFlow {
            send(Resource.Loading())
            val documentPath = "users/$userId"
            val document = firestore.document(documentPath)
            val listener = document.addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                value?.toObject(UserDetailsModel::class.java)?.let {
                    if(it.disabled == true) {
                        close(AccountDisabledexception("Account Disabled"))
                        return@addSnapshotListener
                    }
                    trySend(Resource.Success(it))
                } ?: run {
                    close(UserNotFoundException("User not found"))
                }
            }
            awaitClose {
                listener.remove()
            }
        }
}

class UserNotFoundException(message: String) : Exception(message)
class AccountDisabledexception(message: String) : Exception(message)
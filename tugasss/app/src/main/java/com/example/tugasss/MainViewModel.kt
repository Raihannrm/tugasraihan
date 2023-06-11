package com.example.tugasss

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugasss.network.ApiClient
import com.example.tugasss.network.CharacterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tugasss.network.Character
import com.example.tugasss.network.ScreenState


class MainViewModel(private val repository: Repository = Repository(ApiClient.apiService)) : ViewModel() {

    private val charactersLiveData = MutableLiveData<ScreenState<List<Character>?>>()
    val characterLiveData: LiveData<ScreenState<List<Character>?>>
        get() = charactersLiveData

    init {
        fetchCharacter()
    }

    private fun fetchCharacter() {
        val client = repository.getCharacters("1")
        charactersLiveData.postValue(ScreenState.Loading(null))
        client.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    // characterResponse = response.body()
                    charactersLiveData.postValue(ScreenState.Success(response.body()?.result))
                } else {
//                    val errorResponse = response.errorBody()?.string()
//                    //Log.d("Failed", "Response not successful: ${response.code()}, $errorResponse")
                    charactersLiveData.postValue(ScreenState.Error(response.code().toString(),null))
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                //Log.d("Failed", t.message.toString())
                charactersLiveData.postValue(ScreenState.Error(t.message.toString(), null))
            }
        })
    }
}

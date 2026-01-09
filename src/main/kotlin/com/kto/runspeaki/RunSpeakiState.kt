package com.kto.runspeaki

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.components.State

@Service(Service.Level.APP)
@State(
    name = "RunSpeakiState",
    storages = [Storage("runSpeakiState.xml")]
)
class RunSpeakiState : PersistentStateComponent<RunSpeakiState> {
    enum class SpeakiType(
        val speakiName: String,
        val dir: String
    ) {
        CRYING_SPEAKI("우는 스핔이", "file/cryingSpeaki.gif"),
        HAPPKI("행복한 스핔이","file/happki.gif"),
        KKUK_SPEAKI("꾹 눌리는 스핔이", "file/kkukSpeaki.gif"),
        PARTY_SPEAKI("광란의 스핔이!", "file/partySpeaki.gif")
        ;
    }

    var speakiType: SpeakiType = SpeakiType.CRYING_SPEAKI

    override fun getState(): RunSpeakiState = this

    override fun loadState(state: RunSpeakiState) {
        speakiType = state.speakiType
    }

    companion object {
        val instance: RunSpeakiState
            get() = service()
    }
}
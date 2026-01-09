package com.kto.runspeaki

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class RunSpeakiConfigurable : Configurable {
    private val settings = RunSpeakiState.instance

    private var tempSpeakiType = settings.state.speakiType

    override fun getDisplayName(): @NlsContexts.ConfigurableName String = "Run Speaki"

    override fun createComponent(): JComponent {
        return panel {
            group("스핔이 바꾸기") {
                row {
                    comboBox(RunSpeakiState.SpeakiType.entries.map { it.speakiName })
                        .bindItem(
                            { tempSpeakiType.speakiName },
                            { tempSpeakiType =
                                RunSpeakiState.SpeakiType.entries
                                    .find { speakiType ->
                                        speakiType.speakiName == (it ?: RunSpeakiState.SpeakiType.CRYING_SPEAKI.speakiName)
                                    }!!
                            }
                        )
                        .onChanged { comboBox ->
                            val selectedName = comboBox.selectedItem as? String
                            tempSpeakiType = RunSpeakiState.SpeakiType.entries
                                .find { it.speakiName == selectedName }
                                ?: RunSpeakiState.SpeakiType.CRYING_SPEAKI
                        }
                }
            }
        }
    }

    override fun isModified(): Boolean = tempSpeakiType != settings.state.speakiType

    override fun apply() {
        settings.state.speakiType = tempSpeakiType

        ApplicationManager.getApplication().messageBus
            .syncPublisher(SpeakiTypeListener.TOPIC)
            .onSpeakiTypeChanged()
    }

}
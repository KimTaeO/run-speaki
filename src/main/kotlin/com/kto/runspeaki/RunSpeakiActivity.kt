package com.kto.runspeaki

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.components.JBLabel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLayeredPane

internal class RunSpeakiActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        withContext(Dispatchers.EDT) {
            val frame: JFrame = WindowManager.getInstance().getFrame(project)
                ?: return@withContext

            val layeredPane = frame.layeredPane

            val label = object : JBLabel() {
                var gif =
                    ImageIcon(RunSpeakiActivity::class.java.classLoader.getResource(RunSpeakiState.instance.state.speakiType.dir))

                override fun getPreferredSize(): Dimension = Dimension(layeredPane.width, layeredPane.height)
                override fun paintComponent(g: Graphics) {
                    icon = gif

                    val g2 = g.create() as Graphics2D
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

                    val width = (gif.iconWidth * 0.5).toInt()
                    val height = (gif.iconHeight * 0.5).toInt()

                    g2.drawImage(gif.image, 0, 0, width, height, this)
                    g2.dispose()
                }
            }

            val iconWidth = label.gif.iconWidth
            val iconHeight = label.gif.iconHeight


            label.setBounds(
                (layeredPane.width - iconWidth + 210),
                (layeredPane.height - iconHeight + 200),
                iconWidth,
                iconHeight
            )

            layeredPane.add(label, JLayeredPane.PALETTE_LAYER)
            layeredPane.revalidate()
            layeredPane.repaint()


            val connection = ApplicationManager.getApplication().messageBus.connect()
            connection.subscribe(SpeakiTypeListener.TOPIC, object : SpeakiTypeListener {
                override fun onSpeakiTypeChanged() {
                    invokeLater {
                        val state = RunSpeakiState.instance.state

                        label.gif = ImageIcon(RunSpeakiActivity::class.java.classLoader.getResource(state.speakiType.dir))
                        label.size = Dimension(layeredPane.width, layeredPane.height)
                        layeredPane.revalidate()
                        layeredPane.repaint()
                    }
                }
            })
        }
    }


//    Kotlin Coroutines: 최신 SDK의 ProjectActivity는 suspend 함수이므로 내부에서 UI를 건드릴 때 withContext(Dispatchers.EDT)를 사용하여 UI 스레드에서 실행되도록 보장해야 할 수도 있습니다.
//
//    화면 리사이즈 대응: 위 코드는 처음에만 위치를 잡습니다. 창 크기가 변할 때 위치를 유지하려면 layeredPane에 ComponentListener를 달아서 창 크기가 바뀔 때마다 label.setBounds를 다시 호출해줘야 합니다.
//
//    투명도 조절: label.graphics를 오버라이드하거나 AlphaComposite를 사용하면 여자친구 사진을 더 은은하게(반투명하게) 띄울 수 있어 코딩에 방해가 덜 됩니다.
}
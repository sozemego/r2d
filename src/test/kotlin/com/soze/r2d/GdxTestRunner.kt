package com.soze.lifegame

import org.junit.runner.notification.RunNotifier
import org.junit.runners.model.FrameworkMethod
import com.badlogic.gdx.Gdx
import java.util.HashMap
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.InitializationError
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.stubbing.Answer
import java.nio.IntBuffer


class GdxTestRunner @Throws(InitializationError::class) constructor(klass: Class<*>) : BlockJUnit4ClassRunner(klass), ApplicationListener {

    private val invokeInRender = HashMap<FrameworkMethod, RunNotifier>()

    init {
        val conf = HeadlessApplicationConfiguration()

        HeadlessApplication(this, conf)
        Gdx.gl = mock(GL20::class.java)
        Gdx.gl20 = Gdx.gl
        Gdx.graphics = mock(Gdx.graphics::class.java)
        `when`(Gdx.graphics.getWidth()).then { 500 }
        `when`(Gdx.graphics.getHeight()).then { 500 }
//        `when`(Gdx.gl.glCreateShader(ArgumentMatchers.anyInt())).then { 1 }
//        `when`(Gdx.gl20.glCreateShader(ArgumentMatchers.anyInt())).then { 1 }
//        `when`(Gdx.gl20.glCreateProgram()).then { 1 }
//        val glGetShaderiv = fun(): Answer<Unit>? {
//            return Answer {
//                println("OWN")
//                val intBuffer = it.getArgument(2) as IntBuffer
//                intBuffer.put(0, 1)
//                Unit
//            }
//        }
//        doAnswer(glGetShaderiv()).`when`(Gdx.gl20).glGetShaderiv(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.any())
//
//        val glGetProgramiv = fun(): Answer<Unit>? {
//            return Answer {
//                println("OWN")
//                val intBuffer = it.getArgument(2) as IntBuffer
//                intBuffer.put(0, 1)
//                Unit
//            }
//        }
//        doAnswer(glGetProgramiv()).`when`(Gdx.gl20).glGetProgramiv(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.any())
    }

    override fun create() {}

    override fun resume() {}

    override fun render() {
        synchronized(invokeInRender) {
            for ((key, value) in invokeInRender) {
                super.runChild(key, value)
            }
            invokeInRender.clear()
        }
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun dispose() {}

    override fun runChild(method: FrameworkMethod, notifier: RunNotifier) {
        synchronized(invokeInRender) {
            // add for invoking in render phase, where gl context is available
            invokeInRender.put(method, notifier)
        }
        // wait until that test was invoked
        waitUntilInvokedInRenderMethod()
    }

    /**
     *
     */
    private fun waitUntilInvokedInRenderMethod() {
        try {
            var run = true
            while (run) {
                Thread.sleep(10)
                synchronized(invokeInRender) {
                    if (invokeInRender.isEmpty())
                        run = false
                }
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

}
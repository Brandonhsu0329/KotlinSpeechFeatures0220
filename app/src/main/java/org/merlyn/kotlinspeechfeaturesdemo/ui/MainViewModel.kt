package org.merlyn.kotlinspeechfeaturesdemo.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.merlyn.kotlinspeechfeatures.MathUtils
import org.merlyn.kotlinspeechfeatures.SpeechFeatures
import org.merlyn.kotlinspeechfeaturesdemo.common.WavFile
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "KotlinSpeechFeatures"
    private val speechFeatures = SpeechFeatures()

    fun performSsc() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = fileFromAsset("audioSample")
            for (file in files) {
                val wav = loadWavFile(file)
                val result = speechFeatures.ssc(MathUtils.normalize(wav), nFilt = 64)
                Log.d(TAG, "ssc output for ${file.name}:")
                result.forEach {
                    Log.d(TAG, it.contentToString())
                }
            }
        }
    }

    fun performMfcc() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = fileFromAsset("audioSample")
            for (file in files) {
                val wav = loadWavFile(file)
                val result = speechFeatures.ssc(MathUtils.normalize(wav), nFilt = 64)
                Log.d(TAG, "mfcc output for ${file.name}:")
                result.forEach {
                    Log.d(TAG, it.contentToString())
                }
            }
        }
    }

    fun performFbank() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = fileFromAsset("audioSample")
            for (file in files) {
                val wav = loadWavFile(file)
                val result = speechFeatures.ssc(MathUtils.normalize(wav), nFilt = 64)
                Log.d(TAG, "fbank output for ${file.name}:")
                result.forEach {
                    Log.d(TAG, it.contentToString())
                }
            }
        }
    }

    fun performLogfbank() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = fileFromAsset("audioSample")
            for (file in files) {
                val wav = loadWavFile(file)
                val result = speechFeatures.ssc(MathUtils.normalize(wav), nFilt = 64)
                Log.d(TAG, "logbank output for ${file.name}:")
                result.forEach {
                    Log.d(TAG, it.contentToString())
                }
            }
        }
    }

    private fun loadWavFile(file: File): IntArray {
        val wavFile = WavFile.openWavFile(file)
        val numFrames = wavFile.numFrames.toInt()
        val channels = wavFile.numChannels
        val loopCounter: Int = numFrames * channels / 4096 + 1
        val intBuffer = IntArray(numFrames)
        for (i in 0 until loopCounter) {
            wavFile.readFrames(intBuffer, numFrames)
        }
        return intBuffer
    }

    private fun fileFromAsset(directory: String): List<File> {
        val context = getApplication<Application>()
        val cacheDir = context.cacheDir
        val assetManager = context.assets
        val files = assetManager.list(directory) ?: emptyArray()

        return files.map { fileName ->
            val file = File("$cacheDir/$fileName")
            file.apply {
                writeBytes(assetManager.open("$directory/$fileName").readBytes())
            }
        }
    }
}

//    private fun fileFromAsset(directory: String, name: String): File {
//        val context = getApplication<Application>()
//        val cacheDir = context.cacheDir
//        return File("$cacheDir/$name").apply { writeBytes(context.assets.open("$directory/$name").readBytes()) }
//    }
//}
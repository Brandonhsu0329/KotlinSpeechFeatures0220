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
import org.nield.kotlinstatistics.kurtosis
import org.nield.kotlinstatistics.skewness
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "KotlinSpeechFeatures"
    private val speechFeatures = SpeechFeatures()


    //這邊將audioSample內的所有WAV檔案讀入，每個fun都有
    fun performSsc() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = fileFromAsset("audioSample")//在Assety資料夾中
            for (file in files) {
                val wav = loadWavFile(file)
                val result = speechFeatures.ssc(MathUtils.normalize(wav), nFilt = 64)
//                Log.d(TAG, "ssc output for ${file.name}:")
                result.forEach {
//                    Log.d(TAG, it.contentToString())
                }
            }
        }
    }
    fun calculateSD(numArray: List<Float>): Double {
        var sum = 0.0
        var standardDeviation = 0.0
        for (num in numArray) {
            sum += num
        }
        val mean = sum / numArray.size
        for (num in numArray) {
            standardDeviation += Math.pow(num - mean, 2.0)
        }
        val divider = numArray.size - 1
        return Math.sqrt(standardDeviation / divider)
    }

    fun performMfcc() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = fileFromAsset("audioSample")
            val features = mutableListOf<Double>()

            for (file in files) {
                val wav = loadWavFile(file)
//                val result = speechFeatures.mfcc123123(MathUtils.normalize(wav))
                val result = speechFeatures.mfcc123123(wav.map { it.toFloat() }.toFloatArray(),44100)//採樣平率
                Log.d("TAG555", "new_mfcc: ${result.contentDeepToString()}")
                val mfccSize = result[0].size

                for (row in result) {
//                    Log.d("TAG12345", row.contentToString())
                }



                for (i in 0 until mfccSize) {
                    val columnValues = result.map { it[i] }

                    // 計算平均值
                    val average = columnValues.average()
                    features.add(average)

                    // 計算標準差
                    val stdDev = calculateSD(columnValues)
                    features.add(stdDev)

                    // 計算偏態
                    val skewness = columnValues.kurtosis
                    features.add(skewness)

                    // 計算峰度
                    val kurtosisValue = columnValues.skewness
                    features.add(kurtosisValue)
                }
//                Log.d(TAG, "mfcc output for ${file.name}:")
                result.forEach {
//                    Log.d(TAG, it.contentToString())
                }
            }
            Log.d("TAG end", "Features 0-51: $features")
        }
    }

    fun performFbank() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = fileFromAsset("audioSample")
            for (file in files) {
                val wav = loadWavFile(file)
                val result = speechFeatures.ssc(MathUtils.normalize(wav), nFilt = 64)
//                Log.d(TAG, "fbank output for ${file.name}:")
                result.forEach {
//                    Log.d(TAG, it.contentToString())
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
//                Log.d(TAG, "logbank output for ${file.name}:")
                result.forEach {
//                    Log.d(TAG, it.contentToString())
                }
            }
        }
    }

    private fun loadWavFile(file: File): IntArray {
        val wavFile = WavFile.openWavFile(file)
        val numFrames = wavFile.numFrames.toInt()
        val channels = wavFile.numChannels
        val loopCounter: Int = numFrames * channels / 4096+1
        val intBuffer = IntArray(numFrames * channels)
        for (i in 0 until loopCounter){
            wavFile.readFrames(intBuffer, numFrames)
        }
        Log.d("Tag123", "intBuffer content: ${intBuffer.contentToString()}")
        return intBuffer
    }

//    private fun flattenBuffer(inputBuffer: Array<IntArray>): IntArray {
//        val numChannels = inputBuffer.size
//        val numFrames = inputBuffer[0].size // Assuming all channels have the same number of frames
//
//        val outputBuffer = IntArray(numChannels * numFrames)
//
//        for (c in 0 until numChannels) {
//            for (f in 0 until numFrames) {
//                outputBuffer[c * numFrames + f] = inputBuffer[c][f]
//            }
//        }
//
//        return outputBuffer
//    }


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
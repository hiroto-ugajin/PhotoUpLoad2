package jp.kanoyastore.hiroto.ugajin.photoupload2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import jp.kanoyastore.hiroto.ugajin.photoupload2.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // 追加
    private lateinit var mediaPlayer: MediaPlayer

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("ANDROID", "許可された")
            } else {
                Log.d("ANDROID", "許可されなかった")
                Toast.makeText(this, "パーミッションが拒否されました", Toast.LENGTH_SHORT).show()
            }
        }

    // APIレベルによって許可が必要なパーミッションを切り替える
    private val readImagesPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE

    fun <T> List<T>.shuffle(): List<T> {
        val list = toMutableList()
        val random = Random()
        for (i in list.size - 1 downTo 1) {
            val j = random.nextInt(i + 1)
            val temp = list[i]
            list[i] = list[j]
            list[j] = temp
        }
        return list
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // 追加
        val view = binding.root // 追加
        setContentView(view) // 変更

        binding.buttonPermission.setOnClickListener {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(readImagesPermission) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている

            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissionLauncher.launch(readImagesPermission)
            }
        }

        val mediaPlayerNice = MediaPlayer.create(this, R.raw.nicesound)

        val button = binding.button
        val button1 = binding.button1
        val button2 = binding.button2
        val imageView0 = binding.imageView0
        val imageView1 = binding.imageView1
        val imageView2 = binding.imageView2
        val imageView3 = binding.imageView3
        val imageView4 = binding.imageView4
        val imageView5 = binding.imageView5
        val imageView6 = binding.imageView6
        val imageView7 = binding.imageView7
        val imageView8 = binding.imageView8
        val imageView9 = binding.imageView9
        val imageView10 = binding.imageView10
        val imageView11 = binding.imageView11

        val imageViewList = listOf(
            imageView0, imageView1, imageView2, imageView3, imageView4, imageView5,
            imageView6, imageView7, imageView8, imageView9, imageView10, imageView11
        )

        val isImageVisible = mutableMapOf<ImageView, Boolean>()

        imageViewList.forEach { imageView ->
            isImageVisible[imageView] = false
            imageView.isClickable = true

            imageView.setOnClickListener {
                if (imageView.isClickable) {

                    val isVisible = isImageVisible[imageView] ?: false

                    if (isVisible) {
                        // 画像を非表示にする
                        imageView.alpha = 0.0f // 透明にする
                    } else {
                        // 画像を表示する
                        imageView.alpha = 1.0f // 不透明にする
                    }
                    isImageVisible[imageView] = !isVisible

                    val correctPairs = listOf(
                        listOf(imageView0, imageView1),
                        listOf(imageView2, imageView3),
                        listOf(imageView4, imageView5),
                        listOf(imageView6, imageView7),
                        listOf(imageView8, imageView9),
                        listOf(imageView10, imageView11),
                    )

                    val selectedImageViews = imageViewList.filter { isImageVisible[it] == true }
                    if (selectedImageViews.size == 2) {
                        val isCorrectPair = correctPairs.any { it.containsAll(selectedImageViews) }
                        if (isCorrectPair) {
                            // 正解の処理を行う
                            mediaPlayerNice.start()

                            selectedImageViews[0].isClickable = false
                            selectedImageViews[1].isClickable = false

                            isImageVisible[selectedImageViews[0]] = false
                            isImageVisible[selectedImageViews[1]] = false

                            Toast.makeText(this, "正解！", Toast.LENGTH_SHORT).show()

                            Handler().postDelayed({
                                selectedImageViews[0].alpha = 0.3f
                                selectedImageViews[1].alpha = 0.3f
                            }, 500)

                            // 0.8秒後に音声停止
                            Handler().postDelayed({
                                mediaPlayerNice.pause()
                                mediaPlayerNice.seekTo(0) // 再生位置をリセット
                            }, 800)

                        } else {
                            // 不正解の処理を行う
                            // 0.5秒後にクリックリスナーを有効にし、非表示にする
                            Handler().postDelayed({
                                selectedImageViews.forEach { imageView ->
                                    imageView.alpha = 0.0f // 透明にする
                                }
                                isImageVisible[selectedImageViews[0]] = false
                                isImageVisible[selectedImageViews[1]] = false

                            }, 500)
                        }
                    }
                }
            }
        }

        button2.setOnClickListener {
            val imageViewList = listOf(
                imageView0, imageView1, imageView2, imageView3, imageView4, imageView5,
                imageView6, imageView7, imageView8, imageView9, imageView10, imageView11
            )
            val shuffledImageViewList = imageViewList.shuffle()
            val parentView = findViewById<ViewGroup>(R.id.gridLayout) // 親ViewのIDを指定して取得する
            parentView.removeAllViews() // 現在の配置をクリアする

            for (imageView in shuffledImageViewList) {
                parentView.addView(imageView) // シャッフルされた順序でImageViewを追加する
                imageView.alpha = 0.0f // 透明にする
                imageView.isClickable = true
            }
        }

        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        button1.setOnClickListener {  for (imageView in imageViewList) {
            imageView.setImageDrawable(null)
            imageView.alpha = 1.0f // 透明にする
            imageView.isClickable = false
        } }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // 選択された画像の処理を行う
            val imageView0 = binding.imageView0
            val imageView1 = binding.imageView1
            val imageView2 = binding.imageView2
            val imageView3 = binding.imageView3
            val imageView4 = binding.imageView4
            val imageView5 = binding.imageView5
            val imageView6 = binding.imageView6
            val imageView7 = binding.imageView7
            val imageView8 = binding.imageView8
            val imageView9 = binding.imageView9
            val imageView10 = binding.imageView10
            val imageView11 = binding.imageView11

            val mediaPlayerNice = MediaPlayer.create(this, R.raw.nicesound)

            val selectedImageUri: Uri? = data.data

            if (selectedImageUri != null) {
                // 画像を順番にImageViewに貼り付ける
                val imageViewList = listOf(
                    imageView0, imageView1, imageView2, imageView3, imageView4, imageView5,
                    imageView6, imageView7, imageView8, imageView9, imageView10, imageView11
                )

                val index = imageViewList.indexOfFirst { it.drawable == null }
                if (index >= 0 && index < imageViewList.size) {
                    Picasso.get().load(selectedImageUri).into(imageViewList[index])
                    Picasso.get().load(selectedImageUri).into(imageViewList[index + 1])
                }
                else {
                    Toast.makeText(this, "すべてのImageViewが埋まっています", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val READ_REQUEST_CODE:Int = 42
    }
}

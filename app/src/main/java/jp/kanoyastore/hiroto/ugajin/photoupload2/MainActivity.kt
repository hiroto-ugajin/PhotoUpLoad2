package jp.kanoyastore.hiroto.ugajin.photoupload2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import jp.kanoyastore.hiroto.ugajin.photoupload2.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // 追加

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

    var opennedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // 追加
        val view = binding.root // 追加
        setContentView(view) // 変更

        val button = binding.button
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

            imageView.setOnClickListener {

                val isVisible = isImageVisible[imageView] ?: false

                if (isVisible) {
                    // 画像を非表示にする
                    imageView.alpha = 0.0f // 透明にする
                } else {
                    // 画像を表示する
                    imageView.alpha = 1.0f // 不透明にする
                }
                isImageVisible[imageView] = !isVisible
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

            }
        }

        button.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, READ_REQUEST_CODE)
        }
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

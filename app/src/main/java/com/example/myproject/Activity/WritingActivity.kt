package com.example.myproject.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.R
import com.example.myproject.Retrofit.Model
import com.example.myproject.Retrofit.RetrofitBuilder
import com.example.myproject.databinding.ActivityWritingBinding
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream

class WritingActivity : AppCompatActivity() { //end of WritingActivity
    lateinit var binding: ActivityWritingBinding
    private var retrofitBuilder = RetrofitBuilder()
    private val OPEN_GALLERY = 1
    var profileImageBase64: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.writingToolbar))
        supportActionBar!!.title = "작성하기"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //등록하기버튼
        binding.writingBtn.setOnClickListener{writingBehavior()}
        //앨범이미지불러오기 버튼
        binding.writingImageBtn.setOnClickListener {onClick()}


    } //end of onCr
    //앨범 실행시키기
    fun onClick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, OPEN_GALLERY)//OPEN_GALLERY = 1
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if(requestCode == OPEN_GALLERY || resultCode == RESULT_OK) {
            //currentImageURL에 데이터 담기
            //컴포넌트(액티비티 서비스 등등)간에 통신을 하기 위한 메세지 객체
            var currentImageURL = intent?.data
            //외부에서 데이터를 읽는 역할을 해주는 InputStream
            //OutputStream은 외부로 데이터를 출력하는 역할
            val ins: InputStream? = currentImageURL?.let {
                //contentResolver란 앱의 데이터 접근을 다른 앱에 허용하는 컴포넌트
                applicationContext.contentResolver.openInputStream(
                    it
                )
            }
            //BitmapFactory클래스를 통해 여러가지 디코딩 메소드를 사용
            val bitmap: Bitmap = BitmapFactory.decodeStream(ins)
            ins?.close()
            //1, 스케일 조절Bitmap, 가로, 세로, 필터 적용 여부
            //가로 세로 조정과 필터를 적용하여 이미지를 선명하게 해주고 많은 도움이 됨
            val resized = Bitmap.createScaledBitmap(bitmap, 256, 256, true)
            //바이트배열에 데이터를 입출력하는데 사용되는 스트림(주로 데이터를 임시로 바이트배열에 담아서 변환)
            val byteArrayOutputStream = ByteArrayOutputStream()
            //1, 압축할 파일 타입 2, 압축 퀄리티 1~100까지가능 3, 압축된 ByteArray를 받는 매개 변수
            resized.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            //String 형식으로 변환하기
            //Base64 - 바이너리 데이터를 텍스트 형태로 변환(인코딩)해주는역할
            profileImageBase64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            binding.writingImage.setImageURI(currentImageURL)
        }
    }//end of

    //writing 행동
    fun writingBehavior() {
        if (binding.writingTitle.text.isEmpty() && binding.writingContent.text.isEmpty()
        ) {
            Toast.makeText(this@WritingActivity, "내용을 넣어주세요", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@WritingActivity, "등록 완료", Toast.LENGTH_SHORT).show()
            postBoard(
                binding.writingTitle.text.toString(),
                binding.writingContent.text.toString(),
                //String형식으로 변환된것을 POST
                profileImageBase64,
                binding.writingNickname.text.toString()
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }//end of writingBehavior
    //toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                var intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }//end of toolbar

    private fun postBoard(title: String, text: String, file_string: String, nickname: String) {
        val jsonplaceHolderApi = retrofitBuilder.retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call = jsonplaceHolderApi.createBoard(title, text, file_string, nickname)
        call?.enqueue(object : Callback<List<Model?>?> {
            override fun onResponse(
                call: Call<List<Model?>?>,
                response: Response<List<Model?>?>
            ) {
                binding.writingTitle.setText("")
                binding.writingContent.setText("")
                binding.writingNickname.setText("")
            }
            override fun onFailure(call: Call<List<Model?>?>, t: Throwable) {}
        })
    }

}
package com.example.myproject.Recycler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.Activity.MainActivity
import com.example.myproject.Retrofit.Model
import com.example.myproject.Retrofit.RetrofitBuilder
import com.example.myproject.databinding.ItemViewBinding
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewAdapter(val context: Context, var writingList: MutableList<WritingData>): RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {
    private var retrofitBuilder = RetrofitBuilder()
    val itembinding = ItemViewBinding.inflate(LayoutInflater.from(context))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val binding = holder.binding

        binding.infoSeq.text = writingList[position].seq.toString()
        binding.infoTitle.text = writingList[position].title
        binding.infoNickname.text = writingList[position].nickname
        binding.infoContents.text = writingList[position].content
        binding.infoDate.text = writingList[position].date.toString()

        binding.infoUpdate.setOnClickListener {
            binding.updateViewSeq.text = writingList[position].seq.toString()
            binding.updateViewTitle.setText(writingList.get(position).title)
            binding.updateViewContents.setText(writingList.get(position).content)
            binding.updateViewNickname.setText(writingList.get(position).nickname)
            binding.updateViewDate.text = writingList[position].date.toString()
            binding.infoLayout.visibility = View.GONE
            binding.updateLayout.visibility = View.VISIBLE
        }
        binding.updateCancel.setOnClickListener {
            binding.infoLayout.visibility = View.VISIBLE
            binding.updateLayout.visibility = View.GONE
        }


        binding.updateBtn.setOnClickListener {
            Toast.makeText(context, "수정 성공", Toast.LENGTH_SHORT).show()
            updateBoard(
                binding.updateViewSeq.text.toString().toInt(),
                binding.updateViewTitle.text.toString(),
                binding.updateViewContents.text.toString(),
                binding.updateViewNickname.text.toString(),
            )
            binding.infoLayout.visibility = View.VISIBLE
            binding.updateLayout.visibility = View.GONE
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }

        binding.deleteBtn.setOnClickListener {
            Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show()
            deleteBoard(
                binding.infoSeq.text.toString().toInt()
            )
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }

    }//end of onBindView

    override fun getItemCount(): Int {
        return writingList.size
    }
    class CustomViewHolder(val binding: ItemViewBinding):
        RecyclerView.ViewHolder(binding.root)

    //PATCH
    private fun updateBoard(seq: Int, title: String, content: String, nickname:String) {
        val jsonplaceHolderApi = retrofitBuilder.retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call: Call<List<Model?>?> = jsonplaceHolderApi.updateBoard(seq, title, content, nickname)!!
        call.enqueue(object : Callback<List<Model?>?> {
            override fun onResponse(call: Call<List<Model?>?>, response: Response<List<Model?>?>) {

                    itembinding.updateViewTitle.setText("")
                    itembinding.updateViewContents.setText("")
                    itembinding.updateViewNickname.setText("")

            }
            override fun onFailure(call: Call<List<Model?>?>, t: Throwable) {
            }
        })
    }//end of patchData

    //DELETE
    private fun deleteBoard(seq: Int) {
        val jsonplaceHolderApi = retrofitBuilder.retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call: Call<List<Model?>?> = jsonplaceHolderApi.deleteBoard(seq)!!
        call.enqueue(object : Callback<List<Model?>?> {
            override fun onResponse(call: Call<List<Model?>?>, response: Response<List<Model?>?>) {
                itembinding.infoSeq.text.toString()
            }
            override fun onFailure(call: Call<List<Model?>?>, t: Throwable) {
            }

        })
    }

}//end of


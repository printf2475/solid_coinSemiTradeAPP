package kr.or.mrhi.mycoinkt.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.myCoin.model.NewsItem
import kr.or.mrhi.mycoinkt.R
import kr.or.mrhi.mycoinkt.WebViewActivity
import kr.or.mrhi.mycoinkt.databinding.RecyclerItemBinding
import java.util.ArrayList

class NewsAdapter(items: ArrayList<NewsItem>, context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    var items: ArrayList<NewsItem>
    var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    init {
        this.items = items
        this.context = context
    }

    override fun getItemCount() =
        items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    //이너클래스
    inner class ViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            //현재번째(position) 아이템 얻어오기
            val item: NewsItem = items[position]
            binding.tvTitle.text=item.title
            binding.tvDesc.text=item.desc
            binding.tvDate.text=item.date


            //이미지는 없을 수도 있음.
            if (item.imgUrl == null) {
                binding.iv.visibility = View.GONE
            } else {
                binding.iv.visibility = View.VISIBLE
                //네트워크에 있는 이미지를 보여주려면
                //별도의 Thread가 필요한데 이를 편하게
                //해주는 Library사용(Glide library)
                Glide.with(context).load(item.imgUrl).into(binding.iv)
            }
        }

        init {
            binding.tvTitle.isSelected=true
            //리사이클뷰의 아이템뷰를 클릭했을 때
            itemView.setOnClickListener {
                val link: String = items[getLayoutPosition()].link.toString()

                //웹튜를 가진 새로운 액티비티
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("Link", link)
                context.startActivity(intent)
            }
        }
    }




}
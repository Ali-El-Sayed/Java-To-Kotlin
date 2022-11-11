package com.example.javatokotlin.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.javatokotlin.app.toast
import com.example.javatokotlin.databinding.ListItemBinding
import com.example.javatokotlin.models.Repository

class DisplayAdapter(
    private var context: Context,
    private var data: List<Repository>,
) :
    RecyclerView.Adapter<DisplayAdapter.ViewHolder>() {

    private lateinit var binding: ListItemBinding

    companion object {
        private val TAG = DisplayAdapter::class.java.simpleName
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = data[position]
        holder.setData(current, position)
    }

    override fun getItemCount(): Int = data.size

    fun swap(dataList: List<Repository>) {
        if (dataList.isEmpty())
            context.toast("No Items Found")
        // Util.showMessage(context, "No Items Found")
        this.data = dataList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private var pos: Int = 0
        private var current: Repository? = null

        init {
            binding.imgBookmark.setOnClickListener { bookmarkRepository(current) }
            binding.root.setOnClickListener {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                current?.let {
                    val url = current!!.htmlUrl
                    val webpage = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, webpage)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }

            }

        }

        fun setData(current: Repository, position: Int) {
            binding.txvName.text = current.name
            binding.txvLanguage.text = current.language
            binding.txvForks.text = current.forks.toString()
            binding.txvWatchers.text = current.watchers.toString()
            binding.txvStars.text = current.stars.toString()
            this.pos = position
            this.current = current
        }

        private fun bookmarkRepository(current: Repository?) {

//			Realm realm = Realm.getDefaultInstance();
//			realm.executeTransactionAsync(new Realm.Transaction() {
//				@Override
//				public void execute(@NonNull Realm realm) {
//					realm.copyToRealmOrUpdate(current);
//				}
//			}, new Realm.Transaction.OnSuccess() {
//				@Override
//				public void onSuccess() {
//					Util.showMessage(mContext, "Bookmarked Successfully");
//				}
//			}, new Realm.Transaction.OnError() {
//				@Override
//				public void onError(Throwable error) {
//					Log.i(TAG, error.toString());
//					Util.showMessage(mContext, "Error Occurred");
//				}
//			});
        }
    }
}
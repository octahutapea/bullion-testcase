import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bullion.bulliontestcase.R
import com.bullion.bulliontestcase.data.remote.response.UserItem
import com.bullion.bulliontestcase.databinding.ItemRowUserBinding
import com.bullion.bulliontestcase.ui.detail.DetailActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class ListUserAdapter(private val context: Context) : ListAdapter<UserItem, ListUserAdapter.ListViewHolder>(UserItemDiffCallback()) {

    class ListViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = getItem(position)
        Glide.with(holder.itemView.context)
            .load(user.photo)
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvItemName.text = user.name
        holder.binding.tvItemEmail.text = user.email

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        val date = inputFormat.parse(user.dateOfBirth)
        val formattedDate = outputFormat.format(date!!)

        holder.binding.tvItemBirthDate.text = formattedDate

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("userId", user.id)
            intent.putExtra("userName", user.name)
            context.startActivity(intent)
        }
    }

    class UserItemDiffCallback : DiffUtil.ItemCallback<UserItem>() {
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem == newItem
        }
    }
}

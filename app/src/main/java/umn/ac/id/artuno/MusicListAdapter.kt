package umn.ac.id.artuno

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import umn.ac.id.artuno.MusicListAdapter.MusicViewHolder

class MusicListAdapter internal constructor (
    private val context: Context?,
    private val musicList: MutableList<Music>
) : RecyclerView.Adapter<MusicViewHolder>() {
    inner class MusicViewHolder(
        itemView: View,
        adapter: MusicListAdapter
    ) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        val itemDuration: TextView = itemView.findViewById(R.id.tvItemDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(inflater.inflate(R.layout.item_list, parent, false), this)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.itemTitle.text = musicList[position].title

        val musicLength = musicList[position].duration / 1000
        val seconds = (musicLength % 60).toString()
        val minutes = (musicLength / 60).toString()
        val totalOut = "0$minutes.$seconds"
        val totalNew = "0$minutes.0$seconds"
        if (seconds.length == 1) holder.itemDuration.text = totalNew
        else holder.itemDuration.text = totalOut

        holder.itemView.setOnClickListener {
            context?.startActivity(Intent(context, MusicDetail::class.java)
                .putExtra("position", position))
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
}
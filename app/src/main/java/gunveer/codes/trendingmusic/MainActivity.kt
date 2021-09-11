package gunveer.codes.trendingmusic

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import gunveer.codes.trendingmusic.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), RecViewAdapter.OnItemClickListener {
    companion object{
        private const val TAG = "listt"
    }
    private var videos: ArrayList<Videos> = ArrayList()
    private var mAdapter: RecViewAdapter = RecViewAdapter(this@MainActivity)
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkCall()
        binding.floatingActionButton.setOnClickListener {
            videos = ArrayList()
            networkCall()
            Snackbar.make(binding.root, "Double tap to open a link", Snackbar.LENGTH_LONG).show()
        }
        binding.recView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
        Snackbar.make(binding.root, "Double tap to open a link", Snackbar.LENGTH_LONG).show()
    }

    private fun networkCall() {
        binding.progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&chart=mostPopular&maxResults=50&regionCode=in&videoCategoryId=10&key=AIzaSyCwtWy44V9zUnD502l36yv0a879sLCM4Og"
        val jsonObjectRequest = JsonObjectRequest(url, Response.Listener {
            val items = it.getJSONArray("items")
            val size = items.length() - 1
            for(i in 0..size){
                videos.add(Videos(
                items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"),
                items.getJSONObject(i).getJSONObject("snippet").getString("channelTitle"),
                items.getJSONObject(i).getString("id"),
                items.getJSONObject(i).getJSONObject("snippet").getString("title")))
            }
            Log.d(TAG, "networkCall: ${videos.lastIndex}")
            mAdapter.updateList(videos)
            binding.progressBar.visibility = View.GONE
        }, Response.ErrorListener {
            Log.d(TAG, "networkCall error: ${it.toString()}")
            Snackbar.make(binding.root, "Some error has occurred", Snackbar.LENGTH_LONG).show()
        })
        queue.add(jsonObjectRequest)
        
    }

    override fun onItemClick(position: Int) {
        val url = "https://www.youtube.com/watch?v=" + videos[position].urlId
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

    }
}
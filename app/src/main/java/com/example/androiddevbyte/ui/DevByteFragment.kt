package com.example.androiddevbyte.ui

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevbyte.R
import com.example.androiddevbyte.databinding.DevByteFragmentBinding
import com.example.androiddevbyte.databinding.DevbyteItemBinding
import com.example.androiddevbyte.domain.DevByteVideo

/**
 * Show a list of DevBytes on screen.
 */
class DevByteFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */

     private  val viewModel: DevByteViewModel by lazy{
        val activity = requireNotNull(this.activity){
            "You can only access viewmodel after onActivityCreated()"
        }
        ViewModelProviders.of(this,DevByteViewModel.Factory(activity.application))[DevByteViewModel::class.java]

    }



    /**
     * RecyclerView Adapter for converting a list of Video to cards.
     */
    private var viewModelAdapter: DevByteAdapter? = null

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI.
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DevByteFragmentBinding.inflate(inflater)

        binding.lifecycleOwner =viewLifecycleOwner


        binding.viewModel =viewModel

        viewModelAdapter = DevByteAdapter(DevByteAdapter.VideoClick {
            // When a video is clicked this block or lambda will be called by DevByteAdapter

            // context is not around, we can safely discard this click since the Fragment is no
            // longer on the screen
            val packageManager = context?.packageManager ?: return@VideoClick

            // Try to generate a direct intent to the YouTube app
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if (intent.resolveActivity(packageManager) == null) {
                // YouTube app isn't found, use the web url
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })


//        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = viewModelAdapter
//        }


         binding.recyclerView.layoutManager = LinearLayoutManager(context)
         binding.recyclerView.adapter = viewModelAdapter

        // Observer for the network error.
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        return  binding.root
    }


    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<DevByteVideo>> { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        })
    }


    /**
     * Helper method to generate YouTube app links
     */
    private val DevByteVideo.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }


    /**
     * RecyclerView Adapter for setting up data binding on the items in the list.
     */

    class DevByteAdapter (val videoClick: VideoClick):RecyclerView.Adapter<DevByteViewHolder>(){


        /**
         * The videos that our Adapter will show
         */
        var videos :List<DevByteVideo> = emptyList()
           set(value) {
               field =value

               // Notify any registered observers that the data set has changed. This will cause every
               // element in our RecyclerView to be invalidated.
               notifyDataSetChanged()
           }


        /**
         * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
         * an item.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
           return DevByteViewHolder.from(parent)
        }

        override fun getItemCount() = videos.size


        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
         * position.
         */
        override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
             val videoItems = videos[position]

            bind(holder, videoItems)
        }

        private fun bind(
            holder: DevByteViewHolder,
            videoItems: DevByteVideo
        ) {
            holder.devbyteItemBinding.video = videoItems
            holder.devbyteItemBinding.videoCallback = videoClick
        }


        /**
         * Click listener for Videos. By giving the block a name it helps a reader understand what it does.
         *
         */
        class VideoClick(val clickListiner : (DevByteVideo) -> Unit){

            /**
             * Called when a video is clicked
             *
             * @param video the video that was clicked
             */

            fun onClick(video: DevByteVideo) = clickListiner(video)
        }




    }

    /**
     * ViewHolder for DevByte items. All work is done by data binding.
     */

     class DevByteViewHolder(val devbyteItemBinding: DevbyteItemBinding):RecyclerView.ViewHolder(devbyteItemBinding.root){

         companion object{
             fun from(parent: ViewGroup):DevByteViewHolder{
                   val inflater = LayoutInflater.from(parent.context)
                  val devByteDataBinding = DataBindingUtil.inflate<DevbyteItemBinding>(inflater,R.layout.devbyte_item,parent,false)

                 return DevByteViewHolder(devByteDataBinding)
             }
         }
     }







}
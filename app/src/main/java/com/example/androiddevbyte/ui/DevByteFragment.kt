package com.example.androiddevbyte.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddevbyte.R
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dev_byte_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    class DevByteAdapter (val videoClick: VideoClick):RecyclerView.Adapter<DevByteViewHolder>(){

        var videos :List<DevByteVideo> = emptyList()
           set(value) {
               field =value

               // Notify any registered observers that the data set has changed. This will cause every
               // element in our RecyclerView to be invalidated.
               notifyDataSetChanged()
           }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
           return DevByteViewHolder.from(parent)
        }

        override fun getItemCount() = videos.size

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
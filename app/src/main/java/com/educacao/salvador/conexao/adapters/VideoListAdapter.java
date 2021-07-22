package com.educacao.salvador.conexao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.educacao.salvador.conexao.R;
import com.educacao.salvador.conexao.model.VideoListDataModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideolistHolder> {

    private ArrayList<VideoListDataModel> dataSet;
    private Context context = null;
    private RecycleViewClickListener listener;

    public VideoListAdapter(Context context, ArrayList<VideoListDataModel> dataSet, RecycleViewClickListener listener) {
        this.dataSet = dataSet;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public VideolistHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        VideolistHolder videoListHolder = new VideolistHolder(view);
        return videoListHolder;
    }

    @Override
    public void onBindViewHolder(VideolistHolder holder, int position){
        ImageView thumbnail = holder.thumbnail;
        TextView titulo = holder.titulo;
        TextView descricao = holder.descricao;

        VideoListDataModel object = dataSet.get(position);
        titulo.setText(object.getTitulo());
        descricao.setText(object.getDescricao());
        Picasso.get().load(object.getThumbnailURL()).into(thumbnail);
    }

    @Override
    public int getItemCount(){
        return dataSet.size();
    }

    public class VideolistHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView thumbnail;
        TextView titulo;
        TextView descricao;

        public VideolistHolder(final View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.titulo = (TextView) itemView.findViewById(R.id.titulo);
            this.descricao = (TextView) itemView.findViewById(R.id.descricao);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }

    }

    public interface RecycleViewClickListener{
        void onClick(View v, int position);
    }

}

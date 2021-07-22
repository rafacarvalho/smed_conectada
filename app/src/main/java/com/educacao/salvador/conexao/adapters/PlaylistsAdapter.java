package com.educacao.salvador.conexao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.educacao.salvador.conexao.model.PlaylistDataModel;
import com.educacao.salvador.conexao.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistHolder> {

    private ArrayList<PlaylistDataModel> dataSet;
    private Context context = null;
    private RecycleViewClickListener listener;

    public PlaylistsAdapter(Context context, ArrayList<PlaylistDataModel> dataSet, RecycleViewClickListener listener) {
        this.dataSet = dataSet;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        PlaylistHolder playlistHolder = new PlaylistHolder(view);
        return playlistHolder;
    }

    @Override
    public void onBindViewHolder(PlaylistHolder holder, int position){
        ImageView thumbnail = holder.thumbnail;
        TextView titulo = holder.titulo;

        PlaylistDataModel object = dataSet.get(position);
        titulo.setText(object.getTitulo());
        Picasso.get().load(object.getThumbnailURL()).into(thumbnail);
    }

    @Override
    public int getItemCount(){
        return dataSet.size();
    }

    public class PlaylistHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView thumbnail;
        TextView titulo;

        public PlaylistHolder(final View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.titulo = (TextView) itemView.findViewById(R.id.titulo);
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

package com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leonardo.pokemonapp.R;
import com.example.leonardo.pokemonapp.UI.pokemon.pokemonComments.PokemonCommentsFragment;
import com.example.leonardo.pokemonapp.network.resources.Comment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leonardo on 11/08/17.
 */

public class PokemonCommentsListAdapter extends RecyclerView.Adapter<PokemonCommentsListAdapter.ViewHolder>{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");

    private Comment[] comments;

    private PokemonCommentsFragment fragment;

    public PokemonCommentsListAdapter(PokemonCommentsFragment fragment, Comment[] comments) {
        this.comments = comments;
        this.fragment = fragment;
    }

    @Override
    public PokemonCommentsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.pokemon_comment_element, parent, false);
        return new ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(PokemonCommentsListAdapter.ViewHolder holder, int position) {
        holder.usernameView.setText(comments[position].getAuthor().getUserName());
        holder.dateView.setText(DATE_FORMAT.format(comments[position].getDate()));
        holder.contentView.setText(comments[position].getContent());
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.length;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pokemon_comment_element_username_view)
        TextView usernameView;
        @BindView(R.id.pokemon_comment_element_date_view)
        TextView dateView;
        @BindView(R.id.pokemon_comment_element_content)
        TextView contentView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
 }

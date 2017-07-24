package com.example.leonardo.pokemonapp.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonardo.pokemonapp.PokemonMainActivity;
import com.example.leonardo.pokemonapp.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by leonardo on 24/07/17.
 */

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {

    private final PokemonMainActivity context;

    public DrawerListAdapter(PokemonMainActivity context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.drawer_list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch(position) {
            case 0:
                Picasso.with(context).load(R.drawable.ic_logout).into(holder.drawerListImageView);
                return;
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }



    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.drawer_list_element_text_view)
        TextView draewrListTextView;

        @BindView(R.id.drawer_list_element_image_view)
        ImageView drawerListImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.drawer_list_element_image_view, R.id.drawer_list_element_text_view})
        public void onPokemonNameClick() {
            int adapterPosition = getAdapterPosition();
            switch(adapterPosition) {
                case 0:
                    context.logOut();
                    return;
            }
        }
    }

}

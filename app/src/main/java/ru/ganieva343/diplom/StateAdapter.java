package ru.ganieva343.diplom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StateAdapter  extends RecyclerView.Adapter<StateAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<State> states;
    private OnItemClickListener listener;


    StateAdapter(Context context, List<State> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);

    }
    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, int position) {
        State state = states.get(position);
        holder.imageView.setImageResource(state.getImageResource());
        holder.nameView.setText(state.getName());
        holder.typeView.setText(state.getType());

        // Добавляем слушатель на TextView
        holder.itemView.setOnClickListener (v -> {
            if (listener!= null) {
                listener.onItemClick(v, position);
            }

        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Update_delete.class);

                intent.putExtra("imageResource", state.getImageResource());
                intent.putExtra("name", state.getName());
                intent.putExtra("type", state.getType());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, typeView;

        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.image);
            nameView = view.findViewById(R.id.name);
            typeView = view.findViewById(R.id.type);

        }

    }
}

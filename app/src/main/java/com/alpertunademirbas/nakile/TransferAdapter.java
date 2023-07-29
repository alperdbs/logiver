package com.alpertunademirbas.nakile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {
    private ArrayList<Transfer> transferList;

    public TransferAdapter(List<Transfer> transfers) {
        this.transferList = new ArrayList<>(transfers);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, surname, phoneNumber, transferDate;
        ImageView character;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            surname = itemView.findViewById(R.id.surname);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            transferDate = itemView.findViewById(R.id.transferDate);
            character = itemView.findViewById(R.id.imageView9);

        }
    }

    @Override
    public TransferAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransferAdapter.ViewHolder holder, int position) {
        Transfer transfer = transferList.get(position);
        holder.name.setText(transfer.getNameSurname());
        holder.phoneNumber.setText(transfer.getPhoneNumber());
        holder.transferDate.setText(transfer.getTransferDate());
        String character = transfer.getCharacter();
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(character, "drawable", holder.itemView.getContext().getPackageName());
        holder.character.setImageResource(drawableResourceId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Transfer clickedTransfer = transferList.get(position);

                Intent intent = new Intent(v.getContext(), TransferDetailActivity.class);
                intent.putExtra("name", clickedTransfer.getName());
                intent.putExtra("surname", clickedTransfer.getSurname());
                intent.putExtra("phone", clickedTransfer.getPhoneNumber());
                intent.putExtra("date", clickedTransfer.getTransferDate());
                intent.putExtra("location", clickedTransfer.getTransferLocation());
                intent.putExtra("price", clickedTransfer.getTransferPrice());
                intent.putExtra("character", clickedTransfer.getCharacter());



                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return transferList.size();
    }

    public Transfer getTransfer(int position) {
        return transferList.get(position);
    }

    public void removeTransfer(int position) {
        transferList.remove(position);
        notifyItemRemoved(position);
    }
}


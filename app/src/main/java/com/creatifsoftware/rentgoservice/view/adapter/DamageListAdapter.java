package com.creatifsoftware.rentgoservice.view.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creatifsoftware.rentgoservice.R;
import com.creatifsoftware.rentgoservice.databinding.DamageInformationItemBinding;
import com.creatifsoftware.rentgoservice.model.DamageItem;
import com.creatifsoftware.rentgoservice.model.Equipment;
import com.creatifsoftware.rentgoservice.utils.BlobStorageManager;
import com.creatifsoftware.rentgoservice.view.callback.DamageItemClickCallback;
import com.creatifsoftware.rentgoservice.view.callback.DamageItemDeleteButtonCallback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DamageListAdapter extends RecyclerView.Adapter<DamageListAdapter.ItemListViewHolder> {
    @Nullable
    private final DamageItemClickCallback damageItemClickCallback;
    @Nullable
    private final DamageItemDeleteButtonCallback damageItemDeleteButtonCallback;
    @Nullable
    private final Equipment selectedEquipment;
    @Nullable
    private final String documentNumber;
    @Nullable
    private final String type;
    List<? extends DamageItem> itemList;


    public DamageListAdapter(@Nullable DamageItemClickCallback damageItemClickCallback,
                             @Nullable DamageItemDeleteButtonCallback damageItemDeleteButtonCallback,
                             @Nullable Equipment selectedEquipment,
                             @Nullable String documentNumber,
                             @Nullable String type) {

        this.damageItemClickCallback = damageItemClickCallback;
        this.damageItemDeleteButtonCallback = damageItemDeleteButtonCallback;
        this.selectedEquipment = selectedEquipment;
        this.documentNumber = documentNumber;
        this.type = type;
    }

    public void setDamageList(final List<? extends DamageItem> itemList) {
        if (this.itemList == null) {
            this.itemList = itemList;
            notifyItemRangeInserted(0, itemList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return DamageListAdapter.this.itemList.size();
                }

                @Override
                public int getNewListSize() {
                    return itemList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return DamageListAdapter.this.itemList.get(oldItemPosition).damageId.equals(itemList.get(newItemPosition).damageId);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DamageItem damageItem = itemList.get(newItemPosition);
                    DamageItem old = DamageListAdapter.this.itemList.get(oldItemPosition);
                    return damageItem.damageId.equals(old.damageId);
                }
            });
            this.itemList = itemList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DamageInformationItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.damage_information_item,
                        parent, false);

        binding.setCallback(damageItemClickCallback);
        binding.setDeleteCallback(damageItemDeleteButtonCallback);
        return new ItemListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        DamageItem damageItem = itemList.get(position);
        holder.binding.setDamageItem(damageItem);
        holder.binding.deleteButton.setVisibility(damageItemDeleteButtonCallback == null ? View.GONE :
                damageItem.damageInfo.isNewDamage ? View.VISIBLE : View.GONE);
        if (damageItem.damagePhotoFile != null) {
            Picasso.get().load(damageItem.damagePhotoFile).into(holder.binding.damagePhoto);
        } else if (damageItem.blobStoragePath != null) {
            Picasso.get().load(damageItem.blobStoragePath).into(holder.binding.damagePhoto);
        } else if (damageItem.damagePhotoBitmap != null) {
            holder.binding.damagePhoto.setImageBitmap(damageItem.damagePhotoBitmap);
        }

        holder.binding.executePendingBindings();
    }

    private void downloadPhoto(@NonNull ItemListViewHolder holder, DamageItem damageItem) {
        String refName = BlobStorageManager.instance.prepareEquipmentImageName(selectedEquipment, documentNumber, type, damageItem.damageId);
        final ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        final Handler handler = new Handler();
        Thread th = new Thread(() -> {
            try {
                long imageLength = 0;
                BlobStorageManager.instance.GetImage(BlobStorageManager.instance.getEquipmentsContainerName(), refName, imageStream, imageLength);
                handler.post(() -> {
                    byte[] buffer = imageStream.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                    damageItem.damagePhotoBitmap = bitmap;
                    holder.binding.damagePhoto.setImageBitmap(bitmap);
                    imageStream.reset();
                });
            } catch (Exception ex) {
                final String exceptionMessage = ex.getMessage();
                handler.post(imageStream::reset);
            }
        });
        th.start();
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ItemListViewHolder extends RecyclerView.ViewHolder {

        final DamageInformationItemBinding binding;

        public ItemListViewHolder(DamageInformationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

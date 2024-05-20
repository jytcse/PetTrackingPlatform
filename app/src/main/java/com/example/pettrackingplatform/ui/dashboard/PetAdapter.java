package com.example.pettrackingplatform.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettrackingplatform.R;

import java.util.List;

// PetAdapter 用於在 RecyclerView 中顯示寵物數據
public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    // 寵物數據列表
    private List<Pet> petList;
    // 點擊事件的監聽器
    private OnItemClickListener onItemClickListener;
    // ViewHolder 用於存儲每一個列表項目的視圖
    public static class PetViewHolder extends RecyclerView.ViewHolder {
        // TextView 用於顯示寵物名字
        public TextView petNameTextView;
        public TextView petBirthTextView;

        // ViewHolder 的構造函數，傳入列表項目的視圖
        public PetViewHolder(View itemView) {
            super(itemView);
            // 初始化 TextView，這裡要確保與布局文件中的 ID 匹配
            petNameTextView = itemView.findViewById(R.id.petName);
            petBirthTextView = itemView.findViewById(R.id.petBirth);
        }
    }

    // 構造函數，傳入點擊事件監聽器
    public PetAdapter(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // 設置寵物數據列表
    public void setPets(List<Pet> petList) {
        this.petList = petList;
    }

    // 創建新的列表項目視圖（由佈局管理器調用）
    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 創建一個新的視圖
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_list_component, parent, false);
        return new PetViewHolder(v);
    }

    // 替換視圖的內容（由佈局管理器調用）
    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        // 獲取當前位置的寵物數據
        Pet pet = petList.get(position);
        // 設置 TextView 顯示寵物名字
        holder.petNameTextView.setText(pet.getName());
        holder.petBirthTextView.setText(pet.getBirthday());

        // 添加點擊事件
        holder.itemView.setOnClickListener(view -> {
            // 在點擊事件中執行相應的操作
            if (onItemClickListener != null) {
                // 通知監聽器，將當前寵物作為參數傳遞
                onItemClickListener.onPetClick(pet);
            }
        });
    }

    // 返回數據集的大小（由佈局管理器調用）
    @Override
    public int getItemCount() {
        return petList == null ? 0 : petList.size();
    }

    // 定義點擊監聽器接口
    public interface OnItemClickListener {
        // 當項目被點擊時調用的方法，參數為被點擊的寵物對象
        void onPetClick(Pet pet);
    }
}

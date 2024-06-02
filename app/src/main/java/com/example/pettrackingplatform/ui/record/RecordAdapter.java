package com.example.pettrackingplatform.ui.record;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettrackingplatform.R;

import java.util.List;

// RecordAdapter 用于在 RecyclerView 中显示宠物医疗数据
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    // 医疗记录数据列表
    private List<Record> recordList;

    // ViewHolder 用于存储每一个列表项的视图
    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        // TextView 用于显示医疗记录类型和费用
        public TextView recordTypeTextView;
        public TextView recordCostTextView;

        // ViewHolder 的构造函数，传入列表项的视图
        public RecordViewHolder(View itemView) {
            super(itemView);
            // 初始化 TextView，这里要确保与布局文件中的 ID 匹配
            recordTypeTextView = itemView.findViewById(R.id.record_type);
            recordCostTextView = itemView.findViewById(R.id.record_cost);
        }
    }

    // 设置医疗记录数据列表
    public void setRecords(List<Record> recordList) {
        this.recordList = recordList;
        notifyDataSetChanged(); // 通知数据集更改
    }

    // 创建新的列表项视图（由布局管理器调用）
    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建一个新的视图
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_list_component, parent, false);
        return new RecordViewHolder(v);
    }

    // 替换视图的内容（由布局管理器调用）
    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        // 获取当前位置的医疗记录数据
        Record record = recordList.get(position);
        // 设置 TextView 显示医疗记录类型和费用
        holder.recordTypeTextView.setText(record.getType());
        holder.recordCostTextView.setText("$" + String.valueOf(record.getCost())); // 将费用转换为字符串
    }

    // 返回数据集的大小（由布局管理器调用）
    @Override
    public int getItemCount() {
        return recordList == null ? 0 : recordList.size();
    }
}

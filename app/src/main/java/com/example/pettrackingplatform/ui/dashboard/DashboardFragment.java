package com.example.pettrackingplatform.ui.dashboard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pettrackingplatform.R;
import com.example.pettrackingplatform.SharedPreferencesUtil;
import com.example.pettrackingplatform.databinding.FragmentDashboardBinding;
import com.example.pettrackingplatform.ui.pet.NewPet;
import com.example.pettrackingplatform.ui.record.RecordList;

import java.util.List;

public class DashboardFragment extends Fragment implements PetAdapter.OnItemClickListener {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private PetAdapter petAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
       View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // 初始化 RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.petList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 創建 PetAdapter 實例並設置監聽器
        petAdapter = new PetAdapter(this);
        recyclerView.setAdapter(petAdapter);

        // 從後端服務中獲取寵物數據並更新 RecyclerView
        fetchPetData();

        Button createButton = root.findViewById(R.id.newPet);
        createButton.setOnClickListener(v -> {


                Intent intent = new Intent(getActivity(), NewPet.class);
                startActivity(intent);


        });


        return root;
    }

    private void fetchPetData() {
        // 从 SharedPreferences 中获取用户 ID
        int userId = SharedPreferencesUtil.getUserId(requireContext());

        if (userId == -1) {
            // 在这里处理用户未登录的情况，例如跳转到登录页面
            return;
        }

        // 使用 AsyncTask 异步获取数据
        new FetchPetDataTask(userId).execute();
    }

    @Override
    public void onPetClick(Pet pet) {

        Intent intent = new Intent(getActivity(), RecordList.class);
       intent.putExtra("petID",pet.getId());

        startActivity(intent);

    }

    private class FetchPetDataTask extends AsyncTask<Void, Void, List<Pet>> {
        private int userId;

        public FetchPetDataTask(int userId) {
            this.userId = userId;
        }

        @Override
        protected List<Pet> doInBackground(Void... voids) {
            return PetRepository.getPetListForUser(userId);
        }

        @Override
        protected void onPostExecute(List<Pet> pets) {
            if (pets != null) {
                petAdapter.setPets(pets);
                petAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

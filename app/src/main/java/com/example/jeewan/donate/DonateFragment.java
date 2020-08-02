package com.example.jeewan.donate;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.jeewan.databinding.FragmentDonateBinding;
import com.example.jeewan.request.RequestModel;
import com.example.jeewan.request.RequestViewModel;
import com.example.jeewan.request.RequestViewModelFactory;



import java.util.List;

public class DonateFragment extends Fragment {
    FragmentDonateBinding donateBinding;
    RequestViewModel viewModel;
    String search_criteria;
    List<RequestModel> list;
    final String TAG = "Donate Fragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        donateBinding = FragmentDonateBinding.inflate(inflater, container, false);
        //set linear layout manager for donate_recview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        donateBinding.donateRecyclerview.setLayoutManager(linearLayoutManager);

        return donateBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        donateBinding.swipeRefreshLayout.setRefreshing(true);
        init();

        requestData();

        donateBinding.searchCriteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search_criteria = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        donateBinding.searchChoiceEdittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

                donateBinding.swipeRefreshLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        viewModel.getReqDataListWithCriteria(search_criteria, charSequence.toString()).observe(getActivity(),
                                new Observer<List<RequestModel>>() {
                                    @Override
                                    public void onChanged(List<RequestModel> requestModels) {
                                        if(requestModels.size()>0) {
                                            donateBinding.donateRecyclerview.setAdapter(new DonateAdapter(getActivity(), requestModels));
                                        }
                                        else{
                                            donateBinding.norequestTv.setVisibility(View.VISIBLE);
                                        }
                                        donateBinding.swipeRefreshLayout.setRefreshing(false);
                                        viewModel.getReqDataListWithCriteria(search_criteria,charSequence.toString()).removeObservers(getActivity());
                                    }
                                });
                    }
                }, 200);
                viewModel.getReqDataListWithCriteria(search_criteria,charSequence.toString()).removeObservers(getActivity());

            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //refreshLayout
        donateBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                donateBinding.swipeRefreshLayout.setRefreshing(true);

                if (donateBinding.searchChoiceEdittext.getText().toString().trim().length() == 0) {
                    viewModel.getReqDataList().observe(getActivity(), new Observer<List<RequestModel>>() {
                        @Override
                        public void onChanged(List<RequestModel> requestModels) {
                            Log.d(TAG, "onChanged: in refresh");
                            if(requestModels.size()>0){
                            donateBinding.donateRecyclerview.setAdapter(new DonateAdapter(getActivity(),requestModels));}
                            else{
                                donateBinding.norequestTv.setVisibility(View.VISIBLE);
                            }
                            donateBinding.swipeRefreshLayout.setRefreshing(false);
                            viewModel.getReqDataList().removeObservers(getActivity());
                        }
                    });
                }
                else {

                    viewModel.getReqDataListWithCriteria(search_criteria, donateBinding.searchChoiceEdittext.getText().toString())
                            .observe(getViewLifecycleOwner(),
                            new Observer<List<RequestModel>>() {
                                @Override
                                public void onChanged(List<RequestModel> requestModels) {
                                    if(requestModels.size()>0){
                                    donateBinding.donateRecyclerview.setAdapter(new DonateAdapter(getActivity(), requestModels));}
                                    else{
                                        donateBinding.norequestTv.setVisibility(View.VISIBLE);
                                    }
                                    donateBinding.swipeRefreshLayout.setRefreshing(false);
                                }
                            });
                }
            }
        });


    }

    public void init() {
        viewModel = new ViewModelProvider(getActivity()
                , new RequestViewModelFactory(" ", " ", " ",
                " ", " ", " ", " ", " ", " ")).get(RequestViewModel.class);
    }

    public void requestData(){

        viewModel.getReqDataList().observe(requireActivity(), new Observer<List<RequestModel>>() {
            @Override
            public void onChanged(List<RequestModel> requestModels) {
                if(requestModels.size()>0) {
                    donateBinding.donateRecyclerview.setAdapter(new DonateAdapter(getContext(), requestModels));
                }
                else
                {
                    donateBinding.norequestTv.setVisibility(View.VISIBLE);
                }
                donateBinding.swipeRefreshLayout.setRefreshing(false);
                viewModel.getReqDataList().removeObservers(requireActivity());
            }
        });
    }



}

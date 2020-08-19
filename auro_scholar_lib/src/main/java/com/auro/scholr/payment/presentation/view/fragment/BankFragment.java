package com.auro.scholr.payment.presentation.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.auro.scholr.R;
import com.auro.scholr.core.application.AuroApp;
import com.auro.scholr.core.application.base_component.BaseFragment;
import com.auro.scholr.core.application.di.component.ViewModelFactory;
import com.auro.scholr.core.common.AppConstant;
import com.auro.scholr.core.common.CommonCallBackListner;
import com.auro.scholr.core.common.CommonDataModel;
import com.auro.scholr.core.common.ValidationModel;
import com.auro.scholr.databinding.BankFragmentLayoutBinding;
import com.auro.scholr.home.data.model.DashboardResModel;
import com.auro.scholr.payment.data.model.request.PaytmWithdrawalReqModel;
import com.auro.scholr.payment.data.model.response.PaytmResModel;
import com.auro.scholr.payment.presentation.viewmodel.SendMoneyViewModel;
import com.auro.scholr.util.AppLogger;
import com.auro.scholr.util.ViewUtil;
import com.auro.scholr.util.alert_dialog.CustomDialogModel;
import com.auro.scholr.util.alert_dialog.CustomProgressDialog;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.auro.scholr.core.common.Status.PAYTM_WITHDRAWAL;


public class BankFragment extends BaseFragment implements CommonCallBackListner, View.OnClickListener {

    @Inject
    @Named("BankFragment")
    ViewModelFactory viewModelFactory;
    BankFragmentLayoutBinding binding;
    SendMoneyViewModel viewModel;
    boolean isStateRestore;
    private String TAG = "BankFragment";
    CustomProgressDialog customProgressDialog;
    DashboardResModel mdashboard;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        AuroApp.getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendMoneyViewModel.class);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }


    @Override
    protected void init() {

        if (getArguments() != null) {
            mdashboard = getArguments().getParcelable(AppConstant.DASHBOARD_RES_MODEL);
        }

        binding.walletBalText.setText("₹"+mdashboard.getWalletbalance()+".00");

        if (viewModel != null && viewModel.serviceLiveData().hasObservers()) {
            viewModel.serviceLiveData().removeObservers(this);

        } else {
            observeServiceResponse();
        }
    }



    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {
        binding.sendButton.setOnClickListener(this);
    }


    @Override
    protected int getLayout() {
        return R.layout.bank_fragment_layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {

        }
        init();
        setToolbar();
        setListener();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void commonEventListner(CommonDataModel commonDataModel) {

    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.send_button){
            paytmwithdrawalAmountApi();
        }

    }


    private void reloadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    private void paytmwithdrawalAmountApi() {
        String accountnumber = binding.accountNumber.getText().toString();
        String confirmaccountnumber = binding.confirmAccountNumber.getText().toString();
        String ifscCode = binding.ifscCode.getText().toString();
        ValidationModel bankAccountvalidation = viewModel.paymentUseCase.isValidBankAccountNumber(accountnumber,ifscCode,confirmaccountnumber);

        if(bankAccountvalidation.isStatus()){
            PaytmWithdrawalReqModel reqModel = new PaytmWithdrawalReqModel();
            reqModel.setMobileNumber(AuroApp.getAuroScholarModel().getMobileNumber());
            reqModel.setUpiAddress("test@upi.com");
            reqModel.setDisbursementMonth("202017");
            reqModel.setDisbursement("50");
            reqModel.setBankAccount("00000");
            reqModel.setIfscCode("33445566");
            viewModel.paytmWithdrawal(reqModel);
        }else{
            showSnackbarError(bankAccountvalidation.getMessage());
        }

    }
    private void showSnackbarError(String message) {
        ViewUtil.showSnackBar(binding.getRoot(), message);
    }

    private void observeServiceResponse() {

        viewModel.serviceLiveData().observeForever(responseApi -> {

            switch (responseApi.status) {

                case LOADING:
                    //For ProgressBar

                        openProgressDialog();

                    break;

                case SUCCESS:
                    if (responseApi.apiTypeStatus == PAYTM_WITHDRAWAL) {
                        closeDialog();
                        PaytmResModel mpaytm = (PaytmResModel) responseApi.data;

                    }

                    break;

                case NO_INTERNET:
                case AUTH_FAIL:
                case FAIL_400:
// When Authrization is fail

                    break;


                default:
                    Log.d(TAG, "observeServiceResponse: default");

                    break;
            }

        });
    }

    private void openProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            return;
        }
        CustomDialogModel customDialogModel = new CustomDialogModel();
        customDialogModel.setContext(getActivity());
        customDialogModel.setTitle("Processing your payment...");
        customDialogModel.setTwoButtonRequired(true);
        customProgressDialog = new CustomProgressDialog(customDialogModel);
        Objects.requireNonNull(customProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();
        customProgressDialog.updateDataUi(0);
    }
    public void closeDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }

}

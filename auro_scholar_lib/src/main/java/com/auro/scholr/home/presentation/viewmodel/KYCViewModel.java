package com.auro.scholr.home.presentation.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.auro.scholr.R;
import com.auro.scholr.core.application.AuroApp;
import com.auro.scholr.core.common.MessgeNotifyStatus;
import com.auro.scholr.core.common.ResponseApi;
import com.auro.scholr.core.common.Status;
import com.auro.scholr.home.data.model.KYCDocumentDatamodel;
import com.auro.scholr.home.domain.usecase.HomeDbUseCase;
import com.auro.scholr.home.domain.usecase.HomeRemoteUseCase;
import com.auro.scholr.home.domain.usecase.HomeUseCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.auro.scholr.core.common.Status.UPLOAD_PROFILE_IMAGE;


public class KYCViewModel extends ViewModel {
    CompositeDisposable compositeDisposable;
    public MutableLiveData<ResponseApi> serviceLiveData = new MutableLiveData<>();

    private MutableLiveData<MessgeNotifyStatus> notifyLiveData = new MutableLiveData<>();

    public MutableLiveData<String> mobileNumber = new MutableLiveData<>();
    public HomeUseCase homeUseCase;
    public HomeDbUseCase homeDbUseCase;
    public HomeRemoteUseCase homeRemoteUseCase;

    public KYCViewModel(HomeUseCase homeUseCase, HomeDbUseCase homeDbUseCase, HomeRemoteUseCase homeRemoteUseCase) {
        this.homeUseCase = homeUseCase;
        this.homeDbUseCase=homeDbUseCase;
        this.homeRemoteUseCase=homeRemoteUseCase;
    }


    public void uploadProfileImage(List<KYCDocumentDatamodel> list) {

        Disposable disposable = homeRemoteUseCase.isAvailInternet().subscribe(hasInternet -> {
            if(hasInternet) {

                callUploadImageApi(list);

            } else {

                // please check your internet
                notifyLiveData.setValue(new MessgeNotifyStatus(Status.NO_INTERNET, AuroApp.getAppContext().getString(R.string.internet_check)));
            }

        });
        getCompositeDisposable().add(disposable);

    }



    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }
    private void callUploadImageApi(List<KYCDocumentDatamodel> list) {

        getCompositeDisposable().add(homeRemoteUseCase.uploadProfileImage(list).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable __) throws Exception {
                serviceLiveData.setValue(ResponseApi.loading(null));
            }
        })
                .subscribe(

                        new Consumer<ResponseApi>() {
                            @Override
                            public void accept(ResponseApi checkUpdate) throws Exception {

                                serviceLiveData.setValue(checkUpdate);

                            }
                        },

                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                serviceLiveData.setValue(ResponseApi.fail(AuroApp.getAppContext().getResources().getString(R.string.default_error), UPLOAD_PROFILE_IMAGE));
                            }
                        }));

    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    public LiveData<ResponseApi> serviceLiveData() {

        return serviceLiveData;

    }

}
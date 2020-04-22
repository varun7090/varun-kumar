package com.example.aurosampleapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.auro.scholr.core.common.AppConstant;
import com.auro.scholr.home.data.model.AuroScholarDataModel;
import com.auro.scholr.util.AppLogger;
import com.auro.scholr.util.AuroScholar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuroScholarDataModel auroScholarDataModel = new AuroScholarDataModel();
        auroScholarDataModel.setMobileNumber("7503600687");
        auroScholarDataModel.setStudentClass("6");
        auroScholarDataModel.setScholarID("577159");
        auroScholarDataModel.setRegistrationSource("auro-google");
        auroScholarDataModel.setActivity(this);
        auroScholarDataModel.setFragmentContainerUiId(R.id.home_container);
        AuroScholar.openAuroDashboardFragment(auroScholarDataModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLogger.e("chhonker","Activity requestCode="+requestCode);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
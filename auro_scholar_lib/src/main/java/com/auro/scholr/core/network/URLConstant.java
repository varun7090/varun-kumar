package com.auro.scholr.core.network;


public interface URLConstant {

    String BASE_URL = "http://auroscholar.com/api/"; // PRODUCTION
    // String BASE_URL = "http://3.210.192.55:6060/"; // UAT
//    String BASE_URL = "http://14.142.204.99:7070/"; // SIT
    // String BASE_URL = BuildConfig.BASE_URL;

    String DASHBOARD_API = BASE_URL + "dashboard.php";

    String DEMOGRAPHIC_API = BASE_URL + "demographics.php";


    String UPLOAD_IMAGE_URL = BASE_URL + "kyc.php";
    String GET_ASSIGNMENT_ID = "http://splashwebapi.eklavvya.in/SplashService.svc/AssignExamToStudent";


}
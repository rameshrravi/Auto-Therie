package com.auto.autotherieneu;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class StringConstants {

    public static String mainUrl=" http://demo6.maxwellserver.com/clients/Auto%20Theorie%20neu/mapiv1/index.php";
    public static String loginUrl="index.php";
    public static String getAccessTokenUrl="get_token.php?";

    public static String prefToken ="UserToken";
    public static String prefUserID ="UserID";
    public static String prefTotalQuestions ="TotalQuestions";
    public static String prefName ="Name";
    public static String prefGender ="Gender";
    public static String prefDOB ="DateOfBirth";
    public static String prefBloodGroup ="BloodGroup";
    public static String prefReligion ="Religion";
    public static String prefEmailID ="EmailID";
    public static String prefStandard ="Standard";
    public static String prefSection ="Section";
    public static String prefAdmissionNumber ="AdmissionNumber";
    public static String prefFatherName ="FatherName";
    public static String prefMotherName ="MotherName";
    public static String prefMobileNumber ="MobileNumber";
    public static String prefStreet ="Street";
    public static String prefArea ="Area";
    public static String prefDistrict ="District";
    public static String prefPincode ="Pincode";
    public static String prefTransport ="Transport";
    public static String prefBio ="Bio";
    public static String prefPhoto ="Photo";
    public static String prefLogoutPin ="LogoutPin";
    public static String prefPassword ="Password";


    public static String prefSiteManagerID ="SiteManagerID";
    public static String prefSiteManagerFirstName ="SiteManagerFirstName";
    public static String prefSiteManagerLastName ="SiteManagerLastName";
    public static String prefSiteManagerEmail ="SiteManagerEmail";
    public static String prefSiteManagerPhoneNo ="SiteManagerPhoneNumber";
    public static String prefSiteManagerLogoutPin ="SiteManagerLogoutPin";

 public static String prefPrecinctID ="PrecinctID";
 public static String prefParkingMarshalID ="ParkingMarshaID";
 public static String prefParkingMarshalLoginID ="ParkingMarshalLoginID";
    public static String prefParkingMarshalFirstName ="ParkingMarshalFirstName";
    public static String prefParkingMarshalLastName ="ParkingMarshalLastName";
    public static String prefParkingMarshalEmail ="ParkingMarshalEmail";
    public static String prefParkingMarshalPhoneNo ="ParkingMarshalPhoneNumber";
    public static String prefParkingMarshalLogoutPin ="ParkingMarshalLogoutPin";
    public static String prefParkingMarshalPassword ="ParkingMarshalPassword";

    public static String prefCategory ="Category";
    public static String prefZoneList ="ZoneList";
    public static String prefLanguageID ="LanguageID";

    public static String prefMySharedPreference="AutoTherieNeuPreference";


    public static String ErrorMessage(VolleyError volleyError) {
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
// Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
//Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
//Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        return message;
    }

}

package com.autostrad.rentcar.util;

import com.adoisstudio.helper.JsonList;
import com.autostrad.rentcar.model.CarModel;

public class Config {

    public static String VERIFICATION_FOR = "VERIFICATION_FOR";
    public static String MOBILE_NUMBER = "MOBILE_NUMBER";
    public static String COUNTRY_CODE = "COUNTRY_CODE";
    public static String ENGLISH = "en";
    public static String ARABIC = "ar";
    public static String HOME = "HOME";
    public static String PROFILE = "PROFILE";
    public static String FLEET = "FLEET";
    public static String MENU = "MENU";
    public static String LOGIN = "LOGIN";
    public static String SIGN_UP = "SIGN_UP";
    public static String WEB_URL = "WEB_URL";
    public static String currentFlag = "";
    public static String currentProfileFlag = "";
    public static String currentFAQFlag = "";
    public static String driverIDFORDOC = "";

    public static String SelectedPickUpID = "";
    public static String SelectedPickUpEmirateID = "";
    public static String SelectedPickUpDate = "";
    public static String SelectedPickUpTime = "";
    public static String SelectedPickUpAddress = "";
    public static String SelectedPickUpLandmark = "";

    public static String SelectedDropUpID = "";
    public static String SelectedDropUpEmirateID = "";
    public static String SelectedDropUpDate = "";
    public static String SelectedDropUpTime = "";
    public static String SelectedDropUpAddress = "";
    public static String SelectedDropUpLandmark = "";
    public static CarModel carModel;

    public static String My_Account = "My Account";
    public static String Documents = "Documents";
    public static String Additional_Driver = "Additional Driver";
    public static String Additional_Driver_Document = "Additional Driver Document";
    public static String Booking = "Booking";
    public static String Manage_Payments = "Manage Payments";
    public static String Invoices = "Invoices";
    public static String Salik_Charges = "Salik Charges";
    public static String Traffic_Lines = "Traffic Lines";
    public static String Fast_Loyalty = "Fast Loyalty";
    public static String Refund = "Refund";
    public static String Extend_Booking = "EXTEND BOOKING";

    public static String General = "General";
    public static String Payment_Related = "Payment Related";
    public static String Vehicle_Related = "Vehicle Related";
    public static String Insurance_Related = "Insurance Related";

    public static String PROFILE_TAG = "PROFILE_TAG";
    public static String FAQ_TAG = "FAQ_TAG";
    public static String FLEET_TAG = "FLEET_TAG";
    public static String PAY_TYPE = "PAY_TYPE";
    public static String PAY_ID = "PAY_ID";
    public static String CAR_ID = "CAR_ID";
    public static String SELECTED_AED = "SELECTED_AED";
    public static String pay_now = "pay_now";
    public static String pay_latter = "pay_latter";
    public static String cityName = "cityName";
    public static String countryID = "countryID";
    public static String zipCode = "zipCode";
    public static String address1 = "address1";
    public static String address2 = "address2";

    public static String monthDuration = "monthDuration";
    public static String bookingTYpe = "bookingTYpe";
    public static String pickUpEmirateID = "pickUpEmirateID";
    public static String dropUpEmirateID = "dropUpEmirateID";
    public static String pickUpDate = "pickUpDate";
    public static String dropUpDate = "dropUpDate";
    public static String pickUpTime = "pickUpTime";
    public static String dropUpTime = "dropUpTime";
    public static String pickUpType = "pickUpType";
    public static String pickUpTypeValue = "";
    public static String dropUpType = "dropUpType";
    public static String dropUpTypeValue = "";
    public static String pickUpLocationID = "pickUpLocationID";
    public static String dropUpLocationID = "dropUpLocationID";
    public static String pickUpAddress = "pickUpAddress";
    public static String dropUpAddress = "dropUpAddress";

    public static String cardNumber = "cardNumber";
    public static String cardName = "cardName";
    public static String cardValidMonth = "cardValidMonth";
    public static String cardValidCVV = "cardValidCVV";
    public static String monthly = "monthly";
    public static String daily = "daily";

    public static int OPEN = 1;
    public static int SHARE = 2;
    public static int FILTER_ONE= 1;
    public static int FILTER_TWO= 2;
    public static int FILTER_VALUE = 0;
    public static boolean FROM_MAP = false;

    public static String firstName = "";
    public static String lastName = "";
    public static String email = "";
    public static String phone = "";
    public static String code = "";
    public static Integer positionCode = null;
    public static boolean isEditDetails;

    public static JsonList countryJsonList = new JsonList();


    public static String pickupDay = "";
    public static String pickupMonth = "";
    public static String pickupYear = "";
    public static String pickupTime = "";

    public static String dropOffDay = "";
    public static String dropOffMonth = "";
    public static String dropOffYear = "";
    public static String dropOffTime = "";

    public static String deliveryEmirateName = "";
    public static String collectEmirateName = "";
    public static String collect = "collect";
    public static String deliver = "deliver";
    public static String self_pickup = "self_pickup";
    public static String self_dropoff = "self_return";
    public static boolean HOME_DELIVERY_CHECK = false;
    public static boolean HOME_COLLECT_CHECK = false;
    public static boolean FOR_EDIT_LOCATION = false;

    public static String UAE = "United Arab Emirates";


}

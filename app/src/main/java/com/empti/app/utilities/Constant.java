package com.empti.app.utilities;

import android.location.Location;
import android.util.Log;

public class Constant {
    // public static final String FAQURl="http://ec2-18-219-13-255.us-east-2.compute.amazonaws.com/takai/public/images/users/";
    public static final String FAQURl = "https://empti.org/empti/public/images/users/";
    public static final String UPDATE_TOKEN = "device_token";
    public static final String USER_ID = "userId";
    public static final String EMAIL_ID = "email";
    public static final String BEARER_TOKEN = "bearer";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String DATE_OF_BIRTH = "dob";
    public static final String PHONE_NUMBER = "phoneNum";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String GENDER = "gender";
    public static final String CITY = "city";
    public static final String ROLE = "role";
    public static final String KEY = "takai";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_LOGO = "shop_logo";
    public static final String IMAGE = "image";
    public static final String FRONT_IMAGE = "https://empti.org/empti/public/images/users/";
    public static final String OUTLET_IMAGE = "https://empti.org/empti/public/images/shops/";
    //public static final String FRONT_IMAGE="http://ec2-18-219-13-255.us-east-2.compute.amazonaws.com/takai/public/images/users/";
    //public static final String OUTLET_IMAGE="http://ec2-18-219-13-255.us-east-2.compute.amazonaws.com/takai/public/images/shops/";
    public static final String FACEBOOK_TOKEN = "fbToken";
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String outlet_id = "outlet_id";
    public static String shop_name = "shop_name";
    public static String shop_logo = "shop_logo";
    public static String latitude = "latitude";
    public static String longitude = "longitude";
    public static String name = "name";
    public static String photo = "photo";
    public static String container_id = "container_id";
    public static String total_container = "total_container";
    public static final String CONTAINER_IMAGE = "https://empti.org/empti/public/images/containers/";
    public static  final String USER_IMAGE = "https://empti.org/empti/public/images/users/";
    public static final String BATCHES_IMAGE_BASE_URL = "https://empti.org/empti/public/images/batches/";
    //public static final String CONTAINER_IMAGE="http://ec2-18-219-13-255.us-east-2.compute.amazonaws.com/takai/public/images/containers/";
    public static String available_container = "available_container";
    public static String id = "id";
    public static String qrcode = "qrcode";
    public static String deposit = "deposit";
    public static String fee = "fee";
    public static String type = "type";
    public static String outlet_containers_id = "outlet_containers_id";
    public static String status = "status";
    public static String modified_at = "modified_at";
    public static String picked_date = "picked_date";
    public static String returned_date = "returned_date";
    public static String page_no = "page_no";
    public static String outlet_container_id = "outlet_container_id";
    public static String old_password = "old_password";
    public static String new_password = "new_password";
    public static String total = "total";

    public static double distance(double startLat, double startLng, double endLat, double endLng) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(startLat);
        startPoint.setLongitude(startLng);
        Location endPoint = new Location("locationA");
        endPoint.setLatitude(endLat);
        endPoint.setLongitude(endLng);
        double distance = startPoint.distanceTo(endPoint);
        Log.e("distance", distance + "");
        return distance;
    }
}

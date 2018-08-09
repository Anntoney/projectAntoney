package com.inducesmile.androidecommerceshop.mpesa;


public class ApiConstants {


    public static final String BASE_URL = "https://sandbox.safaricom.co.ke/";
    public static final String PRODUCTION_BASE_URL = "https://api.safaricom.co.ke/";
    public static  String initialResp;

    public  static boolean isFromstk=false;
    public static final String ACCESS_TOKEN_URL = "oauth/v1/generate?grant_type=client_credentials";
    public static final String PROCESS_REQUEST_URL = "mpesa/stkpush/v1/processrequest";

    public static final String DEFAULT_TRANSACTION_TYPE = "CustomerPayBillOnline";


    public static final String safaricom_pass_key = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String safaricom_party_b = "174379";
    public static final String safaricom_bussiness_short_code = "174379";


    public static final String safaricom_Auth_key = "SK3JFaLGnxtJXQNX70nqb06yHcjCCwgK";
    public static final String safaricom_Secret = "fsg7MJ3JSEZt0UZ2";


    public static final String callback_url = "http://hibasoftware.co.ke/mpesa/callback.php";


    public static final int PRODUCTION_RELEASE = 1;
    public static final int PRODUCTION_DEBUG = 2;


    public static final String CHECH_CALLBACK = "http://hibasoftware.co.ke/mpesa/check_callback.php";
    static  boolean isFromStk=false;
}

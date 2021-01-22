package com.empti.app.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.empti.app.R;
import com.empti.app.utilities.CustomProgressDialog;
import com.empti.app.utilities.GpsTracker;
import com.empti.app.utilities.SharedPrefferenceHandler;
import com.empti.app.webservices.RetrofitClient;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


//SignUp with google,facebook and simple

public class SignUp extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView tvLoginSignup;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 107;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private LoginManager facebookLoginManager;
    private Profile facebookProfile;
    private String facebookEmailId, googleEmail, facebookId, googleId, googleImage;
    private String facebookName = "", facebookLastName = "", googleFirstname = "", googleLastname = "";
    private CardView rlGoogleLogin, rlFbLogin;
    private Button btSignup;
    private boolean isSigninFacebook, isSigninGoogle;
    private String type;
    GpsTracker gpsTracker;
    CustomProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        callbackManager = CallbackManager.Factory.create();
        //call initialize method
        init();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        googlesign();
        listener();
        getLocation();
        mAuth = FirebaseAuth.getInstance();
    }

    //initialize
    public void init() {
        tvLoginSignup = findViewById(R.id.tvLoginSignup);
        rlGoogleLogin = findViewById(R.id.rlGoogleLogin);
        rlFbLogin = findViewById(R.id.rlFbLogin);
        btSignup = findViewById(R.id.btSignup);
        pDialog = new CustomProgressDialog(this);
    }

    public void getLocation() {
        gpsTracker = new GpsTracker(SignUp.this);
        if (gpsTracker.canGetLocation()) {
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    //set on click listener
    public void listener() {
        tvLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
            }
        });
        rlGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        rlFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLogin();
            }
        });
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, Information.class);
                i.putExtra("isSocial", false);
                startActivity(i);
            }
        });
    }

    //login through facebook
    private void fbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        //   LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.e("response", object.toString());
                                        facebookProfile = Profile.getCurrentProfile();
//                                        if (facebookProfile != null)
                                        // facebookprofilePicUrl= String.valueOf(facebookProfile.getProfilePictureUri(300,300));
                                        facebookLoginManager = LoginManager.getInstance();
                                        try {
                                            if (object.has("email")) {
                                                facebookEmailId = object.getString("email");
                                            } else {
                                                facebookEmailId = "";
                                            }
                                            facebookId = object.getString("id");
                                            facebookName = object.getString("first_name");
                                            facebookLastName = object.getString("last_name");
                                            //facebookmiddlename = object.getString("middle_name");
                                        } catch (JSONException e) {
                                            Log.e("jsonEXpex", "onCompleted: " + e.getMessage());
                                        }
                                        isSigninFacebook = true;
                                        sendSocialDetail();
                                        //facebookLoginManager.logOut();
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("FacebookException", "onCompleted: " + error.getMessage());
                    }
                });
    }

    //google sign in intialization
    private void googlesign() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(SignUp.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {
            Log.e("enter", "result" + " " + data);
            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.i("Success","get Account");
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleEmail = account.getEmail();
                googleId = account.getId();
                Log.e("gooogleId", "onActivityResult: " + googleId);
               String[] name =  account.getDisplayName().split(" ");
               googleFirstname = name[0];
               googleLastname = name[1];
                googleImage = account.getPhotoUrl().toString();
                //authenticating with firebase
                // firebaseAuthWithGoogle(account);
                isSigninGoogle = true;
                sendSocialDetail();
            } catch (Exception e) {
                Log.i("exception123","error "+e.getMessage());
                //Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //getting the google signin intent
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //firbase authentication
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("fail", "onConnectionFailed: " + connectionResult);
    }

    private void sendSocialDetail() {
        pDialog.setUpDialog();
        JSONObject object = new JSONObject();
        try {
            if (isSigninFacebook) {
                object.put("email", facebookEmailId);
                object.put("facebook_token", facebookId);
                object.put("google_token", "");
                Log.e("myobject", "sendSocialDetail: " + object.toString());
                checkSocialLogin(object);
            } else if (isSigninGoogle) {
                object.put("email", googleEmail);
                object.put("google_token", googleId);
                object.put("facebook_token", "");
                Log.e("putGoogletoken", "sendSocialDetail: " + googleId);
                Log.e("myobject", "sendSocialDetail: " + object.toString());
                checkSocialLogin(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkSocialLogin(JSONObject jsonObject) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type: application/json"), jsonObject.toString());
        Call<ResponseBody> call = RetrofitClient.getRetrofitClient().checkSocial(requestBody);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pDialog.dismissDialog();
                if (response.isSuccessful()) {
                    JSONObject object = null;
                    Log.e("mResponse", "onResponse: " + response.body().toString());

                    try {
                        object = new JSONObject(response.body().string());
                        Log.e("myobjectdata", "sendSocialDetail: " + object.toString());
                        if (isSigninFacebook) {
                            isSigninFacebook = false;
                            type = "facebook";
                            if (object.getInt("status") == 1) {
                                Toast.makeText(SignUp.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                JSONObject object1 = object.getJSONObject("data");
                                SharedPrefferenceHandler.storeAndParseJsonData(SignUp.this, object1);
                                Intent i = new Intent(SignUp.this, MapsActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(SignUp.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignUp.this, Information.class);
                                i.putExtra("fbFirstName", facebookName);
                                i.putExtra("fbLastName", facebookLastName);
                                i.putExtra("fbEmail", facebookEmailId);
                                i.putExtra("isSocial", true);
                                i.putExtra("type", type);
                                i.putExtra("facebook_token", facebookId);
                                startActivity(i);
                                finish();
                            }
                        } else if (isSigninGoogle) {
                            isSigninGoogle = false;
                            type = "google";
                            if (object.getInt("status") == 1) {
                                Toast.makeText(SignUp.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject1 = object.getJSONObject("data");
                                SharedPrefferenceHandler.storeAndParseJsonData(SignUp.this, jsonObject1);
                                Log.e("google sucess", "sucess" + object.toString());
                                Intent i = new Intent(SignUp.this, MapsActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                if (object.getInt("status") == 0) {
                                    Intent i = new Intent(SignUp.this, Information.class);
                                    i.putExtra("fbFirstName", googleFirstname);
                                    i.putExtra("fbLastName", googleLastname);
                                    i.putExtra("fbEmail", googleEmail);
                                    i.putExtra("isSocial", true);
                                    i.putExtra("type", type);
                                    i.putExtra("google_token", googleId);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        } else {
                            Toast.makeText(SignUp.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "errorMessage: unsucess");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("error>>JSON", "" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error>>>>", "myerroe" + t.getMessage());
                pDialog.dismissDialog();
            }
        });
    }
}
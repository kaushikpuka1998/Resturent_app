package com.example.resturent_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserInfo;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    int RC_SIGN_IN =0;
    TextView Username,usermail;
    CircularImageView profile;
    Button logout;
    GoogleSignInClient mgoogleSignInClient;
    GoogleSignInAccount gacc;
    AccessToken accessToken;
    Profile pf;
    LoginResult loginResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);

        Username = findViewById(R.id.profileusername);
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.profilelogout);
        usermail = findViewById(R.id.profileuserEmail);
        accessToken = AccessToken.getCurrentAccessToken();
        pf = Profile.getCurrentProfile();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                switch(view.getId()){
                    case R.id.profilelogout:
                    signOut();
                    break;
                }

            }
        });

       gacc = GoogleSignIn.getLastSignedInAccount(this);
        if(gacc!=null)
        {
            Username.setText(gacc.getDisplayName());
            usermail.setText(gacc.getEmail());
            Uri photourl = gacc.getPhotoUrl();


            Glide.with(this).load(String.valueOf(photourl)).into(profile);
        }else
        {
            if(accessToken!=null && !accessToken.isExpired())
            {
                Username.setText(pf.getName());
                usermail.setText(pf.getId());
                Glide.with(this)
                        .load("http://graph.facebook.com/"+AccessToken.USER_ID_KEY +"picture?type=square&redirect=true")
                        .into(profile);


            }
        }



    }


    private void signOut()
        {
        if(gacc!=null){
                mgoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileActivity.this, "You signed out Successfully", Toast.LENGTH_SHORT).show();
                                Intent y = new Intent(getApplicationContext(),LoginPanelActivity.class);
                                startActivity(y);
                                finish();
                            }
                        });
            }else
        {

            if(pf!=null)
            {
                LoginManager.getInstance().logOut();
                Intent y = new Intent(getApplicationContext(),LoginPanelActivity.class);
                startActivity(y);
                Toast.makeText(ProfileActivity.this, "You signed out Successfully", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
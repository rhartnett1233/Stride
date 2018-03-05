package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class CreateAccountActivity extends AppCompatActivity {

    private android.support.v7.widget.AppCompatButton btn_signup;
    private EditText name;
    private EditText address;
    private EditText email;
    private EditText number;
    private EditText user_type;
    private EditText password;
    private EditText re_password;
    private TextView link_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        btn_signup = (android.support.v7.widget.AppCompatButton) findViewById( R.id.btn_signup );
        name = (EditText) findViewById( R.id.input_name );
        address = (EditText) findViewById( R.id.input_address );
        email = (EditText) findViewById( R.id.input_email );
        number = (EditText) findViewById( R.id.input_mobile );
        user_type = (EditText) findViewById( R.id.user_type );
        password = (EditText) findViewById( R.id.input_password );
        re_password = (EditText) findViewById( R.id.input_reEnterPassword );
        link_login = (TextView) findViewById( R.id.link_login );

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createUser();
                } catch (InterruptedException e) {
                    System.out.println( "&&&&&&&&&&&&&&" );
                    System.out.println( "CANNOT CREATE USER" );
                    System.out.println( "&&&&&&&&&&&&&&" );
                    e.printStackTrace();
                }
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }


    public void createUser() throws InterruptedException {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        UserInformationDO user = new UserInformationDO();

        /**********************************************/
        Context appContext = getApplicationContext();
        AWSConfiguration awsConfig = new AWSConfiguration(appContext);
        IdentityManager identityManager = new IdentityManager(appContext,
                awsConfig);
        IdentityManager.setDefaultIdentityManager(identityManager);
        final AWSCredentialsProvider credentialsProvider = identityManager.getCredentialsProvider();
        final String userId = identityManager.getCachedUserID();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
        final DynamoDBMapper dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        /**********************************************/

        String temp_name = name.getText().toString();
        String temp_address = address.getText().toString();
        String temp_email = email.getText().toString();
        String temp_number = number.getText().toString();
        String temp_user_type = user_type.getText().toString();
        String temp_password = password.getText().toString();
        String temp_re_password = re_password.getText().toString();


        // need to add name, username, address
        user.createItem( dynamoDBMapper, temp_name, temp_password, temp_user_type, temp_email, temp_number );

        if( temp_user_type.equals("Therapist") || temp_user_type.equals("therapist") ) {
            Intent in = new Intent(getApplicationContext(), Therapist_View_Patients.class);
            startActivity(in);
        }
        /*else if( temp_user_type.equals("Patient") || temp_user_type.equals("patient") ){
            Intent in = new Intent(getApplicationContext(), Patient_First_Screen.class);
            startActivity(in);
        }*/


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                    }
                }, 3000);

    }

    public void onSignupSuccess() {
        btn_signup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btn_signup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String temp_name = name.getText().toString();
        String temp_address = address.getText().toString();
        String temp_email = email.getText().toString();
        String temp_number = number.getText().toString();
        String temp_user_type = user_type.getText().toString();
        String temp_password = password.getText().toString();
        String temp_re_password = re_password.getText().toString();

        if (temp_name.isEmpty() || temp_name.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (temp_address.isEmpty()) {
            address.setError("Enter Valid Address");
            valid = false;
        } else {
            address.setError(null);
        }


        if (temp_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(temp_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (temp_number.isEmpty() || temp_number.length()!=10) {
            number.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            number.setError(null);
        }

        if (temp_user_type.isEmpty() || (!temp_user_type.equals( "Therapist") && !temp_user_type.equals( "therapist") && !temp_user_type.equals( "Patient") && !temp_user_type.equals( "patient")) ) {
            user_type.setError("Enter Valid User Type");
            valid = false;
        } else {
            user_type.setError(null);
        }

        if (temp_password.isEmpty() || temp_password.length() < 4 || temp_password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (temp_re_password.isEmpty() || temp_re_password.length() < 4 || temp_re_password.length() > 10 || !(temp_re_password.equals(temp_password))) {
            re_password.setError("Password Do not match");
            valid = false;
        } else {
            re_password.setError(null);
        }

        return valid;
    }
}

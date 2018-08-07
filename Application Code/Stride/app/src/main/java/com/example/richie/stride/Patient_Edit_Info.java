package com.example.richie.stride;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class Patient_Edit_Info extends AppCompatActivity {

    private EditText name;
    private EditText address;
    private EditText email;
    private EditText number;
    private EditText user_type;
    private EditText leg_length;
    private EditText height;
    private android.support.v7.widget.AppCompatButton btn_update_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__edit__info);

        Intent in = getIntent();
        final String cur_patient = in.getStringExtra( "com.example.richie.CURRENT_PATIENT" );

        UserInfoNewDO user = new UserInfoNewDO();

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

        PaginatedQueryList<UserInfoNewDO> user_list = null;
        user.setUsername( cur_patient );
        name = (EditText) findViewById( R.id.input_name_pat );
        email = (EditText) findViewById( R.id.input_email_pat );
        number = (EditText) findViewById( R.id.input_mobile_pat );
        user_type = (EditText) findViewById( R.id.user_type_pat );
        address = (EditText) findViewById( R.id.input_address_pat );
        height = (EditText) findViewById( R.id.input_height_pat );
        leg_length = (EditText) findViewById( R.id.input_leg_length_pat );
        btn_update_info = (android.support.v7.widget.AppCompatButton) findViewById(R.id.btn_update_info);

        try {
            user_list = user.findUser( dynamoDBMapper, user );
        } catch (InterruptedException e) {
            System.out.println( "!!!!!!!!!!!\nGET LIST DID NOT WORK\n!!!!!!!!!!!!!");
            e.printStackTrace();
        }

        if( user_list.size() > 0 ){
            user = user_list.get( 0 );
            name.setText( user.getUsername() );
            email.setText( user.getEmail() );
            number.setText( user.getPhone() );
            user_type.setText( user.getUserType() );
            address.setText( user.getAddress() );
            height.setText( user.getHeight() );
            leg_length.setText( user.getLegLength() );
        }
        else{
            System.out.println( "!!!!!!!!!!!\nSIZE SMALLER THAN 1\n!!!!!!!!!!!!!" );
        }

        final UserInfoNewDO temp_user = user;
        btn_update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_name = name.getText().toString();
                String temp_email = email.getText().toString();
                String temp_number = number.getText().toString();
                String temp_user_type = user_type.getText().toString();
                String temp_address = address.getText().toString();
                String temp_height = height.getText().toString();
                String temp_leg_length = leg_length.getText().toString();
                UserInfoNewDO new_user = temp_user;
                new_user.setUsername( temp_name );
                new_user.setPassword( temp_user.getPassword() );
                new_user.setUserType( temp_user_type );
                new_user.setPhone( temp_number );
                new_user.setEmail( temp_email );
                new_user.setAddress( temp_address );
                new_user.setHeight( temp_height );
                new_user.setLegLength( temp_leg_length );
                try {
                    temp_user.updateItem( dynamoDBMapper, new_user );
                    System.out.println( "!!!!!!!!!!!\nWORKED\n!!!!!!!!!!!!!" );
                } catch (InterruptedException e) {
                    System.out.println("!!!!!!!!!!!\nDID NOT WORK##\n!!!!!!!!!!!!!");
                    e.printStackTrace();
                }
            }
        });
    }
}

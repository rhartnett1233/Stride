package com.example.richie.stride;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-User_Information")

public class UserInformationDO {
    private String _username;
    private String _userType;
    private String _email;
    private String _password;
    private String _phone;
    private UserInformationDO cur_user;
    PaginatedQueryList<UserInformationDO> userList;

    @DynamoDBHashKey(attributeName = "username")
    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return _username;
    }

    public void setUsername(final String _username) {
        this._username = _username;
    }
    @DynamoDBRangeKey(attributeName = "user_type")
    @DynamoDBAttribute(attributeName = "user_type")
    public String getUserType() {
        return _userType;
    }

    public void setUserType(final String _userType) {
        this._userType = _userType;
    }
    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return _email;
    }

    public void setEmail(final String _email) {
        this._email = _email;
    }
    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return _password;
    }

    public void setPassword(final String _password) {
        this._password = _password;
    }
    @DynamoDBAttribute(attributeName = "phone")
    public String getPhone() {
        return _phone;
    }

    public void setPhone(final String _phone) {
        this._phone = _phone;
    }

    ///////////////////////////////////


    ///////////////////////////////////
    public void createItem( final DynamoDBMapper dynamoDBMapper, String username, String password, String type, String email, String phone ) throws InterruptedException {
        final UserInformationDO userInformationDO = new UserInformationDO();
        userInformationDO.setUsername(username);
        userInformationDO.setPassword(password);
        userInformationDO.setUserType(type);
        userInformationDO.setEmail(email);
        userInformationDO.setPhone(phone);

        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.save( userInformationDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void readItem( final DynamoDBMapper dynamoDBMapper, final String hashKey ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                cur_user = dynamoDBMapper.load( UserInformationDO.class, hashKey );
                if( cur_user == null )
                    System.out.println( "DID NOT WORK" );
                else {
                    System.out.println( "DID WORK" );
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void updateItem( final DynamoDBMapper dynamoDBMapper, final UserInformationDO userInformationDO ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                UserInformationDO dd = dynamoDBMapper.load( UserInformationDO.class, userInformationDO.getUsername() , userInformationDO.getUserType() );
                if( dd == null )
                    System.out.println( "DID NOT WORK !!!!!!!!!!!!!" );
                else {
                    dd.setUsername( userInformationDO.getUsername() );
                    dd.setPassword( userInformationDO.getPassword() );
                    dd.setUserType( userInformationDO.getUserType() );
                    dd.setPhone( userInformationDO.getPhone() );
                    dd.setEmail( userInformationDO.getEmail() );
                    dynamoDBMapper.save( dd );
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void deleteItem(final DynamoDBMapper dynamoDBMapper, final DataTableDO dataTableDO ) {
        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.delete( dataTableDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    public PaginatedQueryList<UserInformationDO> findUser(final DynamoDBMapper dynamoDBMapper, final UserInformationDO userInformationDO ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                /*Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(password.toString()));*/

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(userInformationDO)
                        //.withRangeKeyCondition("password", rangeKeyCondition)
                        .withConsistentRead(false);

                userList = dynamoDBMapper.query(UserInformationDO.class, queryExpression);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
        return userList;
    }
    //////////////////////////////////


    //////////////////////////////////
    public int validateUser(DynamoDBMapper dynamoDBMapper, String username, String password ) throws InterruptedException {
        UserInformationDO user = new UserInformationDO();
        user.setUsername( username );
        user.setPassword(password);
        PaginatedQueryList<UserInformationDO> list = user.findUser( dynamoDBMapper, user );
        System.out.println( "**************" );
        System.out.println( list.size() );
        System.out.println( "**************" );
        if( list == null ){
            //validation failed
            return 0;
        }
        else if( list.size() == 1 ){
            cur_user = list.get(0);
            char[] right = cur_user.getPassword().toCharArray();
            char[] entered = password.toCharArray();
            System.out.println( "&&&&&&&&&&&&&&" );
            System.out.println( "Right" );
            boolean correct = true;
            if( right.length == entered.length ){
                for( int i = 0; i < right.length; i++ ){
                    if( right[i] != entered[i] )
                        correct = false;
                    if( correct == false )
                        break;
                }
                if( correct ) {
                    char[] temp_ther = cur_user.getUserType().toCharArray();
                    char[] temp_pat = cur_user.getUserType().toCharArray();
                    if( temp_ther.length == 9 )
                        return 1;
                    else if ( temp_pat.length == 7 )
                        return 2;
                }
                else
                    return 0;
            }
            else
                return 0;
        }

        return -1;
    }

}

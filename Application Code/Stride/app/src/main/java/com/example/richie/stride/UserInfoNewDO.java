package com.example.richie.stride;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;

@DynamoDBTable(tableName = "stride-mobilehub-1191655227-UserInfoNew")

public class UserInfoNewDO {
    private String _username;
    private String _userType;
    private String _address;
    private String _email;
    private String _height;
    private String _legLength;
    private String _name;
    private String _password;
    private String _phone;
    private UserInfoNewDO cur_user;
    private PaginatedQueryList<UserInfoNewDO> userList;

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
    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return _address;
    }

    public void setAddress(final String _address) {
        this._address = _address;
    }
    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return _email;
    }

    public void setEmail(final String _email) {
        this._email = _email;
    }
    @DynamoDBAttribute(attributeName = "height")
    public String getHeight() {
        return _height;
    }

    public void setHeight(final String _height) {
        this._height = _height;
    }
    @DynamoDBAttribute(attributeName = "leg_length")
    public String getLegLength() {
        return _legLength;
    }

    public void setLegLength(final String _legLength) {
        this._legLength = _legLength;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
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
    public void createItem(final DynamoDBMapper dynamoDBMapper, String name, String username, String address, String email, String number, String type, String password, String leg, String height ) throws InterruptedException {
        final UserInfoNewDO userInfoNewDO = new UserInfoNewDO();
        userInfoNewDO.setUsername(username);
        userInfoNewDO.setName(name);
        userInfoNewDO.setAddress(address);
        userInfoNewDO.setPassword(password);
        userInfoNewDO.setUserType(type);
        userInfoNewDO.setEmail(email);
        userInfoNewDO.setPhone(number);
        userInfoNewDO.setLegLength(leg);
        userInfoNewDO.setHeight(height);

        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.save( userInfoNewDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void readItem( final DynamoDBMapper dynamoDBMapper, final String hashKey ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                cur_user = dynamoDBMapper.load( UserInfoNewDO.class, hashKey );
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


    public void updateItem( final DynamoDBMapper dynamoDBMapper, final UserInfoNewDO userInfoNewDO ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                UserInfoNewDO dd = dynamoDBMapper.load( UserInfoNewDO.class, userInfoNewDO.getUsername() , userInfoNewDO.getUserType() );
                if( dd == null )
                    System.out.println( "DID NOT WORK !!!!!!!!!!!!!" );
                else {
                    dd.setUsername( userInfoNewDO.getUsername() );
                    dd.setName( userInfoNewDO.getName() );
                    dd.setAddress( userInfoNewDO.getAddress() );
                    dd.setPassword( userInfoNewDO.getPassword() );
                    dd.setUserType( userInfoNewDO.getUserType() );
                    dd.setPhone( userInfoNewDO.getPhone() );
                    dd.setEmail( userInfoNewDO.getEmail() );
                    dd.setLegLength( userInfoNewDO.getLegLength() );
                    dd.setHeight( userInfoNewDO.getHeight() );
                    dynamoDBMapper.save( dd );
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        mythread.join();
    }


    public void deleteItem(final DynamoDBMapper dynamoDBMapper, final UserInfoNewDO userInfoNewDO ) {
        Runnable runnable = new Runnable() {
            public void run() {
                dynamoDBMapper.delete( userInfoNewDO );
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    public PaginatedQueryList<UserInfoNewDO> findUser(final DynamoDBMapper dynamoDBMapper, final UserInfoNewDO userInfoNewDO ) throws InterruptedException {
        Runnable runnable = new Runnable() {
            public void run() {
                /*Condition rangeKeyCondition = new Condition()
                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
                        .withAttributeValueList(new AttributeValue().withS(password.toString()));*/

                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                        .withHashKeyValues(userInfoNewDO)
                        //.withRangeKeyCondition("password", rangeKeyCondition)
                        .withConsistentRead(false);

                userList = dynamoDBMapper.query(UserInfoNewDO.class, queryExpression);
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
        UserInfoNewDO user = new UserInfoNewDO();
        user.setUsername( username );
        user.setPassword(password);
        PaginatedQueryList<UserInfoNewDO> list = user.findUser( dynamoDBMapper, user );
        if( list == null ){
            //validation failed
            return 0;
        }
        else if( list.size() == 1 ){
            cur_user = list.get(0);
            char[] right = cur_user.getPassword().toCharArray();
            char[] entered = password.toCharArray();
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

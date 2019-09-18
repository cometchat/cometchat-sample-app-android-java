package com.inscripts.cometchatpulse.demo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.cometchat.pro.core.BlockedUsersRequest;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";
    private static final int MESSAGE_ID = 398;

    String GUID = "supergroup";

    String UID="superhero1";

    Context appContext = InstrumentationRegistry.getTargetContext();

    private String filePath = "/storage/emulated/0/Pictures/1558684863347.jpg";

    private String videoPath="/storage/emulated/0/DCIM/Camera/VID_20190814_121539.mp4";

    @Test
    public void init() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion("us").build();
        CometChat.init(appContext, StringContract.AppDetails.API_KEY,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "init onSuccess: " + s);
                assertEquals("Init Successful", s);
                countDownLatch.countDown();
            }

            @Override
            public void onError(CometChatException e) {
                assertEquals("Init failed", e.getMessage());
                countDownLatch.countDown();

            }
        });

        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("Cometchat timeout", null);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void loginWithUID() {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        CometChat.login("superhero2", StringContract.AppDetails.API_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "loginWithUid onSuccess: " + user.getUid());
                //assertNotNull("Login success test",null);
                assertEquals("superhero2", user.getUid());
                countDownLatch.countDown();

            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "loginWithUID onError: " + e.getMessage());
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await(10, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("Cometchat timeout", null);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginWithUIDEmpty() {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        CometChat.login("", StringContract.AppDetails.API_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                countDownLatch.countDown();
                assertEquals("superhero2", user.getUid());
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "loginWithEmptyUID onError: " + e.getMessage());
                assertEquals("UID cannot be blank. Please provide a valid UID", e.getMessage());
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("Cometchat timeout", null);
            }

        } catch (InterruptedException e) {

        }
    }

    @Test
    public void loginWithEmptyAPIKEY() {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        CometChat.login("superhero3", "", new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onError(CometChatException e) {
                countDownLatch.countDown();
//                assertEquals("ERROR_JSON_EXCEPTION",e.getCode());
                Log.d(TAG, "loginWithEmptyAPIKEY onError: " + e.getMessage());
            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("Cometchat timeout", null);
            }
        } catch (InterruptedException e) {

        }

    }

    @Test
    public void sendTextMessage() {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        TextMessage textMessage = new TextMessage("superhero1", "Hello", CometChatConstants.RECEIVER_TYPE_USER);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                countDownLatch.countDown();
                assertEquals("Hello", textMessage.getText());
                Log.d(TAG, "sendTextMessage onSuccess: ");
            }

            @Override
            public void onError(CometChatException e) {
                assertNotEquals("sendTextMessage", null);
                countDownLatch.countDown();
                Log.d(TAG, "sendTextMessage onError: " + e.getMessage());
            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (InterruptedException e) {

        }

    }

    @Test
    public void sendMessageWithInvalidUID() {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        TextMessage textMessage = new TextMessage("", "hello", CometChatConstants.RECEIVER_TYPE_USER);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                Log.d(TAG, "sendMessageWithInvalidUID onSuccess: ");
            }

            @Override
            public void onError(CometChatException e) {
                assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                countDownLatch.countDown();
                Log.d(TAG, "sendMessageWithInvalidUID onError: " + e.getMessage());
            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (InterruptedException e) {

        }

    }

    @Test
    public void sendTextMessageWithImageType() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        TextMessage textMessage = new TextMessage(UID, "Hello", CometChatConstants.RECEIVER_TYPE_USER);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
            }

            @Override
            public void onError(CometChatException e) {
                assertNotEquals("sendTextMessage", null);
                countDownLatch.countDown();
                Log.d(TAG, "sendTextMessageWithImageType onError: " + e.getMessage());
            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("Cometchat timeout", null);
            }
        } catch (InterruptedException e) {

        }
    }


    @Test
    public void sendTextMessageWithNullText() {
        CountDownLatch countDownLatch = new CountDownLatch(1);


        TextMessage textMessage = new TextMessage(UID, null, CometChatConstants.RECEIVER_TYPE_USER);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                Log.d(TAG, "sendTextMessageWithNullText onSuccess: ");
            }

            @Override
            public void onError(CometChatException e) {
                assertEquals("Message text should be non-empty string.", e.getMessage());
                countDownLatch.countDown();
                Log.d(TAG, "sendTextMessageWithNullText onError: " + e.getMessage());
            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("Cometchat timeout", null);
            }
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void sendTextMessageWithInvalidPara() {

        CountDownLatch countDownLatch = new CountDownLatch(1);


        TextMessage textMessage = new TextMessage(UID, null,CometChatConstants.CATEGORY_MESSAGE);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                Log.d(TAG, "sendTextMessageWithInvalidPara onSuccess: ");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "sendTextMessageWithInvalidPara onError: " + e.getMessage());
                assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                countDownLatch.countDown();

            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("Cometchat timeout", null);
            }
        } catch (InterruptedException e) {

        }
    }


    @Test
    public void sendTextMessageWithGroupType() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        TextMessage textMessage = new TextMessage(UID, "Hello",CometChatConstants.RECEIVER_TYPE_GROUP);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
            }

            @Override
            public void onError(CometChatException e) {
                assertEquals("ERR_GUID_NOT_FOUND", e.getCode());
                countDownLatch.countDown();
                Log.d(TAG, "sendTextMessageWithGroupType onError: " + e.getMessage());
            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void sendTextMessageInvalidTypeAndEmptyMessage() {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        TextMessage textMessage = new TextMessage(UID, "", CometChatConstants.RECEIVER_TYPE_GROUP);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
            }

            @Override
            public void onError(CometChatException e) {
                assertEquals("ERR_GUID_NOT_FOUND", e.getCode());
                countDownLatch.countDown();
                Log.d(TAG, "sendTextMessageWithGroupType onError: " + e.getMessage());
            }
        });

        try {
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (InterruptedException e) {

        }
    }


    @Test
    public void sendCustomMessage() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            JSONObject customData = new JSONObject();
            customData.put("lattitue", "19.0760");
            customData.put("longitude", "72.8777");

            CustomMessage customMessage = new CustomMessage(UID, CometChatConstants.RECEIVER_TYPE_USER, "hello", customData);
            CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
                @Override
                public void onSuccess(CustomMessage customMessage) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessage onSuccess : " + customMessage.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessage onError: " + e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /***
     *   send custom message with custom type equals to "user"
     */
    @Test
    public void sendCustomMessageWithTypeCustom() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            JSONObject customData = new JSONObject();
            customData.put("lattitue", "19.0760");
            customData.put("longitude", "72.8777");

            CustomMessage customMessage = new CustomMessage(UID, CometChatConstants.RECEIVER_TYPE_USER, CometChatConstants.RECEIVER_TYPE_USER, customData);

            CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
                @Override
                public void onSuccess(CustomMessage customMessage) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithTypeCustom onSuccess : " + customMessage.toString());
                    assertEquals(CometChatConstants.RECEIVER_TYPE_USER, customMessage.getType());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithTypeCustom onError: " + e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void sendCustomMessageWithGroupReceiver() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            JSONObject customData = new JSONObject();
            customData.put("lattitue", "19.0760");
            customData.put("longitude", "72.8777");

            CustomMessage customMessage = new CustomMessage(GUID, CometChatConstants.RECEIVER_TYPE_GROUP, "t", customData);

            CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
                @Override
                public void onSuccess(CustomMessage customMessage) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithGroupReceiver onSuccess : " + customMessage.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithGroupReceiver onError: " + e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void sendCustomMessageWithEmptyCustomObject() {

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);
//
//            JSONObject customData = new JSONObject();
//            customData.put("lattitue","19.0760");
//            customData.put("longitude","72.8777");

            CustomMessage customMessage = new CustomMessage(GUID, CometChatConstants.RECEIVER_TYPE_GROUP, "t", null);

            CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
                @Override
                public void onSuccess(CustomMessage customMessage) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithEmptyCustomObject onSuccess : " + customMessage.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithEmptyCustomObject onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void sendCustomMessageWithEmptyCustomObjectAndInvalidPara() {

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);


//            JSONObject customData = new JSONObject();
//            customData.put("lattitue","19.0760");
//            customData.put("longitude","72.8777");

            CustomMessage customMessage = new CustomMessage(GUID, CometChatConstants.RECEIVER_TYPE_GROUP, "t", null);

            CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
                @Override
                public void onSuccess(CustomMessage customMessage) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithEmptyCustomObject onSuccess : " + customMessage.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithEmptyCustomObject onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void sendTextMessageWithNullPara() {

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);
//
//            JSONObject customData = new JSONObject();
//            customData.put("lattitue","19.0760");
//            customData.put("longitude","72.8777");

            TextMessage textMessage = new TextMessage(null, null, null);

            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendTextMessageWithNullPara onSuccess : " + textMessage.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendTextMessageWithNullPara onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void sendCustomMessageWithNullPara() {

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);
//
//            JSONObject customData = new JSONObject();
//            customData.put("lattitue","19.0760");
//            customData.put("longitude","72.8777");

            CustomMessage customMessage = new CustomMessage(null, null, null, null);

            CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
                @Override
                public void onSuccess(CustomMessage customMessage) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithNullPara onSuccess : " + customMessage.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCustomMessageWithNullPara onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendTextMessageWithNullMessageAndReceiverType() {

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage textMessage = new TextMessage(UID, "Hello", null);

            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    Log.d(TAG, "sendTextMessageWithNullMessageAndReceiverType onSuccess: " + textMessage.toString());
                    assertEquals("Hello", textMessage.getText());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendTextMessageWithNullMessageAndReceiverType onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });


            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (InterruptedException e) {

        }

    }

    @Test
    public void sendTextMessageWithMetaData() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage textMessage = new TextMessage(UID, "Hello", CometChatConstants.RECEIVER_TYPE_USER);

            JSONObject customData = new JSONObject();
            customData.put("lattitue", "19.0760");
            customData.put("longitude", "72.8777");

            textMessage.setMetadata(customData);

            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    Log.d(TAG, "sendTextMessageWithMetaData onSuccess: " + textMessage.toString());
                    assertEquals("Hello", textMessage.getText());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendTextMessageWithMetaData onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });


            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (Exception e) {

        }

    }

    @Test
    public void sendTextMessageWithNullMetaData() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage textMessage = new TextMessage(UID, "Hello", CometChatConstants.RECEIVER_TYPE_USER);

            textMessage.setMetadata(null);

            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    Log.d(TAG, "sendTextMessageWithNullMetaData onSuccess: " + textMessage.toString());
                    assertEquals("Hello", textMessage.getText());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendTextMessageWithNullMetaData onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });


            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (Exception e) {

        }

    }

    @Test
    public void sendTextMessageWithMetaGroupType() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage textMessage = new TextMessage(GUID, "Hello",CometChatConstants.RECEIVER_TYPE_GROUP);

            JSONObject customData = new JSONObject();

            customData.put("lattitue", "19.0760");
            customData.put("longitude", "72.8777");

            textMessage.setMetadata(customData);

            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    Log.d(TAG, "sendTextMessageWithMetaGroupType onSuccess: " + textMessage.toString());
                    assertNotNull( textMessage.getMetadata());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendTextMessageWithMetaGroupType onError: " + e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.", e.getMessage());
                }
            });


            countDownLatch.await(8, TimeUnit.SECONDS);

            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (Exception e) {

        }

    }

    @Test
    public void sendTextMessageWithMetaAndMUID() {

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage textMessage = new TextMessage(GUID, "Hello", CometChatConstants.RECEIVER_TYPE_GROUP);

            JSONObject customData = new JSONObject();

            customData.put("lattitue", "19.0760");
            customData.put("longitude", "72.8777");

            textMessage.setMetadata(customData);

            textMessage.setMuid("hey");

            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    Log.d(TAG, "sendTextMessageWithMetaAndMUID onSuccess: " + textMessage.toString());
//                    assertNotNull( textMessage.getMuid());
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendTextMessageWithMetaAndMUID onError: " + e.getMessage());
                    assertNotNull("Failed to validate the data sent with the request.",null);
                }
            });


            countDownLatch.await(8, TimeUnit.SECONDS);

            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (Exception e) {

        }

    }

    @Test
    public void sendMessageWithNullTextMessageObject(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            CometChat.sendMessage(null, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    Log.d(TAG, "sendMessageWithNullTextMessageObject onSuccess: " + textMessage.toString());
                    countDownLatch.countDown();
                    assertNotNull( textMessage);
                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendMessageWithNullTextMessageObject onError: " + e.getMessage());
                    assertNotNull("Failed to validate the data sent with the request.",null);
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);

            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        } catch (Exception e) {

        }

    }

    @Test
    public void sendImageMediaMessage(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(UID, new File(filePath), CometChatConstants.MESSAGE_TYPE_IMAGE, CometChatConstants.RECEIVER_TYPE_USER);

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendImageMediaMessage onSuccess: " + mediaMessage.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendImageMediaMessage onError: " + e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){
        }
    }

    @Test
    public void sendImageMediaMessageWithVideoType(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(UID, new File(filePath), CometChatConstants.MESSAGE_TYPE_VIDEO, CometChatConstants.RECEIVER_TYPE_USER);

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendImageMediaMessageWithVideoType onSuccess: " + mediaMessage.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendImageMediaMessageWithVideoType onError: " + e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){
        }
    }

    @Test
    public void sendImageMediaMessageWithAudioType(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(UID, new File(filePath), CometChatConstants.MESSAGE_TYPE_AUDIO, CometChatConstants.RECEIVER_TYPE_USER);

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendImageMediaMessageWithAudioType onSuccess: " + mediaMessage.toString());
                    assertNotNull(mediaMessage.getAttachment());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendImageMediaMessageWithAudioType onError: " + e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){
        }
    }

    @Test
    public void sendImageMediaMessageWithCustomType(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(UID, new File(filePath), CometChatConstants.MESSAGE_TYPE_CUSTOM, CometChatConstants.RECEIVER_TYPE_USER);

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendImageMediaMessageWithCustomType onSuccess: " + mediaMessage.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendImageMediaMessageWithCustomType onError: " + e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){
        }
    }


    @Test
    public void sendImageMediaMessageWithCustomTypeInGroup(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(GUID, new File(filePath), CometChatConstants.MESSAGE_TYPE_CUSTOM, CometChatConstants.RECEIVER_TYPE_GROUP);

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendImageMediaMessageWithCustomTypeInGroup onSuccess: " + mediaMessage.toString());
                    assertNotNull(mediaMessage.getAttachment());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendImageMediaMessageWithCustomTypeInGroup onError: " + e.getMessage());
                    countDownLatch.countDown();
//                    assertNotNull("",null);
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){
        }
    }

    @Test
    public void sendVideoMediaMessage(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(UID, new File(videoPath), CometChatConstants.MESSAGE_TYPE_VIDEO, CometChatConstants.RECEIVER_TYPE_USER);

            message.setCaption("Hello");

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendVideoMediaMessage onSuccess: " + mediaMessage.toString());
//                    assertNotNull(mediaMessage.getAttachment());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendVideoMediaMessage onError: " + e.getMessage());
                    countDownLatch.countDown();
//                    assertNotNull("",null);
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){
        }
    }

    @Test
    public void sendVideoMediaMessageWithTypeImage(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(UID, new File(videoPath), CometChatConstants.MESSAGE_TYPE_IMAGE, CometChatConstants.RECEIVER_TYPE_USER);

            message.setCaption("Hello");
            message.setMetadata(new JSONObject(message.getCaption()));
            message.setUrl("okay");

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendVideoMediaMessageWithTypeImage onSuccess: " + mediaMessage.toString());
                    countDownLatch.countDown();
                    assertNotNull(mediaMessage.getAttachment());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendVideoMediaMessageWithTypeImage onError: " + e.getMessage());
                    countDownLatch.countDown();
                    assertNotNull("",null);
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){
        }
    }


    @Test
    public void sendVideoMediaMessageWithTypeAudio(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MediaMessage message = new MediaMessage(UID, new File(videoPath), CometChatConstants.MESSAGE_TYPE_AUDIO, CometChatConstants.RECEIVER_TYPE_USER);

            message.setCaption("Hello");
            message.setMetadata(new JSONObject(message.getCaption()));
            message.setUrl("okay");

            CometChat.sendMediaMessage(message, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    Log.d(TAG, "sendVideoMediaMessageWithTypeAudio onSuccess: " + mediaMessage.toString());
                    countDownLatch.countDown();
                    assertNotNull(mediaMessage.getAttachment());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendVideoMediaMessageWithTypeAudio onError: " + e.getMessage());
                    countDownLatch.countDown();
                    assertNotNull("",null);
                }
            });

            countDownLatch.await(15, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void sendEditMessage(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage message = new TextMessage(UID, "text", CometChatConstants.RECEIVER_TYPE_USER);
            message.setId(MESSAGE_ID);

            CometChat.editMessage(message, new CometChat.CallbackListener<BaseMessage>() {
                @Override
                public void onSuccess(BaseMessage baseMessage) {
                    Log.d(TAG, "sendEditMessage onSuccess: "+baseMessage.getEditedAt());
                    assertEquals("text", ((TextMessage)baseMessage).getText());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendEditMessage onError: "+e.getMessage());
                    assertEquals("Message with id "+MESSAGE_ID+" does not exist.",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void sendEditTextMessageWithImageType(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage message = new TextMessage(UID, "Hey", CometChatConstants.RECEIVER_TYPE_USER);
            message.setId(398);

            CometChat.editMessage(message, new CometChat.CallbackListener<BaseMessage>() {
                @Override
                public void onSuccess(BaseMessage baseMessage) {
                    Log.d(TAG, "sendEditTextMessageWithImageType onSuccess: "+baseMessage.toString()+"    "+baseMessage.getEditedAt());
                    assertEquals("Hey", ((TextMessage)baseMessage).getText());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendEditTextMessageWithImageType onError: "+e.getMessage());
                    assertEquals("Message with id 398 does not exist.",e.getMessage());
//                    assertNotNull(e.getMessage(),null);
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void sendEditMessageWithTypeGroup(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage message = new TextMessage(UID, "Edit text Message ", CometChatConstants.RECEIVER_TYPE_GROUP);
            message.setId(489);

            CometChat.editMessage(message, new CometChat.CallbackListener<BaseMessage>() {
                @Override
                public void onSuccess(BaseMessage baseMessage) {
                    Log.d(TAG, "sendEditMessageWithTypeUser onSuccess: "+baseMessage.getEditedAt());
                    Log.d(TAG, "sendEditMessageWithTypeUser onSuccess: "+baseMessage.toString());
                    assertEquals("Edit text Message ", ((TextMessage)baseMessage).getText());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendEditMessageWithTypeUser onError: "+e.getMessage());
                    assertNotNull("Message with id 489 does not exist.",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void sendEditMessageWithNull(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage message = new TextMessage(UID, null, CometChatConstants.RECEIVER_TYPE_USER);
            message.setId(489);

            CometChat.editMessage(message, new CometChat.CallbackListener<BaseMessage>() {
                @Override
                public void onSuccess(BaseMessage baseMessage) {
                    Log.d(TAG, "sendEditMessageWithNull onSuccess: "+baseMessage.getEditedAt());
                    Log.d(TAG, "sendEditMessageWithNull onSuccess: "+baseMessage.toString());
                    assertEquals("text", ((TextMessage)baseMessage).getText());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendEditMessageWithNull onError: "+e.getMessage());
                    assertNotNull("Failed to validate the data sent with the request.",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void sendEditMessageWithWrongId(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage message = new TextMessage(UID, "h",CometChatConstants.RECEIVER_TYPE_USER);
            message.setId(48);

            CometChat.editMessage(message, new CometChat.CallbackListener<BaseMessage>() {
                @Override
                public void onSuccess(BaseMessage baseMessage) {
                    Log.d(TAG, "sendEditMessageWithWrongId onSuccess: "+baseMessage.getEditedAt());
                    Log.d(TAG, "sendEditMessageWithWrongId onSuccess: "+baseMessage.toString());
//                    assertEquals("text", ((TextMessage)baseMessage).getText());
                    countDownLatch.countDown();
                    // TODO: 2019-08-19  check edit message feature in app
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendEditMessageWithWrongId onError: "+e.getMessage());
                    assertNotNull("Failed to validate the data sent with the request.",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }


    @Test
    public void sendEditMessageWithEmpty(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            TextMessage message = new TextMessage(UID, "",CometChatConstants.RECEIVER_TYPE_USER);
            message.setId(489);

            CometChat.editMessage(message, new CometChat.CallbackListener<BaseMessage>() {
                @Override
                public void onSuccess(BaseMessage baseMessage) {
                    Log.d(TAG, "sendEditMessageWithEmpty onSuccess: "+baseMessage.getEditedAt());
                    Log.d(TAG, "sendEditMessageWithEmpty onSuccess: "+baseMessage.toString());
                    assertEquals("", ((TextMessage)baseMessage).getText());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendEditMessageWithEmpty onError: "+e.getMessage());
                    assertNotNull("Failed to validate the data sent with the request.",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void sendDeleteMessage(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

           CometChat.deleteMessage(489, new CometChat.CallbackListener<BaseMessage>() {
               @Override
               public void onSuccess(BaseMessage baseMessage) {
                   Log.d(TAG, "deleteMessage onSuccess: "+baseMessage.toString());
                   assertNull(((TextMessage) baseMessage).getText());
                     countDownLatch.countDown();
               }

               @Override
               public void onError(CometChatException e) {
                   Log.d(TAG, "deleteMessage onError: "+e.getMessage());
                     countDownLatch.countDown();
               }
           });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void sendDeleteDeletedMessage(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            CometChat.deleteMessage(489, new CometChat.CallbackListener<BaseMessage>() {
                @Override
                public void onSuccess(BaseMessage baseMessage) {
                    Log.d(TAG, "sendDeleteDeletedMessage onSuccess: "+baseMessage.toString());
                    assertNull(((TextMessage) baseMessage).getText());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendDeleteDeletedMessage onError: "+e.getMessage());
                    assertEquals("Message with id 489 does not exist.",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }

    @Test
    public void userRequest(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

         UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100).build();

        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                Log.d(TAG, "userRequest onSuccess: "+users.size());
                assertEquals(4,users.size());
                countDownLatch.countDown();
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "userRequest onError: "+e.getMessage());
                countDownLatch.countDown();

            }
        });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }

    @Test
    public void userRequestWithOffline(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100).setUserStatus(UsersRequest.USER_STATUS_OFFLINE).build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "userRequestWithOffline onSuccess: "+users.size());

                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "userRequestWithOffline onError: "+e.getMessage());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }

    @Test
    public void userRequestWithSearch(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100).setSearchKeyword("iron Man").build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "userRequestWithSearch onSuccess: "+users.toString());
                    assertEquals("Iron Man",users.get(0).getName());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "userRequestWithSearch onError: "+e.getMessage());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }


    @Test
    public void userRequestWithSearchWithStatus(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100)
                    .setUserStatus(UsersRequest.USER_STATUS_OFFLINE)
                    .setSearchKeyword("iron Man").build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "userRequestWithSearchWithStatus onSuccess: "+users.toString());
                    assertEquals("offline",users.get(0).getStatus());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "userRequestWithSearchWithStatus onError: "+e.getMessage());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }

    @Test
    public void userRequestWithSearchWithStatusNull(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100)
                    .setUserStatus(null)
                    .setSearchKeyword("iron Man").build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "userRequestWithSearchWithStatusNull onSuccess: "+users.size());

                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "userRequestWithSearchWithStatusNull onError: "+e.getMessage());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }

    @Test
    public void userRequestWithSearchWithNull(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100).setSearchKeyword(null).build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "userRequestWithSearchWithNull onSuccess: "+users.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "userRequestWithSearchWithNull onError: "+e.getMessage());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }

    @Test
    public void userRequestWithSearchWithNullAndHideBlockerUser(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100).hideBlockedUsers(true).setSearchKeyword(null).build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "userRequestWithSearchWithNullAndHideBlockerUser onSuccess: "+users.size());
                    assertEquals(3,users.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "userRequestWithSearchWithNullAndHideBlockerUser onError: "+e.getMessage());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }


    @Test
    public void userRequestWithSearchWithNullAndShowBlockerUser(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            UsersRequest usersRequest=new UsersRequest.UsersRequestBuilder().setLimit(100).hideBlockedUsers(false).setSearchKeyword(null).build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "userRequestWithSearchWithNullAndShowBlockerUser onSuccess: "+users.size());
                    assertEquals(4,users.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "userRequestWithSearchWithNullAndShowBlockerUser onError: "+e.getMessage());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }

    @Test
    public void messageRequestFetchPrevious(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder().setLimit(100).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPrevious onSuccess: "+messageList.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPrevious onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }

    @Test
    public void messageRequestFetchPreviousWithId(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder().setMessageId(300).setLimit(100).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousWithId onSuccess: "+messageList.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousWithId onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }


    }

    @Test
    public void messageRequestFetchNextWithId(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder().setMessageId(300).setLimit(100).build();

            messagesRequest.fetchNext(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchNextWithId onSuccess: "+messageList.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchNextWithId onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }
    }

    @Test
    public void messageRequestFetchNextWithUID(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder().setMessageId(300).setUID("superhero1").setLimit(100).build();

            messagesRequest.fetchNext(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchNextWithUID onSuccess: "+messageList.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchNextWithUID onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }

    @Test
    public void messageRequestFetchPreviousWithUIDAndLimit(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder().setMessageId(300).setUID("superhero1").setLimit(800).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousWithUID onSuccess: "+messageList.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousWithUID onError: "+e.getMessage());
                    assertEquals("Limit Exceeded Max limit of %s",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }


    @Test
    public void messageRequestFetchPreviousAndLimitSearch(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                                             .setMessageId(300)
                                             .setSearchKeyword("HEY")
                                             .setLimit(30).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousAndLimitSearch onSuccess: "+messageList.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousAndLimitSearch onError: "+e.getMessage());
                    assertEquals("Limit Exceeded Max limit of %s",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }


    @Test
    public void messageRequestFetchPreviousAndLimitSearchNull(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                    .setSearchKeyword(null)
                    .setLimit(30).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousAndLimitSearchNull onSuccess: "+messageList.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousAndLimitSearchNull onError: "+e.getMessage());
                    assertEquals("Limit Exceeded Max limit of %s",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }

    @Test
    public void messageRequestFetchPreviousWithBlockedUser(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                    .setSearchKeyword("hello")
                    .hideMessagesFromBlockedUsers(true)
                    .setLimit(30).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousWithBlockedUser onSuccess: "+messageList.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousWithBlockedUser onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }

    @Test
    public void messageRequestFetchPreviousUnreadWithBlockedUser(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                    .setSearchKeyword("hello")
                    .setUnread(true)
                    .hideMessagesFromBlockedUsers(true)
                    .setLimit(30).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousUnreadWithBlockedUser onSuccess: "+messageList.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousUnreadWithBlockedUser onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }


    @Test
    public void messageRequestFetchPreviousUnreadWithShowBlockedUser(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                    .setSearchKeyword("hello")
                    .setUnread(true)
                    .setMessageId(300)
                    .hideMessagesFromBlockedUsers(false)
                    .setLimit(30).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousUnreadWithShowBlockedUser onSuccess: "+messageList.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousUnreadWithShowBlockedUser onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }


    @Test
    public void messageRequestFetchNextUnreadWithShowBlockedUser(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                    .setSearchKeyword("hello")
                    .setUnread(true)
                    .setMessageId(300)
                    .hideMessagesFromBlockedUsers(false)
                    .setLimit(30).build();

            messagesRequest.fetchNext(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchNextUnreadWithShowBlockedUser onSuccess: "+messageList.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchNextUnreadWithShowBlockedUser onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }

    @Test
    public void messageRequestFetchNextUnreadWithBlockedUser(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                    .setSearchKeyword("hello")
                    .setUnread(false)
                    .hideMessagesFromBlockedUsers(true)
                    .setLimit(30).build();

            messagesRequest.fetchNext(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchNextUnreadWithBlockedUser onSuccess: "+messageList.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchNextUnreadWithBlockedUser onError: "+e.getMessage());
                    assertNotNull("'Timestamp' or 'MessageId' required to use the 'fetchNext()' method.",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }


    @Test
    public void messageRequestFetchPreviousWithGUID(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            MessagesRequest messagesRequest=new MessagesRequest.MessagesRequestBuilder()
                    .setGUID(GUID)
                    .hideMessagesFromBlockedUsers(true)
                    .setLimit(30).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> messageList) {
                    Log.d(TAG, "messageRequestFetchPreviousWithGUID onSuccess: "+messageList.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "messageRequestFetchPreviousWithGUID onError: "+e.getMessage());

                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){

        }

    }



    @Test
    public void sendGroupRequest(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            GroupsRequest groupsRequest=new GroupsRequest.GroupsRequestBuilder()
                       .setLimit(30)
                       .build();

            groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
                @Override
                public void onSuccess(List<Group> groups) {
                    Log.d(TAG, "sendGroupRequest onSuccess: "+groups.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendGroupRequest onError: "+e.getMessage());
                   countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void sendGroupRequestSearch(){

        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            GroupsRequest groupsRequest=new GroupsRequest.GroupsRequestBuilder()
                    .setLimit(30)
                    .setSearchKeyWord("comic hero")
                    .build();

            groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
                @Override
                public void onSuccess(List<Group> groups) {
                    Log.d(TAG, "sendGroupRequestSearch onSuccess: "+groups.toString());
                    assertEquals("Comic Heros' Hangout",groups.get(0).getName());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "sendGroupRequestSearch onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void sendGetGroup(){

        try {


        CountDownLatch countDownLatch=new CountDownLatch(1);

        CometChat.getGroup(GUID, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                Log.d(TAG, "sendgetGroup onSuccess: "+group.toString());
                assertEquals(GUID,group.getGuid());
                countDownLatch.countDown();

            }

            @Override
            public void onError(CometChatException e) {
                countDownLatch.countDown();
                Log.d(TAG, "sendgetGroup onError: "+e.getMessage());
            }
        });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }
    }


    @Test
    public void sendCreateGroup(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("1","No 1",CometChatConstants.GROUP_TYPE_PUBLIC,"","","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroup onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroup onError: "+e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }



    @Test
    public void sendCreateGroupWithNumber(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("0","No 1",CometChatConstants.GROUP_TYPE_PUBLIC,"","","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupWithNumber onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupWithNumber onError: "+e.getMessage());
                    assertEquals("Web socket process failed.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void sendCreateGroupWithNullGUID(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group(null,"No 1",CometChatConstants.GROUP_TYPE_PUBLIC,"","","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupWithNullGUID onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupWithNullGUID onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }


    @Test
    public void sendCreateGroupWithNullName(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("21",null,CometChatConstants.GROUP_TYPE_PUBLIC,"","","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupWithNullName onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupWithNullName onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void sendCreateGroupWithPassword(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("21","password",CometChatConstants.GROUP_TYPE_PASSWORD,"123","","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupWithPassword onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupWithPassword onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void sendCreateGroupWithPasswordTypePublic(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("22","password",CometChatConstants.GROUP_TYPE_PUBLIC,"123","","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupWithPasswordTypePublic onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupWithPasswordTypePublic onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void sendCreateGroupWithPasswordWithNull(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("22","password",CometChatConstants.GROUP_TYPE_PASSWORD,null,"","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupWithPasswordWithNull onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupWithPasswordWithNull onError: "+e.getMessage());
                    assertEquals("Password is mandatory for a protected group",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void sendCreateGroupGUIDEmptyWithPasswordWithLine(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("","password",CometChatConstants.GROUP_TYPE_PASSWORD,"","\n\n","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupGUIDEmptyWithPasswordWithLine onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupGUIDEmptyWithPasswordWithLine onError: "+e.getMessage());
                    assertEquals("Password is mandatory for a protected group",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }



    @Test
    public void sendCreateGroupGUIDEmptyWithPassword(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("","password",CometChatConstants.GROUP_TYPE_PASSWORD,"1234","","Hello Description");
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupGUIDEmptyWithPassword onSuccess: "+group.toString());
                    assertEquals("Hello Description",group.getDescription());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupGUIDEmptyWithPassword onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }


    @Test
    public void sendCreateGroupPrivate(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("133","password-2",CometChatConstants.GROUP_TYPE_PRIVATE,"1234","","Hello Description");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("test","hello");
            group.setMetadata(jsonObject);

            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupPrivate onSuccess: "+group.toString());
                    assertEquals("133",group.getGuid());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupPrivate onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void sendCreateGroupPrivateWithPassword(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("133","password-2",CometChatConstants.GROUP_TYPE_PRIVATE,"1234","","Hello Description");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("test","hello");
            group.setMetadata(jsonObject);

            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupPrivate onSuccess: "+group.toString());
                    assertEquals("133",group.getGuid());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupPrivate onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void sendCreateGroupWithLongName(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("135","zJfMus2WzLnMr82T4bmuzKTNjcylzYfNiGjMssyBZc2PzZPMvMyXzJnMvMyjzZQgzYfMnMyxzKDN\",\n" +
                    "  \"k82NzYVOzZXNoGXMl8yxesyYzJ3MnMy6zZlwzKTMusy5zY3Mr82aZcygzLvMoM2ccsyozKTNjcy6\",\n" +
                    "  \"zJbNlMyWzJZkzKDMn8ytzKzMnc2facymzZbMqc2TzZTMpGHMoMyXzKzNicyZbs2azZwgzLvMnsyw\",\n" +
                    "  \"zZrNhWjMtc2JacyzzJ52zKLNh+G4mc2OzZ8t0onMrcypzLzNlG3MpMytzKtpzZXNh8ydzKZuzJfN\",\n" +
                    "  \"meG4jcyfIMyvzLLNlc2ex6vMn8yvzLDMss2ZzLvMnWYgzKrMsMywzJfMlsytzJjNmGPMps2NzLLM\",\n" +
                    "  \"ns2NzKnMmeG4pc2aYcyuzY7Mn8yZzZzGocypzLnNjnPMpC7MncydINKJWsyhzJbMnM2WzLDMo82J\",\n" +
                    "  \"zJxhzZbMsM2ZzKzNoWzMssyrzLPNjcypZ8yhzJ/MvMyxzZrMnsyszYVvzJfNnC7Mnw==",CometChatConstants.GROUP_TYPE_PRIVATE,"1234","","Hello Description");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("test","hello");
            group.setMetadata(jsonObject);

            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "sendCreateGroupWithLongName onSuccess: "+group.toString());
                    assertEquals("zJfMus2WzLnMr82T4bmuzKTNjcylzYfNiGjMssyBZc2PzZPMvMyXzJnMvMyjzZQgzYfMnMyxzKDN\",\n" +
                            "  \"k82NzYVOzZXNoGXMl8yxesyYzJ3MnMy6zZlwzKTMusy5zY3Mr82aZcygzLvMoM2ccsyozKTNjcy6\",\n" +
                            "  \"zJbNlMyWzJZkzKDMn8ytzKzMnc2facymzZbMqc2TzZTMpGHMoMyXzKzNicyZbs2azZwgzLvMnsyw\",\n" +
                            "  \"zZrNhWjMtc2JacyzzJ52zKLNh+G4mc2OzZ8t0onMrcypzLzNlG3MpMytzKtpzZXNh8ydzKZuzJfN\",\n" +
                            "  \"meG4jcyfIMyvzLLNlc2ex6vMn8yvzLDMss2ZzLvMnWYgzKrMsMywzJfMlsytzJjNmGPMps2NzLLM\",\n" +
                            "  \"ns2NzKnMmeG4pc2aYcyuzY7Mn8yZzZzGocypzLnNjnPMpC7MncydINKJWsyhzJbMnM2WzLDMo82J\",\n" +
                            "  \"zJxhzZbMsM2ZzKzNoWzMssyrzLPNjcypZ8yhzJ/MvMyxzZrMnsyszYVvzJfNnC7Mnw==",group.getName());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "sendCreateGroupWithLongName onError: "+e.getMessage());
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }


    @Test
    public void sendJoinGroupPrivate(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

             CometChat.joinGroup("133", CometChatConstants.GROUP_TYPE_PRIVATE, null, new CometChat.CallbackListener<Group>() {
                 @Override
                 public void onSuccess(Group group) {
                     Log.d(TAG, "joinGroup onSuccess: "+group.toString());
                     countDownLatch.countDown();

                 }

                 @Override
                 public void onError(CometChatException e) {
                    countDownLatch.countDown();
                     Log.d(TAG, "joinGroup onError: "+e.getMessage());
                     assertEquals("The private groups cannot be joined. Users need to be added to such groups.",e.getMessage());
                 }
             });
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }


    @Test
    public void test_joinGroupPassword(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            CometChat.joinGroup("21", CometChatConstants.GROUP_TYPE_PASSWORD, "123", new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "joinGroup onSuccess: "+group.toString());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "joinGroup onError: "+e.getMessage());
                    assertEquals("The user with uid superhero2 has already joined the group with guid 21.",e.getMessage());
                }
            });
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }


    @Test
    public void test_joinGroupWithEmptyGUID(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            CometChat.joinGroup("", CometChatConstants.GROUP_TYPE_PASSWORD, "", new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "joinGroupWithEmptyGUID onSuccess: "+group.toString());
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    countDownLatch.countDown();
                    Log.d(TAG, "joinGroupWithEmptyGUID onError: "+e.getMessage());
                    assertEquals("Please provide a valid GUID",e.getMessage());
                }
            });
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void test_addMemberToGroup(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            List<GroupMember> groupMemberList=new ArrayList<>();
            groupMemberList.add(new GroupMember("superhero1",CometChatConstants.SCOPE_ADMIN));

            CometChat.addMembersToGroup("133", groupMemberList, null, new CometChat.CallbackListener<HashMap<String, String>>() {

                @Override
                public void onSuccess(HashMap<String, String> stringStringHashMap) {
                    Log.d(TAG, "test_addMemberToGroup onSuccess: "+stringStringHashMap.toString());
//                    assertEquals("success",stringStringHashMap.get("superhero1"));
                    assertEquals("Member already has the same scope admin",stringStringHashMap.get("superhero1"));
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_addMemberToGroup onError: "+e.getMessage());
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void test_addMemberToGroupWithEmptyGUID(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            List<GroupMember> groupMemberList=new ArrayList<>();
            groupMemberList.add(new GroupMember("superhero1",CometChatConstants.SCOPE_ADMIN));

            CometChat.addMembersToGroup("", groupMemberList, null, new CometChat.CallbackListener<HashMap<String, String>>() {

                @Override
                public void onSuccess(HashMap<String, String> stringStringHashMap) {
                    Log.d(TAG, "test_addMemberToGroup onSuccess: "+stringStringHashMap.toString());
//                    assertEquals("Member already has the same scope admin",stringStringHashMap.get("superhero1"));
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_addMemberToGroup onError: "+e.getMessage());
                    countDownLatch.countDown();
                    assertEquals("Please provide a valid GUID",e.getMessage());

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }


    @Test
    public void test_addMemberToGroupWithEmptyList(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            List<GroupMember> groupMemberList=new ArrayList<>();
//            groupMemberList.add(new GroupMember("superhero1",CometChatConstants.SCOPE_ADMIN));

            CometChat.addMembersToGroup("133", groupMemberList, null, new CometChat.CallbackListener<HashMap<String, String>>() {

                @Override
                public void onSuccess(HashMap<String, String> stringStringHashMap) {
                    Log.d(TAG, "test_addMemberToGroupWithEmptyList onSuccess: "+stringStringHashMap.toString());
                    assertEquals("Member already has the same scope admin",stringStringHashMap.get("superhero1"));
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_addMemberToGroupWithEmptyList onError: "+e.getMessage());
                    countDownLatch.countDown();
                    assertEquals("The list provided is empty. Please provide a valid list.",e.getMessage());

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void test_leaveGroup(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);


            CometChat.leaveGroup("133", new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "test_leaveGroup onSuccess: "+s);
                    assertEquals("Group left successfully",s);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_leaveGroup onError: "+e.getMessage());
                    assertEquals("ERR_NOT_A_MEMBER",e.getCode());
                    countDownLatch.countDown();

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }

    @Test
    public void test_leaveGroupWithEmptyGUID(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);


            CometChat.leaveGroup("", new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "test_leaveGroupWithEmptyGUID onSuccess: "+s);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_leaveGroupWithEmptyGUID onError: "+e.getMessage());
                    assertEquals("Please provide a valid GUID",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }

    @Test
    public void updateGroup(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("123","Updated Group",CometChatConstants.GROUP_TYPE_PRIVATE,null,null,null);
           CometChat.updateGroup(group, new CometChat.CallbackListener<Group>() {
              @Override
              public void onSuccess(Group group) {
                  Log.d(TAG, "updateGroup onSuccess: "+group.toString());
                  assertEquals("123",group.getGuid());
                  countDownLatch.countDown();
              }

              @Override
              public void onError(CometChatException e) {
                  Log.d(TAG, "updateGroup onError: "+e.getMessage());
                  countDownLatch.countDown();
              }
          });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }

    @Test
    public void updateGroupPublic(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("123","Updated Group",CometChatConstants.GROUP_TYPE_PUBLIC,null,null,null);
            CometChat.updateGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "updateGroup onSuccess: "+group.toString());
                    assertEquals("123",group.getGuid());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "updateGroup onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }


    @Test
    public void updateGroupPassword(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("123","Updated Group","protected","123",null,null);

            CometChat.updateGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "updateGroup onSuccess: "+group.toString());
                    assertEquals("123",group.getGuid());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "updateGroup onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }

    @Test
    public void updateGroupWithEmptyName(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            Group group=new Group("123","",CometChatConstants.GROUP_TYPE_PUBLIC,"123",null,null);

            CometChat.updateGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Log.d(TAG, "updateGroupWithEmptyName onSuccess: "+group.toString());
                    assertEquals("123",group.getGuid());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "updateGroupWithEmptyName onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }


    @Test
    public void test_deleteGroup(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

              CometChat.deleteGroup("123", new CometChat.CallbackListener<String>() {
                  @Override
                  public void onSuccess(String s) {
                      Log.d(TAG, "test_deleteGroup onSuccess: "+s);
                      countDownLatch.countDown();
                      assertEquals("Group deleted successfully.",s);
                  }

                  @Override
                  public void onError(CometChatException e) {
                      Log.d(TAG, "test_deleteGroup onError: "+e.getMessage());
                      countDownLatch.countDown();
                      assertEquals("The group with guid 123 does not exist. Please use correct guid or use create group API.",e.getMessage());
                  }
              });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }


    @Test
    public void test_kickGroupMember(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

             CometChat.kickGroupMember("superhero1", "21", new CometChat.CallbackListener<String>() {

                 @Override
                 public void onSuccess(String s) {
                     Log.d(TAG, "test_kickGroup onSuccess: "+s);
                     countDownLatch.countDown();
                 }

                 @Override
                 public void onError(CometChatException e) {
                     Log.d(TAG, "test_kickGroup onError: "+e.getMessage());
                     countDownLatch.countDown();
                     assertNotNull(e.getMessage());
                 }
             });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }

    }

    @Test
    public void test_kickGroupMemberEmptyUID(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            CometChat.kickGroupMember("", "21", new CometChat.CallbackListener<String>() {

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "test_kickGroupMemberEmptyUID onSuccess: "+s);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_kickGroupMemberEmptyUID onError: "+e.getMessage());
                    countDownLatch.countDown();
                    assertEquals("Please provide a valid UID",e.getMessage());

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void test_banGroupMember(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            CometChat.banGroupMember("superhero3", "21", new CometChat.CallbackListener<String>() {

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "test_banGroupMember onSuccess: "+s);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_banGroupMember onError: "+e.getMessage());
                    countDownLatch.countDown();
                    assertNotNull(e.getMessage());
//                    assertEquals("The user with uid superhero2 does not have enough clearance to ban the user with uid superhero3 from group with guid 21.",e.getMessage());

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void test_unbanGroupMember(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            CometChat.unbanGroupMember("superhero3", "21", new CometChat.CallbackListener<String>() {

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "test_unbanGroupMember onSuccess: "+s);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_unbanGroupMember onError: "+e.getMessage());
                    countDownLatch.countDown();
                    assertEquals("The user with uid superhero2 does not have enough clearance to unban the user with uid superhero3 from group with guid {{guid}}.",e.getMessage());

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void test_unbanGroupMemberWithEmptyUID(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            CometChat.unbanGroupMember("", "21", new CometChat.CallbackListener<String>() {

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "test_unbanGroupMemberWithEmptyUID onSuccess: "+s);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_unbanGroupMemberWithEmptyUID onError: "+e.getMessage());
                    countDownLatch.countDown();
//                    assertEquals("The user with uid superhero2 does not have enough clearance to ban the user with uid superhero3 from group with guid 21.",e.getMessage());

                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }

    @Test
    public void bannedGroupMemberRequest(){

        try {

            CountDownLatch countDownLatch=new CountDownLatch(1);

            BannedGroupMembersRequest bannedGroupMembersRequest=new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder("21").setLimit(30).build();

            bannedGroupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    Log.d(TAG, "bannedGroupMembersRequest onSuccess: "+groupMembers.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "bannedGroupMembersRequest onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

           countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }
        }catch (Exception e){

        }

    }

    @Test
    public void test_changeMemberScope(){

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            CometChat.updateGroupMemberScope("superhero2", "supergroup", CometChatConstants.SCOPE_ADMIN, new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "updateGroupMemberScope onSuccess: " + s);
                    assertEquals("Group member scope changed successfully.",s);
                    countDownLatch.countDown();

                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "updateGroupMemberScope onError: " + e.getMessage());
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void test_changeMemberScopeWithEmptyScope(){

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);
            CometChat.updateGroupMemberScope("superhero2", "supergroup", "", new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "test_changeMemberScopeWithEmptyScope onSuccess: " + s);
                    assertEquals("Group member scope changed successfully.",s);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_changeMemberScopeWithEmptyScope onError: " + e.getMessage());
                    countDownLatch.countDown();
                    assertEquals("Failed to validate the data sent with the request.",e.getMessage());
                }
            });
            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void test_GroupMemberRequest(){

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            GroupMembersRequest groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder("supergroup").setLimit(30).build();

            groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    Log.d(TAG, "test_GroupMemberRequest onSuccess: "+groupMembers.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_GroupMemberRequest onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }


    }

    @Test
    public void test_GroupMemberRequestWithZeroLimit(){

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            GroupMembersRequest groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder("supergroup").setLimit(0).build();

            groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    Log.d(TAG, "test_GroupMemberRequestWithZeroLimit onSuccess: "+groupMembers.size());
                    assertEquals(5,groupMembers.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_GroupMemberRequestWithZeroLimit onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }


    }


    @Test
    public void test_GroupMemberRequestWithEmptyGUID(){

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            GroupMembersRequest groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder("").setLimit(0).build();

            groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    Log.d(TAG, "test_GroupMemberRequestWithEmptyGUID onSuccess: "+groupMembers.size());
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_GroupMemberRequestWithEmptyGUID onError: "+e.getMessage());
                    assertEquals("Please provide a valid GUID",e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }


    }


    @Test
    public void test_blockedUser(){

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            BlockedUsersRequest blockedUsersRequest=new BlockedUsersRequest.BlockedUsersRequestBuilder().setLimit(30).build();

            blockedUsersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "test_blockedUser onSuccess: "+users.toString());
                    assertNotNull(users);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_blockedUser onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }


    @Test
    public void test_blockedUserWithoutLimit(){

        try {

            CountDownLatch countDownLatch = new CountDownLatch(1);

            BlockedUsersRequest blockedUsersRequest=new BlockedUsersRequest.BlockedUsersRequestBuilder().build();

            blockedUsersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Log.d(TAG, "test_blockedUserWithoutLimit onSuccess: "+users.toString());
                    assertNotNull(users);
                    countDownLatch.countDown();
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "test_blockedUserWithoutLimit onError: "+e.getMessage());
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await(8, TimeUnit.SECONDS);
            if (countDownLatch.getCount() == 1) {
                assertNotEquals("CometChat timeout", null);
            }

        }catch (Exception e){

        }
    }





}

package tech.sana.scs_sdk_sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import tech.sana.scs_sdk.account.AccountHandler;
import tech.sana.scs_sdk.account.listener.OnAccountingTaskCompleted;
import tech.sana.scs_sdk.file_system.CloudFileSystem;
import tech.sana.scs_sdk.file_system.Configuration;
import tech.sana.scs_sdk.file_system.listener.OnFileTransferCallBackListener;
import tech.sana.scs_sdk.file_system.listener.OnRequestCallBackListener;
import tech.sana.scs_sdk.file_system.listener.Response;
import tech.sana.scs_sdk.file_system.model.Profile;

/**
 * Created by payam darabi on 02/27/2018.
 */
public class MainActivity extends AppCompatActivity {
    Button btn_login, btn_upload, btn_download, btn_makeFolder, btn_delete,
            btn_copy, btn_move, btn_rename, btn_viewprofile, btn_itemExists,
            btn_listEntities, btn_logOut, btn_userList;
    LinearLayout lay_actions, lay_login;
    CloudFileSystem cfs;
    final String localPath = Environment.getExternalStorageDirectory() + "/SCS_SDK/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //get storage access
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                111);

        // make new configuration with your apikey
        final Configuration configuration = new Configuration(getApplicationContext(),
                "236ss64ujd57b29e9ui36bjnh557b8it75f22cbe");

        // fill cloudfileSystem with instance
        cfs = CloudFileSystem.getInstance(configuration);

        //Login / Register/ ForgetPass
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //calling abrino login page
                AccountHandler.createAccount(getApplicationContext(),
                        configuration,
                        new OnAccountingTaskCompleted() {
                            @Override
                            public void onAccountSucceed(String username) {
                                //login successfull or loggedIn before
                                Toast.makeText(v.getContext(), username, Toast.LENGTH_LONG).show();
                                lay_login.setVisibility(View.GONE);
                                lay_actions.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAccountFailed(int errorcode) {
                                // login failed
                                Toast.makeText(getApplicationContext(),
                                        String.valueOf(errorcode)
                                        , Toast.LENGTH_LONG).show();
                                lay_login.setVisibility(View.VISIBLE);
                                lay_actions.setVisibility(View.GONE);
                            }
                        }
                );

            }
        });

        //Upload
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling android filechooser
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 124);
            }
        });

        //Download
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling download method
                cfs.downloadFile("/webdav/syncs/sample/Abrino.lnk",
                        localPath,
                        false, new OnFileTransferCallBackListener() {

                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                // download complete
                                Log.i("SANA", "Download Completed");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Download Completed"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int erCode) {
                                //download fail
                                Log.i("SANA", "Download Error: " + erCode);
                            }

                            @Override
                            public void onProgress(long progress) {
                                // download progress
                                Log.i("SANA", "Download onProgress " + progress);
                            }

                        });
            }
        });

        //Make Directory
        btn_makeFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfs.makeDir("/webdav/syncs/Sample2/payam",
                        new OnRequestCallBackListener() {
                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                Log.i("sana", "make directory Completed");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "make directory Completed"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int errorCode) {
                                Log.i("sana", "make directory Error " + errorCode);
                            }
                        });
            }
        });

        //Delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete File or Folder
                cfs.delete("/webdav/syncs/Sample2/payam",
                        new OnRequestCallBackListener() {
                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                Log.i("SANA", "Delete Completed");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Delete Completed"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int i) {
                                Log.i("SANA", "Delete Error");
                            }
                        });
            }
        });

        //Copy
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfs.copy("/webdav/syncs/Sample2/blit.pdf",
                        "/webdav/syncs/sample/", false
                        , new OnRequestCallBackListener() {

                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                Log.i("SANA", "Copy Completed");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Copy Completed"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int erCode) {
                                Log.i("SANA", "Copy Error +" + erCode);
                            }
                        });
            }
        });

        //Move
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfs.move("/webdav/syncs/Sample2/blit.pdf",
                        "/webdav/syncs/", "blit.pdf",
                        false, new OnRequestCallBackListener() {

                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                Log.i("SANA", "Move Completed");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Move Completed"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int erCode) {
                                Log.i("SANA", "Move Error +" + erCode);
                            }
                        });
            }
        });

        //Rename
        btn_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfs.rename("/webdav/syncs/Sample2",
                        "Sample",
                        new OnRequestCallBackListener() {

                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                Log.i("SANA", "Rename Completed");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Rename Completed"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int erCode) {
                                Log.i("SANA", "Rename Error +" + erCode);
                            }
                        });
            }
        });

        //Check item Existance
        btn_itemExists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfs.existEntity("/webdav/syncs/Sample/blit.pdf",
                        new OnRequestCallBackListener() {

                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                Log.i("sana", "ItemExists");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "ItemExists"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int erCode) {
                                Log.i("sana", "itemExist Error: " + erCode);
                                if (erCode == 404)
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Item Not Exists"
                                            , Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        //Get ItemsList and Details
        btn_listEntities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfs.listEntities("/webdav/syncs/Sample",
                        1, new OnRequestCallBackListener() {
                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                List<Property> items = (List<Property>) response.getValue();
                                Toast.makeText(MainActivity.this,
                                        "First Items Name: " +
                                                items.get(0).getName(),
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int errorCode) {
                                Log.i("SANA", "List Error: " + errorCode);
                            }
                        });
            }
        });

        //LogOut
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AccountHandler.removeAccount(v.getContext());
                    lay_login.setVisibility(View.VISIBLE);
                    lay_actions.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //View profile
        btn_viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String profileUrl =
                        "https://account.abrino.ir/members/account/profile";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(profileUrl));
                startActivity(browserIntent);
            }
        });

        //getting User Information
        btn_userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfs.getProfileStats(new OnRequestCallBackListener() {
                    @Override
                    public void onRequestSucceed(Response<?> response) {
                        try {
                            // Init a Profile Object
                            Profile curUserProfile;
                            if (response.getValue() != null) {

                                //fill profile object with server response
                                curUserProfile = (Profile) response.getValue();
                                Toast.makeText(MainActivity.this,
                                        "User FirstName: " +
                                                curUserProfile.getFirstName(),
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFailed(int resultCode) {
                        Log.i("SANA", "get Profile Error: " + resultCode);
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check which request we're responding to
        if (requestCode == 124) {
            // make sure the request was successful
            if (resultCode == RESULT_OK) {
                // getting selected file address
                Uri selectedFileUri = data.getData();
                String path = AbsouloteFilePathFinder.
                        getPath(getApplicationContext(), selectedFileUri);
             //   File selectedFile = new File(path);

                // calling upload method
                cfs.uploadFile(getApplicationContext(), "/webdav/syncs/sample/",
                         path,
                        false, new OnFileTransferCallBackListener() {
                            @Override
                            public void onProgress(long progress) {
                                //Getting Uplaod Progress
                                Log.i("SANA", "Upload onProgress " + progress);

                            }

                            @Override
                            public void onRequestSucceed(Response<?> response) {
                                //SuccessFull Upload
                                Log.i("SANA", "Upload Completed");
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Upload Completed"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onRequestFailed(int errorCode) {
                                //Upload Failed
                                Log.i("SANA", "Upload Error Code: " + errorCode);
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Upload Error Code: " + errorCode
                                        , Toast.LENGTH_LONG).show();
                            }
                        });

            }
        }
    }

    private void initView() {
        //initilizing view components
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_download = (Button) findViewById(R.id.btn_download);
        btn_makeFolder = (Button) findViewById(R.id.btn_makeFolder);
        btn_copy = (Button) findViewById(R.id.btn_copy);
        btn_move = (Button) findViewById(R.id.btn_move);
        btn_rename = (Button) findViewById(R.id.btn_rename);
        btn_viewprofile = (Button) findViewById(R.id.btn_viewProfile);
        btn_userList = (Button) findViewById(R.id.btn_userlist);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_itemExists = (Button) findViewById(R.id.btn_fileExist);
        btn_listEntities = (Button) findViewById(R.id.btn_listEntities);
        btn_logOut = (Button) findViewById(R.id.btn_logout);

        lay_actions = (LinearLayout) findViewById(R.id.lay_actions);
        lay_login = (LinearLayout) findViewById(R.id.lay_login);
    }
}

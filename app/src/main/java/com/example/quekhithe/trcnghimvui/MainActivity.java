package com.example.quekhithe.trcnghimvui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quekhithe.trcnghimvui.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    EditText  txtUserName, txtUserPassword;
    MaterialEditText txtNewUserName, txtNewUserPassword, txtNewUserEmail;
    Button btnSign_up, btnSign_in;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Components();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("User");

        btnSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        btnSign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(txtUserName.getText().toString(), txtUserPassword.getText().toString());
            }
        });
    }

    private void login(final String userName, final String passWord) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userName).exists()) {
                    if (!userName.isEmpty()) {
                        User login = dataSnapshot.child(userName).getValue(User.class);
                        if (login.getPassword().equals(passWord)) {
                            Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please enter User Name", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "User is not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Sign up");
        dialog.setMessage("Please fill information");
        dialog.setIcon(R.drawable.ic_supervisor_account_black_24dp);

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        txtNewUserName = sign_up_layout.findViewById(R.id.txtNewUserName);
        txtNewUserPassword = sign_up_layout.findViewById(R.id.txtNewUserPassword);
        txtNewUserEmail = sign_up_layout.findViewById(R.id.txtNewUserEmail);

        dialog.setView(sign_up_layout);

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(txtNewUserName.getText().toString(),
                                        txtNewUserPassword.getText().toString(),
                                        txtNewUserEmail.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists()) {
                            Toast.makeText(MainActivity.this, "User exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void Components() {

        txtUserName = findViewById(R.id.txtUserName);
        txtUserPassword = findViewById(R.id.txtUserPassword);
        btnSign_in = findViewById(R.id.btnSign_in);
        btnSign_up = findViewById(R.id.btnSign_up);
    }
}

package com.group12.journeysharing.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group12.journeysharing.R;
import com.group12.journeysharing.Util;
import com.group12.journeysharing.fragment.AccountFragment;
import com.group12.journeysharing.fragment.BookingHistoryFragment;
import com.group12.journeysharing.fragment.HomeFragment;
import com.group12.journeysharing.fragment.JourneyFragment;
import com.group12.journeysharing.fragment.SupportFragment;
import com.group12.journeysharing.model.User;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_CAMERA_PERMISSION_CODE = 2;
    private static final int CAMERA_REQUEST = 3;
    Class selectedFragmentClass = null;
    ImageView profileImageView;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        final TextView fullNameTextView = headerView.findViewById(R.id.fullName);
        final TextView ratingTextView = headerView.findViewById(R.id.rating);
        final RatingBar ratingBar = headerView.findViewById(R.id.ratingBar);
        profileImageView = headerView.findViewById(R.id.profileImageView);
        ImageButton imageButton = headerView.findViewById(R.id.imageButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Toast.makeText(this, firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                Toast.makeText(HomeActivity.this, user.getFullName(), Toast.LENGTH_SHORT).show();
                fullNameTextView.setText(user.getFullName());
                ratingTextView.setText(String.valueOf(user.getRating()));
                ratingBar.setRating((float) user.getRating());

                if(user.isActive())
                    setInitialFragment(JourneyFragment.class);
                else
                    setInitialFragment(HomeFragment.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);

                }
                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


    }

    public void setInitialFragment(Class fragmentClass)
    {
        selectedFragmentClass = fragmentClass;

        Fragment fragment = null;
        try {
            fragment = (Fragment) selectedFragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            profileImageView.setImageBitmap(photo);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to close Journey Sharing?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle(R.string.logout_string);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;


        if (id == R.id.nav_home) {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    user = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                    if(user.isActive())
                        setInitialFragment(JourneyFragment.class);
                    else
                        setInitialFragment(HomeFragment.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (id == R.id.nav_account) {
            setInitialFragment(AccountFragment.class);
//            selectedFragmentClass = AccountFragment.class;

        } else if (id == R.id.nav_booking_history) {
            setInitialFragment(BookingHistoryFragment.class);
//            selectedFragmentClass = BookingHistoryFragment.class;
        } else if (id == R.id.nav_support) {
            setInitialFragment(SupportFragment.class);
//            selectedFragmentClass = SupportFragment.class;

        }

//        try {
//            fragment = (Fragment) selectedFragmentClass.newInstance();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        assert fragment != null;
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

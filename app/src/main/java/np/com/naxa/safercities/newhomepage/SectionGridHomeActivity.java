package np.com.naxa.safercities.newhomepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.activity.MyCircleProfileActivity;
import np.com.naxa.safercities.activity.NotifyOthersActivity;
import np.com.naxa.safercities.emergencyContacts.EmergencyContactsActivity;
import np.com.naxa.safercities.report.ReportActivity;
import np.com.naxa.safercities.disasterinfo.HazardInfoActivity;
import np.com.naxa.safercities.event.GmailLoginEvent;
import np.com.naxa.safercities.mapboxmap.OpenSpaceMapActivity;
import np.com.naxa.safercities.mycircle.registeruser.LoginResponse;
import np.com.naxa.safercities.network.UrlClass;
import np.com.naxa.safercities.network.retrofit.NetworkApiClient;
import np.com.naxa.safercities.network.retrofit.NetworkApiInterface;
import np.com.naxa.safercities.quiz.QuizHomeActivity;
import np.com.naxa.safercities.report.SavedFormListActivity;
import np.com.naxa.safercities.report.wardstaff.UnverifiedReportFormListActivity;
import np.com.naxa.safercities.settings.SettingsActivity;
import np.com.naxa.safercities.utils.DialogFactory;
import np.com.naxa.safercities.utils.JsonGsonConverterUtils;
import np.com.naxa.safercities.utils.SharedPreferenceUtils;
import np.com.naxa.safercities.utils.imageutils.CircleTransform;
import np.com.naxa.safercities.utils.recycleviewutils.LinearLayoutManagerWithSmoothScroller;
import np.com.naxa.safercities.utils.recycleviewutils.RecyclerViewType;

public class SectionGridHomeActivity extends AppCompatActivity {

    protected static final String RECYCLER_VIEW_TYPE = "recycler_view_type";
    private static final String TAG = "SectionGridHomeActivity";
    @BindView(R.id.btn_disaster_info)
    Button btnDisasterInfo;
    @BindView(R.id.btn_react_quickly)
    Button btnReactQuickly;
    @BindView(R.id.btn_info)
    Button btnDisasterNews;
    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.appbar_general)
    AppBarLayout appbar;
    @BindView(R.id.imageView7)
    ImageView imageView;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.btnAskForBlood)
    Button btnAskForBlood;
    @BindView(R.id.btnNotifyOthers)
    Button btnNotifyOthers;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.btn_disaster_info_top)
    Button btnDisasterInfoTop;
    @BindView(R.id.btn_react_quickly_top)
    Button btnReactQuicklyTop;
    @BindView(R.id.btn_info_top)
    Button btnInfoTop;
    @BindView(R.id.quickActionButtonLayout)
    LinearLayout quickActionButtonLayout;

    private RecyclerViewType recyclerViewType;
    private RecyclerView recyclerView;
    SectionRecyclerViewAdapter adapter;

    boolean isLoginBtnClick = false;

    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, SectionGridHomeActivity.class);
        context.startActivity(intent);
    }


    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    String userPhotoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_grid_home);
        ButterKnife.bind(this);

//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);


        setupCollapsingToolbar();
        // load nav menu header data
//        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        recyclerViewType = RecyclerViewType.GRID;
//        setUpToolbarTitle();
        setUpRecyclerView();
        populateRecyclerView();

        setupGmailLogin();

    }

    private void setupCollapsingToolbar(){
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));



        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    Log.d(TAG, "onOffsetChanged: Collapsed");
                    quickActionButtonLayout.setVisibility(View.GONE);
                }
                else
                {
                    //Expanded
                    Log.d(TAG, "onOffsetChanged: Expanded");
                    quickActionButtonLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    //setup recycler view
    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.sectioned_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //populate recycler view
    private void populateRecyclerView() {
        String[] sectionHeader = {"प्रकोपमाथि प्रकाश", "जानकारी पोर्टल", "तयार हुनुहोस्", ""};
        String[] sectionChildTitle = {"प्रकोपको बारेमा जानकारी", "विपद् शब्दावली", "हाजिरीजवाफ खेल्नुहोस्", "मौसमी तयारी पात्रो", "अडियो", "भिडियो", "कागजातहरू", "ब्रोशर",
                "घर गृहस्थीमा तयारी", "विद्यालयमा तयारी", "स्वास्थ्यमा तयारी", "स्थानीय तहमा तयारी", "", "", "", ""};

        ArrayList<Drawable> gridIcon = new ArrayList<Drawable>();
        gridIcon.add(getResources().getDrawable(R.drawable.ic_hazard_info_grid));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_drr_dictionary_grid));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_quiz_grid));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_date_calendar_24dp));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_notify_others_grid));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_video_library24dp));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_picture_as_pdf_24dp));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_library_brochure_24dp));

        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_home_be_ready_home_update));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_home_be_ready_school_update));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_home_be_ready_health_update));
        gridIcon.add(getResources().getDrawable(R.drawable.ic_grid_home_be_ready_community_update));




        ArrayList<SectionModel> sectionModelArrayList = new ArrayList<>();
        //for loop for sections
        int sectionChildTitlePos = 0;
        for (int i = 1; i <= 3; i++) {
            ArrayList<String> itemArrayList = new ArrayList<>();
            ArrayList<Drawable> itemIconArrayList = new ArrayList<>();
            //for loop for items
            if (i == 1) {
                for (int j = 1; j <= 4; j++) {
                    itemArrayList.add(sectionChildTitle[sectionChildTitlePos]);
                    itemIconArrayList.add(gridIcon.get(sectionChildTitlePos));
                    sectionChildTitlePos++;
                }
            }
            if (i == 2) {
                for (int j = 1; j <= 4; j++) {
                    itemArrayList.add(sectionChildTitle[sectionChildTitlePos]);
                    itemIconArrayList.add(gridIcon.get(sectionChildTitlePos));
                    sectionChildTitlePos++;
                }
            }
            if (i == 3) {
                for (int j = 1; j <= 4; j++) {
                    itemArrayList.add(sectionChildTitle[sectionChildTitlePos]);
                    itemIconArrayList.add(gridIcon.get(sectionChildTitlePos));
                    sectionChildTitlePos++;
                }
            }



            //add the section and items to array list
            sectionModelArrayList.add(new SectionModel(sectionHeader[i - 1], itemArrayList, itemIconArrayList));

        }
        sectionModelArrayList.add(null);

        adapter = new SectionRecyclerViewAdapter(this, recyclerViewType, sectionModelArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_disaster_info, R.id.btn_react_quickly, R.id.btn_info, R.id.btnAskForBlood, R.id.btnNotifyOthers,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_disaster_info:
                appbar.setExpanded(false);
                smoothScrollRecyclerToPosition(0);
                break;
            case R.id.btn_react_quickly:
                appbar.setExpanded(false);
                smoothScrollRecyclerToPosition(2);

                break;
            case R.id.btn_info:
                appbar.setExpanded(false);
                smoothScrollRecyclerToPosition(1);

                break;

            case R.id.btnAskForBlood:
                startActivity(new Intent(SectionGridHomeActivity.this, EmergencyContactsActivity.class));
                break;

            case R.id.btnNotifyOthers:
                startActivity(new Intent(SectionGridHomeActivity.this, NotifyOthersActivity.class));
                break;
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("");
        txtWebsite.setText("");

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load("https://thumbs.dreamstime.com/b/profile-icon-male-avatar-portrait-casual-person-silhouette-face-flat-design-vector-46846328.jpg")
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
//        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(SectionGridHomeActivity.this, MyCircleProfileActivity.class));
                        break;
                    case R.id.nav_ask_for_help:
                        startActivity(new Intent(SectionGridHomeActivity.this, ReportActivity.class));
                        break;

                    case R.id.nav_report:
                        startActivity(new Intent(SectionGridHomeActivity.this, ReportActivity.class));
                        break;

                    case R.id.nav_view_saved_report:
                        startActivity(new Intent(SectionGridHomeActivity.this, SavedFormListActivity.class));
                        break;

                    case R.id.nav_view_unverified_report:
                        startActivity(new Intent(SectionGridHomeActivity.this, UnverifiedReportFormListActivity.class));
                        break;

                    case R.id.nav_notify_oithers:
                        startActivity(new Intent(SectionGridHomeActivity.this, NotifyOthersActivity.class));

                        break;
                    case R.id.nav_hazard_info:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(SectionGridHomeActivity.this, HazardInfoActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_play_quiz:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(SectionGridHomeActivity.this, QuizHomeActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_settings:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(SectionGridHomeActivity.this, SettingsActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_map:
                        startActivity(new Intent(SectionGridHomeActivity.this, OpenSpaceMapActivity.class));
                        drawer.closeDrawers();
                        return true;

                        case R.id.nav_login:
                            isLoginBtnClick = true;
                            DialogFactory.createGmailLoginDialog(SectionGridHomeActivity.this).show();
                            return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }



    // gmail Login Start
    private void setupGmailLogin() {

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            return;
        }

        Toast.makeText(this, "Google Sign-in complete", Toast.LENGTH_SHORT).show();

        userPhotoUri = account.getPhotoUrl().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(this);
            jsonObject.put("email", account.getEmail());
            jsonObject.put("token", sharedPreferenceUtils.getStringValue(SharedPreferenceUtils.TOKEN_ID, null));


        Log.d(TAG, "convertDataToJson: "+jsonObject.toString());

        if(sharedPreferenceUtils.getBoolanValue(SharedPreferenceUtils.USER_ALREADY_LOGGED_IN, false)){
            return;
        }

        NetworkApiInterface apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);

        apiInterface.getLoginResponse(UrlClass.API_ACCESS_TOKEN, jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<LoginResponse>() {
                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        if(isLoginBtnClick) {
                            if (loginResponse.getError() == 0) {
                                DialogFactory.createCustomDialog(SectionGridHomeActivity.this, loginResponse.getMessage(), new DialogFactory.CustomDialogListener() {
                                    @Override
                                    public void onClick() {
                                        sharedPreferenceUtils.setValue(SharedPreferenceUtils.USER_DETAILS, JsonGsonConverterUtils.getJsonFromGson(loginResponse.getData()));
                                        sharedPreferenceUtils.setValue(SharedPreferenceUtils.USER_ALREADY_REGISTERED, true);
                                        sharedPreferenceUtils.setValue(SharedPreferenceUtils.USER_ALREADY_LOGGED_IN, true);

                                    }
                                }).show();
                            }

                            if (loginResponse.getError() == 1) {
                                DialogFactory.createCustomErrorDialog(SectionGridHomeActivity.this, loginResponse.getMessage(), new DialogFactory.CustomDialogListener() {
                                    @Override
                                    public void onClick() {

                                        startActivity(new Intent(SectionGridHomeActivity.this, MyCircleProfileActivity.class));
                                    }
                                }).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isLoginBtnClick) {
                            DialogFactory.createCustomErrorDialog(SectionGridHomeActivity.this, e.getMessage(), new DialogFactory.CustomDialogListener() {
                                @Override
                                public void onClick() {

                                }
                            }).show();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//    gmail login end


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGmailLoginEvent(GmailLoginEvent.loginButtonClick itemClick) {

        signIn();

        Toast.makeText(this, "Gmail account Logging in", Toast.LENGTH_SHORT).show();

    }




    private void smoothScrollRecyclerToPosition(int position){
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();


//                        displayedposition = llm.findFirstVisibleItemPosition();


            }
        });

        LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
//        llm.scrollToPositionWithOffset(position , -200);
        llm.scrollToPositionWithOffset(position , 0);
    }

}

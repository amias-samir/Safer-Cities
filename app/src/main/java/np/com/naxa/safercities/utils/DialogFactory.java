package np.com.naxa.safercities.utils;

/**
 * Created by Nishon Tandukar on 16 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.gms.common.SignInButton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import np.com.naxa.safercities.R;
import np.com.naxa.safercities.event.GmailLoginEvent;
import np.com.naxa.safercities.event.MyCircleContactEvent;
import np.com.naxa.safercities.mapboxmap.mapboxutils.DrawGeoJsonOnMap;
import np.com.naxa.safercities.mapboxmap.mapboxutils.MapDataLayerDialogCloseListen;
import np.com.naxa.safercities.mycircle.ContactModel;
import np.com.naxa.safercities.mycircle.contactlistdialog.contactlistadapter.MyCircleContactAddDialogListAdapter;
import np.com.naxa.safercities.utils.sectionmultiitemUtils.SectionMultipleItem;
import np.com.naxa.safercities.utils.sectionmultiitemUtils.SectionMultipleItemAdapter;

import static np.com.naxa.safercities.utils.SharedPreferenceUtils.KEY_MUNICIPAL_BOARDER;
import static np.com.naxa.safercities.utils.SharedPreferenceUtils.KEY_OPENSTREET;
import static np.com.naxa.safercities.utils.SharedPreferenceUtils.KEY_SATELLITE;
import static np.com.naxa.safercities.utils.SharedPreferenceUtils.KEY_STREET;
import static np.com.naxa.safercities.utils.SharedPreferenceUtils.KEY_WARD;


public final class DialogFactory {

    private static final String TAG = "DialogFactory";

    private ProgressDialog progressDialog;

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }


    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createDataSyncErrorDialog(Context context, String message, String code) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(context.getString(R.string.dialog_error_title_sync_failed, code))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }


    public static Dialog createMessageDialog(final Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }


    public static AlertDialog.Builder createActionDialog(final Context context, String title, String message) {
        return new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(title).setCancelable(false)
                .setMessage(message);
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static Dialog createDataSyncErrorDialog(Context context, String responseCode, @StringRes int messageResource) {
        return createDataSyncErrorDialog(context, context.getString(messageResource), responseCode);
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.RiseUpDialog);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressBarDialog(Context context, String title, String message) {

        final ProgressDialog progress = new ProgressDialog(context, R.style.RiseUpDialog);

        DialogInterface.OnClickListener buttonListerns =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                progress.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };


        progress.setMessage(message);
        progress.setTitle(title);

        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progress.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_action_hide), buttonListerns);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);


        return progress;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    public static Dialog createCustomDialog(@NonNull Context context, @NonNull String message, @NonNull CustomDialogListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_success);

        TextView text = (TextView) dialog.findViewById(R.id.tv_message);
        text.setText(message);

        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


    public interface CustomDialogListener {
        void onClick();
    }

    public static Dialog createCustomErrorDialog(@NonNull Context context, @NonNull String message, @NonNull CustomDialogListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_error);

        TextView text = (TextView) dialog.findViewById(R.id.tv_message);
        text.setText(message);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick();
            }
        });
        return dialog;
    }


    public static Dialog createBaseLayerDialog(@NonNull Context context, @NonNull CustomBaseLayerDialogListner listner) {

        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.select_base_layer_custom_dialog_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_closeDialog);

        Switch street = (Switch) dialog.findViewById(R.id.switchStreetView);
        Switch satellite = (Switch) dialog.findViewById(R.id.switchSatelliteView);
        Switch openStreet = (Switch) dialog.findViewById(R.id.switchOpenStreetView);
        Switch municipality = (Switch) dialog.findViewById(R.id.switchMetropolitianBoundary);
        Switch ward = (Switch) dialog.findViewById(R.id.switchWardBoundary);


        if (sharedPreferenceUtils.getIntValue(SharedPreferenceUtils.MAP_BASE_LAYER, -1) == KEY_STREET) {
            street.setChecked(true);
            satellite.setChecked(false);
            openStreet.setChecked(false);
            listner.onStreetClick();

        } else if (sharedPreferenceUtils.getIntValue(SharedPreferenceUtils.MAP_BASE_LAYER, -1) == KEY_SATELLITE) {
            satellite.setChecked(true);
            street.setChecked(false);
            openStreet.setChecked(false);
            listner.onSatelliteClick();
        } else if (sharedPreferenceUtils.getIntValue(SharedPreferenceUtils.MAP_BASE_LAYER, -1) == KEY_OPENSTREET) {
            openStreet.setChecked(true);
            openStreet.setChecked(true);
            satellite.setChecked(false);
            listner.onOpenStreetClick();
        }


        if (sharedPreferenceUtils.getIntValue(SharedPreferenceUtils.MAP_OVERLAY_LAYER, -1) == KEY_MUNICIPAL_BOARDER) {
            municipality.setChecked(true);
            ward.setChecked(false);
            listner.onMetropolitanClick();
        } else if (sharedPreferenceUtils.getIntValue(SharedPreferenceUtils.MAP_OVERLAY_LAYER, -1) == KEY_WARD) {
            ward.setChecked(true);
            municipality.setChecked(false);
            listner.onWardClick();
        }


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        street.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    satellite.setChecked(false);
                    openStreet.setChecked(false);
                    listner.onStreetClick();
                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.MAP_BASE_LAYER, KEY_STREET);
                }
            }
        });

        satellite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    street.setChecked(false);
                    openStreet.setChecked(false);
                    listner.onSatelliteClick();
                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.MAP_BASE_LAYER, KEY_SATELLITE);
                }
            }
        });

        openStreet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    street.setChecked(false);
                    satellite.setChecked(false);
                    listner.onOpenStreetClick();
                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.MAP_BASE_LAYER, KEY_OPENSTREET);
                }
            }
        });

        municipality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    ward.setChecked(false);
                    listner.onMetropolitanClick();
                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.MAP_OVERLAY_LAYER, KEY_MUNICIPAL_BOARDER);

                }
            }
        });

        ward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    municipality.setChecked(false);
                    listner.onWardClick();
                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.MAP_OVERLAY_LAYER, KEY_WARD);
                }
            }
        });


        dialog.getWindow().setAttributes(lp);
        return dialog;
    }


    public interface CustomBaseLayerDialogListner {
        void onStreetClick();

        void onSatelliteClick();

        void onOpenStreetClick();

        void onMetropolitanClick();

        void onWardClick();
    }


    public static Dialog createContactListDialog(@NonNull Context context, List<ContactModel> contactModelArrayList) {


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_my_circle_contact_list_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        FloatingSearchView floatingSearchView = (FloatingSearchView) dialog.findViewById(R.id.floating_search_contacts_dialog);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerViewContactsDialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.btnClose);


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MyCircleContactEvent.MyCircleContactDialogCloseClick());
                dialog.dismiss();

            }
        });

        MyCircleContactAddDialogListAdapter myCircleContactAddDialogListAdapter = new MyCircleContactAddDialogListAdapter(R.layout.contact_dialog_row_item_layout, contactModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(myCircleContactAddDialogListAdapter);

//        ((MyCircleContactAddDialogListAdapter) recyclerView.getAdapter()).replaceData(contactModelArrayList);


        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    public static Dialog createMapDataLayerDialog(@NonNull Context context, List<SectionMultipleItem> mapDataCategoryArrayList,
                                                  DrawGeoJsonOnMap drawGeoJsonOnMap, boolean isFirsttime, @NonNull MapDataLayerDialogCloseListen listner) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.map_data_filter_custom_dialog_layout);

//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerViewDialogMapDataCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_close_dialog);


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onDialogClose();
                dialog.dismiss();
            }
        });

        SectionMultipleItemAdapter sectionAdapter = new SectionMultipleItemAdapter(R.layout.map_data_layer_list_section_head_custom_layout, mapDataCategoryArrayList);
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SectionMultipleItem item = (SectionMultipleItem) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.card_view:
                        if (item.getMultiItemSectionModel() != null) {
                            if (item.getMultiItemSectionModel().getData_value().equals("") || item.getMultiItemSectionModel().getData_value() == null) {
                                Log.d(TAG, "onItemChildClick: null value ");
                                return;
                            }
                            Toast.makeText(context, item.getMultiItemSectionModel().getData_key(), Toast.LENGTH_LONG).show();

                        }
                        break;
                    default:
                        Toast.makeText(context, "OnItemChildClickListener " + position, Toast.LENGTH_LONG).show();
                        break;

                }
            }
        });
        recyclerView.setAdapter(sectionAdapter);

        if (isFirsttime) {
            listner.isFirstTime();
        }

//        Toast.makeText(context, "createMapDataLayerDialog set adapter", Toast.LENGTH_SHORT).show();
//        dialog.getWindow().setAttributes(lp);
        return dialog;
    }


    public static Dialog createGmailLoginDialog(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.gmail_login_custom_dialog);


        SignInButton dialogButton = (SignInButton) dialog.findViewById(R.id.gmail_sign_in_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(new GmailLoginEvent.loginButtonClick());

            }
        });
        return dialog;
    }


    public static Dialog createQuizAnsElaborationDialog(@NonNull Context context, @NonNull String questionProgress,
                                                        @NonNull String questionStatus, @NonNull String questionDetails,
                                                        @NonNull String questionElaboration, @NonNull CustomDialogListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.quiz_answer_elaboration_dialog_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView tvQuestionProgress = (TextView) dialog.findViewById(R.id.tv_question_progress);
        TextView tvQuestionStatus = (TextView) dialog.findViewById(R.id.tv_question_status);
        LinearLayout lLQuestionLayout = (LinearLayout) dialog.findViewById(R.id.questionLayout);
        lLQuestionLayout.setVisibility(View.GONE);
        TextView tvQuestionDetails = (TextView) dialog.findViewById(R.id.tv_question_details);
        TextView tvQuestionElaboration = (TextView) dialog.findViewById(R.id.tv_answer_elaboration);

        tvQuestionProgress.setText(questionProgress);
        tvQuestionStatus.setText(questionStatus);
        tvQuestionDetails.setText(questionDetails);
        tvQuestionElaboration.setText(questionElaboration);

        Button btnSeeQuestion = (Button) dialog.findViewById(R.id.btn_dialog_see_question);
        Button btnNextQuestion = (Button) dialog.findViewById(R.id.btn_dialog_next_question);
        btnSeeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lLQuestionLayout.setVisibility(View.VISIBLE);

            }
        });

        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setAttributes(lp);
        return dialog;
    }


}

package np.com.naxa.safercities.publications;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.safercities.R;
import np.com.naxa.safercities.event.PublicationListItemEvent;
import np.com.naxa.safercities.publications.entity.PublicationsListDetails;
import np.com.naxa.safercities.publications.youtubeplayer.YoutubePlayerActivity;
import np.com.naxa.safercities.publications.youtubeplayer.helper.YoutubeConstants;
import np.com.naxa.safercities.utils.Constants;
import np.com.naxa.safercities.utils.CreateAppMainFolderUtils;
import np.com.naxa.safercities.utils.NetworkUtils;
import np.com.naxa.safercities.utils.ToastUtils;
import np.com.naxa.safercities.utils.imageutils.LoadImageUtils;

import static android.text.Html.fromHtml;

public class PublicationDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PublicationDetails";
    @BindView(R.id.toolbar_general)
    Toolbar toolbar;
    @BindView(R.id.tv_publication_title)
    TextView tvPublicationTitle;
    @BindView(R.id.imageViewPublicationDetails)
    ImageView imageViewPublicationDetails;
    @BindView(R.id.tv_publication_desc)
    TextView tvPublicationDesc;
    @BindView(R.id.btn_view_files_video)
    Button btnViewFilesVideo;

    PublicationsListDetails publicationsListDetails;
//    @BindView(R.id.pdfView)
//    PDFView pdfView;

    private DownloadManager downloadManager;
    CreateAppMainFolderUtils createAppMainFolderUtils;

    private long pdf_DownloadId;
    private long audio_DownloadId;
    private boolean isPDFView = false;
    private String PDFFileName;
    private String audioType;
    private String audioFileName;

    private static final int KEY_AUDIO_ID = 1;
    private static final int KEY_PDF_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication_details);
        ButterKnife.bind(this);

        createAppMainFolderUtils = new CreateAppMainFolderUtils(PublicationDetailsActivity.this, CreateAppMainFolderUtils.appmainFolderName);


        //set filter to only when download is complete and register broadcast receiver
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);


        Intent intent = getIntent();
        publicationsListDetails = intent.getParcelableExtra(Constants.KEY_OBJECT);
        if (publicationsListDetails.getType().equals(PublicationListItemEvent.KEY_IMAGE)) {
            btnViewFilesVideo.setVisibility(View.GONE);
        } else if (publicationsListDetails.getType().equals(PublicationListItemEvent.KEY_FILES)) {
            if(publicationsListDetails.getSubfilecategory().equals(PublicationListItemEvent.KEY_SUB_CAT)){
                btnViewFilesVideo.setVisibility(View.GONE);
            }else {
                btnViewFilesVideo.setText("View Files");
            }
        }else if(publicationsListDetails.getType().equals(PublicationListItemEvent.KEY_AUDIO)){
            btnViewFilesVideo.setText("Play Audio");

        }
        setupToolBar(publicationsListDetails.getTitle());

        tvPublicationTitle.setText(publicationsListDetails.getTitle());


        tvPublicationDesc.setText(publicationsListDetails.getSummary());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPublicationDesc.setText(fromHtml(publicationsListDetails.getSummary(), 0, new ImageGetter(), null));
        } else {
            tvPublicationDesc.setText(fromHtml(publicationsListDetails.getSummary()));
        }

        if(publicationsListDetails.getType() .equals(PublicationListItemEvent.KEY_VIDEO)){
            String videoUrl = publicationsListDetails.getVideolink();
            int stringLength = videoUrl.length();
            String videoId = videoUrl.substring(stringLength-11,stringLength);

            String videoImageUrl = "https://img.youtube.com/vi/"+videoId+"/hqdefault.jpg";
            LoadImageUtils.loadImageToViewFromSrc(imageViewPublicationDetails, videoImageUrl);

        }else {
            LoadImageUtils.loadImageToViewFromSrc(imageViewPublicationDetails, publicationsListDetails.getPhoto());
        }

    }

    private void setupToolBar(String toolbarTitle) {
        setSupportActionBar(toolbar);
        if (toolbarTitle == null) {
            getSupportActionBar().setTitle("Multimedia and Publications");
        } else {
            getSupportActionBar().setTitle(toolbarTitle);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @OnClick(R.id.btn_view_files_video)
    public void onViewClicked() {

        viewFilesVideo(publicationsListDetails);
    }


    private void viewFilesVideo(@NonNull PublicationsListDetails publicationsListDetails) {
        String type = publicationsListDetails.getType();

        switch (type) {
            case PublicationListItemEvent.KEY_IMAGE:
                btnViewFilesVideo.setVisibility(View.GONE);

                break;

            case PublicationListItemEvent.KEY_VIDEO:
                if(NetworkUtils.isNetworkAvailable()) {
                    Intent intent = new Intent(PublicationDetailsActivity.this, YoutubePlayerActivity.class);
                    intent.putExtra(YoutubeConstants.VIDEO_KEY, publicationsListDetails);
                    startActivity(intent);
                }else {
                    ToastUtils.showShortToast("No internet connection");
                }
                break;

            case PublicationListItemEvent.KEY_FILES:
                Log.d(TAG, "viewFilesVideo: " + publicationsListDetails.getFile());
                viewPDFData(publicationsListDetails);
                break;

            case PublicationListItemEvent.KEY_AUDIO:
                Log.d(TAG, "viewFilesVideo: " + publicationsListDetails.getFile());
                Toast.makeText(this, "Playing Audio File", Toast.LENGTH_SHORT).show();
                downloadAudioFile(publicationsListDetails);
                break;
        }
    }


    List<String> imageList = new ArrayList<String>();

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            if (!source.equals("")) {
//                id = R.drawable.hughjackman;
                imageList.add(source);
                Log.d(TAG, "getDrawable: " + imageList.size() + " " + source);
            } else {
                return null;
            }
            return null;
        }
    }


    private void viewPDFData(@NonNull PublicationsListDetails publicationsListDetails) {
//        isPDFView = true;
        PDFFileName = publicationsListDetails.getTitle() + ".pdf";

        File targetFile = new File(createAppMainFolderUtils.getAppMediaFolderName() + File.separator + PDFFileName);
        if (targetFile.exists()) {
            viewPDFFile(createAppMainFolderUtils.getAppMediaFolderName(), PDFFileName);
        } else {
            if(NetworkUtils.isNetworkAvailable()) {
                pdf_DownloadId = DownloadData(publicationsListDetails, KEY_PDF_ID);
            }else{
                ToastUtils.showShortToast("No internet connection");
            }
        }
    }

    private void downloadAudioFile(@NonNull PublicationsListDetails publicationsListDetails){

        if(TextUtils.isEmpty(publicationsListDetails.getAudio())){
            Toast.makeText(this, "No Audio FIle Found", Toast.LENGTH_SHORT).show();
        }else {
            String audioUrl = publicationsListDetails.getAudio();
            int stringLength = audioUrl.length();
            audioType = audioUrl.substring(stringLength - 4, stringLength);
            audioFileName = publicationsListDetails.getTitle() + audioType;

            File targetFile = new File(createAppMainFolderUtils.getAppMediaFolderName() + File.separator + audioFileName);
            if (targetFile.exists()) {
                playAudioFile(createAppMainFolderUtils.getAppMediaFolderName(), audioFileName);
            } else {
                if (NetworkUtils.isNetworkAvailable()) {
                    audio_DownloadId = DownloadData(publicationsListDetails, KEY_AUDIO_ID);
                } else {
                    ToastUtils.showShortToast("No internet connection");
                }
            }
        }
    }

    private long DownloadData(@NonNull PublicationsListDetails publicationsListDetails, int typeID) {

        DownloadManager.Query query = null;
        Cursor c = null;


        long downloadReference;
        DownloadManager.Request request = null;

            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if(typeID == KEY_PDF_ID) {

            request = new DownloadManager.Request(Uri.parse(publicationsListDetails.getFile()));

            //Setting title of request
            request.setTitle(PDFFileName);

            //Setting description of request
            request.setDescription("सुरक्षित शहर विपद व्यबस्थापन समिति");

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(CreateAppMainFolderUtils.appmainFolderName + "/" + CreateAppMainFolderUtils.mediaFolderName, publicationsListDetails.getTitle() + ".pdf");
        }else {

            request = new DownloadManager.Request(Uri.parse(publicationsListDetails.getAudio()));

            //Setting title of request
            request.setTitle(audioFileName);

            //Setting description of request
            request.setDescription("सुरक्षित शहर विपद व्यबस्थापन समिति");

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(CreateAppMainFolderUtils.appmainFolderName + "/" + CreateAppMainFolderUtils.mediaFolderName, publicationsListDetails.getTitle() + audioType);

        }
//Enqueue download and save the referenceId
        downloadReference = downloadManager.enqueue(request);


        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, @NonNull Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId == pdf_DownloadId) {
                Toast toast = Toast.makeText(PublicationDetailsActivity.this,
                        "pdf Download Complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

                viewPDFFile(createAppMainFolderUtils.getAppMediaFolderName(), PDFFileName);
            }

            if (referenceId == audio_DownloadId) {
                Toast toast = Toast.makeText(PublicationDetailsActivity.this,
                        "Audio file Download Complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

                playAudioFile(createAppMainFolderUtils.getAppMediaFolderName(), audioFileName);
            }

        }
    };

    private void viewPDFFile(String filePath, @NonNull String fileName) {
        String path = filePath;
        File targetFile = new File(path + File.separator + fileName.trim());

        Uri targetUri1 = Uri.fromFile(targetFile.getAbsoluteFile());
        Uri targetUri;
        path = targetFile.getAbsolutePath();
        targetUri = FileProvider.getUriForFile(PublicationDetailsActivity.this,
                getString(R.string.file_provider_authority),
                targetFile);
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(targetUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Choose Viewer"));
    }


    private void playAudioFile(String appMediaFolderName, String audioFileName) {
        Log.d(TAG, "playAudioPath: "+appMediaFolderName );
        Log.d(TAG, "playAudioFile: "+audioFileName );

        //set up MediaPlayer
            MediaPlayer mp = new MediaPlayer();

            try {
                mp.setDataSource(appMediaFolderName + File.separator + audioFileName);
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }


        Toast.makeText(this, "playAudioFile: "+audioFileName , Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}

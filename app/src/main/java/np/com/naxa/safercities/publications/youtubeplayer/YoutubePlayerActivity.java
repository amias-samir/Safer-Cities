package np.com.naxa.safercities.publications.youtubeplayer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import np.com.naxa.safercities.R;
import np.com.naxa.safercities.publications.entity.PublicationsListDetails;
import np.com.naxa.safercities.publications.youtubeplayer.helper.YoutubeConstants;


public class YoutubePlayerActivity extends YouTubeBaseActivity {


    private String[] videoIds = {"6JYIGclVQdw", "LvetJ9U_tVY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        Intent intent = getIntent();
        PublicationsListDetails publicationsListDetails = intent.getParcelableExtra(YoutubeConstants.VIDEO_KEY);

//        String videoId = publicationsListDetails.getVideolink().replaceAll("https://www.youtube.com/watch?v=", "");

        String videoUrl = publicationsListDetails.getVideolink();
        int stringLength = videoUrl.length();
        String videoId = videoUrl.substring(stringLength-11,stringLength);

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(YoutubeConstants.API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(videoId);

                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });



    }


}

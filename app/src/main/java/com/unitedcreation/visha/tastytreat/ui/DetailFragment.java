package com.unitedcreation.visha.tastytreat.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unitedcreation.visha.tastytreat.R;
import com.unitedcreation.visha.tastytreat.utils.DpToPixel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.unitedcreation.visha.tastytreat.ui.DetailActivity.fragmentPosition;
import static com.unitedcreation.visha.tastytreat.ui.DetailActivity.position;
import static com.unitedcreation.visha.tastytreat.ui.DetailActivity.tabletLayout;
import static com.unitedcreation.visha.tastytreat.ui.HomeActivity.recipeList;

public class DetailFragment extends Fragment {

    private PlayerView playerView;
    private TextView descriptionTitleTv;
    private TextView decriptionTv;
    private SimpleExoPlayer simpleExoPlayer;

    public DetailFragment () {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        /**
         * Reverting fragmentPosition to default in case of exceeding or falling short of steps' list size.
         */
        if (fragmentPosition == recipeList.get(position).getStepsList().size() || fragmentPosition < 0)
            fragmentPosition = 0;

        descriptionTitleTv = rootView.findViewById(R.id.description_title_tv);
        descriptionTitleTv.setText(recipeList.get(position).getStepsList().get(fragmentPosition).getShortDescription());

        decriptionTv = rootView.findViewById(R.id.description_tv);
        decriptionTv.setText(recipeList.get(position).getStepsList().get(fragmentPosition).getDescription());

        playerView = rootView.findViewById(R.id.video_player);
        initiatePlayer();

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            landscapeUiConfiguration();

        return rootView;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        /**
         * Handling orientation configuration manually for mobile devices.
         */
        if (!tabletLayout) {

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                landscapeUiConfiguration();


            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

                /**
                 * Reverting back to default exoPlayer's layout size.
                 */
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = DpToPixel.convert(200, Objects.requireNonNull(getActivity()));
                playerView.setLayoutParams(params);

            }
        }
    }

    private void initiatePlayer () {

        // Measures bandwidth during playback. Can be null if not required.
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getActivity()),
                Util.getUserAgent(getActivity(), getString(R.string.app_name)));

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(recipeList.get(position).getStepsList().get(fragmentPosition).getVideoURL()));

        // Creating the simpleExoPlayer.
        simpleExoPlayer =
                ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        // Preparing simpleExoPlayer with the source.
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);

        playerView.setPlayer(simpleExoPlayer);

    }

    private void landscapeUiConfiguration() {

        /**
         * Changing exoPlayer's layout params to match parent (for full screen view).
         */
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        playerView.setLayoutParams(params);

    }

    @Override
    public void onResume() {
        super.onResume();
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {

            simpleExoPlayer.release();
            simpleExoPlayer = null;

        }
    }
}

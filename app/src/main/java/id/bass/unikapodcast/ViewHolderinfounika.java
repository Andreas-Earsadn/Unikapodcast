package id.bass.unikapodcast;

import android.app.Application;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;

public class ViewHolderinfounika extends RecyclerView.ViewHolder {
    View mView;
    SimpleExoPlayer exoPlayer;

    private PlayerView mExoplayerView;

    public ViewHolderinfounika(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

    }

    public  void setAudio (final Application ctx, String title, final String url){

        TextView mtextView =mView.findViewById(R.id.titleaudio);
        mExoplayerView = mView.findViewById(R.id.exoplayerview);

        mtextView.setText(title);

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(ctx).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer=(SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(ctx);

            Uri audio = Uri.parse(url);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("audio");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(audio, dataSourceFactory, extractorsFactory, null, null);
            mExoplayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }catch (Exception e){
            Log.e("ViewHolderinfounika","exoplayer error" + e.toString());
        }
    }
}

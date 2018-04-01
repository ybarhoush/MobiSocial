package ybarhoush.twitterfetch;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class MainActivity extends ListActivity {
    // Followed https://github.com/twitter/twitter-kit-android/wiki/Show-Timelines

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("Cf5uvphQe4bDbnjZJu2neiQEV", "CONSUMER_SECRET"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("ybarhoush")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
    }


}

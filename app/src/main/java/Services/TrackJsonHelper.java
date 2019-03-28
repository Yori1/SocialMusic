package Services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Models.Track;

public class TrackJsonHelper {
    public static List<Track> ToTracks(String jsonStringQueryResult) {
        ArrayList<Track> tracksFoundFromJson = new ArrayList<>();

        try {
            JSONObject message = new JSONObject(jsonStringQueryResult).getJSONObject("message");
            JSONObject body = message.getJSONObject("body");
            JSONArray tracks = body.getJSONArray("track_list");

            for (int count = 0; count < tracks.length(); count++) {
                JSONObject jsonObjectFromArray = tracks.getJSONObject(count);
                Track track = jsonObjectToTrack(jsonObjectFromArray);
                tracksFoundFromJson.add(track);
            }
        } catch (JSONException jsonException) {
            Log.d("e", jsonException.getLocalizedMessage());
        }

        return tracksFoundFromJson;
    }

    private static Track jsonObjectToTrack(JSONObject jsonTrack)
    {
        Track track = null;
        try {
            JSONObject innerTrack = jsonTrack.getJSONObject("track");
            String name = innerTrack.getString("track_name");
            String artistName = innerTrack.getString("artist_name");
            track = new Track(name, artistName);
        }

        catch (JSONException jsonException) {
            Log.e("e", jsonException.getMessage());
        }

        return track;
    }
}

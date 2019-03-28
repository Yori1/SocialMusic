package Models;

import android.os.AsyncTask;

import org.junit.Assert;
import org.junit.Test;

import Services.PostAPICaller;

public class PostAPICallerTest {

    @Test
    public void testTokenRequest() {
        String urlToCall = "http://socialmusicwebapi.azurewebsites.net/values/ValidateToken";
        String jsonToPost = "{\"TokenString\":\"a\"}";
        String[] params = {urlToCall, jsonToPost};

        PostAPICaller apiCaller = new PostAPICaller();
        apiCaller.execute(params);
        while(apiCaller.getStatus() != AsyncTask.Status.FINISHED) {}
        Assert.assertEquals(apiCaller.getResult(), "a");
    }


}
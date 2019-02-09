package Models;

import android.os.AsyncTask;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class APICallerTest {

    @Test
    public void testTokenRequest() {
        String urlToCall = "http://socialmusicwebapi.azurewebsites.net/values/ValidateToken";
        String jsonToPost = "{\"TokenString\":\"a\"}";
        String[] params = {urlToCall, jsonToPost};

        APICaller apiCaller = new APICaller();
        apiCaller.execute(params);
        while(apiCaller.getStatus() != AsyncTask.Status.FINISHED) {}
        Assert.assertEquals(apiCaller.getResult(), "a");
    }


}
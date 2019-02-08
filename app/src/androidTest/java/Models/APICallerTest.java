package Models;

import android.os.AsyncTask;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class APICallerTest {

    @Test
    public void testTokenRequest()
    {
        String urlToCall = "http://socialmusicwebapi.azurewebsites.net/values/ValidateToken";
        String jsonToPost = "{\"TokenString\":\"a\"}";
        String[] params = {urlToCall, jsonToPost};

        APICaller apiCaller = new APICaller();
        String result = null;
        try {
            result = apiCaller.execute(params).get();
        }

        catch (Exception e)
        {}
        String expectedResult = "a";

        Assert.assertTrue(result == expectedResult);
    }


}
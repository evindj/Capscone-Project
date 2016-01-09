package com.work.evindj.travler;

import com.work.evindj.travler.remote.RemoteEndpointUtil;
import com.work.evindj.travler.remote.RequestWrapper;
import com.work.evindj.travler.remote.SliceRequest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by evindj on 1/5/16.
 */
public class RemoteEndpointUtilTest {
    private RemoteEndpointUtil remoteEndpointUtil;
    private RequestWrapper requestWrapper;
    @Test
    public void sendflightRequest() throws Exception {
        SliceRequest sliceData = new SliceRequest("NSI","ATL","2016-02-02");
        ArrayList<SliceRequest> reqData = new ArrayList<SliceRequest>();
        reqData.add(sliceData);
        RequestWrapper requestWrapper = new RequestWrapper(1, reqData);
        Assert.assertNotNull(RemoteEndpointUtil.trips);
    }
}

package com.dialogflowfulfillment.controller;

import com.dialogflowfulfillment.delegate.FulfillmentManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@Ignore
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FulfillmentControllerTest {

    @Mock
    FulfillmentController fulfillmentController;

    @Mock
    FulfillmentManager fulfillmentManagerMock;




    @Test
    public void getHealthCheckFromRedisSuccess() throws Exception
    {
        /*TODO
        * call  the method to see if we are getting a valid json payload*/


    }




}

package com.refinitiv.collab.platform.service;

import com.refinitiv.collab.platform.WebapiApplication;
import com.refinitiv.collab.platform.common.SampleConstValues;
import com.refinitiv.collab.platform.service.auth.JwtService;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.ConnectionState;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.ClientOptions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebapiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtServiceImplTest {
    @LocalServerPort
    private int port;

    //    @Resource     //it's jdk annotation
    @Autowired          //it's spring annotation
    private JwtService jwtServiceImpl;

    private String uuid = SampleConstValues.UUID;
    private String capability = SampleConstValues.JWTCapability;
    private String jwt = SampleConstValues.JWTToken;

    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
    }


    @Ignore
    @Test
    void newJwt() {
        String newJwt = jwtServiceImpl.newJwt(uuid, capability);
        System.out.println("newJwt: " + newJwt);
    }

//    @Test
//    void refreshJwt() {
//        String newJwt = jwtServiceImpl.refreshJwt(uuid, userName, capability, jwt);
//        System.out.println("refreshJwt: " + newJwt);
//    }

    @Ignore
    @Test
    void validateJwt() {
//        boolean valid = jwtServiceImpl.validateJwt(uuid, jwt);
//        System.out.println("validateJwt: " + valid);
    }

    /**
     * Request a JWT
     * Verifies that the connection is setup using JWT
     */
    @Ignore
    @Test
    public void ablyRealtimeJwt() {
        try {
            /* create ably realtime with JWT token */
            String newJwt = jwtServiceImpl.newJwt(uuid, capability);
            System.out.println("newJwt: " + newJwt);

            ClientOptions realtimeOptions = new ClientOptions(newJwt);

            assertNotNull("Expected token value", realtimeOptions.token);
            AblyRealtime ablyRealtime = new AblyRealtime(realtimeOptions);

            /* wait for connected state */
            Helpers.ConnectionWaiter connectionWaiter = new Helpers.ConnectionWaiter(ablyRealtime.connection);
            connectionWaiter.waitFor(ConnectionState.connected);
            assertEquals("Connected state was NOT reached", ConnectionState.connected, ablyRealtime.connection.state);

            ablyRealtime.close();
        } catch (AblyException e) {
            e.printStackTrace();
            fail();
        }
    }

}
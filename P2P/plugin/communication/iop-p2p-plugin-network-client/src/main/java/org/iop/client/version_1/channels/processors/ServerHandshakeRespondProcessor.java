/*
* @#ServerHandshakeRespondProcessor.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package org.iop.client.version_1.channels.processors;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.ServerHandshakeRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.base.STATUS;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.PackageType;

import org.iop.client.version_1.channels.endpoints.NetworkClientCommunicationChannel;

import javax.websocket.Session;

/**
 * The Class <code>ServerHandshakeRespondProcessor</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 27/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ServerHandshakeRespondProcessor extends PackageProcessor{

    /**
     * Constructor whit parameter
     *
     * @param networkClientCommunicationChannel register
     */
    public ServerHandshakeRespondProcessor(final NetworkClientCommunicationChannel networkClientCommunicationChannel) {
        super(
                networkClientCommunicationChannel,
                PackageType.SERVER_HANDSHAKE_RESPONSE
        );
    }

    /**
     * (non-javadoc)
     * @see PackageProcessor#processingPackage(Session, Package)
     */
    @Override
    public void processingPackage(Session session, Package packageReceived) {

        System.out.println("Processing new package received, packageType: " + packageReceived.getPackageType());
        ServerHandshakeRespond serverHandshakeRespond = ServerHandshakeRespond.parseContent(packageReceived.getContent());
        
        if(serverHandshakeRespond.getStatus() == STATUS.SUCCESS) {

            /*
             * Send the clientProfile
             */
            getChannel().getConnection().registerInNode();

        }else{
            System.err.println("Algo está mal..");
            //there is some wrong
        }

    }

}

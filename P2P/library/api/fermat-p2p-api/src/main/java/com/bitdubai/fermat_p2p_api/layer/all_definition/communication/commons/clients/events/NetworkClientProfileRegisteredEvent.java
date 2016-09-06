package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events;

import com.bitdubai.fermat_api.layer.all_definition.events.common.AbstractEvent;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.base.STATUS;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;

import java.util.UUID;

/**
 * The class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.events.NetworkClientProfileRegisteredEvent</code>
 * is the representation of a network client registered profile event.
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 02/05/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public class NetworkClientProfileRegisteredEvent extends AbstractEvent<P2pEventType> {

    private UUID packageId;

    /**
     * Represent the registered profile.
     */
    private String publicKey;
    /**
     * Represent the status
     */
    private STATUS status;

    /**
     * Constructor with parameters
     *
     * @param p2pEventType
     */
    public NetworkClientProfileRegisteredEvent(P2pEventType p2pEventType) {
        super(p2pEventType);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public UUID getPackageId() {
        return packageId;
    }

    public void setPackageId(UUID packageId) {
        this.packageId = packageId;
    }

    @Override
    public String toString() {
        return "NetworkClientProfileRegisteredEvent{" +
                "pk='" + publicKey + '\'' +
                ", status=" + status +
                '}';
    }
}

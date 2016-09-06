package com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantRegisterProfileException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantRequestActorFullPhotoException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantRequestProfileListException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantUnregisterProfileException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.exceptions.CantUpdateRegisteredProfileException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.DiscoveryQueryParameters;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.enums.UpdateTypes;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NetworkServiceProfile;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.Profile;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.CommunicationChannels;

import java.util.List;
import java.util.UUID;

/**
 * The interface <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkClientConnection</code>
 * contains all the method related with a network client connection.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 18/04/2016.
 *
 * @author  lnacosta
 * @version 1.0
 * @since   Java JDK 1.7
 */
public interface NetworkClientConnection {

    /**
     * Through the method <code>registerProfile</code> we can register a profile
     * in the server.
     *
     * @param profile  of the component that we're trying to register.
     *
     * @throws CantRegisterProfileException      if something goes wrong.
     */
    UUID registerProfile(Profile profile) throws CantRegisterProfileException;

//    /**
//     * Through the method <code>updateRegisteredProfile</code> we can update registered profile
//     * in the server.
//     * We must to indicate which type of update we're trying to do.
//     *
//     * @param profile  of the component that we're trying to update.
//     * @param type     of the update.
//     *
//     * @throws CantUpdateRegisteredProfileException      if something goes wrong.
//     */
//    void updateRegisteredProfile(Profile     profile,
//                                 UpdateTypes type   ) throws CantUpdateRegisteredProfileException;

    /**
     * Through the method <code>unregisterProfile</code> we can unregister a profile in the server.
     *
     * @param profile that we're trying to unregister.
     *
     * @throws CantUnregisterProfileException if something goes wrong.
     */
//     void unregisterProfile(Profile profile) throws CantUnregisterProfileException;

    /**
     * Through this method we can ask to the fermat network a list of online actors.
     * This method is asynchronous, it will raise a NETWORK_CLIENT_ACTOR_LIST_RECEIVED event
     * when it get the results.
     *
     * @param discoveryQueryParameters  parameters for the query
     * @param networkServicePublicKey   network service asking for the list of actors
     * @param requesterPublicKey        actor public key of the actor which request the discovery
     *
     * @return query id
     *
     * @throws CantRequestProfileListException if something goes wrong.
     */
     UUID discoveryQuery(final DiscoveryQueryParameters discoveryQueryParameters,
                         final String                   networkServicePublicKey ,
                         final String                   requesterPublicKey      ) throws CantRequestProfileListException;

    /**
     * Through the method <code>listRegisteredActorProfiles</code> we can get a list of registered actors
     * filtering them with an instance of discovery query parameters.
     *
     * @param discoveryQueryParameters parameters to discover the actors.
     *
     * @return a list of actors profile that meets the parameters.
     *
     * @throws CantRequestProfileListException
     */
//    List<ActorProfile> listRegisteredActorProfiles(DiscoveryQueryParameters discoveryQueryParameters) throws CantRequestProfileListException;

    /**
     * Through the method <code>getCommunicationChannelType</code> we can get the communication channel type
     * of the network client connection object.
     *
     * @return a CommunicationChannels enum element.
     */
    CommunicationChannels getCommunicationChannelType();

    /**
     * Through the method <code>isConnected</code> we can verify if the connection object is
     * connected with the server.
     *
     * @return boolean
     */
    boolean isConnected();

    /**
     * Through the method <code>isRegistered</code> we can verify if the client is
     * registered on a server.
     *
     * @return boolean
     */
    boolean isRegistered();

    /**
     * Through the method <code>getActorFullPhoto</code> we can get Full Photo of an actor specific.
     *
     * @param publicKey of the actor
     *
     * @return a String encodeBase64String
     */
    String getActorFullPhoto(final String publicKey) throws CantRequestActorFullPhotoException;

}

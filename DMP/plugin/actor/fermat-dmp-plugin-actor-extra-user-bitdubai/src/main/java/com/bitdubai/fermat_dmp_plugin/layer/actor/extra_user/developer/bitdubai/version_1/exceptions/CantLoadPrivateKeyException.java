package com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * The Class <code>package com.bitdubai.fermat_dmp_plugin.layer.actor.extra_user.developer.bitdubai.version_1.exceptions.CantLoadPrivateKeyException</code>
 * is thrown when i cant load the private key.
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 03/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CantLoadPrivateKeyException extends FermatException {

    public static final String DEFAULT_MESSAGE = "CAN'T LOAD PRIVATE KEY EXCEPTION";

    public CantLoadPrivateKeyException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantLoadPrivateKeyException(final String message, final Exception cause) {
        this(message, cause, "", "");
    }

    public CantLoadPrivateKeyException(final String message) {
        this(message, null);
    }

    public CantLoadPrivateKeyException() {
        this(DEFAULT_MESSAGE);
    }
}
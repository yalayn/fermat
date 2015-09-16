package com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.interfaces.CryptoIndexInterface;

/**
 * Created by francisco on 11/09/15.
 */

/**
 *
 */
public class CryptoIndex implements CryptoIndexInterface {

    private String cryptoCurrency;
    private String fiatCurrency;
    private String time;
    private double rateExchange;
    /**
     *
     *
     * @param cryptoCurrency
     * @param fiatCurrency
     * @param time
     * @param rateExchange
     */
    public CryptoIndex( String cryptoCurrency, String fiatCurrency, String time, double rateExchange) {
        this.cryptoCurrency = cryptoCurrency;
        this.fiatCurrency = fiatCurrency;
        this.time = time;
        this.rateExchange = rateExchange;
    }

    /**
     *
     * @return
     */
    @Override
    public String getCryptyCurrency() {
        return cryptoCurrency;
    }
    /**
     *
     * @return
     */
    @Override
    public String getFiatCurrency() {
        return fiatCurrency;
    }
    /**
     *
     * @return
     */
    @Override
    public String getTime() {
        return time;
    }
    /**
     *
     * @return
     */
    @Override
    public double getRateExchange() {
        return rateExchange;
    }
}

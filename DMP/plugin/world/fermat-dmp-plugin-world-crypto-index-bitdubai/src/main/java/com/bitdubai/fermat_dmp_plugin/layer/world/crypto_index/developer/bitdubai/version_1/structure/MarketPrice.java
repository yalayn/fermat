package com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.exception.CantGetBufferedReader;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.exception.CantGetInputStream;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.exception.CantGetJsonObject;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.interfaces.MarketRateProvider;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.providers.BtceServiceAPI;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.providers.BterServiceAPI;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.providers.CcexServiceAPI;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.providers.CexioServiceAPI;
import com.bitdubai.fermat_dmp_plugin.layer.world.crypto_index.developer.bitdubai.version_1.providers.CryptocoinchartsServiceAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by francisco on 12/08/15.
 */
public class MarketPrice implements MarketRateProvider {
    Double marketExchangeRate = null;
    HTTPJson jsonService = new HTTPJson();
    BtceServiceAPI btceServiceAPI = new BtceServiceAPI();
    BterServiceAPI bterServiceAPI = new BterServiceAPI();
    CcexServiceAPI ccexServiceAPI = new CcexServiceAPI();
    CexioServiceAPI cexioServiceAPI = new CexioServiceAPI();
    CryptocoinchartsServiceAPI cryptocoinchartsServiceAPI = new CryptocoinchartsServiceAPI();
    String url = null;
    /**
     * Through getBestMarketPrice method, the best market price returns
     * @param c
     * @param f
     * @param time
     * @return
     */
    @Override
    public double getHistoricalExchangeRate(CryptoCurrency c, FiatCurrency f, long time) {
            String crypto= c.getCode().toString();
            String fiat = f.getCode().toString();
            marketExchangeRate=null;
            marketExchangeRate=getBestMarketPrice(crypto, fiat);
        return marketExchangeRate;
    }
    /**
     *The url obtained providers is stored in different variable type String.
     * These are added to a list so, then it is sorted from lowest to highest, and thus return the best market price.
     * @param crypto
     * @param fiat
     * @return
     */
    public double getBestMarketPrice (String crypto, String fiat ){
        marketExchangeRate=null;
        url=null;
        List<String> priceList = new ArrayList<>();

        String urlAPI1=btceServiceAPI.getUrlAPI(crypto,fiat);
        String urlAPI2=bterServiceAPI.getUrlAPI(crypto, fiat);
        String urlAPI3=ccexServiceAPI.getUrlAPI(crypto, fiat);
        String urlAPI4=cexioServiceAPI.getUrlAPI(crypto, fiat);
        String urlAPI5=cryptocoinchartsServiceAPI.getUrlAPI(crypto, fiat);

            priceList.add(jsonService.getJSONFromUrl(urlAPI1).getJSONObject(crypto.toLowerCase() + "_" + fiat.toLowerCase()).get("last").toString());
            priceList.add(jsonService.getJSONFromUrl(urlAPI2).get("last").toString());
            priceList.add(jsonService.getJSONFromUrl(urlAPI3).getJSONObject("ticker").get("lastprice").toString());
            priceList.add(jsonService.getJSONFromUrl(urlAPI4).getString("last"));
            priceList.add(jsonService.getJSONFromUrl(urlAPI5).getString("price"));

        Collections.sort(priceList);
        marketExchangeRate= Double.valueOf(priceList.get(priceList.size()-1));
        return marketExchangeRate;
    }
}










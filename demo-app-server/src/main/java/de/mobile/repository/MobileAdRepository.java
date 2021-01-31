package de.mobile.repository;

import de.mobile.entity.MobileAd;

import java.util.List;

public interface MobileAdRepository {

    Long create(MobileAd ad);

    MobileAd get(Long adId);

    List<MobileAd> list();

}
